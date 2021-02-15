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
package org.openhab.binding.mqtt.tasmota.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.jetbrains.annotations.Nullable;
import org.openhab.binding.mqtt.tasmota.internal.deviceState.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;

/**
 * @author Jörg Ostertag - Parse more of the Json responses from Tasmota
 */
public class DeviceStateParser {

    private static final Gson gson = new GsonBuilder() //
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss") //
            .setLongSerializationPolicy(LongSerializationPolicy.DEFAULT) //
            .create();

    private static final Logger logger = LoggerFactory.getLogger(DeviceStateParser.class);

    private static boolean onlyLimitedParsingForDebugging = false;

    public static @NonNull TasmotaState parseState(String stateAsJson) {
        TasmotaState tasmotaStateFromJson = null;
        try {
            tasmotaStateFromJson = gson.fromJson(stateAsJson, TasmotaState.class);

            boolean forDebugCheckIfAllIsParsed = false;
            if (forDebugCheckIfAllIsParsed) {
                String toJson = gson.toJson(tasmotaStateFromJson);
                if (Math.abs(stateAsJson.length() - toJson.length()) > 12) {
                    System.out.println();
                    System.out.println("in:  " + stateAsJson);
                    System.out.println("out: " + toJson);
                    System.out.println();
                }
            }
        } catch (Exception ex) {
            logger.error("Error parsing json: {}", ex.getMessage());
        }
        if (null == tasmotaStateFromJson) {
            tasmotaStateFromJson = new TasmotaState();
        }
        return tasmotaStateFromJson;
    }

    public static Map<@NonNull String, @NonNull Object> stateToHashMap(TasmotaState tasmotaState) {

        Map<String, Object> deviceStateMap = new HashMap<>();
        deviceStateMap.putAll(parseSensors(tasmotaState));
        deviceStateMap.putAll(parseConfigItems(tasmotaState));

        return deviceStateMap;
    }

    @Nullable
    public static Map<String, Object> parseConfigItems(TasmotaState tasmotaState) {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put("Config.Sleep", tasmotaState.Sleep);
        configMap.put("Config.SleepMode", tasmotaState.SleepMode);

        StatusFWR statusFWR = tasmotaState.StatusFWR;
        if (null != statusFWR) {
            configMap.put("Config.StatusFWR.Boot", statusFWR.Boot);
            configMap.put("Config.StatusFWR.BuildDateTime", statusFWR.BuildDateTime); //
            configMap.put("Config.StatusFWR.Core", statusFWR.Core);
            configMap.put("Config.StatusFWR.CpuFrequency", statusFWR.CpuFrequency);
            configMap.put("Config.StatusFWR.CR", statusFWR.CR);
            configMap.put("Config.StatusFWR.Hardware", statusFWR.Hardware);
            configMap.put("Config.StatusFWR.SDK", statusFWR.SDK);
            configMap.put("Config.StatusFWR.Version", statusFWR.Version); //
        }

        if (onlyLimitedParsingForDebugging) {
            logger.warn("Only Limited State Parsing for Debugging");
            return configMap;
        }

        Wifi wifi = tasmotaState.Wifi;
        parseWifi(configMap, wifi);

        StatusLOG statusLOG = tasmotaState.StatusLOG;
        if (null != statusLOG) {
            configMap.put("Config.StatusLOG.LogHost", statusLOG.LogHost);
            configMap.put("Config.StatusLOG.LogPort", statusLOG.LogPort);
            configMap.put("Config.StatusLOG.MqttLog", statusLOG.MqttLog);
            configMap.put("Config.StatusLOG.Resolution", statusLOG.Resolution);
            configMap.put("Config.StatusLOG.SerialLog", statusLOG.SerialLog);
            configMap.put("Config.StatusLOG.SetOption", statusLOG.SetOption);
            configMap.put("Config.StatusLOG.SSId", statusLOG.SSId);
            configMap.put("Config.StatusLOG.TelePeriod", statusLOG.TelePeriod);
            configMap.put("Config.StatusLOG.WebLog", statusLOG.WebLog);
        }

        StatusMEM statusMEM = tasmotaState.StatusMEM;
        if (null != statusMEM) {
            configMap.put("Config.StatusMEM.Drivers", statusMEM.Drivers);
            configMap.put("Config.StatusMEM.Features", statusMEM.Features);
            configMap.put("Config.StatusMEM.FlashChipId", statusMEM.FlashChipId);
            configMap.put("Config.StatusMEM.FlashFrequency", statusMEM.FlashFrequency);
            configMap.put("Config.StatusMEM.FlashMode", statusMEM.FlashMode);
            configMap.put("Config.StatusMEM.FlashSize", statusMEM.FlashSize);
            configMap.put("Config.StatusMEM.Free", statusMEM.Free);
            configMap.put("Config.StatusMEM.Heap", statusMEM.Heap);
            configMap.put("Config.StatusMEM.ProgramFlashSize", statusMEM.ProgramFlashSize);
            configMap.put("Config.StatusMEM.ProgramSize", statusMEM.ProgramSize);
            configMap.put("Config.StatusMEM.Sensors", statusMEM.Sensors);
        }

        StatusMQT statusMQT = tasmotaState.StatusMQT;
        if (null != statusMQT) {
            configMap.put("Config.StatusMQT.KEEPALIVE", statusMQT.KEEPALIVE);
            configMap.put("Config.StatusMQT.MAX_PACKET_SIZE", statusMQT.MAX_PACKET_SIZE);
            configMap.put("Config.StatusMQT.MqttClient", statusMQT.MqttClient);
            configMap.put("Config.StatusMQT.MqttClientMask", statusMQT.MqttClientMask);
            configMap.put("Config.StatusMQT.MqttCount", statusMQT.MqttCount);
            configMap.put("Config.StatusMQT.MqttHost", statusMQT.MqttHost);
            configMap.put("Config.StatusMQT.MqttPort", statusMQT.MqttPort);
            configMap.put("Config.StatusMQT.MqttUser", statusMQT.MqttUser);
        }

        StatusNET statusNET = tasmotaState.StatusNET;
        if (null != statusNET) {
            configMap.put("Config.StatusNET.DNSServer", statusNET.DNSServer);
            configMap.put("Config.StatusNET.Gateway", statusNET.Gateway);
            configMap.put("Config.StatusNET.Hostname", statusNET.Hostname);
            configMap.put("Config.StatusNET.IPAddress", statusNET.IPAddress);
            configMap.put("Config.StatusNET.Mac", statusNET.Mac);
            configMap.put("Config.StatusNET.Subnetmask", statusNET.Subnetmask);
            configMap.put("Config.StatusNET.Webserver", statusNET.Webserver);
            configMap.put("Config.StatusNET.WifiConfig", statusNET.WifiConfig);
            configMap.put("Config.StatusNET.WifiPower", statusNET.WifiPower);
        }

        StatusPRM statusPRM = tasmotaState.StatusPRM;
        if (null != statusPRM) {
            configMap.put("Config.StatusPRM.OtaUrl", statusPRM.Baudrate);
            configMap.put("Config.StatusPRM.BCResetTime", statusPRM.BCResetTime);
            configMap.put("Config.StatusPRM.BootCount", statusPRM.BootCount);
            configMap.put("Config.StatusPRM.CfgHolder", statusPRM.CfgHolder);
            configMap.put("Config.StatusPRM.GroupTopic", statusPRM.GroupTopic);
            configMap.put("Config.StatusPRM.OtaUrl", statusPRM.OtaUrl);
            configMap.put("Config.StatusPRM.RestartReason", statusPRM.RestartReason);
            configMap.put("Config.StatusPRM.SaveAddress", statusPRM.SaveAddress);
            configMap.put("Config.StatusPRM.SaveCount", statusPRM.SaveCount);
            configMap.put("Config.StatusPRM.SerialConfig", statusPRM.SerialConfig);
            configMap.put("Config.StatusPRM.SerialConfig", statusPRM.Sleep);
            configMap.put("Config.StatusPRM.StartupUTC", statusPRM.StartupUTC);
            configMap.put("Config.StatusPRM.Uptime", statusPRM.Uptime);
        }

        StatusPTH statusPTH = tasmotaState.StatusPTH;
        if (null != statusPTH) {
            configMap.put("Config.StatusSTS.CurrentHigh", statusPTH.CurrentHigh);
            configMap.put("Config.StatusSTS.CurrentLow", statusPTH.CurrentLow);
            configMap.put("Config.StatusSTS.PowerDelta", statusPTH.PowerDelta);
            configMap.put("Config.StatusSTS.PowerLow", statusPTH.PowerLow);
            configMap.put("Config.StatusSTS.VoltageHigh", statusPTH.VoltageHigh);
            configMap.put("Config.StatusSTS.VoltageLow", statusPTH.VoltageLow);
        }

        StatusSNS statusSNS = tasmotaState.StatusSNS;
        if (null != statusSNS) {
            if (null != statusSNS.TempUnit) {
                configMap.put("Config.StatusSNS.TempUnit", statusSNS.TempUnit);
            }
            if (statusSNS.Time != null) {
                configMap.put("Config.StatusSNS.Time", statusSNS.Time);
            }
        }

        StatusSTS statusSTS = tasmotaState.StatusSTS;
        if (null != statusSTS) {
            configMap.put("Config.StatusSTS.Heap", statusSTS.Heap);
            configMap.put("Config.StatusSTS.LoadAvg", statusSTS.LoadAvg);
            configMap.put("Config.StatusSTS.MqttCount", statusSTS.MqttCount);
            configMap.put("Config.StatusSTS.Sleep", statusSTS.Sleep);
            configMap.put("Config.StatusSTS.SleepMode", statusSTS.SleepMode);
            configMap.put("Config.StatusSTS.Time", statusSTS.Time);
            configMap.put("Config.StatusSTS.Uptime", statusSTS.Uptime);
            configMap.put("Config.StatusSTS.UptimeSec", statusSTS.UptimeSec);
            parseWifi(configMap, statusSTS.Wifi);
        }

        StatusTIM statusTIM = tasmotaState.StatusTIM;
        if (null != statusTIM) {
            configMap.put("Config.StatusTIM.EndDST", statusTIM.EndDST);
            configMap.put("Config.StatusTIM.Local", statusTIM.Local);
            configMap.put("Config.StatusTIM.StartDST", statusTIM.StartDST);
            configMap.put("Config.StatusTIM.Sunrise", statusTIM.Sunrise);
            configMap.put("Config.StatusTIM.Sunset", statusTIM.Sunset);
            configMap.put("Config.StatusTIM.Timezone", statusTIM.Timezone);
            configMap.put("Config.StatusTIM.UTC", statusTIM.UTC);
        }
        return configMap;
    }

    public static boolean parseDeviceTypeKnown(TasmotaState tasmotaState, Map<String, Object> deviceStateMap) {
        boolean deviceKnown = false;
        deviceKnown |= (tasmotaState.Dimmer != null);
        deviceKnown |= (tasmotaState.POWER != null);
        if (null != tasmotaState.Status) {
            deviceKnown |= (tasmotaState.Status.Power != null);
        }

        Energy energy = tasmotaState.ENERGY;
        if (null == energy && (null != tasmotaState.StatusSNS)) {
            energy = tasmotaState.StatusSNS.ENERGY;
        }
        if (null != energy) {
            deviceKnown |= (energy.Power != null);
            deviceKnown |= (energy.Current != null);
        }

        DHT11 dht11 = tasmotaState.Dht11;
        if (null == dht11 && null != tasmotaState.StatusSNS) {
            dht11 = tasmotaState.StatusSNS.DHT11;
        }
        if (null != dht11) {
            deviceKnown |= (dht11.Temperature != null);
            deviceKnown |= (dht11.Humidity != null);
        }
        DS18B20 ds18B20 = tasmotaState.DS18B20;
        if (null == ds18B20 && null != tasmotaState.StatusSNS) {
            ds18B20 = tasmotaState.StatusSNS.DS18B20;
        }
        if (null != ds18B20) {
            deviceKnown |= (ds18B20.Temperature != null);
        }

        for (Map.Entry<String, Object> property : deviceStateMap.entrySet()) {
            String propertyName = property.getKey();
            Object propertyValue = property.getValue();
            deviceKnown |= (propertyName.startsWith("Sensor.") && (null != propertyValue));
        }
        return deviceKnown;
    }

    private static void parseWifi(Map<String, Object> properties, Wifi wifi) {
        if (null != wifi) {
            properties.put("Config.Wifi.AP", wifi.AP);
            properties.put("Config.Wifi.BSSId", wifi.BSSId);
            properties.put("Config.Wifi.Channel", wifi.Channel);
            properties.put("Config.Wifi.Downtime", wifi.Downtime);
            properties.put("Config.Wifi.LinkCount", wifi.LinkCount);
            properties.put("Config.Wifi.wifi.RSSI", wifi.RSSI);
            properties.put("Config.Wifi.Signal", wifi.Signal);
            properties.put("Config.Wifi.SSId", wifi.SSId);

        }
    }

    public static Map<String, Object> parseSensors(TasmotaState tasmotaState) {
        Map<String, Object> deviceStateMap = new HashMap<>();
        if (tasmotaState.Dimmer != null) {
            deviceStateMap.put("Sensor.Dimmer", tasmotaState.Dimmer);
        }
        if (tasmotaState.POWER != null) {
            deviceStateMap.put("Sensor.Power", tasmotaState.POWER);
        }

        Energy energy = tasmotaState.ENERGY;
        if (null == energy && null != tasmotaState.StatusSNS) {
            energy = tasmotaState.StatusSNS.ENERGY;
        }
        if (null != energy) {
            deviceStateMap.put("Sensor.ENERGY.ApparentPower", energy.ApparentPower);
            deviceStateMap.put("Sensor.ENERGY.Current", energy.Current);
            deviceStateMap.put("Sensor.ENERGY.Factor", energy.Factor);
            deviceStateMap.put("Sensor.ENERGY.Power", energy.Power);
            deviceStateMap.put("Sensor.ENERGY.Period", energy.Period);
            deviceStateMap.put("Sensor.ENERGY.Time", energy.Time);
            deviceStateMap.put("Sensor.ENERGY.Today", energy.Today);
            deviceStateMap.put("Sensor.ENERGY.Total", energy.Total);
            deviceStateMap.put("Sensor.ENERGY.TotalStartTime", energy.TotalStartTime);
            deviceStateMap.put("Sensor.ENERGY.Voltage", energy.Voltage);
            deviceStateMap.put("Sensor.ENERGY.Yesterday", energy.Yesterday);
            deviceStateMap.put("Sensor.ENERGY.ReactivePower", energy.ReactivePower);
        }

        DHT11 dht11 = tasmotaState.Dht11;
        if (null == dht11 && null != tasmotaState.StatusSNS) {
            dht11 = tasmotaState.StatusSNS.DHT11;
        }
        if (null != dht11) {
            deviceStateMap.put("Sensor.DHT11.Temperature", dht11.Temperature);
            deviceStateMap.put("Sensor.DHT11.DewPoint", dht11.DewPoint);
            deviceStateMap.put("Sensor.DHT11.Humidity", dht11.Humidity);
        }

        DS18B20 ds18B20 = tasmotaState.DS18B20;
        if (null == ds18B20 && null != tasmotaState.StatusSNS) {
            ds18B20 = tasmotaState.StatusSNS.DS18B20;
        }
        if (null != ds18B20) {
            deviceStateMap.put("Sensor.DS1820.Temperature", ds18B20.Temperature);
            deviceStateMap.put("Sensor.DS1820.Id", ds18B20.Id);

        }
        return deviceStateMap;
    }

    public static TasmotaState parseState(byte[] payload) {
        return parseState(new String(payload));
    }
}
