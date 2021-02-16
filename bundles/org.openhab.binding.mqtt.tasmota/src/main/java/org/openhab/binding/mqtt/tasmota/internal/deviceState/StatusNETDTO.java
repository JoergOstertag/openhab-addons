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

/**
 * @author Daan Meijer - Initial contribution
 * @author Jörg Ostertag - Modifications to compile with openhab-3.1.0
 */
public class StatusNETDTO extends AbstractJsonDTO {

    public String Hostname; // ""ESP-Pow-1"
    public String IPAddress; // ""192.168.10.201"
    public String Gateway; // ""10.11.0.1"
    public String Subnetmask; // ""255.255.0.0"
    public String DNSServer; // ""10.11.0.1"
    public String Mac; // ""BC:DD:C2:41:2A:14"
    public Integer Webserver; // "2
    public Integer WifiConfig; // "4
    public String WifiPower; // "17.0}}
}
