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
 *
 *
 */

package org.openhab.binding.mqtt.tasmota.internal.deviceState;

import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Daan Meijer - Initial contribution
 * @author Jörg Ostertag - Modifications to compile with openhab-3.1.0
 *
 */
public class TasmotaState {
    public Integer Dimmer;
    public String POWER;
    public Date Time;
    public Integer Heap;
    public Integer LoadAvg;
    public Integer MqttCount;
    public Integer Sleep;
    public String SleepMode;
    public String Uptime;
    public Integer UptimeSec;
    public Energy ENERGY;
    public DHT11 Dht11;
    public Wifi Wifi;

    public StatusSNS StatusSNS;
    public StatusPRM StatusPRM;
    public StatusSTS StatusSTS;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
