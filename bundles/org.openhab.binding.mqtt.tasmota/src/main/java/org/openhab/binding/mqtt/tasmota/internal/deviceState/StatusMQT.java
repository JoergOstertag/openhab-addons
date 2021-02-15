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
package org.openhab.binding.mqtt.tasmota.internal.deviceState;

/**
 * @author Daan Meijer - Initial contribution
 * @author Jörg Ostertag - Modifications to compile with openhab-3.1.0
 */
public class StatusMQT extends AbstractJsonDTO {

    public String MqttHost; // ""10.12.12.22"
    public Integer MqttPort; // 1883
    public String MqttClientMask; // "Client-Pow-1"
    public String MqttClient; // "Client-Pow-1"
    public String MqttUser; // "DVES_USER"
    public Integer MqttCount; // 1
    public Integer MAX_PACKET_SIZE; // 1200
    public Integer KEEPALIVE; // 30
}
