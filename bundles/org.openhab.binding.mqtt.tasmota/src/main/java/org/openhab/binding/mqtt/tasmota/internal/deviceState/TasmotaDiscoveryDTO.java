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

import java.util.List;

/**
 * @author Daan Meijer - Initial contribution
 * @author Jörg Ostertag - Modifications to compile with openhab-3.1.0
 */
public class TasmotaDiscoveryDTO extends AbstractJsonDTO {

    public String ip; // "192.168.10.103"

    @SerializedName(value = "dn")
    public String deviceName; // "x3"

    @SerializedName(value = "fn")
    public List<String> friendlyName; // ["Basic-3 x3",null,null,null,null,null,null,null]

    @SerializedName(value = "hn")
    public String hostName; // "ESP-Basic-3"

    public String mac; // "84F3EB487D21"

    @SerializedName(value = "md")
    public String module; // "Sonoff Basic"

    public String ty; // 0

    @SerializedName(value = "if")
    public String ifVal; // 0

    public String ofln; // "Offline"
    public String onln; // "Online"

    @SerializedName(value = "state")
    public List<String> state; // ["OFF","ON","TOGGLE","HOLD"]

    @SerializedName(value = "sw")
    public String sw; // "9.2.0"

    @SerializedName(value = "t")
    public String topic; // "Basic-3"

    @SerializedName(value = "ft")
    public String fullTopic; // "%prefix%/%topic%/"

    @SerializedName(value = "tp")
    public List<String> tp; // ["cmnd","stat","tele"]

    @SerializedName(value = "rl")
    public List<String> relayList; // [1,0,0,0,0,0,0,0]

    public List<String> swc; // [-1,-1,-1,-1,-1,-1,-1,-1]

    @SerializedName(value = "swn")
    public List<String> switchList; // [null,null,null,null,null,null,null,null]

    @SerializedName(value = "btn")
    public List<String> buttonList; // [0,0,0,0]

    // @SerializedName(value = "so")
    // public String setOption; // {"4":0,"11":0,"13":0,"17":1,"20":0,"30":0,"68":0,"73":0,"82":0,"114":0}

    public String lk; // 1

    public String lt_st; // 0

    public List<String> sho; // [0,0,0,0]

    @SerializedName(value = "ver")
    public String discoveryProtocolVersion; // 1
}
