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
public class StatusTIMDTO extends AbstractJsonDTO {

    public String UTC; // ""2021-01-28 T20:14:52"
    public String Local; // ""2021-01-28 T21:14:52"
    public String StartDST; // ""2021-03-28 T02:00:00"
    public String EndDST; // ""2021-10-31 T03:00:00"
    public String Timezone; // "99
    public String Sunrise; // ""07:44"
    public String Sunset; // ""17:04"
}
