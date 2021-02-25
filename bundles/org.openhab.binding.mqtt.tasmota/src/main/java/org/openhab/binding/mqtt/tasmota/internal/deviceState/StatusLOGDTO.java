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

import java.util.List;

/**
 * @author Daan Meijer - Initial contribution
 * @author Jörg Ostertag - Modifications to compile with openhab-3.1.0
 */
public class StatusLOGDTO extends AbstractJsonDTO {

    public String SerialLog; // 0
    public String WebLog; // 3
    public String MqttLog; // 0
    public String SysLog; // 0
    public String LogHost; // ""
    public Integer LogPort; // 514
    public List<String> SSId; // ["test1", "test2"]
    public String TelePeriod; // 30
    public String Resolution; // "558180C0"
    public List<String> SetOption; // ["00028009","000"]}}
}
