/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
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
package org.openhab.binding.mqtt.tasmota.internal;

import java.util.Collections;
import java.util.HashMap;
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

    @Activate
    public TasmotaDiscovery(@Reference MQTTTopicDiscoveryService discoveryService) {
        super(Collections.singleton(TasmotaBindingConstants.TASMOTA_MQTT_SWITCH), 3, true, "tele/+/STATE");
        this.discoveryService = discoveryService;
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
    public void receivedMessage(ThingUID connectionBridge, MqttBrokerConnection connection, String topic,
            byte[] payload) {
        resetTimeout();

        final String deviceID = extractDeviceID(topic);
        if (deviceID == null) {
            logger.trace("Found tasmota device, but could not extract device ID from {}.", topic);
            return;
        }

        TasmotaState state = Device.parseState(new String(payload));

        Map<String, Object> properties = new HashMap<>();
        properties.put("deviceid", deviceID);

        if (state.Dimmer != null) {
            publishDevice(TasmotaBindingConstants.TASMOTA_MQTT_DIMMER, connectionBridge, properties, deviceID);
        } else {
            publishDevice(TasmotaBindingConstants.TASMOTA_MQTT_SWITCH, connectionBridge, properties, deviceID);
        }
    }

    void publishDevice(ThingTypeUID type, ThingUID connectionBridge, Map<String, Object> properties, String deviceID) {

        thingDiscovered(DiscoveryResultBuilder.create(new ThingUID(type, connectionBridge, deviceID))
                .withBridge(connectionBridge).withProperties(properties).withRepresentationProperty("deviceid")
                .withLabel(deviceID).build());
    }

    @Override
    public void topicVanished(ThingUID connectionBridge, MqttBrokerConnection connection, String topic) {
        String deviceID = extractDeviceID(topic);
        if (deviceID == null) {
            return;
        }
        thingRemoved(new ThingUID(TasmotaBindingConstants.TASMOTA_MQTT_SWITCH, connectionBridge, deviceID));
    }
}
