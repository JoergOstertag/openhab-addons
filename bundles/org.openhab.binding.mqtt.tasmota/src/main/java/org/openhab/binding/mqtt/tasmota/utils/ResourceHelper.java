package org.openhab.binding.mqtt.tasmota.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ResourceHelper {

    public static BufferedReader bufferedReaderFromReource(String resourceName) {
        InputStream is = ResourceHelper.class.getClassLoader().getResourceAsStream(resourceName);
        BufferedReader reader = null;
        InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
        reader = new BufferedReader(streamReader);
        return reader;
    }
}
