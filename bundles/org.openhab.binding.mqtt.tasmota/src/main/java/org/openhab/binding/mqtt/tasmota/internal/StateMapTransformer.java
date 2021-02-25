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

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;
import org.openhab.binding.mqtt.tasmota.internal.deviceState.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jörg Ostertag - Initial contribution
 */
public class StateMapTransformer {

    private static final Logger logger = LoggerFactory.getLogger(new Object() {
    }.getClass().getEnclosingClass());

    @Nullable
    public static Map<String, Object> transformConfigItemsToStateMap(TasmotaStateDTO tasmotaState) {
        Map<String, Object> stateMap = new HashMap<>();
        stateMap.put("Config.Sleep", tasmotaState.Sleep);
        stateMap.put("Config.SleepMode", tasmotaState.SleepMode);

        StatusFWRDTO statusFWRDTO = tasmotaState.StatusFWR;
        if (null != statusFWRDTO) {
            stateMap.put("Config.StatusFWR.Boot", statusFWRDTO.Boot);
            stateMap.put("Config.StatusFWR.BuildDateTime", statusFWRDTO.BuildDateTime); //
            stateMap.put("Config.StatusFWR.Core", statusFWRDTO.Core);
            stateMap.put("Config.StatusFWR.CpuFrequency", statusFWRDTO.CpuFrequency);
            stateMap.put("Config.StatusFWR.CR", statusFWRDTO.CR);
            stateMap.put("Config.StatusFWR.Hardware", statusFWRDTO.Hardware);
            stateMap.put("Config.StatusFWR.SDK", statusFWRDTO.SDK);
            stateMap.put("Config.StatusFWR.Version", statusFWRDTO.Version); //
        }

        if (TasmotaBindingConstants.onlyLimitedParsingForDebugging) {
            logger.warn("Only Limited State Parsing for Debugging");
            return stateMap;
        }

        WifiDTO wifiDTO = tasmotaState.Wifi;
        parseWifi(stateMap, wifiDTO);

        StatusLOGDTO statusLOGDTO = tasmotaState.StatusLOG;
        if (null != statusLOGDTO) {
            stateMap.put("Config.StatusLOG.LogHost", statusLOGDTO.LogHost);
            stateMap.put("Config.StatusLOG.LogPort", statusLOGDTO.LogPort);
            stateMap.put("Config.StatusLOG.MqttLog", statusLOGDTO.MqttLog);
            stateMap.put("Config.StatusLOG.Resolution", statusLOGDTO.Resolution);
            stateMap.put("Config.StatusLOG.SerialLog", statusLOGDTO.SerialLog);
            stateMap.put("Config.StatusLOG.SetOption", statusLOGDTO.SetOption);
            stateMap.put("Config.StatusLOG.SSId", statusLOGDTO.SSId);
            stateMap.put("Config.StatusLOG.TelePeriod", statusLOGDTO.TelePeriod);
            stateMap.put("Config.StatusLOG.WebLog", statusLOGDTO.WebLog);
        }

        StatusMEMDTO statusMEMDTO = tasmotaState.StatusMEM;
        if (null != statusMEMDTO) {
            stateMap.put("Config.StatusMEM.Drivers", statusMEMDTO.Drivers);
            stateMap.put("Config.StatusMEM.Features", statusMEMDTO.Features);
            stateMap.put("Config.StatusMEM.FlashChipId", statusMEMDTO.FlashChipId);
            stateMap.put("Config.StatusMEM.FlashFrequency", statusMEMDTO.FlashFrequency);
            stateMap.put("Config.StatusMEM.FlashMode", statusMEMDTO.FlashMode);
            stateMap.put("Config.StatusMEM.FlashSize", statusMEMDTO.FlashSize);
            stateMap.put("Config.StatusMEM.Free", statusMEMDTO.Free);
            stateMap.put("Config.StatusMEM.Heap", statusMEMDTO.Heap);
            stateMap.put("Config.StatusMEM.ProgramFlashSize", statusMEMDTO.ProgramFlashSize);
            stateMap.put("Config.StatusMEM.ProgramSize", statusMEMDTO.ProgramSize);
            stateMap.put("Config.StatusMEM.Sensors", statusMEMDTO.Sensors);
        }

        StatusMQTDTO statusMQTDTO = tasmotaState.StatusMQT;
        if (null != statusMQTDTO) {
            stateMap.put("Config.StatusMQT.KEEPALIVE", statusMQTDTO.KEEPALIVE);
            stateMap.put("Config.StatusMQT.MAX_PACKET_SIZE", statusMQTDTO.MAX_PACKET_SIZE);
            stateMap.put("Config.StatusMQT.MqttClient", statusMQTDTO.MqttClient);
            stateMap.put("Config.StatusMQT.MqttClientMask", statusMQTDTO.MqttClientMask);
            stateMap.put("Config.StatusMQT.MqttCount", statusMQTDTO.MqttCount);
            stateMap.put("Config.StatusMQT.MqttHost", statusMQTDTO.MqttHost);
            stateMap.put("Config.StatusMQT.MqttPort", statusMQTDTO.MqttPort);
            stateMap.put("Config.StatusMQT.MqttUser", statusMQTDTO.MqttUser);
        }

        StatusNETDTO statusNETDTO = tasmotaState.StatusNET;
        if (null != statusNETDTO) {
            stateMap.put("Config.StatusNET.DNSServer", statusNETDTO.DNSServer);
            stateMap.put("Config.StatusNET.Gateway", statusNETDTO.Gateway);
            stateMap.put("Config.StatusNET.Hostname", statusNETDTO.Hostname);
            stateMap.put("Config.StatusNET.IPAddress", statusNETDTO.IPAddress);
            stateMap.put("Config.StatusNET.Mac", statusNETDTO.Mac);
            stateMap.put("Config.StatusNET.Subnetmask", statusNETDTO.Subnetmask);
            stateMap.put("Config.StatusNET.Webserver", statusNETDTO.Webserver);
            stateMap.put("Config.StatusNET.WifiConfig", statusNETDTO.WifiConfig);
            stateMap.put("Config.StatusNET.WifiPower", statusNETDTO.WifiPower);
        }

        StatusPRMDTO statusPRMDTO = tasmotaState.StatusPRM;
        if (null != statusPRMDTO) {
            stateMap.put("Config.StatusPRM.Baudrate", statusPRMDTO.Baudrate);
            stateMap.put("Config.StatusPRM.BCResetTime", statusPRMDTO.BCResetTime);
            stateMap.put("Config.StatusPRM.BootCount", statusPRMDTO.BootCount);
            stateMap.put("Config.StatusPRM.CfgHolder", statusPRMDTO.CfgHolder);
            stateMap.put("Config.StatusPRM.GroupTopic", statusPRMDTO.GroupTopic);
            stateMap.put("Config.StatusPRM.OtaUrl", statusPRMDTO.OtaUrl);
            stateMap.put("Config.StatusPRM.RestartReason", statusPRMDTO.RestartReason);
            stateMap.put("Config.StatusPRM.SaveAddress", statusPRMDTO.SaveAddress);
            stateMap.put("Config.StatusPRM.SaveCount", statusPRMDTO.SaveCount);
            stateMap.put("Config.StatusPRM.SerialConfig", statusPRMDTO.SerialConfig);
            stateMap.put("Config.StatusPRM.Sleep", statusPRMDTO.Sleep);
            stateMap.put("Config.StatusPRM.StartupUTC", statusPRMDTO.StartupUTC);
            stateMap.put("Config.StatusPRM.Uptime", statusPRMDTO.Uptime);
        }

        StatusPTHDTO statusPTHDTO = tasmotaState.StatusPTH;
        if (null != statusPTHDTO) {
            stateMap.put("Config.StatusSTS.CurrentHigh", statusPTHDTO.CurrentHigh);
            stateMap.put("Config.StatusSTS.CurrentLow", statusPTHDTO.CurrentLow);
            stateMap.put("Config.StatusSTS.PowerDelta", statusPTHDTO.PowerDelta);
            stateMap.put("Config.StatusSTS.PowerLow", statusPTHDTO.PowerLow);
            stateMap.put("Config.StatusSTS.VoltageHigh", statusPTHDTO.VoltageHigh);
            stateMap.put("Config.StatusSTS.VoltageLow", statusPTHDTO.VoltageLow);
        }

        StatusSNSDTO statusSNSDTO = tasmotaState.StatusSNS;
        if (null != statusSNSDTO) {
            if (null != statusSNSDTO.TempUnit) {
                stateMap.put("Config.StatusSNS.TempUnit", statusSNSDTO.TempUnit);
            }
            if (statusSNSDTO.Time != null) {
                stateMap.put("Config.StatusSNS.Time", statusSNSDTO.Time);
            }
        }

        StatusSTSDTO statusSTSDTO = tasmotaState.StatusSTS;
        if (null != statusSTSDTO) {
            stateMap.put("Config.StatusSTS.Heap", statusSTSDTO.Heap);
            stateMap.put("Config.StatusSTS.LoadAvg", statusSTSDTO.LoadAvg);
            stateMap.put("Config.StatusSTS.MqttCount", statusSTSDTO.MqttCount);
            stateMap.put("Config.StatusSTS.Sleep", statusSTSDTO.Sleep);
            stateMap.put("Config.StatusSTS.SleepMode", statusSTSDTO.SleepMode);
            stateMap.put("Config.StatusSTS.Time", statusSTSDTO.Time);
            stateMap.put("Config.StatusSTS.Uptime", statusSTSDTO.Uptime);
            stateMap.put("Config.StatusSTS.UptimeSec", statusSTSDTO.UptimeSec);
            parseWifi(stateMap, statusSTSDTO.Wifi);
        }

        StatusTIMDTO statusTIMDTO = tasmotaState.StatusTIM;
        if (null != statusTIMDTO) {
            stateMap.put("Config.StatusTIM.EndDST", statusTIMDTO.EndDST);
            stateMap.put("Config.StatusTIM.Local", statusTIMDTO.Local);
            stateMap.put("Config.StatusTIM.StartDST", statusTIMDTO.StartDST);
            stateMap.put("Config.StatusTIM.Sunrise", statusTIMDTO.Sunrise);
            stateMap.put("Config.StatusTIM.Sunset", statusTIMDTO.Sunset);
            stateMap.put("Config.StatusTIM.Timezone", statusTIMDTO.Timezone);
            stateMap.put("Config.StatusTIM.UTC", statusTIMDTO.UTC);
        }
        return stateMap;
    }

    private static void parseWifi(Map<String, Object> stateMap, WifiDTO wifiDTO) {
        if (null != wifiDTO) {
            stateMap.put("Config.Wifi.AP", wifiDTO.AP);
            stateMap.put("Config.Wifi.BSSId", wifiDTO.BSSId);
            stateMap.put("Config.Wifi.Channel", wifiDTO.Channel);
            stateMap.put("Config.Wifi.Downtime", wifiDTO.Downtime);
            stateMap.put("Config.Wifi.LinkCount", wifiDTO.LinkCount);
            stateMap.put("Config.Wifi.wifi.RSSI", wifiDTO.RSSI);
            stateMap.put("Config.Wifi.Signal", wifiDTO.Signal);
            stateMap.put("Config.Wifi.SSId", wifiDTO.SSId);

        }
    }

    public static Map<String, Object> parseSensors(TasmotaStateDTO tasmotaState) {
        Map<String, Object> stateMap = new HashMap<>();
        if (tasmotaState.Dimmer != null) {
            stateMap.put("Sensor.Dimmer", tasmotaState.Dimmer);
        }
        if (tasmotaState.POWER != null) {
            stateMap.put("Sensor.Power", tasmotaState.POWER);
        }

        EnergyDTO energyDTO = tasmotaState.ENERGY;
        if (null == energyDTO && null != tasmotaState.StatusSNS) {
            energyDTO = tasmotaState.StatusSNS.ENERGY;
        }
        if (null != energyDTO) {
            stateMap.put("Sensor.ENERGY.ApparentPower", energyDTO.ApparentPower);
            stateMap.put("Sensor.ENERGY.Current", energyDTO.Current);
            stateMap.put("Sensor.ENERGY.Factor", energyDTO.Factor);
            stateMap.put("Sensor.ENERGY.Power", energyDTO.Power);
            stateMap.put("Sensor.ENERGY.Period", energyDTO.Period);
            stateMap.put("Sensor.ENERGY.Time", energyDTO.Time);
            stateMap.put("Sensor.ENERGY.Today", energyDTO.Today);
            stateMap.put("Sensor.ENERGY.Total", energyDTO.Total);
            stateMap.put("Sensor.ENERGY.TotalStartTime", energyDTO.TotalStartTime);
            stateMap.put("Sensor.ENERGY.Voltage", energyDTO.Voltage);
            stateMap.put("Sensor.ENERGY.Yesterday", energyDTO.Yesterday);
            stateMap.put("Sensor.ENERGY.ReactivePower", energyDTO.ReactivePower);
        }

        Dht11DTO dht11DTO = tasmotaState.Dht11;
        if (null == dht11DTO && null != tasmotaState.StatusSNS) {
            dht11DTO = tasmotaState.StatusSNS.DHT11;
        }
        if (null != dht11DTO) {
            stateMap.put("Sensor.DHT11.Temperature", dht11DTO.Temperature);
            stateMap.put("Sensor.DHT11.DewPoint", dht11DTO.DewPoint);
            stateMap.put("Sensor.DHT11.Humidity", dht11DTO.Humidity);
        }

        Ds18B20DTO ds18B20DTO = tasmotaState.DS18B20;
        if (null == ds18B20DTO && null != tasmotaState.StatusSNS) {
            ds18B20DTO = tasmotaState.StatusSNS.DS18B20;
        }
        if (null != ds18B20DTO) {
            stateMap.put("Sensor.DS1820.Temperature", ds18B20DTO.Temperature);
            stateMap.put("Sensor.DS1820.Id", ds18B20DTO.Id);

        }

        if (null != tasmotaState.POWER) {
            stateMap.put("Sensor.POWER", tasmotaState.POWER);
        }
        if (null != tasmotaState.POWER1) {
            stateMap.put("Sensor.POWER1", tasmotaState.POWER1);
        }
        if (null != tasmotaState.POWER2) {
            stateMap.put("Sensor.POWER2", tasmotaState.POWER2);
        }
        if (null != tasmotaState.POWER3) {
            stateMap.put("Sensor.POWER3", tasmotaState.POWER3);
        }
        if (null != tasmotaState.POWER4) {
            stateMap.put("Sensor.POWER4", tasmotaState.POWER4);
        }

        if (null != tasmotaState.StatusSTS) {
            if (null != tasmotaState.StatusSTS.POWER) {
                stateMap.put("Sensor.POWER", tasmotaState.StatusSTS.POWER);
            }
            if (null != tasmotaState.StatusSTS.POWER1) {
                stateMap.put("Sensor.POWER1", tasmotaState.StatusSTS.POWER1);
            }
            if (null != tasmotaState.StatusSTS.POWER2) {
                stateMap.put("Sensor.POWER2", tasmotaState.StatusSTS.POWER2);
            }
            if (null != tasmotaState.StatusSTS.POWER3) {
                stateMap.put("Sensor.POWER3", tasmotaState.StatusSTS.POWER3);
            }
            if (null != tasmotaState.StatusSTS.POWER4) {
                stateMap.put("Sensor.POWER4", tasmotaState.StatusSTS.POWER4);
            }
        }

        return stateMap;
    }
}
