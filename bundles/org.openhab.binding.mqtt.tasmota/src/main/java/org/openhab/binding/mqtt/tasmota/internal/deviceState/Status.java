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

import java.util.List;

/**
 * @author Daan Meijer - Initial contribution
 * @author Jörg Ostertag - Modifications to compile with openhab-3.1.0
 *
 */
public class Status {
    Integer Module; // 43
    String DeviceName; // Kueche
    List<String> FriendlyName; // ":["Pow-1-Kueche"]
    public String Topic; // ":"Pow-1"
    public Integer ButtonTopic; // ":"0"
    public Integer Power; // ":1
    public Integer PowerOnState; // 3
    public Integer LedState; // ":1
    public String LedMask; // ":"FFFF"
    public Integer SaveData; // ":1;
    public Integer SaveState; // ":1;
    public String SwitchTopic; // ":"0";
    public List<Integer> SwitchMode; // ":[0,0,0,0,0,0,0,0];
    public Integer ButtonRetain; // ":0;
    public Integer SwitchRetain; // ":0;
    public String SensorRetain; // ":0;
    public Integer PowerRetain; // ":0}}
}
