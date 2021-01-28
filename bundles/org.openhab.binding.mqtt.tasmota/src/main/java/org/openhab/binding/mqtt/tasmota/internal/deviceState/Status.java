package org.openhab.binding.mqtt.tasmota.internal.deviceState;

import java.util.List;

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
