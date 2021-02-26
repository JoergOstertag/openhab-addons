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
 */
package org.openhab.binding.mqtt.tasmota.internal;

import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.mqtt.tasmota.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link TasmotaMessageItemConfig} is responsible for transforming the incoming Map from the json conversion to
 * Single Entries
 *
 * @author Jörg Ostertag - Initial Contribution
 */
@NonNullByDefault
public class TasmotaMessageItemConfig {

    private static final Logger logger = LoggerFactory.getLogger(new Object() {
    }.getClass().getEnclosingClass());

    static Map<String, MessageConfigItem> configItems = new TreeMap<>();

    static boolean initialized = false;

    private static Map<String, MessageConfigItem> getConfigMap() {
        if (initialized) {
            return configItems;
        }

        addActor("POWER");
        addActor("POWER1");
        addActor("POWER2");
        addActor("POWER3");
        addActor("POWER4");
        addActor("Dimmer"); // "$.Dimmer": 30

        addSensor("DS18B20.Temperature");
        addStatus("DS18B20.Id"); // "$.DS18B20.Id": 01162E1C14EE

        addSensor("DHT11.Temperature");
        addSensor("DHT11.Humidity");
        addSensor("DHT11.DewPoint");

        addSensor("SHT3X-0x45.Temperature"); // "$.SHT3X-0x45.Temperature": 31.4
        addSensor("SHT3X-0x45.Humidity"); // "$.SHT3X-0x45.Humidity": 30.0
        addSensor("SHT3X-0x45.DewPoint"); // "$.SHT3X-0x45.DewPoint": 11.7

        addSensor("ENERGY.TotalStartTime");
        addSensor("ENERGY.Total");
        addSensor("ENERGY.Yesterday");
        addSensor("ENERGY.Today");
        addSensor("ENERGY.Period");
        addSensor("ENERGY.Power");
        addSensor("ENERGY.ApparentPower");
        addSensor("ENERGY.ReactivePower");
        addSensor("ENERGY.Factor");
        addSensor("ENERGY.Voltage");
        addSensor("ENERGY.Current");
        addSensor("StatusPTH.VoltageLow"); // "$.StatusPTH.VoltageLow": 0
        addSensor("StatusPTH.PowerLow"); // "$.StatusPTH.PowerLow": 0
        addSensor("StatusPTH.PowerHigh"); // "$.StatusPTH.PowerHigh": 0

        addConfig("TelePeriod");
        addConfig("Topic"); // "$.Status.Topic": Basic-3
        addConfig("Module"); // $.Status.Module": 1
        addConfig("Sleep"); // "$.Sleep": 100
        addConfig("DeviceName"); // "$.Status.DeviceName": WZ 3+4fach Steckdose
        addConfig("FriendlyName.0"); // "$.Status.FriendlyName.0": Pow-4 WZ 3+4fach Steckdose
        addConfig("Status.PowerOnState"); // "$.Status.PowerOnState": 3
        addConfig("Status.PowerRetain"); // "$.Status.PowerRetain": 0
        addConfig("Status.SaveData"); // "$.Status.SaveData": 1
        addConfig("Status.SaveState"); // "$.Status.SaveState": 1
        addConfig("Status.SensorRetain"); // "$.Status.SensorRetain": 0
        addConfig("Status.SwitchMode.0"); // "$.Status.SwitchMode.0": 0
        addConfig("Status.SwitchMode.1"); // "$.Status.SwitchMode.1": 0
        addConfig("Status.SwitchMode.2"); // "$.Status.SwitchMode.2": 0
        addConfig("Status.SwitchMode.3"); // "$.Status.SwitchMode.3": 0
        addConfig("Status.SwitchMode.4"); // "$.Status.SwitchMode.4": 0
        addConfig("Status.SwitchMode.5"); // "$.Status.SwitchMode.5": 0
        addConfig("Status.SwitchMode.6"); // "$.Status.SwitchMode.6": 0
        addConfig("Status.SwitchMode.7"); // "$.Status.SwitchMode.7": 0
        addConfig("Status.SwitchRetain"); // "$.Status.SwitchRetain": 0
        addConfig("Status.SwitchTopic"); // "$.Status.SwitchTopic": 0

        addConfig("TempUnit"); // "$.TempUnit": C
        addConfig("SleepMode"); // "$.SleepMode": Dynamic
        addConfig("ButtonRetain"); // "$.Status.ButtonRetain": 0
        addConfig("ButtonTopic"); // "$.Status.ButtonTopic": 0
        addConfig("FriendlyName.1"); // "$.Status.FriendlyName.1": Sonoff Touch 0b
        addConfig("InfoRetain"); // "$.Status.InfoRetain": 0
        addConfig("LedMask"); // "$.Status.LedMask": FFFF
        addConfig("LedState"); // "$.Status.LedState": 1
        addConfig("StateRetain"); // "$.Status.StateRetain": 0

        addSensor("StatusPTH.VoltageLow"); // "$.StatusPTH.VoltageLow": 0
        addStatus("StatusSNS.Time"); // "$.StatusSNS.Time": 2021-02-26T20:16:41
        addStatus("Heap");// "Heap":26,
        addStatus("MqttCount"); // $.MqttCount": 42
        addStatus("Time"); // "Time":"2021-02-26T04:10:06"
        addStatus("Uptime");// "Uptime":"0T14:46:14",
        addStatus("UptimeSec"); // $.UptimeSec": 362015
        addStatus("LoadAvg"); // $.LoadAvg": 17
        addStatus("Wifi.AP"); // "$.Wifi.AP": 1
        addStatus("Wifi.BSSId"); // $.Wifi.BSSId": 20:0D:B0:57:F7:C4
        addStatus("Wifi.Channel"); // "$.Wifi.Channel": 6
        addStatus("Wifi.Downtime"); // "$.Wifi.Downtime": 0T00:00:07
        addStatus("Wifi.LinkCount"); // $.Wifi.LinkCount": 1
        addStatus("Wifi.RSSI"); // $.Wifi.RSSI": 100
        addStatus("Wifi.SSId"); // $.Wifi.SSId": my-SSid
        addStatus("Wifi.Signal"); // $.Wifi.Signal": -23

        addStatus("StatusMEM.FlashChipId"); // "$.StatusMEM.FlashChipId": 1640EF
        addStatus("StatusMEM.FlashFrequency"); // "$.StatusMEM.FlashFrequency": 40
        addStatus("StatusMEM.FlashMode"); // "$.StatusMEM.FlashMode": 3
        addStatus("StatusMEM.FlashSize"); // "$.StatusMEM.FlashSize": 1024
        addStatus("StatusMEM.Free"); // "$.StatusMEM.Free": 416
        addStatus("StatusMEM.Heap"); // "$.StatusMEM.Heap": 25
        addStatus("StatusMEM.ProgramFlashSize"); // "$.StatusMEM.ProgramFlashSize": 1024
        addStatus("StatusMEM.ProgramSize"); // "$.StatusMEM.ProgramSize": 586
        addStatus("StatusMEM.Drivers"); // "$.StatusMEM.Drivers": 1,2,3,4,5,6,7,8,9,10,12,14,16,17,20,21,24,29,34
        addStatus("StatusMEM.Features.0"); // "$.StatusMEM.Features.0": 00000809
        addStatus("StatusMEM.Features.1"); // "$.StatusMEM.Features.1": 8FDA8787
        addStatus("StatusMEM.Features.2"); // "$.StatusMEM.Features.2": 0415A005
        addStatus("StatusMEM.Features.3"); // "$.StatusMEM.Features.3": B7FFBFCF
        addStatus("StatusMEM.Features.4"); // "$.StatusMEM.Features.4": 01DA9BC4
        addStatus("StatusMEM.Features.5"); // "$.StatusMEM.Features.5": 64367CC7
        addStatus("StatusMEM.Features.6"); // "$.StatusMEM.Features.6": 00084052
        addStatus("StatusMEM.Features.7"); // "$.StatusMEM.Features.7": 00000000
        addStatus("StatusMEM.FlashChipId"); // "$.StatusMEM.FlashChipId": 16405E
        addStatus("StatusMEM.FlashFrequency"); // "$.StatusMEM.FlashFrequency": 40
        addStatus("StatusMEM.FlashMode"); // "$.StatusMEM.FlashMode": 3
        addStatus("StatusMEM.FlashSize"); // "$.StatusMEM.FlashSize": 4096
        addStatus("StatusMEM.Free"); // "$.StatusMEM.Free": 364
        addStatus("StatusMEM.Heap"); // "$.StatusMEM.Heap": 19
        addStatus("StatusMEM.ProgramFlashSize"); // "$.StatusMEM.ProgramFlashSize": 1024
        addStatus("StatusMEM.ProgramSize"); // "$.StatusMEM.ProgramSize": 638
        addStatus("StatusMEM.Sensors"); // "$.StatusMEM.Sensors":
        // 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,26,28,31,34,37,39,40,42,43,51,52,55,56,58,59,62,64,66,67,74

        addStatus("StatusTIM.UTC"); // "$.StatusTIM.UTC": 2021-02-26T19:10:35
        addStatus("StatusTIM.Timezone"); // "$.StatusTIM.Timezone": 99
        addStatus("StatusTIM.Sunset"); // "$.StatusTIM.Sunset": 17:51
        addStatus("StatusTIM.Sunrise"); // "$.StatusTIM.Sunrise": 06:58
        addStatus("StatusTIM.StartDST"); // "$.StatusTIM.StartDST": 2021-03-28T02:00:00
        addStatus("StatusTIM.Local"); // "$.StatusTIM.Local": 2021-02-26T20:10:35
        addStatus("StatusTIM.EndDST"); // "$.StatusTIM.EndDST": 2021-10-31T03:00:00
        addStatus("StatusTIM.UTC"); // "$.StatusTIM.UTC": 2021-02-26T19:10:35

        addStatus("StatusMEM.Free"); // "$.StatusMEM.Free": 364

        // initialized=true;

        return configItems;
    }

    public static MessageConfigItem getConfigItem(String topic, String key1, Object value) {
        String key = MessageConfigItem.getUnifiedKey(topic, key1);

        Map<String, MessageConfigItem> configMap = getConfigMap();
        MessageConfigItem messageConfigItem = configMap.get(key);
        if (null != messageConfigItem) {
            return messageConfigItem;
        }

        logger.trace("Missing Configuration for key {} with value {}", key, value);
        messageConfigItem = createMissingMessageConfigItem(topic, key1, value);

        return messageConfigItem;
    }

    private static MessageConfigItem createMissingMessageConfigItem(String topic, String key, Object value) {
        MessageConfigItem messageConfigItem = new MessageConfigItem(topic, key);
        messageConfigItem.setPropertyName(key.replace("$.", ""));

        String shortKey = key.replaceAll("(\\$\\.|statusSNS|statusPTH)", "");
        String filename = "/tmp/openhab/TasmotaMesageItemConfig.java.suggestion";
        if (key.matches(".*status.*SNS.*")) {
            FileUtils.fileAppend(filename,
                    "addSensor(\"" + shortKey + "\"); // \"" + key + "\": " + String.valueOf(value) + "\n");
        } else {
            FileUtils.fileAppend(filename,
                    "addConfig(\"" + shortKey + "\"); // \"" + key + "\": " + String.valueOf(value) + "\n");

            FileUtils.fileAppend(filename,
                    "addStatus(\"" + shortKey + "\"); // \"" + key + "\": " + String.valueOf(value) + "\n");
        }
        return messageConfigItem;
    }

    private static MessageConfigItem addActor(String key) {
        MessageConfigItem messageConfigItem = new MessageConfigItem("", key);

        messageConfigItem.setChannelName("Actor." + key);
        messageConfigItem.setActionCmndTopic(key);

        addMessageConfigItem("$." + key, messageConfigItem);
        addMessageConfigItem("$.state." + key, messageConfigItem);
        addMessageConfigItem("$.status." + key, messageConfigItem);

        return messageConfigItem;
    }

    private static MessageConfigItem addConfig(String key) {
        MessageConfigItem messageConfigItem = new MessageConfigItem("", key);

        // XXX: For Debugging we use a Property (later a seperate Channel in a seperate ChannelGroup)
        messageConfigItem.setPropertyName("Config." + key);
        // messageConfigItem.setChannelName("Config." + key);

        addMessageConfigItem("$." + key, messageConfigItem);
        addMessageConfigItem("$.Status." + key, messageConfigItem);

        return messageConfigItem;
    }

    private static MessageConfigItem addSensor(String key) {
        MessageConfigItem messageConfigItem = new MessageConfigItem("", key);

        messageConfigItem.setChannelName("Sensor." + key);

        addMessageConfigItem("$." + key, messageConfigItem);
        if (!key.startsWith("Status")) {
            addMessageConfigItem("$.status.SNS." + key, messageConfigItem);
        }

        return messageConfigItem;
    }

    private static MessageConfigItem addStatus(String key) {
        MessageConfigItem messageConfigItem = new MessageConfigItem("", key);

        messageConfigItem.setPropertyName("Status." + key);

        addMessageConfigItem("$." + key, messageConfigItem);

        addMessageConfigItem("$.status." + key, messageConfigItem);

        return messageConfigItem;
    }

    private static void addMessageConfigItem(String key, MessageConfigItem messageConfigItem) {
        configItems.put(key.toLowerCase(), messageConfigItem);
    }
}
