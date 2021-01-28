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
 *
 *
 */
package org.openhab.binding.mqtt.tasmota.internal.deviceState;

import java.util.List;

/**
 * @author Daan Meijer - Initial contribution
 * @author Jörg Ostertag - Modifications to compile with openhab-3.1.0
 *
 */
public class StatusPTH {

    public List<Integer> PowerDelta; // ; // "[0,0,0]
    public Integer PowerLow; // "0
    public Integer PowerHigh; // "0
    public Integer VoltageLow; // "0
    public Integer VoltageHigh; // "0
    public Integer CurrentLow; // "0
    public Integer CurrentHigh; // "0}}
}
