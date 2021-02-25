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

import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jörg Ostertag - Initial contribution
 */
@NonNullByDefault
class JsonHelperTest {
    private final Logger logger = LoggerFactory.getLogger(new Object() {
    }.getClass().getEnclosingClass());

    Map<String, String> exampleMessages = MockExampleMessages.getExampleMessages();

    @BeforeEach
    void setUp() {
    }

    @Test
    void jsonToMap() {
        for (Map.Entry<String, String> exampleMessage : exampleMessages.entrySet()) {
            String topic = exampleMessage.getKey();
            String message = exampleMessage.getValue();
            if (true || message.startsWith("{")) {
                logger.info("stringObjectMap: {}", message);
                Map<String, Object> stringObjectMap = JsonHelper.jsonStringToMap("$", message);
                logger.info("stringObjectMap: {}", stringObjectMap);
                for (Map.Entry<String, Object> element : stringObjectMap.entrySet()) {
                    logger.info("\t{}:\t{}", element.getKey(), element.getValue());
                }
            }

        }
    }
}
