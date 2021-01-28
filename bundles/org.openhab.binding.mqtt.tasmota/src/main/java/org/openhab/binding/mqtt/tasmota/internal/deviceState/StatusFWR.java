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

/**
 * @author Daan Meijer - Initial contribution
 * @author Jörg Ostertag - Modifications to compile with openhab-3.1.0
 *
 */
public class StatusFWR {

    public String Version; // ":"9.2.0(tasmota)"
    public String BuildDateTime; // ":"2020-12-21 T15:03:40"
    public Integer Boot; // ":31
    public String Core; // ":"2_7_4_9"
    public String SDK; // ":"2.2.2-dev(38a443e)"
    public Integer CpuFrequency; // ":80

    public String Hardware; // ":"ESP8266EX"
    public String CR; // ":"430/699"}}
}
