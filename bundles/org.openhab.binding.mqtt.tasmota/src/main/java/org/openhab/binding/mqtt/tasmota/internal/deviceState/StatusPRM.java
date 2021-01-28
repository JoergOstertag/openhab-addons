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

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Daan Meijer - Initial contribution
 * @author Jörg Ostertag - Modifications to compile with openhab-3.1.0
 *
 */
public class StatusPRM {
    public String BCResetTime;
    public Integer Baudrate;
    public Integer BootCount;
    public Integer CfgHolder;
    public String GroupTopic; // "tasmotas",
    public String OtaUrl; // "http://ota.tasmota.com/tasmota/release-9.2.0/tasmota-sensors.bin.gz",
    public String RestartReason; // "Hardware Watchdog",
    public String SaveAddress; // "F9000",
    public Integer SaveCount; // 68744,
    public String SerialConfig; // "8N1",
    public Integer Sleep; // 100,
    public String StartupUTC; // "2021-01-11T06:39:47",
    public String Uptime; // "4T14:13:39"

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
