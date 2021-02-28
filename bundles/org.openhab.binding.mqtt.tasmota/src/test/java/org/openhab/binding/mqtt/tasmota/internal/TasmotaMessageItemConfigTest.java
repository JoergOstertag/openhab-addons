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

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.junit.jupiter.api.Test;
import org.openhab.binding.mqtt.tasmota.test.AbstractTest;
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

    @Test
    void transformToStateMap() throws IOException {
        List<MqttMessage> exampleMqttMessageList = MockExampleMessages.getExampleMqttMessages();

        for (MqttMessage mqttMessage : exampleMqttMessageList) {
            logger.info("stringObjectMap: {}", mqttMessage.message);
            Map<String, Object> stateMap = JsonHelper.jsonStringToMap("$", mqttMessage.message);
            for (Map.Entry<String, Object> entry : stateMap.entrySet()) {
                MessageConfigItem configItem = TasmotaMessageItemConfig.getConfigItem(mqttMessage.topic, entry.getKey(),
                        entry.getValue());
                logger.debug("key: {}, value: {}", entry.getKey(), entry.getValue());
                logger.debug("configItem: {}", configItem);
            }
        }
    }
}
