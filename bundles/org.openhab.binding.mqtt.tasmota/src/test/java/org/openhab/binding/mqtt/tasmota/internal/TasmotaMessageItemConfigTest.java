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

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openhab.binding.mqtt.tasmota.utils.AbstractTest;
import org.openhab.binding.mqtt.tasmota.utils.JsonHelper;
import org.openhab.binding.mqtt.tasmota.utils.MockExampleMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

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
    void transformToStateMap() throws IOException {
        BufferedReader reader = bufferedReaderFromReource("example-mqtt-messages.txt");

        String line;
        while ((line = reader.readLine()) != null) {

            String[] lineArray = line.split(" ", 2);
            String topic = lineArray[0];
            String message = lineArray[1];

            logger.info("stringObjectMap: {}", message);
            Map<String, Object> stateMap = JsonHelper.jsonStringToMap("$", message);
            for (Map.Entry<String, Object> entry : stateMap.entrySet()) {
                MessageConfigItem configItem = TasmotaMessageItemConfig.getConfigItem(topic, entry.getKey(),
                        entry.getValue());
                logger.debug("key: {}, value: {}", entry.getKey(), entry.getValue());
                logger.debug("configItem: {}", configItem);
            }
        }
    }

    public BufferedReader bufferedReaderFromReource(String resourceName) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(resourceName);
        BufferedReader reader = null;
        InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
        reader = new BufferedReader(streamReader);
        return reader;
    }
}
