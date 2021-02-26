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
package org.openhab.binding.mqtt.tasmota.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jörg Ostertag - Initial contribution
 */
@NonNullByDefault
public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(new Object() {
    }.getClass().getEnclosingClass());

    public static void fileAppend(String filename, String content) {
        createDirForFile(new File(filename));
        try {
            FileWriter fw = new FileWriter(filename, true); // the true will append the new data
            fw.write(content);
            fw.close();
        } catch (IOException e) {
            logger.error("Error writing file {}\n" + //
                    "{}", //
                    filename, ExceptionHelper.compactStackTrace(e));
        }
    }

    /**
     * Create Directory and parent Directories if necessary
     *
     * @param file
     */
    public static void createDirForFile(File file) {
        String parent = file.getParent();
        if (null != parent) {
            File directoryAsFile = new File(parent);
            if (!directoryAsFile.exists()) {
                logger.debug("Create directrory: {}", directoryAsFile);
                directoryAsFile.mkdirs();
            }
        }
    }
}
