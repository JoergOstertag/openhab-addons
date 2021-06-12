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

import org.eclipse.jdt.annotation.NonNull;
import org.openhab.binding.mqtt.tasmota.internal.deviceState.TasmotaState;

/**
 * @author Daan Meijer - Initial contribution
 * @author Jörg Ostertag - Adaptions to compile with openhab-3.1.0-SNAPSHOT
 */
public interface TasmotaListener {

    void processVariableState(@NonNull String name, @NonNull String payload);

    void processTelemetryMessage(@NonNull String name, @NonNull String payload);

    void processState(@NonNull TasmotaState state);
}
