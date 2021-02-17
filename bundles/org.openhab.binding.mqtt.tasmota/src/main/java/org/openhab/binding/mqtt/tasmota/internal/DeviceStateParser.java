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

    public static @NonNull TasmotaStateDTO parseState(String stateAsJson) {
        TasmotaStateDTO tasmotaStateDTOFromJson = null;
        try {
            tasmotaStateDTOFromJson = gson.fromJson(stateAsJson, TasmotaStateDTO.class);

            boolean forDebugCheckIfAllIsParsed = false;
            if (forDebugCheckIfAllIsParsed) {
                String toJson = gson.toJson(tasmotaStateDTOFromJson);
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
        if (null == tasmotaStateDTOFromJson) {
            tasmotaStateDTOFromJson = new TasmotaStateDTO();
        }
        return tasmotaStateDTOFromJson;
    }

    public static Map<@NonNull String, @NonNull Object> stateToHashMap(TasmotaStateDTO tasmotaState) {

        Map<String, Object> deviceStateMap = new HashMap<>();
        deviceStateMap.putAll(parseSensors(tasmotaState));
        deviceStateMap.putAll(parseConfigItems(tasmotaState));

        return deviceStateMap;
    }

    @Nullable
    public static Map<String, Object> parseConfigItems(TasmotaStateDTO tasmotaState) {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put("Config.Sleep", tasmotaState.Sleep);
        configMap.put("Config.SleepMode", tasmotaState.SleepMode);

        StatusFWRDTO statusFWRDTO = tasmotaState.StatusFWR;
        if (null != statusFWRDTO) {
            configMap.put("Config.StatusFWR.Boot", statusFWRDTO.Boot);
            configMap.put("Config.StatusFWR.BuildDateTime", statusFWRDTO.BuildDateTime); //
            configMap.put("Config.StatusFWR.Core", statusFWRDTO.Core);
            configMap.put("Config.StatusFWR.CpuFrequency", statusFWRDTO.CpuFrequency);
            configMap.put("Config.StatusFWR.CR", statusFWRDTO.CR);
            configMap.put("Config.StatusFWR.Hardware", statusFWRDTO.Hardware);
            configMap.put("Config.StatusFWR.SDK", statusFWRDTO.SDK);
            configMap.put("Config.StatusFWR.Version", statusFWRDTO.Version); //
        }

        if (onlyLimitedParsingForDebugging) {
            logger.warn("Only Limited State Parsing for Debugging");
            return configMap;
        }

        WifiDTO wifiDTO = tasmotaState.Wifi;
        parseWifi(configMap, wifiDTO);

        StatusLOGDTO statusLOGDTO = tasmotaState.StatusLOG;
        if (null != statusLOGDTO) {
            configMap.put("Config.StatusLOG.LogHost", statusLOGDTO.LogHost);
            configMap.put("Config.StatusLOG.LogPort", statusLOGDTO.LogPort);
            configMap.put("Config.StatusLOG.MqttLog", statusLOGDTO.MqttLog);
            configMap.put("Config.StatusLOG.Resolution", statusLOGDTO.Resolution);
            configMap.put("Config.StatusLOG.SerialLog", statusLOGDTO.SerialLog);
            configMap.put("Config.StatusLOG.SetOption", statusLOGDTO.SetOption);
            configMap.put("Config.StatusLOG.SSId", statusLOGDTO.SSId);
            configMap.put("Config.StatusLOG.TelePeriod", statusLOGDTO.TelePeriod);
            configMap.put("Config.StatusLOG.WebLog", statusLOGDTO.WebLog);
        }

        StatusMEMDTO statusMEMDTO = tasmotaState.StatusMEM;
        if (null != statusMEMDTO) {
            configMap.put("Config.StatusMEM.Drivers", statusMEMDTO.Drivers);
            configMap.put("Config.StatusMEM.Features", statusMEMDTO.Features);
            configMap.put("Config.StatusMEM.FlashChipId", statusMEMDTO.FlashChipId);
            configMap.put("Config.StatusMEM.FlashFrequency", statusMEMDTO.FlashFrequency);
            configMap.put("Config.StatusMEM.FlashMode", statusMEMDTO.FlashMode);
            configMap.put("Config.StatusMEM.FlashSize", statusMEMDTO.FlashSize);
            configMap.put("Config.StatusMEM.Free", statusMEMDTO.Free);
            configMap.put("Config.StatusMEM.Heap", statusMEMDTO.Heap);
            configMap.put("Config.StatusMEM.ProgramFlashSize", statusMEMDTO.ProgramFlashSize);
            configMap.put("Config.StatusMEM.ProgramSize", statusMEMDTO.ProgramSize);
            configMap.put("Config.StatusMEM.Sensors", statusMEMDTO.Sensors);
        }

        StatusMQTDTO statusMQTDTO = tasmotaState.StatusMQT;
        if (null != statusMQTDTO) {
            configMap.put("Config.StatusMQT.KEEPALIVE", statusMQTDTO.KEEPALIVE);
            configMap.put("Config.StatusMQT.MAX_PACKET_SIZE", statusMQTDTO.MAX_PACKET_SIZE);
            configMap.put("Config.StatusMQT.MqttClient", statusMQTDTO.MqttClient);
            configMap.put("Config.StatusMQT.MqttClientMask", statusMQTDTO.MqttClientMask);
            configMap.put("Config.StatusMQT.MqttCount", statusMQTDTO.MqttCount);
            configMap.put("Config.StatusMQT.MqttHost", statusMQTDTO.MqttHost);
            configMap.put("Config.StatusMQT.MqttPort", statusMQTDTO.MqttPort);
            configMap.put("Config.StatusMQT.MqttUser", statusMQTDTO.MqttUser);
        }

        StatusNETDTO statusNETDTO = tasmotaState.StatusNET;
        if (null != statusNETDTO) {
            configMap.put("Config.StatusNET.DNSServer", statusNETDTO.DNSServer);
            configMap.put("Config.StatusNET.Gateway", statusNETDTO.Gateway);
            configMap.put("Config.StatusNET.Hostname", statusNETDTO.Hostname);
            configMap.put("Config.StatusNET.IPAddress", statusNETDTO.IPAddress);
            configMap.put("Config.StatusNET.Mac", statusNETDTO.Mac);
            configMap.put("Config.StatusNET.Subnetmask", statusNETDTO.Subnetmask);
            configMap.put("Config.StatusNET.Webserver", statusNETDTO.Webserver);
            configMap.put("Config.StatusNET.WifiConfig", statusNETDTO.WifiConfig);
            configMap.put("Config.StatusNET.WifiPower", statusNETDTO.WifiPower);
        }

        StatusPRMDTO statusPRMDTO = tasmotaState.StatusPRM;
        if (null != statusPRMDTO) {
            configMap.put("Config.StatusPRM.OtaUrl", statusPRMDTO.Baudrate);
            configMap.put("Config.StatusPRM.BCResetTime", statusPRMDTO.BCResetTime);
            configMap.put("Config.StatusPRM.BootCount", statusPRMDTO.BootCount);
            configMap.put("Config.StatusPRM.CfgHolder", statusPRMDTO.CfgHolder);
            configMap.put("Config.StatusPRM.GroupTopic", statusPRMDTO.GroupTopic);
            configMap.put("Config.StatusPRM.OtaUrl", statusPRMDTO.OtaUrl);
            configMap.put("Config.StatusPRM.RestartReason", statusPRMDTO.RestartReason);
            configMap.put("Config.StatusPRM.SaveAddress", statusPRMDTO.SaveAddress);
            configMap.put("Config.StatusPRM.SaveCount", statusPRMDTO.SaveCount);
            configMap.put("Config.StatusPRM.SerialConfig", statusPRMDTO.SerialConfig);
            configMap.put("Config.StatusPRM.SerialConfig", statusPRMDTO.Sleep);
            configMap.put("Config.StatusPRM.StartupUTC", statusPRMDTO.StartupUTC);
            configMap.put("Config.StatusPRM.Uptime", statusPRMDTO.Uptime);
        }

        StatusPTHDTO statusPTHDTO = tasmotaState.StatusPTH;
        if (null != statusPTHDTO) {
            configMap.put("Config.StatusSTS.CurrentHigh", statusPTHDTO.CurrentHigh);
            configMap.put("Config.StatusSTS.CurrentLow", statusPTHDTO.CurrentLow);
            configMap.put("Config.StatusSTS.PowerDelta", statusPTHDTO.PowerDelta);
            configMap.put("Config.StatusSTS.PowerLow", statusPTHDTO.PowerLow);
            configMap.put("Config.StatusSTS.VoltageHigh", statusPTHDTO.VoltageHigh);
            configMap.put("Config.StatusSTS.VoltageLow", statusPTHDTO.VoltageLow);
        }

        StatusSNSDTO statusSNSDTO = tasmotaState.StatusSNS;
        if (null != statusSNSDTO) {
            if (null != statusSNSDTO.TempUnit) {
                configMap.put("Config.StatusSNS.TempUnit", statusSNSDTO.TempUnit);
            }
            if (statusSNSDTO.Time != null) {
                configMap.put("Config.StatusSNS.Time", statusSNSDTO.Time);
            }
        }

        StatusSTSDTO statusSTSDTO = tasmotaState.StatusSTS;
        if (null != statusSTSDTO) {
            configMap.put("Config.StatusSTS.Heap", statusSTSDTO.Heap);
            configMap.put("Config.StatusSTS.LoadAvg", statusSTSDTO.LoadAvg);
            configMap.put("Config.StatusSTS.MqttCount", statusSTSDTO.MqttCount);
            configMap.put("Config.StatusSTS.Sleep", statusSTSDTO.Sleep);
            configMap.put("Config.StatusSTS.SleepMode", statusSTSDTO.SleepMode);
            configMap.put("Config.StatusSTS.Time", statusSTSDTO.Time);
            configMap.put("Config.StatusSTS.Uptime", statusSTSDTO.Uptime);
            configMap.put("Config.StatusSTS.UptimeSec", statusSTSDTO.UptimeSec);
            parseWifi(configMap, statusSTSDTO.Wifi);
        }

        StatusTIMDTO statusTIMDTO = tasmotaState.StatusTIM;
        if (null != statusTIMDTO) {
            configMap.put("Config.StatusTIM.EndDST", statusTIMDTO.EndDST);
            configMap.put("Config.StatusTIM.Local", statusTIMDTO.Local);
            configMap.put("Config.StatusTIM.StartDST", statusTIMDTO.StartDST);
            configMap.put("Config.StatusTIM.Sunrise", statusTIMDTO.Sunrise);
            configMap.put("Config.StatusTIM.Sunset", statusTIMDTO.Sunset);
            configMap.put("Config.StatusTIM.Timezone", statusTIMDTO.Timezone);
            configMap.put("Config.StatusTIM.UTC", statusTIMDTO.UTC);
        }
        return configMap;
    }

    public static boolean parseDeviceTypeKnown(TasmotaStateDTO tasmotaState, Map<String, Object> deviceStateMap) {
        boolean deviceKnown = false;
        deviceKnown |= (tasmotaState.Dimmer != null);
        deviceKnown |= (tasmotaState.POWER != null);
        if (null != tasmotaState.Status) {
            deviceKnown |= (tasmotaState.Status.Power != null);
        }

        EnergyDTO energyDTO = tasmotaState.ENERGY;
        if (null == energyDTO && (null != tasmotaState.StatusSNS)) {
            energyDTO = tasmotaState.StatusSNS.ENERGY;
        }
        if (null != energyDTO) {
            deviceKnown |= (energyDTO.Power != null);
            deviceKnown |= (energyDTO.Current != null);
        }

        Dht11DTO dht11DTO = tasmotaState.Dht11;
        if (null == dht11DTO && null != tasmotaState.StatusSNS) {
            dht11DTO = tasmotaState.StatusSNS.DHT11;
        }
        if (null != dht11DTO) {
            deviceKnown |= (dht11DTO.Temperature != null);
            deviceKnown |= (dht11DTO.Humidity != null);
        }
        Ds18B20DTO ds18B20DTO = tasmotaState.DS18B20;
        if (null == ds18B20DTO && null != tasmotaState.StatusSNS) {
            ds18B20DTO = tasmotaState.StatusSNS.DS18B20;
        }
        if (null != ds18B20DTO) {
            deviceKnown |= (ds18B20DTO.Temperature != null);
        }

        for (Map.Entry<String, Object> property : deviceStateMap.entrySet()) {
            String propertyName = property.getKey();
            Object propertyValue = property.getValue();
            deviceKnown |= (propertyName.startsWith("Sensor.") && (null != propertyValue));
        }
        return deviceKnown;
    }

    private static void parseWifi(Map<String, Object> properties, WifiDTO wifiDTO) {
        if (null != wifiDTO) {
            properties.put("Config.Wifi.AP", wifiDTO.AP);
            properties.put("Config.Wifi.BSSId", wifiDTO.BSSId);
            properties.put("Config.Wifi.Channel", wifiDTO.Channel);
            properties.put("Config.Wifi.Downtime", wifiDTO.Downtime);
            properties.put("Config.Wifi.LinkCount", wifiDTO.LinkCount);
            properties.put("Config.Wifi.wifi.RSSI", wifiDTO.RSSI);
            properties.put("Config.Wifi.Signal", wifiDTO.Signal);
            properties.put("Config.Wifi.SSId", wifiDTO.SSId);

        }
    }

    public static Map<String, Object> parseSensors(TasmotaStateDTO tasmotaState) {
        Map<String, Object> deviceStateMap = new HashMap<>();
        if (tasmotaState.Dimmer != null) {
            deviceStateMap.put("Sensor.Dimmer", tasmotaState.Dimmer);
        }
        if (tasmotaState.POWER != null) {
            deviceStateMap.put("Sensor.Power", tasmotaState.POWER);
        }

        EnergyDTO energyDTO = tasmotaState.ENERGY;
        if (null == energyDTO && null != tasmotaState.StatusSNS) {
            energyDTO = tasmotaState.StatusSNS.ENERGY;
        }
        if (null != energyDTO) {
            deviceStateMap.put("Sensor.ENERGY.ApparentPower", energyDTO.ApparentPower);
            deviceStateMap.put("Sensor.ENERGY.Current", energyDTO.Current);
            deviceStateMap.put("Sensor.ENERGY.Factor", energyDTO.Factor);
            deviceStateMap.put("Sensor.ENERGY.Power", energyDTO.Power);
            deviceStateMap.put("Sensor.ENERGY.Period", energyDTO.Period);
            deviceStateMap.put("Sensor.ENERGY.Time", energyDTO.Time);
            deviceStateMap.put("Sensor.ENERGY.Today", energyDTO.Today);
            deviceStateMap.put("Sensor.ENERGY.Total", energyDTO.Total);
            deviceStateMap.put("Sensor.ENERGY.TotalStartTime", energyDTO.TotalStartTime);
            deviceStateMap.put("Sensor.ENERGY.Voltage", energyDTO.Voltage);
            deviceStateMap.put("Sensor.ENERGY.Yesterday", energyDTO.Yesterday);
            deviceStateMap.put("Sensor.ENERGY.ReactivePower", energyDTO.ReactivePower);
        }

        Dht11DTO dht11DTO = tasmotaState.Dht11;
        if (null == dht11DTO && null != tasmotaState.StatusSNS) {
            dht11DTO = tasmotaState.StatusSNS.DHT11;
        }
        if (null != dht11DTO) {
            deviceStateMap.put("Sensor.DHT11.Temperature", dht11DTO.Temperature);
            deviceStateMap.put("Sensor.DHT11.DewPoint", dht11DTO.DewPoint);
            deviceStateMap.put("Sensor.DHT11.Humidity", dht11DTO.Humidity);
        }

        Ds18B20DTO ds18B20DTO = tasmotaState.DS18B20;
        if (null == ds18B20DTO && null != tasmotaState.StatusSNS) {
            ds18B20DTO = tasmotaState.StatusSNS.DS18B20;
        }
        if (null != ds18B20DTO) {
            deviceStateMap.put("Sensor.DS1820.Temperature", ds18B20DTO.Temperature);
            deviceStateMap.put("Sensor.DS1820.Id", ds18B20DTO.Id);

        }

        if (null != tasmotaState.POWER) {
            deviceStateMap.put("Sensor.POWER", tasmotaState.POWER);
        }
        if (null != tasmotaState.POWER1) {
            deviceStateMap.put("Sensor.POWER1", tasmotaState.POWER1);
        }
        if (null != tasmotaState.POWER2) {
            deviceStateMap.put("Sensor.POWER2", tasmotaState.POWER2);
        }
        if (null != tasmotaState.POWER3) {
            deviceStateMap.put("Sensor.POWER3", tasmotaState.POWER3);
        }
        if (null != tasmotaState.POWER4) {
            deviceStateMap.put("Sensor.POWER4", tasmotaState.POWER4);
        }

        if (null != tasmotaState.StatusSTS) {
            if (null != tasmotaState.StatusSTS.POWER) {
                deviceStateMap.put("Sensor.POWER", tasmotaState.StatusSTS.POWER);
            }
            if (null != tasmotaState.StatusSTS.POWER1) {
                deviceStateMap.put("Sensor.POWER1", tasmotaState.StatusSTS.POWER1);
            }
            if (null != tasmotaState.StatusSTS.POWER2) {
                deviceStateMap.put("Sensor.POWER2", tasmotaState.StatusSTS.POWER2);
            }
            if (null != tasmotaState.StatusSTS.POWER3) {
                deviceStateMap.put("Sensor.POWER3", tasmotaState.StatusSTS.POWER3);
            }
            if (null != tasmotaState.StatusSTS.POWER4) {
                deviceStateMap.put("Sensor.POWER4", tasmotaState.StatusSTS.POWER4);
            }
        }

        return deviceStateMap;
    }

    public static TasmotaStateDTO parseState(byte[] payload) {
        return parseState(new String(payload));
    }
}
