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
package org.openhab.binding.mqtt.tasmota.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jörg Ostertag - Initial contribution
 */
@NonNullByDefault
public class SystemUtils {

    private static final Logger logger = LoggerFactory.getLogger(new Object() {
    }.getClass().getEnclosingClass());

    /**
     * Get my Computer Name from Environment
     *
     * @return
     */
    public static String getLocalComputerName() {
        @Nullable
        String os = System.getProperty("os.name").toLowerCase();
        @Nullable
        String computerName = "UNKNOWN";
        if (os.contains("win")) {
            computerName = System.getenv("COMPUTERNAME");
        } else {
            computerName = System.getenv("HOSTNAME");
        }
        computerName = (null == computerName) ? "UNKNOWN" : computerName;
        return computerName;
    }

    /**
     * Get Hostname from Network interface definitions
     *
     * @return
     */
    public static String getLocalHostname() {
        String hostname = "";
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            logger.error("ERROR while getting hostname\n" //
                    + "{}", ExceptionHelper.compactStackTrace(e));
        }
        return hostname;
    }
}
