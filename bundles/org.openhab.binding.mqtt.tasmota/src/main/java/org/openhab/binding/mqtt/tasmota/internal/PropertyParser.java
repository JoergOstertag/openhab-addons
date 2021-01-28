package org.openhab.binding.mqtt.tasmota.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.openhab.binding.mqtt.tasmota.internal.deviceState.StatusPRM;
import org.openhab.binding.mqtt.tasmota.internal.deviceState.StatusSTS;
import org.openhab.binding.mqtt.tasmota.internal.deviceState.TasmotaState;
import org.openhab.binding.mqtt.tasmota.internal.deviceState.Wifi;

public class PropertyParser {

    public static Map<@NonNull String, @NonNull Object> parseProperties(TasmotaState tasmotaState) {

        Map<String, Object> properties = new HashMap<>();

        properties.put("Config.Sleep", (tasmotaState.Sleep));
        properties.put("Config.SleepMode", tasmotaState.SleepMode);

        Wifi wifi = tasmotaState.Wifi;
        parseWifi(properties, wifi);

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
            properties.put("Config.wifi.AP", (wifi.AP));
            properties.put("Config.wifi.BSSId", wifi.BSSId);
            properties.put("Config.wifi.Channel", (wifi.Channel));
            properties.put("Config.wifi.Signal", (wifi.Signal));
        }
    }
}
