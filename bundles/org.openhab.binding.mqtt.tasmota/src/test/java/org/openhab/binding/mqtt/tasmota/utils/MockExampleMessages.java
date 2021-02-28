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

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.mqtt.tasmota.internal.MqttMessage;

/**
 * @author Jörg Ostertag - Initial contribution
 */
@NonNullByDefault
public class MockExampleMessages {

    public static List<MqttMessage> getExampleMqttMessages() throws IOException {
        BufferedReader reader = ResourceHelper.bufferedReaderFromReource("example-mqtt-messages.txt");

        List<MqttMessage> exampleMqttMessageList = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {

            String[] lineArray = line.split(" ", 2);
            MqttMessage mqttMessage = new MqttMessage(lineArray[0], lineArray[1]);
            exampleMqttMessageList.add(mqttMessage);
        }
        return exampleMqttMessageList;
    }
}
