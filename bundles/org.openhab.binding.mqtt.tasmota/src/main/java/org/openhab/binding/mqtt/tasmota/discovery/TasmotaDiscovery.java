/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.mqtt.tasmota.discovery;

import static org.openhab.binding.mqtt.tasmota.internal.TasmotaBindingConstants.debugLimitDiscovery;

import java.util.Collections;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.mqtt.discovery.AbstractMQTTDiscovery;
import org.openhab.binding.mqtt.discovery.MQTTTopicDiscoveryService;
import org.openhab.binding.mqtt.tasmota.internal.MqttMessageTransformer;
import org.openhab.binding.mqtt.tasmota.internal.TasmotaBindingConstants;
import org.openhab.binding.mqtt.tasmota.utils.ExceptionHelper;
import org.openhab.core.config.discovery.DiscoveryResult;
import org.openhab.core.config.discovery.DiscoveryResultBuilder;
import org.openhab.core.config.discovery.DiscoveryService;
import org.openhab.core.io.transport.mqtt.MqttBrokerConnection;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingRegistry;
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
 * @author Jörg Ostertag - Adaptions
 */
@Component(immediate = true, service = DiscoveryService.class, configurationPid = "discovery.tasmota")
@NonNullByDefault
public class TasmotaDiscovery extends AbstractMQTTDiscovery {

    private final Logger logger = LoggerFactory.getLogger(new Object() {
    }.getClass().getEnclosingClass());

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
        // logger.debug("Discovery receivedMessage({}, {}, {}, {})", connectionBridge.getAsString(),
        // connection.getHost(),
        // topic, new String(payload));
        resetTimeout();

        String deviceID = MqttMessageTransformer.extractDeviceID(topic, payload);
        if (deviceID == null) {
            logger.warn("Found tasmota device, but could not extract device ID from {}.", topic);
            return;
        }
        ThingTypeUID thingTypeUid = TasmotaBindingConstants.TASMOTA_MQTT_THING;
        ThingUID thingUID = new ThingUID(thingTypeUid, connectionBridge, deviceID);

        @Nullable
        Thing existingThing = thingRegistry.get(thingUID);
        if (null != existingThing) {
            logger.trace("Discovery: Thing {} already exists in thingsRegistry: Ignoring", thingUID);
            return;
        }

        Map<String, Object> deviceStateMap = deviceStateMap = DiscoveryCache.getCachedProperties(thingUID);

        deviceStateMap.put("deviceid", deviceID);

        if (!topic.startsWith("tasmota/discovery/")) {

            deviceStateMap.putAll(MqttMessageTransformer.toPropertiesFromMessage(topic, payload));
        }

        if (debugLimitDiscovery && !deviceID.contains("Basic-3")) {
            logger.warn("DEVELOP MODE: Tasmota discovery topic ignored: Topic: {}", topic);
            return;
        }

        try {
            // publishDevice(thingTypeUid, connectionBridge, deviceStateMap, deviceID, tasmotaState);
            // logger.debug("received Message for Device( thing: {}, properties: {}, deviceID: {}", thingUID,
            // deviceStateMap, deviceID);

            DiscoveryResult discoveryResult = DiscoveryResultBuilder.create(thingUID) //
                    .withBridge(connectionBridge) //
                    // .withThingType(type) //
                    .withProperties(deviceStateMap) //
                    .withRepresentationProperty("deviceid")//
                    .withLabel(deviceID).build();
            thingDiscovered(discoveryResult);
        } catch (Exception e) {
            logger.error("Cannot publishDevice({})\n" + //
                    "Exception:\n" + //
                    "{}", thingUID, ExceptionHelper.compactStackTrace(e));
        }
    }

    @Override
    public void topicVanished(ThingUID connectionBridge, MqttBrokerConnection connection, String topic) {
        String deviceID = MqttMessageTransformer.extractDeviceID(topic, new byte[0]);
        if (deviceID == null) {
            return;
        }

        // XXX: Shall we really remove the thing if we see a topic with empty message?
        ThingUID thingUID = new ThingUID(TasmotaBindingConstants.TASMOTA_MQTT_THING, connectionBridge, deviceID);
        logger.info("Topic Vanished Remove Thing: {}", thingUID);
        thingRemoved(thingUID);
    }

    protected void triggerMqttDiscoverAnswers() {
        final boolean retain = false;
        final int qos = 2;

        String topic = "";

        // Status 10, 11
        topic = "cmnd/tasmotas/Status";
        for (Integer i = 10; i <= 11; i++) {
            byte[] payload = String.valueOf(11).getBytes();
            logger.debug("publish topic: {}, {}", topic, i);
            getDiscoveryService().publish(topic, payload, qos, retain);
        }

        // Teleperiod triggers tele/+/STATUS Answers
        topic = "cmnd/tasmotas/Teleperiod";
        logger.debug("publish topic: {}", topic);
        getDiscoveryService().publish(topic, "".getBytes(), qos, retain);
    }
}
