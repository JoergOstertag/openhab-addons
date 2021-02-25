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

import java.util.concurrent.CompletableFuture;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.io.transport.mqtt.MqttBrokerConnection;
import org.openhab.core.io.transport.mqtt.MqttMessageSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Daan Meijer - Initial contribution
 * @author Jörg Ostertag - Adaptions to compile with openhab-3.1.0-SNAPSHOT
 */
@NonNullByDefault
public class TasmotaSubscriber implements MqttMessageSubscriber {

    public final String deviceID;

    private static final Logger logger = LoggerFactory.getLogger(TasmotaSubscriber.class);

    private MqttBrokerConnection connection;

    private TasmotaHandler tasmotaHandler;

    public TasmotaSubscriber(MqttBrokerConnection connection, String deviceID, TasmotaHandler tasmotaHandler) {
        this.deviceID = deviceID;
        this.connection = connection;
        this.tasmotaHandler = tasmotaHandler;

        connection.subscribe(getTelemetryTopic("STATE"), this);
        connection.subscribe(getTelemetryTopic("SENSOR"), this);
        connection.subscribe(getStateTopic("POWER"), this);
        connection.subscribe(getStateTopic("STATUS"), this);
        connection.subscribe(getStateTopic("STATUS1"), this);
        connection.subscribe(getStateTopic("STATUS2"), this);
        connection.subscribe(getStateTopic("STATUS3"), this);
        connection.subscribe(getStateTopic("STATUS4"), this);
        connection.subscribe(getStateTopic("STATUS5"), this);
        connection.subscribe(getStateTopic("STATUS6"), this);
        connection.subscribe(getStateTopic("STATUS7"), this);
        connection.subscribe(getStateTopic("STATUS8"), this);
        connection.subscribe(getStateTopic("STATUS9"), this);
        connection.subscribe(getStateTopic("STATUS10"), this);
        logger.debug("Device created: {}", deviceID);
    }

    public String getTopic(String type, String name) {
        return String.format("%s/%s/%s", type, deviceID, name);
    }

    public String getCommandTopic(String name) {
        return getTopic("cmnd", name);
    }

    public String getTelemetryTopic(String name) {
        return getTopic("tele", name);
    }

    public String getStateTopic(String name) {
        return getTopic("stat", name);
    }

    public CompletableFuture<Boolean> publishCommand(String name, String value) {
        String topic = getCommandTopic(name);
        return publish(topic, value);
    }

    public CompletableFuture<Boolean> publish(String topic, String value) {
        logger.debug("Publishing to topic {}: {}", topic, value);
        return connection.publish(topic, value.getBytes(), 2, false);
    }

    @Override
    public void processMessage(String topic, byte[] payload) {
        tasmotaHandler.processMessage(topic, new String(payload));
    }

    public void triggerUpdate() {
        publishCommand("STATE", "");
        publishCommand("STATUS", "0");
        publishCommand("Teleperiod", "");
    }
}
