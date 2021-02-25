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
package org.openhab.binding.mqtt.tasmota.utils;

import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * @author Jörg Ostertag - Initial contribution
 */
@NonNullByDefault
public class MockExampleMessages {

    public static Map<String, String> getExampleMessages() {
        Map<String, String> messages = new TreeMap();

        messages.put("tele/Basic-3/LWT", "Online");
        messages.put("tele/DHT-8/LWT", "Online");
        messages.put("tele/DS1820-0/LWT", "Online");
        messages.put("tele/Pow-1/MARGINS", "{\"MARGINS\":{\"PowerDelta\":-1213}}");
        messages.put("tasmota/discovery/84F3EB487D21/config",
                "{\"ip\":\"10.211.10.103\",\"dn\":\"x3\",\"fn\":[\"Basic-3 x3\",null,null,null,null,null,null,null],\"hn\":\"ESP-Basic-3\",\"mac\":\"84F3EB487D21\",\"md\":\"Sonoff Basic\",\"ty\":0,\"if\":0,\"ofln\":\"Offline\",\"onln\":\"Online\",\"state\":[\"OFF\",\"ON\",\"TOGGLE\",\"HOLD\"],\"sw\":\"9.2.0\",\"t\":\"Basic-3\",\"ft\":\"%prefix%/%topic%/\",\"tp\":[\"cmnd\",\"stat\",\"tele\"],\"rl\":[1,0,0,0,0,0,0,0],\"swc\":[-1,-1,-1,-1,-1,-1,-1,-1],\"swn\":[null,null,null,null,null,null,null,null],\"btn\":[0,0,0,0],\"so\":{\"4\":0,\"11\":0,\"13\":0,\"17\":1,\"20\":0,\"30\":0,\"68\":0,\"73\":0,\"82\":0,\"114\":0},\"lk\":1,\"lt_st\":0,\"sho\":[0,0,0,0],\"ver\":1}");
        messages.put("tasmota/discovery/84F3EB487D21/sensors", "{\"sn\":{\"Time\":\"2021-02-24T08:50:09\"},\"ver\":1}");
        messages.put("stat/DS1820-0/STATUS",
                "{\"Status\":{\"Module\":18,\"DeviceName\":\"Temp-Dev-0\",\"FriendlyName\":[\"DS1820-0 Temp-Dev-0\"],\"Topic\":\"DS1820-0\",\"ButtonTopic\":\"0\",\"Power\":0,\"PowerOnState\":3,\"LedState\":1,\"LedMask\":\"FFFF\",\"SaveData\":1,\"SaveState\":1,\"SwitchTopic\":\"0\",\"SwitchMode\":[0,0,0,0,0,0,0,0],\"ButtonRetain\":0,\"SwitchRetain\":0,\"SensorRetain\":0,\"PowerRetain\":0}}");
        messages.put("stat/DS1820-0/STATUS1",
                "{\"StatusPRM\":{\"Baudrate\":115200,\"SerialConfig\":\"8N1\",\"GroupTopic\":\"tasmotas\",\"OtaUrl\":\"http://ota.tasmota.com/tasmota/release-9.2.0/tasmota-sensors.bin.gz\",\"RestartReason\":\"Software/System restart\",\"Uptime\":\"5T22:59:33\",\"StartupUTC\":\"2021-02-18T10:40:37\",\"Sleep\":100,\"CfgHolder\":4617,\"BootCount\":46,\"BCResetTime\":\"2020-04-20T00:27:30\",\"SaveCount\":97,\"SaveAddress\":\"F9000\"}}");
        messages.put("stat/DS1820-0/STATUS2",
                "{\"StatusFWR\":{\"Version\":\"9.2.0(sensors)\",\"BuildDateTime\":\"2020-12-21T15:04:56\",\"Boot\":31,\"Core\":\"2_7_4_9\",\"SDK\":\"2.2.2-dev(38a443e)\",\"CpuFrequency\":80,\"Hardware\":\"ESP8266EX\",\"CR\":\"427/699\"}}");
        messages.put("stat/DS1820-0/STATUS3",
                "{\"StatusLOG\":{\"SerialLog\":2,\"WebLog\":3,\"MqttLog\":0,\"SysLog\":0,\"LogHost\":\"\",\"LogPort\":514,\"SSId\":[\"my-SSID\",\"my-other-SSID\"],\"TelePeriod\":300,\"Resolution\":\"558180C0\",\"SetOption\":[\"00028009\",\"2805C8000100060000005A00000000000000\",\"00000240\",\"00006000\",\"00000000\"]}}");
        messages.put("stat/DS1820-0/STATUS4",
                "{\"StatusMEM\":{\"ProgramSize\":638,\"Free\":364,\"Heap\":20,\"ProgramFlashSize\":1024,\"FlashSize\":4096,\"FlashChipId\":\"16405E\",\"FlashFrequency\":40,\"FlashMode\":3,\"Features\":[\"00000809\",\"8FDA8787\",\"0415A005\",\"B7FFBFCF\",\"01DA9BC4\",\"64367CC7\",\"00084052\",\"00000000\"],\"Drivers\":\"1,2,3,4,5,6,7,8,9,10,12,14,16,17,20,21,24,29,34\",\"Sensors\":\"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,26,28,31,34,37,39,40,42,43,51,52,55,56,58,59,62,64,66,67,74\"}}");
        messages.put("stat/DS1820-0/STATUS5",
                "{\"StatusNET\":{\"Hostname\":\"ESP-DS1820-0\",\"IPAddress\":\"10.211.12.150\",\"Gateway\":\"10.211.0.1\",\"Subnetmask\":\"255.255.0.0\",\"DNSServer\":\"10.211.0.1\",\"Mac\":\"98:F4:AB:DA:54:80\",\"Webserver\":2,\"WifiConfig\":4,\"WifiPower\":17.0}}");
        messages.put("stat/DS1820-0/STATUS6",
                "{\"StatusMQT\":{\"MqttHost\":\"10.211.0.45\",\"MqttPort\":1883,\"MqttClientMask\":\"Client-DS1820-0\",\"MqttClient\":\"Client-DS1820-0\",\"MqttUser\":\"DVES_USER\",\"MqttCount\":30,\"MAX_PACKET_SIZE\":1200,\"KEEPALIVE\":30}}");
        messages.put("stat/DS1820-0/STATUS7",
                "{\"StatusTIM\":{\"UTC\":\"2021-02-24T09:40:10\",\"Local\":\"2021-02-24T10:40:10\",\"StartDST\":\"2021-03-28T02:00:00\",\"EndDST\":\"2021-10-31T03:00:00\",\"Timezone\":99,\"Sunrise\":\"07:02\",\"Sunset\":\"17:48\"}}");
        messages.put("stat/DS1820-0/STATUS10",
                "{\"StatusSNS\":{\"Time\":\"2021-02-24T10:40:10\",\"DS18B20\":{\"Id\":\"01162E1C14EE\",\"Temperature\":24.8},\"TempUnit\":\"C\"}}");
        messages.put("stat/DS1820-0/STATUS11",
                "{\"StatusSTS\":{\"Time\":\"2021-02-24T10:40:10\",\"Uptime\":\"5T22:59:33\",\"UptimeSec\":514773,\"Heap\":20,\"SleepMode\":\"Dynamic\",\"Sleep\":100,\"LoadAvg\":9,\"MqttCount\":30,\"Wifi\":{\"AP\":1,\"SSId\":\"my-SSID\",\"BSSId\":\"20:0D:B0:65:43:21\",\"Channel\":6,\"RSSI\":100,\"Signal\":-21,\"LinkCount\":1,\"Downtime\":\"0T00:00:07\"}}}");
        messages.put("stat/Basic-3/STATUS",
                "{\"Status\":{\"Module\":1,\"DeviceName\":\"x3\",\"FriendlyName\":[\"Basic-3 x3\"],\"Topic\":\"Basic-3\",\"ButtonTopic\":\"0\",\"Power\":0,\"PowerOnState\":3,\"LedState\":1,\"LedMask\":\"FFFF\",\"SaveData\":1,\"SaveState\":1,\"SwitchTopic\":\"0\",\"SwitchMode\":[0,0,0,0,0,0,0,0],\"ButtonRetain\":0,\"SwitchRetain\":0,\"SensorRetain\":0,\"PowerRetain\":0}}");
        messages.put("stat/Basic-3/STATUS1",
                "{\"StatusPRM\":{\"Baudrate\":115200,\"SerialConfig\":\"8N1\",\"GroupTopic\":\"tasmotas\",\"OtaUrl\":\"http://ota.tasmota.com/tasmota/release-9.2.0/tasmota.bin.gz\",\"RestartReason\":\"Power On\",\"Uptime\":\"0T01:52:04\",\"StartupUTC\":\"2021-02-24T07:48:07\",\"Sleep\":100,\"CfgHolder\":4617,\"BootCount\":160,\"BCResetTime\":\"2020-04-12T21:25:06\",\"SaveCount\":716,\"SaveAddress\":\"F8000\"}}");
        messages.put("stat/Basic-3/STATUS2",
                "{\"StatusFWR\":{\"Version\":\"9.2.0(tasmota)\",\"BuildDateTime\":\"2020-12-21T15:03:40\",\"Boot\":6,\"Core\":\"2_7_4_9\",\"SDK\":\"2.2.2-dev(38a443e)\",\"CpuFrequency\":80,\"Hardware\":\"ESP8266EX\",\"CR\":\"431/699\"}}");
        messages.put("tele/DS1820-0/STATE",
                "{\"Time\":\"2021-02-24T10:40:10\",\"Uptime\":\"5T22:59:33\",\"UptimeSec\":514773,\"Heap\":20,\"SleepMode\":\"Dynamic\",\"Sleep\":100,\"LoadAvg\":18,\"MqttCount\":30,\"Wifi\":{\"AP\":1,\"SSId\":\"my-SSID\",\"BSSId\":\"20:0D:B0:65:43:21\",\"Channel\":6,\"RSSI\":100,\"Signal\":-21,\"LinkCount\":1,\"Downtime\":\"0T00:00:07\"}}");
        messages.put("stat/Basic-3/STATUS3",
                "{\"StatusLOG\":{\"SerialLog\":2,\"WebLog\":3,\"MqttLog\":0,\"SysLog\":0,\"LogHost\":\"\",\"LogPort\":514,\"SSId\":[\"my-SSID\",\"my-other-SSID\"],\"TelePeriod\":300,\"Resolution\":\"558180C0\",\"SetOption\":[\"00028009\",\"2805C8000100060000005A00000000000000\",\"00000240\",\"00006000\",\"00000000\"]}}");
        messages.put("tele/DS1820-0/SENSOR",
                "{\"Time\":\"2021-02-24T10:40:10\",\"DS18B20\":{\"Id\":\"01162E1C14EE\",\"Temperature\":24.8},\"TempUnit\":\"C\"}");
        messages.put("stat/Basic-3/STATUS4",
                "{\"StatusMEM\":{\"ProgramSize\":586,\"Free\":416,\"Heap\":25,\"ProgramFlashSize\":1024,\"FlashSize\":1024,\"FlashChipId\":\"14405E\",\"FlashFrequency\":40,\"FlashMode\":3,\"Features\":[\"00000809\",\"8FDAC787\",\"04368001\",\"000000CF\",\"010013C0\",\"C000F981\",\"00004004\",\"00001000\"],\"Drivers\":\"1,2,3,4,5,6,7,8,9,10,12,16,18,19,20,21,22,24,26,27,29,30,35,37,45\",\"Sensors\":\"1,2,3,4,5,6\"}}");
        messages.put("stat/Basic-3/STATUS5",
                "{\"StatusNET\":{\"Hostname\":\"ESP-Basic-3\",\"IPAddress\":\"10.211.10.103\",\"Gateway\":\"10.211.0.1\",\"Subnetmask\":\"255.255.0.0\",\"DNSServer\":\"10.211.0.1\",\"Mac\":\"84:F3:EB:48:7D:21\",\"Webserver\":2,\"WifiConfig\":4,\"WifiPower\":17.0}}");
        messages.put("stat/Basic-3/STATUS6",
                "{\"StatusMQT\":{\"MqttHost\":\"10.211.0.45\",\"MqttPort\":1883,\"MqttClientMask\":\"Client-Basic-3\",\"MqttClient\":\"Client-Basic-3\",\"MqttUser\":\"DVES_USER\",\"MqttCount\":1,\"MAX_PACKET_SIZE\":1200,\"KEEPALIVE\":30}}");
        messages.put("stat/Basic-3/STATUS7",
                "{\"StatusTIM\":{\"UTC\":\"2021-02-24T09:40:11\",\"Local\":\"2021-02-24T10:40:11\",\"StartDST\":\"2021-03-28T02:00:00\",\"EndDST\":\"2021-10-31T03:00:00\",\"Timezone\":99,\"Sunrise\":\"07:02\",\"Sunset\":\"17:48\"}}");
        messages.put("stat/Basic-3/STATUS10", "{\"StatusSNS\":{\"Time\":\"2021-02-24T10:40:11\"}}");
        messages.put("stat/Basic-3/STATUS11",
                "{\"StatusSTS\":{\"Time\":\"2021-02-24T10:40:11\",\"Uptime\":\"0T01:52:04\",\"UptimeSec\":6724,\"Heap\":25,\"SleepMode\":\"Dynamic\",\"Sleep\":100,\"LoadAvg\":9,\"MqttCount\":1,\"POWER\":\"OFF\",\"Wifi\":{\"AP\":1,\"SSId\":\"my-SSID\",\"BSSId\":\"5C:92:5E:65:43:21\",\"Channel\":11,\"RSSI\":48,\"Signal\":-76,\"LinkCount\":1,\"Downtime\":\"0T00:00:07\"}}}");
        messages.put("stat/DS1820-0/RESULT", "{\"Command\":\"Error\"}");
        messages.put("stat/Basic-3/RESULT", "{\"POWER\":\"OFF\"}");
        messages.put("stat/Basic-3/POWER", "OFF");
        messages.put("stat/DHT-8/STATUS",
                "{\"Status\":{\"Module\":18,\"DeviceName\":\"Balkon\",\"FriendlyName\":[\"DHT-8 Balkon\"],\"Topic\":\"DHT-8\",\"ButtonTopic\":\"0\",\"Power\":0,\"PowerOnState\":3,\"LedState\":1,\"LedMask\":\"FFFF\",\"SaveData\":1,\"SaveState\":1,\"SwitchTopic\":\"0\",\"SwitchMode\":[0,0,0,0,0,0,0,0],\"ButtonRetain\":0,\"SwitchRetain\":0,\"SensorRetain\":0,\"PowerRetain\":0}}");
        messages.put("stat/DHT-8/STATUS1",
                "{\"StatusPRM\":{\"Baudrate\":115200,\"SerialConfig\":\"8N1\",\"GroupTopic\":\"tasmotas\",\"OtaUrl\":\"http://ota.tasmota.com/tasmota/release-9.2.0/tasmota-sensors.bin.gz\",\"RestartReason\":\"Exception\",\"Uptime\":\"2T05:58:19\",\"StartupUTC\":\"2021-02-22T03:41:53\",\"Sleep\":100,\"CfgHolder\":4617,\"BootCount\":710,\"BCResetTime\":\"2020-04-11T23:15:06\",\"SaveCount\":24902,\"SaveAddress\":\"F4000\"}}");
        messages.put("stat/DHT-8/STATUS2",
                "{\"StatusFWR\":{\"Version\":\"9.2.0(sensors)\",\"BuildDateTime\":\"2020-12-21T15:04:56\",\"Boot\":31,\"Core\":\"2_7_4_9\",\"SDK\":\"2.2.2-dev(38a443e)\",\"CpuFrequency\":80,\"Hardware\":\"ESP8266EX\",\"CR\":\"407/699\"}}");
        messages.put("stat/DHT-8/STATUS3",
                "{\"StatusLOG\":{\"SerialLog\":2,\"WebLog\":3,\"MqttLog\":0,\"SysLog\":0,\"LogHost\":\"\",\"LogPort\":514,\"SSId\":[\"my-SSID\",\"my-other-SSID\"],\"TelePeriod\":300,\"Resolution\":\"558180C0\",\"SetOption\":[\"00028009\",\"2805C8000100060000005A00000000000000\",\"00000240\",\"00006000\",\"00000000\"]}}");
        messages.put("stat/DHT-8/STATUS4",
                "{\"StatusMEM\":{\"ProgramSize\":638,\"Free\":364,\"Heap\":20,\"ProgramFlashSize\":1024,\"FlashSize\":4096,\"FlashChipId\":\"164020\",\"FlashFrequency\":40,\"FlashMode\":3,\"Features\":[\"00000809\",\"8FDA8787\",\"0415A005\",\"B7FFBFCF\",\"01DA9BC4\",\"64367CC7\",\"00084052\",\"00000000\"],\"Drivers\":\"1,2,3,4,5,6,7,8,9,10,12,14,16,17,20,21,24,29,34\",\"Sensors\":\"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,26,28,31,34,37,39,40,42,43,51,52,55,56,58,59,62,64,66,67,74\"}}");
        messages.put("stat/DHT-8/STATUS5",
                "{\"StatusNET\":{\"Hostname\":\"ESP-DHT-8\",\"IPAddress\":\"10.211.11.178\",\"Gateway\":\"10.211.0.1\",\"Subnetmask\":\"255.255.0.0\",\"DNSServer\":\"10.211.0.1\",\"Mac\":\"DC:4F:22:10:9F:A8\",\"Webserver\":2,\"WifiConfig\":4,\"WifiPower\":17.0}}");
        messages.put("stat/DHT-8/STATUS6",
                "{\"StatusMQT\":{\"MqttHost\":\"10.211.0.45\",\"MqttPort\":1883,\"MqttClientMask\":\"Client-DHT-8\",\"MqttClient\":\"Client-DHT-8\",\"MqttUser\":\"DVES_USER\",\"MqttCount\":5,\"MAX_PACKET_SIZE\":1200,\"KEEPALIVE\":30}}");
        messages.put("stat/DHT-8/STATUS7",
                "{\"StatusTIM\":{\"UTC\":\"2021-02-24T09:40:12\",\"Local\":\"2021-02-24T10:40:12\",\"StartDST\":\"2021-03-28T02:00:00\",\"EndDST\":\"2021-10-31T03:00:00\",\"Timezone\":99,\"Sunrise\":\"07:02\",\"Sunset\":\"17:48\"}}");
        messages.put("stat/DHT-8/STATUS10",
                "{\"StatusSNS\":{\"Time\":\"2021-02-24T10:40:12\",\"DHT11\":{\"Temperature\":19.0,\"Humidity\":16.0,\"DewPoint\":-7.5},\"TempUnit\":\"C\"}}");
        messages.put("stat/DHT-8/STATUS11",
                "{\"StatusSTS\":{\"Time\":\"2021-02-24T10:40:12\",\"Uptime\":\"2T05:58:19\",\"UptimeSec\":194299,\"Heap\":20,\"SleepMode\":\"Dynamic\",\"Sleep\":100,\"LoadAvg\":9,\"MqttCount\":5,\"Wifi\":{\"AP\":1,\"SSId\":\"my-SSID\",\"BSSId\":\"20:0D:B0:65:43:21\",\"Channel\":6,\"RSSI\":100,\"Signal\":-18,\"LinkCount\":1,\"Downtime\":\"0T00:00:07\"}}}");
        messages.put("stat/DHT-8/STATUS12",
                "{\"StatusSTK\":{\"Exception\":29,\"Reason\":\"Exception\",\"EPC\":[\"4000df64\",\"00000000\",\"00000000\"],\"EXCVADDR\":\"00000000\",\"DEPC\":\"00000000\",\"CallChain\":[\"40101748\",\"4026c037\",\"4026bfcc\",\"4026bf73\",\"4026b0d0\",\"4026b0f9\",\"40268b19\",\"40268b0e\",\"40103b96\",\"40264ec9\",\"40269857\",\"40000f58\",\"402692ca\",\"40272a2b\",\"402722eb\",\"402500ac\",\"40000f49\",\"40000f49\",\"40000e19\",\"40105d15\",\"40105d1b\",\"4010000d\",\"402711d0\",\"40271181\",\"40100cf8\",\"40100cf8\",\"40105c0f\",\"4010295c\",\"40105c0f\",\"401034f3\",\"401036d4\"]}}");
        messages.put("stat/DHT-8/RESULT", "{\"TelePeriod\":300}");
        messages.put("tele/Basic-3/STATE",
                "{\"Time\":\"2021-02-24T10:40:12\",\"Uptime\":\"0T01:52:05\",\"UptimeSec\":6725,\"Heap\":26,\"SleepMode\":\"Dynamic\",\"Sleep\":100,\"LoadAvg\":12,\"MqttCount\":1,\"POWER\":\"OFF\",\"Wifi\":{\"AP\":1,\"SSId\":\"my-SSID\",\"BSSId\":\"5C:92:5E:65:43:21\",\"Channel\":11,\"RSSI\":36,\"Signal\":-82,\"LinkCount\":1,\"Downtime\":\"0T00:00:07\"}}");
        messages.put("tele/DHT-8/STATE",
                "{\"Time\":\"2021-02-24T10:40:13\",\"Uptime\":\"2T05:58:20\",\"UptimeSec\":194300,\"Heap\":22,\"SleepMode\":\"Dynamic\",\"Sleep\":100,\"LoadAvg\":13,\"MqttCount\":5,\"Wifi\":{\"AP\":1,\"SSId\":\"my-SSID\",\"BSSId\":\"20:0D:B0:65:43:21\",\"Channel\":6,\"RSSI\":100,\"Signal\":-17,\"LinkCount\":1,\"Downtime\":\"0T00:00:07\"}}");
        messages.put("tele/DHT-8/SENSOR",
                "{\"Time\":\"2021-02-24T10:40:13\",\"DHT11\":{\"Temperature\":19.0,\"Humidity\":16.0,\"DewPoint\":-7.5},\"TempUnit\":\"C\"}");
        return messages;
    }
}
