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
package org.openhab.binding.mqtt.tasmota.internal.deviceState;

import com.google.gson.annotations.SerializedName;

/**
 * @author Daan Meijer - Initial contribution
 * @author Jörg Ostertag - Modifications to compile with openhab-3.1.0
 */
public class StatusSNS extends AbstractJsonDTO {
    // {"StatusSNS":{"Time":"2021-01-28T21:14:52",
    // "ENERGY":{"TotalStartTime":"2020-" ... "Current":0.128}}}
    public Energy ENERGY;

    public DHT11 DHT11;

    public ANALOG ANALOG;

    @SerializedName(value = "SHT3X-0x45")
    public SHT3X_0x45 SHT3X_0x45;

    public DS18B20 DS18B20;

    public String TempUnit;

    public String Time;
}
