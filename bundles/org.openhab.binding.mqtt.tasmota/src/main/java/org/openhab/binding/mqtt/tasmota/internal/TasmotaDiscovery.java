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

import static org.openhab.binding.mqtt.tasmota.internal.DeviceStateParser.parseDeviceTypeKnown;

import java.util.Collections;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.mqtt.discovery.AbstractMQTTDiscovery;
import org.openhab.binding.mqtt.discovery.MQTTTopicDiscoveryService;
import org.openhab.binding.mqtt.tasmota.internal.deviceState.TasmotaStateDTO;
import org.openhab.core.config.discovery.DiscoveryResult;
import org.openhab.core.config.discovery.DiscoveryResultBuilder;
import org.openhab.core.config.discovery.DiscoveryService;
import org.openhab.core.io.transport.mqtt.MqttBrokerConnection;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingRegistry;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.ThingUID;
import org.openhab.core.thing.binding.ThingHandler;
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

    static final String discoverySubscribeTopic1 = "tele/+/STATE";
    static final String discoverySubscribeTopic2 = "stat/#";
    static final String discoverySubscribeTopic3 = "tasmota/discovery/+/config";
    private ThingRegistry thingRegistry;

    @Activate
    public TasmotaDiscovery(@Reference MQTTTopicDiscoveryService discoveryService, //
            @Reference ThingRegistry thingRegistry) {
        super(Collections.singleton(TasmotaBindingConstants.TASMOTA_MQTT_THING), 3, true, discoverySubscribeTopic1);
        this.thingRegistry = thingRegistry;
        logger.debug("Started Tasmota Discovery with topic '" + discoverySubscribeTopic1 + "'");
        this.discoveryService = discoveryService;

        discoveryService.subscribe(this, discoverySubscribeTopic2);
        discoveryService.subscribe(this, discoverySubscribeTopic3);
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

        String deviceID = TasmotaHandlerImpl.extractDeviceID(topic, payload);
        if (deviceID == null) {
            logger.warn("Found tasmota device, but could not extract device ID from {}.", topic);
            return;
        }

        if (topic.startsWith("tasmota/discovery/")) {
            logger.warn("Tasmota discovery topic not supported yet: Topic: {}", topic);
            return;
        }

        TasmotaStateDTO tasmotaStateDTO = DeviceStateParser.parseState(payload);

        Map<String, Object> deviceStateMap = DeviceStateParser.stateToHashMap(tasmotaStateDTO);

        deviceStateMap.put("deviceid", deviceID);

        ThingTypeUID thingTypeUid = TasmotaBindingConstants.TASMOTA_MQTT_THING;
        ThingUID thingUID = new ThingUID(thingTypeUid, connectionBridge, deviceID);

        try {
            // publishDevice(thingTypeUid, connectionBridge, deviceStateMap, deviceID, tasmotaState);
            logger.debug("received Message for Device( thing: {}, properties: {}, deviceID: {}", thingUID,
                    deviceStateMap, deviceID);

            @Nullable
            Thing existingThing = thingRegistry.get(thingUID);
            if (null != existingThing) {
                logger.debug("Discovery: Thing {} already exists: Updating", thingUID);
                @Nullable
                ThingHandler existingThingHandler = existingThing.getHandler();
                if (existingThingHandler != null) {
                    if (existingThingHandler instanceof TasmotaHandler) {
                        TasmotaHandler existingTasmotaHandler = (TasmotaHandlerImpl) existingThingHandler;
                        existingTasmotaHandler.updateExistingThing(tasmotaStateDTO);
                    } else {
                        logger.warn("Discovery: Thing {} wrong type ({}) of Thing Handler", thingUID,
                                existingThingHandler.getClass().getSimpleName());
                    }
                } else {
                    logger.warn("Discovery: Thing {} missing Thing Handler", thingUID);
                }
            } else {
                if (!parseDeviceTypeKnown(tasmotaStateDTO, deviceStateMap)) {
                    logger.info("Cannot fully recognize Tasmota Device from MQTT-Message. Topic: {} PayLoad: {}", topic,
                            new String(payload));
                    // return;
                }
                logger.debug("New Thing Discovered: ThingUID: {}", thingUID);
                DiscoveryResult discoveryResult = DiscoveryResultBuilder.create(thingUID) //
                        .withBridge(connectionBridge) //
                        // .withThingType(type) //
                        .withProperties(deviceStateMap) //
                        .withRepresentationProperty("deviceid")//
                        .withLabel(deviceID).build();
                thingDiscovered(discoveryResult);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Cannot publishDevice({}): {}, {}", thingUID, e.getMessage(), e.getCause());
        }
    }

    @Override
    public void topicVanished(ThingUID connectionBridge, MqttBrokerConnection connection, String topic) {
        String deviceID = TasmotaHandlerImpl.extractDeviceID(topic, new byte[0]);
        if (deviceID == null) {
            return;
        }

        ThingUID thingUID = new ThingUID(TasmotaBindingConstants.TASMOTA_MQTT_THING, connectionBridge, deviceID);
        logger.info("Topic Vanished Remove Thing: {}", thingUID);
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
