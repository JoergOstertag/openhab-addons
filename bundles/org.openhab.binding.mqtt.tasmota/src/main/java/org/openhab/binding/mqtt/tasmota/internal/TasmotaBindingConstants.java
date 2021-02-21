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

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.thing.ThingTypeUID;

/**
 * The {@link TasmotaBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Daan Meijer - Initial contribution
 * @author Jörg Ostertag - Adaptions to compile with openhab-3.1.0-SNAPSHOT
 */
@NonNullByDefault
public class TasmotaBindingConstants {

    public static final String BINDING_ID = "mqtt";

    // List of all Thing Type UIDs
    public static final ThingTypeUID TASMOTA_MQTT_THING = new ThingTypeUID(BINDING_ID, "tasmota");

    // List of all Channel ids
    public static final String CHANNEL_SWITCH = "switch";
    public static final String CHANNEL_DIMMER = "dimmer";

    public static final String CHANNEL_TEMPERATURE = "Sensor_DHT11_Temperature";
    public static final String CHANNEL_HUMIDITY = "Sensor_DHT11_Humidity";
    public static final String CHANNEL_DEWPOINT = "Sensor_DHT11_dewPoint";

    public static final String CHANNEL_VOLTAGE = "Sensor_ENERGY_Voltage";
    public static final String CHANNEL_POWER_LOAD = "Sensor_ENERGY_PowerLoad";

    public static boolean addChannelsForConfigValues = false;

    public static boolean debugSkipPropertyUpdate = false;
    public static boolean debugLimitDiscovery = false;
//    public static boolean debugSkipPropertyUpdate = true;
//    public static boolean debugLimitDiscovery = true;
}
