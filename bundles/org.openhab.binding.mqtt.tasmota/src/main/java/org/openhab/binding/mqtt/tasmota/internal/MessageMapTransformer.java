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
package org.openhab.binding.mqtt.tasmota.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link MessageMapTransformer} is responsible for transforming the incoming Map from the json conversion to
 * Single Entries
 *
 * @author Jörg Ostertag - Initial Contribution
 */
public class MessageMapTransformer {

    private class MessageConfigItem {
        String propertyName;
        String channelName;
    }

    private static final Logger logger = LoggerFactory.getLogger(new Object() {
    }.getClass().getEnclosingClass());

    @Nullable
    public static Map<String, Object> transformToStateMap(Map<String, Object> messageMap) {
        Map<String, Object> stateMap = new HashMap<>();

        Map<String, MessageConfigItem> config = getConfig();
        for (Map.Entry<String, Object> messageEntry : messageMap.entrySet()) {
            String key = messageEntry.getKey();
            Object value = messageEntry.getValue();

            MessageConfigItem messageConfigItem = config.get(key);
            if (null == messageConfigItem) {
                logger.error("Missing Configuration for Json Path {} with value {}", key, value);
                continue;
            }

            String propertyName = messageConfigItem.propertyName;
        }
        stateMap.put("Config.Sleep", messageMap.get("Sleep"));
        stateMap.put("Config.SleepMode", messageMap.get("SleepMode"));
        return stateMap;
    }

    private static Map<String, MessageConfigItem> getConfig() {
        Map<String, MessageConfigItem> configItems = new TreeMap<>();
        return configItems;
    }
}
