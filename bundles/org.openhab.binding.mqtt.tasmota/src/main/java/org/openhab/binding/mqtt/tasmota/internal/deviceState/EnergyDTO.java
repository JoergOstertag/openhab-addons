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
package org.openhab.binding.mqtt.tasmota.internal.deviceState;

/**
 * @author Daan Meijer - Initial contribution
 * @author Jörg Ostertag - Modifications to compile with openhab-3.1.0
 */
public class EnergyDTO extends AbstractJsonDTO {

    public Integer ApparentPower;
    public Double Current;
    public Double Factor;
    public Integer Period;
    public Double Power;
    public Double ReactivePower;
    public Double Today;
    public Double Total;
    public String TotalStartTime;
    public Double Voltage;
    public Double Yesterday;
    public String Time;
}
