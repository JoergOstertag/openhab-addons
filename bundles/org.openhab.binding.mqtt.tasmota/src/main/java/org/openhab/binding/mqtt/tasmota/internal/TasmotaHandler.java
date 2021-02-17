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

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.mqtt.tasmota.internal.deviceState.TasmotaStateDTO;

/**
 * @author Daan Meijer - Initial contribution
 * @author Jörg Ostertag - Adaptions to compile with openhab-3.1.0-SNAPSHOT
 */
public interface TasmotaHandler {

    void processVariableState(@NonNull String name, @NonNull String payload);

    void processTelemetryMessage(@NonNull String name, @NonNull String payload);

    void processState(@NonNull TasmotaStateDTO state);

    void updateExistingThing(@NonNull TasmotaStateDTO tasmotaStateDTO);

    void processMessage(@NonNull String topic, @Nullable byte[] payload);
}
