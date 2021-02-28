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
package org.openhab.binding.mqtt.tasmota.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.junit.jupiter.api.Test;
import org.openhab.binding.mqtt.tasmota.internal.MqttMessage;
import org.openhab.binding.mqtt.tasmota.test.AbstractTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jörg Ostertag - Initial contribution
 */
@NonNullByDefault
class JsonHelperTest extends AbstractTest {
    private final Logger logger = LoggerFactory.getLogger(new Object() {
    }.getClass().getEnclosingClass());

    @Test
    void jsonToMap() throws IOException {
        List<MqttMessage> exampleMqttMessageList = MockExampleMessages.getExampleMqttMessages();

        for (MqttMessage mqttMessage : exampleMqttMessageList) {
            logger.info("stringObjectMap: {}", mqttMessage.message);
            Map<String, Object> stringObjectMap = JsonHelper.jsonStringToMap("$", mqttMessage.message);
            logger.info("stringObjectMap: {}", stringObjectMap);
            for (Map.Entry<String, Object> element : stringObjectMap.entrySet()) {
                logger.info("\t{}:\t{}", element.getKey(), element.getValue());
            }

        }
    }
}
