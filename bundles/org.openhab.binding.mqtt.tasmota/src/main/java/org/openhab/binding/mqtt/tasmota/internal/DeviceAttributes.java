package org.openhab.binding.mqtt.tasmota.internal;

import org.eclipse.jdt.annotation.NonNull;
import org.openhab.binding.mqtt.generic.mapping.AbstractMqttAttributeClass;

public class DeviceAttributes extends AbstractMqttAttributeClass {

    public String name;

    @Override
    public @NonNull Object getFieldsOf() {
        return this;
    }
}
