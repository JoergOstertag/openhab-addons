package org.openhab.binding.mqtt.tasmota.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemUtils {

    private static final Logger logger = LoggerFactory.getLogger(new Object() {
    }.getClass().getEnclosingClass());

    public static String getLocalComputerName() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return System.getenv("COMPUTERNAME");
        }
        return System.getenv("HOSTNAME");
    }

    public static String getLocalHostname() {
        String hostname = "";
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            logger.error("ERROR while getting hostname: " + ExceptionHelper.compactStackTrace(e));
        }
        return hostname;
    }
}
