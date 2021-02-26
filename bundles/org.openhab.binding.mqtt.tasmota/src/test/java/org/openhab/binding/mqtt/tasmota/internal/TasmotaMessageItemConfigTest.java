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

import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openhab.binding.mqtt.tasmota.utils.AbstractTest;
import org.openhab.binding.mqtt.tasmota.utils.JsonHelper;
import org.openhab.binding.mqtt.tasmota.utils.MockExampleMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link TasmotaMessageItemConfig} is responsible for transforming the incoming Map from the json conversion to
 * Single Entries
 *
 * @author Jörg Ostertag - Initial contribution
 */
@NonNullByDefault
class TasmotaMessageItemConfigTest extends AbstractTest {

    private final Logger logger = LoggerFactory.getLogger(new Object() {
    }.getClass().getEnclosingClass());

    Map<String, String> exampleMessages = MockExampleMessages.getExampleMessages();

    @BeforeEach
    void setUp() {
    }

    @Test
    void transformToStateMap() {
        for (Map.Entry<String, String> exampleMessage : exampleMessages.entrySet()) {
            String topic = exampleMessage.getKey();
            String message = exampleMessage.getValue();
            logger.info("stringObjectMap: {}", message);
            Map<String, Object> stateMap = JsonHelper.jsonStringToMap("", message);
            for (Map.Entry<String, Object> entry : stateMap.entrySet()) {
                MessageConfigItem configItem = TasmotaMessageItemConfig.getConfigItem(topic, entry.getKey(),
                        entry.getValue());
                logger.debug("key: {}, value: {}", entry.getKey(), entry.getValue());
                logger.debug("configItem: {}", configItem);
            }
        }
    }
}
