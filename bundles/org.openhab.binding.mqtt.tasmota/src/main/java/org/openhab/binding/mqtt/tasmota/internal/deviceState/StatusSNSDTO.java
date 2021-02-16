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
package org.openhab.binding.mqtt.tasmota.internal.deviceState;

import com.google.gson.annotations.SerializedName;

/**
 * @author Daan Meijer - Initial contribution
 * @author Jörg Ostertag - Modifications to compile with openhab-3.1.0
 */
public class StatusSNSDTO extends AbstractJsonDTO {
    // {"StatusSNS":{"Time":"2021-01-28T21:14:52",
    // "ENERGY":{"TotalStartTime":"2020-" ... "Current":0.128}}}
    public EnergyDTO ENERGY;

    public Dht11DTO DHT11;

    public AnalogDTO ANALOG;

    @SerializedName(value = "SHT3X-0x45")
    public Sht3X_0x45DTO SHT3X_0x45;

    public Ds18B20DTO DS18B20;

    public String TempUnit;

    public String Time;
}
