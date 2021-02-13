package org.openhab.binding.mqtt.tasmota.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.openhab.binding.mqtt.tasmota.internal.deviceState.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeviceStateParser {

    private static final Logger logger = LoggerFactory.getLogger(DeviceStateParser.class);

    private static boolean onlyLimitedParsingForDebugging = false;

    public static Map<@NonNull String, @NonNull Object> stateToHashMap(TasmotaState tasmotaState) {

        Map<String, Object> deviceStateMap = new HashMap<>();

        deviceStateMap.put("Config.Sleep", (tasmotaState.Sleep));
        deviceStateMap.put("Config.SleepMode", tasmotaState.SleepMode);

        hasFeatureParsing(tasmotaState, deviceStateMap);

        StatusFWR statusFWR = tasmotaState.StatusFWR;
        if (null != statusFWR) {
            deviceStateMap.put("Config.StatusFWR.Boot", (statusFWR.Boot));
            deviceStateMap.put("Config.StatusFWR.BuildDateTime", (statusFWR.BuildDateTime)); //
            deviceStateMap.put("Config.StatusFWR.Core", (statusFWR.Core));
            deviceStateMap.put("Config.StatusFWR.CpuFrequency", (statusFWR.CpuFrequency));
            deviceStateMap.put("Config.StatusFWR.CR", (statusFWR.CR));
            deviceStateMap.put("Config.StatusFWR.Hardware", (statusFWR.Hardware));
            deviceStateMap.put("Config.StatusFWR.SDK", (statusFWR.SDK));
            deviceStateMap.put("Config.StatusFWR.Version", (statusFWR.Version)); //
        }

        if (onlyLimitedParsingForDebugging) {
            logger.warn("Only Limited State Parsing for Debugging");
            return deviceStateMap;
        }

        Wifi wifi = tasmotaState.Wifi;
        parseWifi(deviceStateMap, wifi);

        StatusLOG statusLOG = tasmotaState.StatusLOG;
        if (null != statusLOG) {
            deviceStateMap.put("Config.StatusLOG.LogHost", (statusLOG.LogHost));
            deviceStateMap.put("Config.StatusLOG.LogPort", (statusLOG.LogPort));
            deviceStateMap.put("Config.StatusLOG.MqttLog", (statusLOG.MqttLog));
            deviceStateMap.put("Config.StatusLOG.Resolution", (statusLOG.Resolution));
            deviceStateMap.put("Config.StatusLOG.SerialLog", (statusLOG.SerialLog));
            deviceStateMap.put("Config.StatusLOG.SetOption", (statusLOG.SetOption));
            deviceStateMap.put("Config.StatusLOG.SSId", (statusLOG.SSId));
            deviceStateMap.put("Config.StatusLOG.TelePeriod", (statusLOG.TelePeriod));
            deviceStateMap.put("Config.StatusLOG.WebLog", (statusLOG.WebLog));
        }

        StatusMEM statusMEM = tasmotaState.StatusMEM;
        if (null != statusMEM) {
            deviceStateMap.put("Config.StatusMEM.Drivers", (statusMEM.Drivers));
            deviceStateMap.put("Config.StatusMEM.Features", (statusMEM.Features));
            deviceStateMap.put("Config.StatusMEM.FlashChipId", (statusMEM.FlashChipId));
            deviceStateMap.put("Config.StatusMEM.FlashFrequency", (statusMEM.FlashFrequency));
            deviceStateMap.put("Config.StatusMEM.FlashMode", (statusMEM.FlashMode));
            deviceStateMap.put("Config.StatusMEM.FlashSize", (statusMEM.FlashSize));
            deviceStateMap.put("Config.StatusMEM.Free", (statusMEM.Free));
            deviceStateMap.put("Config.StatusMEM.Heap", (statusMEM.Heap));
            deviceStateMap.put("Config.StatusMEM.ProgramFlashSize", (statusMEM.ProgramFlashSize));
            deviceStateMap.put("Config.StatusMEM.ProgramSize", (statusMEM.ProgramSize));
            deviceStateMap.put("Config.StatusMEM.Sensors", (statusMEM.Sensors));
        }

        StatusMQT statusMQT = tasmotaState.StatusMQT;
        if (null != statusMQT) {
            deviceStateMap.put("Config.StatusMQT.KEEPALIVE", (statusMQT.KEEPALIVE));
            deviceStateMap.put("Config.StatusMQT.MAX_PACKET_SIZE", (statusMQT.MAX_PACKET_SIZE));
            deviceStateMap.put("Config.StatusMQT.MqttClient", (statusMQT.MqttClient));
            deviceStateMap.put("Config.StatusMQT.MqttClientMask", (statusMQT.MqttClientMask));
            deviceStateMap.put("Config.StatusMQT.MqttCount", (statusMQT.MqttCount));
            deviceStateMap.put("Config.StatusMQT.MqttHost", (statusMQT.MqttHost));
            deviceStateMap.put("Config.StatusMQT.MqttPort", (statusMQT.MqttPort));
            deviceStateMap.put("Config.StatusMQT.MqttUser", (statusMQT.MqttUser));
        }

        StatusNET statusNET = tasmotaState.StatusNET;
        if (null != statusNET) {
            deviceStateMap.put("Config.StatusNET.DNSServer", (statusNET.DNSServer));
            deviceStateMap.put("Config.StatusNET.Gateway", (statusNET.Gateway));
            deviceStateMap.put("Config.StatusNET.Hostname", (statusNET.Hostname));
            deviceStateMap.put("Config.StatusNET.IPAddress", (statusNET.IPAddress));
            deviceStateMap.put("Config.StatusNET.Mac", (statusNET.Mac));
            deviceStateMap.put("Config.StatusNET.Subnetmask", (statusNET.Subnetmask));
            deviceStateMap.put("Config.StatusNET.Webserver", (statusNET.Webserver));
            deviceStateMap.put("Config.StatusNET.WifiConfig", (statusNET.WifiConfig));
            deviceStateMap.put("Config.StatusNET.WifiPower", (statusNET.WifiPower));
        }

        StatusPRM statusPRM = tasmotaState.StatusPRM;
        if (null != statusPRM) {
            deviceStateMap.put("Config.StatusPRM.OtaUrl", (statusPRM.Baudrate));
            deviceStateMap.put("Config.StatusPRM.BCResetTime", statusPRM.BCResetTime);
            deviceStateMap.put("Config.StatusPRM.BootCount", (statusPRM.BootCount));
            deviceStateMap.put("Config.StatusPRM.CfgHolder", (statusPRM.CfgHolder));
            deviceStateMap.put("Config.StatusPRM.GroupTopic", statusPRM.GroupTopic);
            deviceStateMap.put("Config.StatusPRM.OtaUrl", statusPRM.OtaUrl);
            deviceStateMap.put("Config.StatusPRM.RestartReason", statusPRM.RestartReason);
            deviceStateMap.put("Config.StatusPRM.SaveAddress", statusPRM.SaveAddress);
            deviceStateMap.put("Config.StatusPRM.SaveCount", (statusPRM.SaveCount));
            deviceStateMap.put("Config.StatusPRM.SerialConfig", statusPRM.SerialConfig);
            deviceStateMap.put("Config.StatusPRM.SerialConfig", (statusPRM.Sleep));
            deviceStateMap.put("StartupUTC", statusPRM.StartupUTC);
            deviceStateMap.put("Config.StatusPRM.Uptime", statusPRM.Uptime);
        }

        StatusPTH statusPTH = tasmotaState.StatusPTH;
        if (null != statusPTH) {
            deviceStateMap.put("Config.StatusSTS.CurrentHigh", (statusPTH.CurrentHigh));
            deviceStateMap.put("Config.StatusSTS.CurrentLow", (statusPTH.CurrentLow));
            deviceStateMap.put("Config.StatusSTS.PowerDelta", (statusPTH.PowerDelta));
            deviceStateMap.put("Config.StatusSTS.PowerLow", (statusPTH.PowerLow));
            deviceStateMap.put("Config.StatusSTS.VoltageHigh", (statusPTH.VoltageHigh));
            deviceStateMap.put("Config.StatusSTS.VoltageLow", (statusPTH.VoltageLow));
        }

        StatusSNS statusSNS = tasmotaState.StatusSNS;
        if (null != statusSNS) {
            deviceStateMap.put("Config.StatusSNS.TempUnit", (statusSNS.TempUnit));
            deviceStateMap.put("Config.StatusSNS.Time", (statusSNS.Time));
        }

        StatusSTS statusSTS = tasmotaState.StatusSTS;
        if (null != statusSTS) {
            deviceStateMap.put("Config.StatusSTS.Heap", (statusSTS.Heap));
            deviceStateMap.put("Config.StatusSTS.LoadAvg", (statusSTS.LoadAvg));
            deviceStateMap.put("Config.StatusSTS.MqttCount", (statusSTS.MqttCount));
            deviceStateMap.put("Config.StatusSTS.Sleep", (statusSTS.Sleep));
            deviceStateMap.put("Config.StatusSTS.SleepMode", (statusSTS.SleepMode));
            deviceStateMap.put("Config.StatusSTS.Time", statusSTS.Time);
            deviceStateMap.put("Config.StatusSTS.Uptime", statusSTS.Uptime);
            deviceStateMap.put("Config.StatusSTS.UptimeSec", (statusSTS.UptimeSec));
            parseWifi(deviceStateMap, statusSTS.Wifi);
        }

        StatusTIM statusTIM = tasmotaState.StatusTIM;
        if (null != statusTIM) {
            deviceStateMap.put("Config.StatusTIM.EndDST", (statusTIM.EndDST));
            deviceStateMap.put("Config.StatusTIM.Local", (statusTIM.Local));
            deviceStateMap.put("Config.StatusTIM.StartDST", (statusTIM.StartDST));
            deviceStateMap.put("Config.StatusTIM.Sunrise", (statusTIM.Sunrise));
            deviceStateMap.put("Config.StatusTIM.Sunset", (statusTIM.Sunset));
            deviceStateMap.put("Config.StatusTIM.Timezone", (statusTIM.Timezone));
            deviceStateMap.put("Config.StatusTIM.UTC", (statusTIM.UTC));
        }

        if (null != statusTIM) {
            deviceStateMap.put("Config.StatusTIM.EndDST", (statusTIM.EndDST));
        }

        return deviceStateMap;
    }

    private static void hasFeatureParsing(TasmotaState tasmotaState, Map<String, Object> deviceStateMap) {
        if (tasmotaState.Dimmer != null) {
            deviceStateMap.put("hasFeature.Dimmer", "true");
            deviceStateMap.put("deviceTypeKnown", true);
        }
        if (tasmotaState.POWER != null) {
            deviceStateMap.put("hasFeature.Power", "true");
            deviceStateMap.put("deviceTypeKnown", true);
        }

        if (null != tasmotaState.ENERGY) {
            if (tasmotaState.ENERGY.Power != null) {
                deviceStateMap.put("hasFeature.Energy", "true");
                deviceStateMap.put("deviceTypeKnown", true);
            }
        }

        if (null != tasmotaState.Dht11) {
            if (tasmotaState.Dht11.Temperature != null) {
                deviceStateMap.put("hasFeature.Temperature", "true");
                deviceStateMap.put("deviceTypeKnown", true);
            }
            if (tasmotaState.Dht11.Humidity != null) {
                deviceStateMap.put("hasFeature.Humidity", "true");
                deviceStateMap.put("deviceTypeKnown", true);
            }
        }
    }

    private static void parseWifi(Map<String, Object> properties, Wifi wifi) {
        if (null != wifi) {
            properties.put("Config.Wifi.AP", (wifi.AP));
            properties.put("Config.Wifi.BSSId", wifi.BSSId);
            properties.put("Config.Wifi.Channel", (wifi.Channel));
            properties.put("Config.Wifi.Downtime", (wifi.Downtime));
            properties.put("Config.Wifi.LinkCount", (wifi.LinkCount));
            properties.put("Config.Wifi.wifi.RSSI", (wifi.RSSI));
            properties.put("Config.Wifi.Signal", (wifi.Signal));
            properties.put("Config.Wifi.SSId", (wifi.SSId));

        }
    }
}
