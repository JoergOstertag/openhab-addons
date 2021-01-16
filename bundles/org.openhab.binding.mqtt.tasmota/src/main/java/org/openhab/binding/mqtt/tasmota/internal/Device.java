package org.openhab.binding.mqtt.tasmota.internal;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.smarthome.io.transport.mqtt.MqttBrokerConnection;
import org.eclipse.smarthome.io.transport.mqtt.MqttMessageSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
        connection.subscribe(getStateTopic("POWER"), this);
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

    public CompletableFuture<Boolean> command(String name, String value) {
        final String topic = getCommandTopic(name);
        return publish(topic, value);
    }

    public CompletableFuture<Boolean> publish(String topic, String value) {
        logger.debug("Publishing to topic {}: {}", topic, value);
        return connection.publish(topic, value.getBytes());
    }

    @Override
    public void processMessage(String topic, byte[] payload) {
        String[] parts = topic.split("/");
        if (parts.length != 3) {
            logger.warn("Unknown topic format", topic);
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
        return gson.fromJson(state, TasmotaState.class);
    }

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

    private void processTelemetryMessage(String name, String payload) {
        switch (name) {
            case "STATE":
                listener.processState(parseState(payload));
                break;

            default:
                listener.processTelemetryMessage(name, payload);
                break;
        }
    }

    public void update() {
        command("STATE", "");
    }

    protected class TasmotaState {
        String POWER;
        Date Time;
        Integer Dimmer;
    }
}
