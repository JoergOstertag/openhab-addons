package org.openhab.binding.mqtt.tasmota.internal;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class MqttMessage {

    public String topic;

    /**
     * Empty messages are represented by ""
     */
    public String message;

    public MqttMessage(String topic, String message) {
        this.topic = topic;
        this.message = message;
    }
}
