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

        if (!(jsonString.contains("{") || jsonString.contains("["))) {
            result.put(baseKey, jsonString);
            return result;
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            // convert JSON string to Map
            logger.debug("jsonToMapFasterXml({})", jsonString);

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

    private static Map<String, Object> jsonNodeToMap(String baseKey, JsonNode jsonNode) {
        if (jsonNode.isArray()) {
            return jsonNodeArrayToMap(baseKey, jsonNode);
        } else if (jsonNode.isValueNode()) {
            Map<String, Object> resultTreeMap = new TreeMap<>();
            resultTreeMap.put(baseKey, jsonNode);
            return resultTreeMap;
        } else {
            return jsonNodeMapToMap(baseKey, jsonNode);
        }
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
            result.putAll(jsonNodeToMap(baseKey + "." + count++, elment));
        }
        return result;
    }
}
