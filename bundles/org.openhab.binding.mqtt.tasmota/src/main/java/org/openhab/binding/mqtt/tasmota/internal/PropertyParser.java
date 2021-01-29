package org.openhab.binding.mqtt.tasmota.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.openhab.binding.mqtt.tasmota.internal.deviceState.StatusFWR;
import org.openhab.binding.mqtt.tasmota.internal.deviceState.StatusLOG;
import org.openhab.binding.mqtt.tasmota.internal.deviceState.StatusMEM;
import org.openhab.binding.mqtt.tasmota.internal.deviceState.StatusMQT;
import org.openhab.binding.mqtt.tasmota.internal.deviceState.StatusNET;
import org.openhab.binding.mqtt.tasmota.internal.deviceState.StatusPRM;
import org.openhab.binding.mqtt.tasmota.internal.deviceState.StatusPTH;
import org.openhab.binding.mqtt.tasmota.internal.deviceState.StatusSNS;
import org.openhab.binding.mqtt.tasmota.internal.deviceState.StatusSTS;
import org.openhab.binding.mqtt.tasmota.internal.deviceState.StatusTIM;
import org.openhab.binding.mqtt.tasmota.internal.deviceState.TasmotaState;
import org.openhab.binding.mqtt.tasmota.internal.deviceState.Wifi;

public class PropertyParser {

    public static Map<@NonNull String, @NonNull Object> parseProperties(TasmotaState tasmotaState) {

        Map<String, Object> properties = new HashMap<>();

        properties.put("Config.Sleep", (tasmotaState.Sleep));
        properties.put("Config.SleepMode", tasmotaState.SleepMode);

        Wifi wifi = tasmotaState.Wifi;
        parseWifi(properties, wifi);

        StatusFWR statusFWR = tasmotaState.StatusFWR;
        if (null != statusFWR) {
            properties.put("Config.StatusFWR.Boot", (statusFWR.Boot));
            properties.put("Config.StatusFWR.BuildDateTime", (statusFWR.BuildDateTime));
            properties.put("Config.StatusFWR.Core", (statusFWR.Core));
            properties.put("Config.StatusFWR.CpuFrequency", (statusFWR.CpuFrequency));
            properties.put("Config.StatusFWR.CR", (statusFWR.CR));
            properties.put("Config.StatusFWR.Hardware", (statusFWR.Hardware));
            properties.put("Config.StatusFWR.SDK", (statusFWR.SDK));
            properties.put("Config.StatusFWR.Version", (statusFWR.Version));
        }

        StatusLOG statusLOG = tasmotaState.StatusLOG;
        if (null != statusLOG) {
            properties.put("Config.StatusLOG.LogHost", (statusLOG.LogHost));
            properties.put("Config.StatusLOG.LogPort", (statusLOG.LogPort));
            properties.put("Config.StatusLOG.MqttLog", (statusLOG.MqttLog));
            properties.put("Config.StatusLOG.Resolution", (statusLOG.Resolution));
            properties.put("Config.StatusLOG.SerialLog", (statusLOG.SerialLog));
            properties.put("Config.StatusLOG.SetOption", (statusLOG.SetOption));
            properties.put("Config.StatusLOG.SSId", (statusLOG.SSId));
            properties.put("Config.StatusLOG.TelePeriod", (statusLOG.TelePeriod));
            properties.put("Config.StatusLOG.WebLog", (statusLOG.WebLog));
        }

        StatusMEM statusMEM = tasmotaState.StatusMEM;
        if (null != statusMEM) {
            properties.put("Config.StatusMEM.Drivers", (statusMEM.Drivers));
            properties.put("Config.StatusMEM.Features", (statusMEM.Features));
            properties.put("Config.StatusMEM.FlashChipId", (statusMEM.FlashChipId));
            properties.put("Config.StatusMEM.FlashFrequency", (statusMEM.FlashFrequency));
            properties.put("Config.StatusMEM.FlashMode", (statusMEM.FlashMode));
            properties.put("Config.StatusMEM.FlashSize", (statusMEM.FlashSize));
            properties.put("Config.StatusMEM.Free", (statusMEM.Free));
            properties.put("Config.StatusMEM.Heap", (statusMEM.Heap));
            properties.put("Config.StatusMEM.ProgramFlashSize", (statusMEM.ProgramFlashSize));
            properties.put("Config.StatusMEM.ProgramSize", (statusMEM.ProgramSize));
            properties.put("Config.StatusMEM.Sensors", (statusMEM.Sensors));
        }

        StatusMQT statusMQT = tasmotaState.StatusMQT;
        if (null != statusMQT) {
            properties.put("Config.StatusMQT.KEEPALIVE", (statusMQT.KEEPALIVE));
            properties.put("Config.StatusMQT.MAX_PACKET_SIZE", (statusMQT.MAX_PACKET_SIZE));
            properties.put("Config.StatusMQT.MqttClient", (statusMQT.MqttClient));
            properties.put("Config.StatusMQT.MqttClientMask", (statusMQT.MqttClientMask));
            properties.put("Config.StatusMQT.MqttCount", (statusMQT.MqttCount));
            properties.put("Config.StatusMQT.MqttHost", (statusMQT.MqttHost));
            properties.put("Config.StatusMQT.MqttPort", (statusMQT.MqttPort));
            properties.put("Config.StatusMQT.MqttUser", (statusMQT.MqttUser));
        }

        StatusNET statusNET = tasmotaState.StatusNET;
        if (null != statusNET) {
            properties.put("Config.StatusNET.DNSServer", (statusNET.DNSServer));
            properties.put("Config.StatusNET.Gateway", (statusNET.Gateway));
            properties.put("Config.StatusNET.Hostname", (statusNET.Hostname));
            properties.put("Config.StatusNET.IPAddress", (statusNET.IPAddress));
            properties.put("Config.StatusNET.Mac", (statusNET.Mac));
            properties.put("Config.StatusNET.Subnetmask", (statusNET.Subnetmask));
            properties.put("Config.StatusNET.Webserver", (statusNET.Webserver));
            properties.put("Config.StatusNET.WifiConfig", (statusNET.WifiConfig));
            properties.put("Config.StatusNET.WifiPower", (statusNET.WifiPower));
        }

        StatusPRM statusPRM = tasmotaState.StatusPRM;
        if (null != statusPRM) {
            properties.put("Config.StatusPRM.OtaUrl", (statusPRM.Baudrate));
            properties.put("Config.StatusPRM.BCResetTime", statusPRM.BCResetTime);
            properties.put("Config.StatusPRM.BootCount", (statusPRM.BootCount));
            properties.put("Config.StatusPRM.CfgHolder", (statusPRM.CfgHolder));
            properties.put("Config.StatusPRM.GroupTopic", statusPRM.GroupTopic);
            properties.put("Config.StatusPRM.OtaUrl", statusPRM.OtaUrl);
            properties.put("Config.StatusPRM.RestartReason", statusPRM.RestartReason);
            properties.put("Config.StatusPRM.SaveAddress", statusPRM.SaveAddress);
            properties.put("Config.StatusPRM.SaveCount", (statusPRM.SaveCount));
            properties.put("Config.StatusPRM.SerialConfig", statusPRM.SerialConfig);
            properties.put("Config.StatusPRM.SerialConfig", (statusPRM.Sleep));
            properties.put("StartupUTC", statusPRM.StartupUTC);
            properties.put("Config.StatusPRM.Uptime", statusPRM.Uptime);
        }

        StatusPTH statusPTH = tasmotaState.StatusPTH;
        if (null != statusPTH) {
            properties.put("Config.StatusSTS.CurrentHigh", (statusPTH.CurrentHigh));
            properties.put("Config.StatusSTS.CurrentLow", (statusPTH.CurrentLow));
            properties.put("Config.StatusSTS.PowerDelta", (statusPTH.PowerDelta));
            properties.put("Config.StatusSTS.PowerLow", (statusPTH.PowerLow));
            properties.put("Config.StatusSTS.VoltageHigh", (statusPTH.VoltageHigh));
            properties.put("Config.StatusSTS.VoltageLow", (statusPTH.VoltageLow));
        }

        StatusSNS statusSNS = tasmotaState.StatusSNS;
        if (null != statusSNS) {
            properties.put("Config.StatusSNS.TempUnit", (statusSNS.TempUnit));
            properties.put("Config.StatusSNS.Time", (statusSNS.Time));
        }

        StatusSTS statusSTS = tasmotaState.StatusSTS;
        if (null != statusSTS) {
            properties.put("Config.StatusSTS.Heap", (statusSTS.Heap));
            properties.put("Config.StatusSTS.LoadAvg", (statusSTS.LoadAvg));
            properties.put("Config.StatusSTS.MqttCount", (statusSTS.MqttCount));
            properties.put("Config.StatusSTS.Sleep", (statusSTS.Sleep));
            properties.put("Config.StatusSTS.SleepMode", (statusSTS.SleepMode));
            properties.put("Config.StatusSTS.Time", statusSTS.Time);
            properties.put("Config.StatusSTS.Uptime", statusSTS.Uptime);
            properties.put("Config.StatusSTS.UptimeSec", (statusSTS.UptimeSec));
            parseWifi(properties, statusSTS.Wifi);
        }

        StatusTIM statusTIM = tasmotaState.StatusTIM;
        if (null != statusTIM) {
            properties.put("Config.StatusTIM.EndDST", (statusTIM.EndDST));
            properties.put("Config.StatusTIM.Local", (statusTIM.Local));
            properties.put("Config.StatusTIM.StartDST", (statusTIM.StartDST));
            properties.put("Config.StatusTIM.Sunrise", (statusTIM.Sunrise));
            properties.put("Config.StatusTIM.Sunset", (statusTIM.Sunset));
            properties.put("Config.StatusTIM.Timezone", (statusTIM.Timezone));
            properties.put("Config.StatusTIM.UTC", (statusTIM.UTC));
        }

        if (null != statusTIM) {
            properties.put("Config.StatusTIM.EndDST", (statusTIM.EndDST));
        }

        if (tasmotaState.Dimmer != null) {
            properties.put("hasFeature.Dimmer", "true");
            properties.put("deviceTypeKnown", true);
        }
        if (tasmotaState.POWER != null) {
            properties.put("hasFeature.Power", "true");
            properties.put("deviceTypeKnown", true);
        }

        if (null != tasmotaState.ENERGY) {
            if (tasmotaState.ENERGY.Power != null) {
                properties.put("hasFeature.Energy", "true");
                properties.put("deviceTypeKnown", true);
            }
        }

        if (null != tasmotaState.Dht11) {
            if (tasmotaState.Dht11.Temperature != null) {
                properties.put("hasFeature.Temperature", "true");
                properties.put("deviceTypeKnown", true);
            }
            if (tasmotaState.Dht11.Humidity != null) {
                properties.put("hasFeature.Humidity", "true");
                properties.put("deviceTypeKnown", true);
            }
        }

        return properties;
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
