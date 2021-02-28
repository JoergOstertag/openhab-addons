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

import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.mqtt.tasmota.utils.ExceptionHelper;
import org.openhab.binding.mqtt.tasmota.utils.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jörg Ostertag - Initial Contribution
 * @author Jörg Ostertag - Parse more of the Json responses from Tasmota
 */
@NonNullByDefault
public class MqttMessageTransformer {

    private static final Logger logger = LoggerFactory.getLogger(new Object() {
    }.getClass().getEnclosingClass());

    public static boolean isDeviceTypeKnown(Map<String, Object> stateMap) {
        boolean deviceKnown = false;
        deviceKnown |= (stateMap.get("Dimmer") != null);
        deviceKnown |= (stateMap.get("POWER") != null);
        deviceKnown |= (stateMap.get("Status.Power") != null);

        deviceKnown |= (stateMap.get("StatusSNS.ENERGY.Power") != null);
        deviceKnown |= (stateMap.get("StatusSNS.ENERGY.Current") != null);

        deviceKnown |= (stateMap.get("StatusSNS.dht11DTO.Temperature") != null);
        deviceKnown |= (stateMap.get("StatusSNS.dht11DTO.Humidity") != null);
        deviceKnown |= (stateMap.get("dht11DTO.Temperature") != null);
        deviceKnown |= (stateMap.get("dht11DTO.Humidity") != null);

        deviceKnown |= (stateMap.get("StatusSNS.DS18B20.Temperatire") != null);
        for (Map.Entry<String, Object> property : stateMap.entrySet()) {
            String propertyName = property.getKey();
            Object propertyValue = property.getValue();
            deviceKnown |= (propertyName.startsWith("Sensor.") && (null != propertyValue));
        }
        return deviceKnown;
    }

    public static Map<String, Object> transformMessage(String topic, byte[] payload) {

        String prefix = "$";

        if (topic.matches("tasmota/discovery/.*/config")) {
            prefix = "$.discovery";
        } else if (topic.matches("tasmota/discovery/.*/sensors")) {
            prefix = "$.discovery.sensors";
        }

        Map<String, Object> stateMap = JsonHelper.jsonStringToMap(prefix, new String(payload));

        return stateMap;
    }

    /**
     * @param topic A topic like "tele/office/STATE"
     * @param payload
     * @return Returns the "office" part of the example
     */
    public static @Nullable String extractDeviceID(String topic, byte[] payload) {
        if (topic.matches("tasmota/discovery/.*/config")) {
            Map<String, Object> stringObjectMap = JsonHelper.jsonStringToMap("$.discovery", new String(payload));
            try {
                String JSON_KEY_DISCOVERY_TOPIC = "$.discovery.t";
                Object topicValue = stringObjectMap.get(JSON_KEY_DISCOVERY_TOPIC);
                String deviceId = (String) topicValue;
                return deviceId;
            } catch (Exception ex) {
                logger.error("Error while parsing Topic: {} Message {}\n" + //
                        "Exception:\n" + //
                        "{}", topic, new String(payload), ExceptionHelper.compactStackTrace(ex));
                return null;
            }
        }

        String[] strings = topic.split("/");
        if (strings.length > 2) {
            String deviceId = strings[1];
            return deviceId;
        }
        return null;
    }

    public static Map<String, ?> toPropertiesFromMessage(String topic, byte[] payload) {
        Map<String, Object> stateMap = transformMessage(topic, payload);
        Map<String, Object> resultMap = new TreeMap();
        for (Map.Entry<String, Object> entry : stateMap.entrySet()) {
            MessageConfigItem configItem = TasmotaMessageItemConfig.getConfigItem(topic, entry.getKey(),
                    entry.getValue());

            @Nullable
            String propertyName = configItem.getPropertyName();

            if (null != propertyName) {
                resultMap.put(propertyName, entry.getValue());
            }
        }
        return resultMap;
    }
}
