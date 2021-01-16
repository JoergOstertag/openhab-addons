package org.openhab.binding.mqtt.tasmota.internal;

import org.eclipse.jdt.annotation.NonNull;
import org.openhab.binding.mqtt.tasmota.internal.Device.TasmotaState;

public interface TasmotaListener {

    void processVariableState(@NonNull String name, @NonNull String payload);

    void processTelemetryMessage(@NonNull String name, @NonNull String payload);

    void processState(@NonNull TasmotaState state);
}
