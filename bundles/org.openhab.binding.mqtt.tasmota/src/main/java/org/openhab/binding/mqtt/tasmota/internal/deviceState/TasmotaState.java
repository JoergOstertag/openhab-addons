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

import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * @author Daan Meijer - Initial contribution
 * @author Jörg Ostertag - Modifications to compile with openhab-3.1.0
 */
public class TasmotaState extends AbstractJsonDTO {

    public String POWER;
    public String POWER1;
    public String POWER2;
    public String POWER3;
    public String POWER4;

    public Integer Dimmer;

    public String Color;
    public String HSBColor;
    public List<String> Channel;
    public String Scheme;
    public String Fade;
    public String Speed;
    public String LedTable;

    public String TempUnit;

    public String Var1;
    public String Var2;
    public String Var3;
    public String Var4;
    public String Var5;

    public Date Time;
    public Integer Heap;
    public Integer LoadAvg;
    public Integer MqttCount;
    public Integer Sleep;
    public String SleepMode;
    public String Uptime;
    public String Downtime;
    public Integer UptimeSec;
    public Integer TelePeriod;

    public Energy ENERGY;

    public DS18B20 DS18B20;

    public ANALOG ANALOG;

    @SerializedName(value = "Dht11", alternate = "DHT11")
    public DHT11 Dht11;

    @SerializedName(value = "SHT3X-0x45")
    public SHT3X_0x45 SHT3X_0x45;

    public String Event;

    public Wifi Wifi;

    public Status Status;
    public StatusFWR StatusFWR;
    public StatusLOG StatusLOG;
    public StatusMEM StatusMEM;
    public StatusMQT StatusMQT;
    public StatusNET StatusNET;
    public StatusPRM StatusPRM;
    public StatusPTH StatusPTH;
    public StatusSNS StatusSNS;
    public StatusSTS StatusSTS;
    public StatusSTK StatusSTK;
    public StatusTIM StatusTIM;
}
