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
package org.openhab.binding.mqtt.tasmota.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Jörg Ostertag - Initial contribution
 */
@NonNullByDefault
public class JsonHelper {

    private static final Logger logger = LoggerFactory.getLogger(new Object() {
    }.getClass().getEnclosingClass());

    static SimpleDateFormat dateFormatTasmota = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public static Map<String, Object> jsonStringToMap(String baseKey, byte[] payload) {
        return jsonStringToMap(baseKey, new String(payload));
    }

    /**
     * converts a Json String to a flat Hashmap with one key for each value. The keys look like:
     *
     * <PRE>
     * $.StatusSTS.Wifi.Downtime: "0T00:00:07"
     * $.StatusPRM.GroupTopic: "tasmotas"
     * </PRE>
     *
     * @param baseKey
     * @param jsonString
     * @return
     */
    public static Map<String, Object> jsonStringToMap(String baseKey, String jsonString) {

        Map<String, Object> result = new TreeMap<>();

        if (!isJsonString(jsonString)) {
            result.put(baseKey, jsonString);
            return result;
        }

        ObjectMapper mapper = new ObjectMapper();
        // mapper.setDateFormat(dateFormatTasmota);

        try {
            // convert JSON string to Map
            // logger.debug("jsonStringToMap({})", jsonString);

            JsonNode jsonNode = mapper.readTree(jsonString);
            result = jsonNodeToMap(baseKey, jsonNode);
            return result;

        } catch (Exception e) {
            logger.error("Error json in parsing:\n" + //
                    "Input: {}\n" + //
                    "Exception:\n" + //
                    "{}", jsonString, ExceptionHelper.compactStackTrace(e));
        }
        return result;
    }

    /**
     * Check if we see { or [ to determine if it is possibly a json string
     *
     * @param jsonString
     * @return
     */
    private static boolean isJsonString(String jsonString) {
        return jsonString.contains("{") || jsonString.contains("[");
    }

    private static Map<String, Object> jsonNodeToMap(String baseKey, JsonNode jsonNode) {
        if (jsonNode.isArray()) {
            return jsonNodeArrayToMap(baseKey, jsonNode);
        } else if (jsonNode.isValueNode()) {
            Map<String, Object> resultTreeMap = new TreeMap<>();
            Object resultObject = toTypedObject(jsonNode);
            resultTreeMap.put(baseKey, resultObject);
            return resultTreeMap;
        } else {
            return jsonNodeMapToMap(baseKey, jsonNode);
        }
    }

    private static Object toTypedObject(JsonNode jsonNode) {
        Object result = jsonNode.asText();

        if (jsonNode.isLong()) {
            result = jsonNode.asLong();
        } else if (jsonNode.isBigInteger()) {
            result = jsonNode.asLong();
        } else if (jsonNode.isInt()) {
            result = jsonNode.asLong();
        } else if (jsonNode.isBigDecimal()) {
            result = jsonNode.asDouble();
        } else if (jsonNode.isDouble()) {
            result = jsonNode.asDouble();
        } else if (jsonNode.isBoolean()) {
            result = jsonNode.asBoolean();
        } else if (jsonNode.isFloatingPointNumber()) {
            result = jsonNode.asDouble();
        } else if (jsonNode.isNumber()) {
            result = jsonNode.asDouble();
        } else if (jsonNode.isTextual()) {
            String string = jsonNode.asText();
            result = string;
            if (string.matches("\\d\\d\\d\\d-\\d\\d-\\dT.*")) {
                try {
                    result = dateFormatTasmota.parse(string);
                } catch (ParseException | NumberFormatException e) {
                }
            }
        } else {
            result = jsonNode.toString();
        }
        return result;
    }

    private static Map<String, Object> jsonNodeMapToMap(String baseKey, JsonNode jsonNode) {
        Map<String, Object> result = new TreeMap<String, Object>();

        Iterator<Map.Entry<String, JsonNode>> mapEntrySubNodes = jsonNode.fields();
        while (mapEntrySubNodes.hasNext()) {
            Map.Entry<String, JsonNode> mapEntry = mapEntrySubNodes.next();
            String key = mapEntry.getKey();
            JsonNode value = mapEntry.getValue();

            Map<String, Object> subMap = jsonNodeToMap(baseKey + "." + key, value);
            result.putAll(subMap);

        }
        return result;
    }

    private static Map<String, Object> jsonNodeArrayToMap(String baseKey, JsonNode array) {
        Map<String, Object> result = new TreeMap<String, Object>();
        int count = 0;
        for (JsonNode elment : array) {
            result.putAll(jsonNodeToMap(baseKey + "." + count, elment));
            count++;
        }
        return result;
    }
}
