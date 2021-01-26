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

import org.eclipse.jdt.annotation.NonNull;
import org.openhab.binding.mqtt.tasmota.internal.deviceState.TasmotaState;
import org.openhab.core.io.transport.mqtt.MqttBrokerConnection;
import org.openhab.core.io.transport.mqtt.MqttMessageSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author Daan Meijer - Initial contribution
 * @author Jörg Ostertag - Adaptions to compile with openhab-3.1.0-SNAPSHOT
 *
 */
public class Device implements MqttMessageSubscriber {

    public final String deviceID;

    private final Logger logger = LoggerFactory.getLogger(Device.class);
    private MqttBrokerConnection connection;

    private TasmotaListener listener;

    public Device(@NonNull MqttBrokerConnection connection, String deviceID, TasmotaListener listener) {
        this.deviceID = deviceID;
        this.connection = connection;
        this.listener = listener;

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
        final String topic = getCommandTopic(name);
        return publish(topic, value);
    }

    public CompletableFuture<Boolean> publish(String topic, String value) {
        logger.debug("Publishing to topic {}: {}", topic, value);
        return connection.publish(topic, value.getBytes(), 2, false);
    }

    @Override
    public void processMessage(String topic, byte[] payload) {
        logger.debug("processMessage(topic: {}, payload: {}", topic, new String(payload));

        String[] parts = topic.split("/");
        if (parts.length != 3) {
            logger.warn("Unknown topic format: {}", topic);
            return;
        }

        String base = parts[0];
        String deviceId = parts[1];
        String name = parts[2];

        String strPayload = new String(payload);

        switch (base) {
            case "tele":
                processTelemetryMessage(name, strPayload);
                break;

            case "stat":
                processStateMessage(name, strPayload);
                break;
        }
    }

    private void processStateMessage(String name, String payload) {
        switch (name) {
            case "RESULT":
                break;

            default:
                listener.processVariableState(name, payload);

        }
    }

    public static @NonNull TasmotaState parseState(String state) {
        TasmotaState fromJson = gson.fromJson(state, TasmotaState.class);
        if (null == fromJson) {
            fromJson = new TasmotaState();
        }
        return fromJson;
    }

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

    private void processTelemetryMessage(String name, String payload) {
        switch (name) {
            case "STATE":
                listener.processState(parseState(payload));
                break;

            case "SENSOR":
                listener.processState(parseState(payload));
                break;

            case "STATUS":
                listener.processState(parseState(payload));
                break;

            case "STATUS1":
                listener.processState(parseState(payload));
                break;

            case "STATUS7":
                listener.processState(parseState(payload));
                break;

            case "STATUS8":
                listener.processState(parseState(payload));
                break;

            case "STATUS10":
                listener.processState(parseState(payload));
                break;

            default:
                listener.processTelemetryMessage(name, payload);
                break;
        }
    }

    public void update() {
        publishCommand("STATE", "");
        publishCommand("STATUS", "0");
    }
}
