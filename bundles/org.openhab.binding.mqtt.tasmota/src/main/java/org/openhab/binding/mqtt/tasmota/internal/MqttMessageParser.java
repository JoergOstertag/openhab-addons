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

import static org.openhab.binding.mqtt.tasmota.internal.TasmotaBindingConstants.debugSkipPropertyUpdate;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.jetbrains.annotations.NotNull;
import org.openhab.binding.mqtt.tasmota.internal.deviceState.*;
import org.openhab.binding.mqtt.tasmota.utils.ExceptionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;

/**
 * @author Jörg Ostertag - Parse more of the Json responses from Tasmota
 */
public class MqttMessageParser {

    private static final Logger logger = LoggerFactory.getLogger(new Object() {
    }.getClass().getEnclosingClass());

    private static final Gson gson = new GsonBuilder() //
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss") //
            .setLongSerializationPolicy(LongSerializationPolicy.DEFAULT) //
            .create();

    public static @NonNull TasmotaStateDTO parseMessage(String stateAsJson) {
        TasmotaStateDTO tasmotaStateDTOFromJson = null;
        try {
            tasmotaStateDTOFromJson = gson.fromJson(stateAsJson, TasmotaStateDTO.class);

            boolean forDebugCheckIfAllIsParsed = false;
            if (forDebugCheckIfAllIsParsed) {
                String toJson = gson.toJson(tasmotaStateDTOFromJson);
                if (Math.abs(stateAsJson.length() - toJson.length()) > 12) {
                    System.out.println();
                    System.out.println("in:  " + stateAsJson);
                    System.out.println("out: " + toJson);
                    System.out.println();
                }
            }
        } catch (Exception ex) {
            logger.error("Error parsing json: {}", ExceptionHelper.compactStackTrace());
        }
        if (null == tasmotaStateDTOFromJson) {
            tasmotaStateDTOFromJson = new TasmotaStateDTO();
        }
        return tasmotaStateDTOFromJson;
    }

    public static Map<@NonNull String, @NonNull Object> stateToHashMap(TasmotaStateDTO tasmotaState) {

        Map<String, Object> stateMap = new HashMap<>();
        stateMap.putAll(StateMapTransformer.parseSensors(tasmotaState));
        stateMap.putAll(StateMapTransformer.transformConfigItemsToStateMap(tasmotaState));

        return stateMap;
    }

    @NotNull
    public static Map<String, String> getPropertiesStringMap(TasmotaStateDTO tasmotaState) {
        Map<String, Object> properties = MqttMessageParser.stateToHashMap(tasmotaState);
        Map<String, String> propertiesString = new HashMap<>();
        for (Map.Entry<String, Object> property : properties.entrySet()) {
            String propertyName = property.getKey();
            Object propertyValue = property.getValue();

            if (debugSkipPropertyUpdate //
                    && !propertyName.matches("Sensor|IPAddress")) {
                continue;
            }

            if (null != propertyValue) {
                propertiesString.put(propertyName, String.valueOf(propertyValue));
                logger.trace("updateProperty({},{})", propertyName, String.valueOf(propertyValue));
            }
        }
        return propertiesString;
    }

    public static boolean parseDeviceTypeKnown(TasmotaStateDTO tasmotaState, Map<String, Object> stateMap) {
        boolean deviceKnown = false;
        deviceKnown |= (tasmotaState.Dimmer != null);
        deviceKnown |= (tasmotaState.POWER != null);
        if (null != tasmotaState.Status) {
            deviceKnown |= (tasmotaState.Status.Power != null);
        }

        EnergyDTO energyDTO = tasmotaState.ENERGY;
        if (null == energyDTO && (null != tasmotaState.StatusSNS)) {
            energyDTO = tasmotaState.StatusSNS.ENERGY;
        }
        if (null != energyDTO) {
            deviceKnown |= (energyDTO.Power != null);
            deviceKnown |= (energyDTO.Current != null);
        }

        Dht11DTO dht11DTO = tasmotaState.Dht11;
        if (null == dht11DTO && null != tasmotaState.StatusSNS) {
            dht11DTO = tasmotaState.StatusSNS.DHT11;
        }
        if (null != dht11DTO) {
            deviceKnown |= (dht11DTO.Temperature != null);
            deviceKnown |= (dht11DTO.Humidity != null);
        }
        Ds18B20DTO ds18B20DTO = tasmotaState.DS18B20;
        if (null == ds18B20DTO && null != tasmotaState.StatusSNS) {
            ds18B20DTO = tasmotaState.StatusSNS.DS18B20;
        }
        if (null != ds18B20DTO) {
            deviceKnown |= (ds18B20DTO.Temperature != null);
        }

        for (Map.Entry<String, Object> property : stateMap.entrySet()) {
            String propertyName = property.getKey();
            Object propertyValue = property.getValue();
            deviceKnown |= (propertyName.startsWith("Sensor.") && (null != propertyValue));
        }
        return deviceKnown;
    }

    public static TasmotaStateDTO parseMessage(String topic, byte[] payload) {

        String[] parts = topic.split("/");
        if (parts.length != 3) {
            logger.warn("Unknown topic format: {}", topic);
            return new TasmotaStateDTO();
        }

        String base = parts[0];
        String deviceId = parts[1];
        String name = parts[2];

        if (name.matches("(STATE|SENSOR|STATUS.*)")) {
            return parseMessage(new String(payload));
        } else if (topic.startsWith("tasmota/discovery/")) {
            TasmotaDiscoveryDTO tasmotaDiscoveryDTO = gson.fromJson(new String(payload), TasmotaDiscoveryDTO.class);
            logger.warn("Tasmota discovery topic not supported yet: Topic: {}", topic);
        }
        return new TasmotaStateDTO();
    }

    /**
     * @param topic A topic like "tele/office/STATE"
     * @param payload
     * @return Returns the "office" part of the example
     */
    public static @org.eclipse.jdt.annotation.Nullable String extractDeviceID(String topic, byte[] payload) {
        if (topic.matches("tasmota/discovery/.*/config")) {
            try {
                TasmotaDiscoveryDTO tasmotaDiscoveryDTO = gson.fromJson(new String(payload), TasmotaDiscoveryDTO.class);
                String deviceId = tasmotaDiscoveryDTO.topic;
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
            return strings[1];
        }
        return null;
    }
}
