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

import java.util.List;

/**
 * @author Daan Meijer - Initial contribution
 * @author Jörg Ostertag - Modifications to compile with openhab-3.1.0
 */
public class StatusSTS extends AbstractJsonDTO {

    public Integer Heap; // " : 22,
    public Integer LoadAvg; // " : 9,
    public Integer MqttCount; // " : 54,
    public Integer Sleep; // " : 100,
    public String SleepMode; // " : "Dynamic",
    public String Time; // " : "2021-01-15T21:53:26",
    public String Uptime; // " : "2T13:31:22",
    public Integer UptimeSec; // " : 221482,
    public Wifi Wifi;

    public String Color;
    public String HSBColor;
    public List<String> Channel;
    public String Scheme;
    public String Fade;
    public String Speed;
    public String LedTable;

    public String POWER; // "OFF",
    public String POWER1;
    public String POWER2;
    public String POWER3;
    public String POWER4;
}
