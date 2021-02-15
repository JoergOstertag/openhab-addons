/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
 * <p>
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 * <p>
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 * <p>
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.mqtt.tasmota.internal;

import java.util.Collections;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.mqtt.discovery.AbstractMQTTDiscovery;
import org.openhab.binding.mqtt.discovery.MQTTTopicDiscoveryService;
import org.openhab.binding.mqtt.tasmota.internal.deviceState.TasmotaState;
import org.openhab.core.config.discovery.DiscoveryResultBuilder;
import org.openhab.core.config.discovery.DiscoveryService;
import org.openhab.core.io.transport.mqtt.MqttBrokerConnection;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.ThingUID;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link TasmotaDiscovery} is responsible for discovering Tasmota MQTT device nodes.
 *
 * @author Daan Meijer - Initial contribution
 */
@Component(immediate = true, service = DiscoveryService.class, configurationPid = "discovery.tasmota")
@NonNullByDefault
public class TasmotaDiscovery extends AbstractMQTTDiscovery {
    private final Logger logger = LoggerFactory.getLogger(TasmotaDiscovery.class);

    protected final MQTTTopicDiscoveryService discoveryService;

    static final String discoverySubscribeTopic = "tele/+/STATE";
    static final String discoveryPublishCommand = "cmnd/tasmotas/Teleperiod";

    @Activate
    public TasmotaDiscovery(@Reference MQTTTopicDiscoveryService discoveryService) {
        super(Collections.singleton(TasmotaBindingConstants.TASMOTA_MQTT_THING), 3, true, discoverySubscribeTopic);
        logger.debug("Started Tasmota Discovery with topic '" + discoverySubscribeTopic + "'");
        this.discoveryService = discoveryService;
        discoveryService.subscribe(this, "stat/#");
        triggerMqttDiscoverAnswers();
    }

    @Override
    protected void stopBackgroundDiscovery() {
        discoveryService.unsubscribe(this);
        super.stopBackgroundDiscovery();
    }

    @Override
    protected MQTTTopicDiscoveryService getDiscoveryService() {
        return discoveryService;
    }

    /**
     * @param topic A topic like "tele/office/STATE"
     * @return Returns the "office" part of the example
     */
    public static @Nullable String extractDeviceID(String topic) {
        String[] strings = topic.split("/");
        if (strings.length > 2) {
            return strings[1];
        }
        return null;
    }

    @Override
    protected void startScan() {
        logger.debug("Start Scanning ....");

        logger.debug("Super Start Scanning ....");
        super.startScan();
        logger.debug("DONE Super Start Scanning.");
        triggerMqttDiscoverAnswers();
    }

    @Override
    public void receivedMessage(ThingUID connectionBridge, MqttBrokerConnection connection, String topic,
            byte[] payload) {
        // logger.trace("receivedMessage({}, {}, {}, {})", connectionBridge.getAsString(), connection.getHost(), topic,
        // new String(payload));
        resetTimeout();

        String deviceID = extractDeviceID(topic);
        if (deviceID == null) {
            logger.warn("Found tasmota device, but could not extract device ID from {}.", topic);
            return;
        }

        String payloadString = new String(payload);
        TasmotaState tasmotaState = Device.parseState(payloadString);

        Map<String, Object> properties = DeviceStateParser.stateToHashMap(tasmotaState);

        properties.put("deviceid", deviceID);

        if (null == properties.get("deviceTypeKnown")) {
            logger.info("Cannot recognize Tasmota Device from MQTT-Message. Topic: {} PayLoad: {}", topic,
                    payloadString);
            return;
        }

        ThingTypeUID thingTypeUid = TasmotaBindingConstants.TASMOTA_MQTT_THING;

        try {
            publishDevice(thingTypeUid, connectionBridge, properties, deviceID);
        } catch (Exception e) {
            logger.error("Cannot publishDevice: {}", e.getMessage());
        }
    }

    void publishDevice(ThingTypeUID type, ThingUID connectionBridge, Map<String, Object> properties, String deviceID) {

        logger.debug("publishDevice( type: {}, connectionBridge: {}, properties: {}, deviceID: {}", type,
                connectionBridge, properties, deviceID);

        ThingUID thingUID = new ThingUID(type, connectionBridge, deviceID);
        logger.debug("thingDiscovered: ThingUID: {}", thingUID);
        thingDiscovered( //
                DiscoveryResultBuilder.create(thingUID) //
                        .withBridge(connectionBridge) //
                        // .withThingType(type) //
                        .withProperties(properties) //
                        .withRepresentationProperty("deviceid")//
                        .withLabel(deviceID).build());
    }

    @Override
    public void topicVanished(ThingUID connectionBridge, MqttBrokerConnection connection, String topic) {
        String deviceID = extractDeviceID(topic);
        if (deviceID == null) {
            return;
        }

        ThingUID thingUID = new ThingUID(TasmotaBindingConstants.TASMOTA_MQTT_THING, connectionBridge, deviceID);

        thingRemoved(thingUID);
    }

    protected void triggerMqttDiscoverAnswers() {
        final boolean retain = false;
        final int qos = 2;

        String topic = "";

        // TODO: Deactivated until we also listen for the STATUS Answers
        if (true) {
            // Status 10, 11
            topic = "cmnd/tasmotas/Status";
            for (Integer i = 10; i <= 11; i++) {
                byte[] payload = String.valueOf(11).getBytes();
                logger.debug("publish topic: {}, {}", topic, i);
                getDiscoveryService().publish(topic, payload, qos, retain);
            }
        }

        // Teleperiod triggers tele/+/STATUS Answers
        topic = "cmnd/tasmotas/Teleperiod";
        logger.debug("publish topic: {}", topic);
        getDiscoveryService().publish(topic, "".getBytes(), qos, retain);
    }
}
