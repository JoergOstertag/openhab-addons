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
public class StatusMEMDTO extends AbstractJsonDTO {

    public Integer ProgramSize; // "586
    public Integer Free; // "416
    public Integer Heap; // "25
    public String ProgramFlashSize; // "1024
    public String FlashSize; // "4096
    public String FlashChipId; // ""1640EF"
    public Integer FlashFrequency; // "40
    public Integer FlashMode; // "3
    public List<String> Features; // "["00000809",...,"00001000"]
    public String Drivers; // ""1,2,3,4,5,6,7,8,9,10,12,16,18,19,20,21,22,24,26,27,29,30,35,37,45"
    public String Sensors; // ""1,2,3,4,5,6"}}
}
