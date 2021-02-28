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

import java.io.*;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
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
        createParentDirsForFile(new File(filename));
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
     * @param filename
     */
    public static void createParentDirsForFile(String filename) {
        createParentDirsForFile(new File(filename));
    }

    /**
     * Create Directory and parent Directories if necessary
     *
     * @param file
     */
    public static void createParentDirsForFile(File file) {
        String parent = file.getParent();
        if (null != parent) {
            File directoryAsFile = new File(parent);
            if (!directoryAsFile.exists()) {
                logger.debug("Create directrory: {}", directoryAsFile);
                directoryAsFile.mkdirs();
            }
        }
    }

    /**
     * Read the file into a String(UTF-8).<BR>
     * If we cannot find/read the file we log a warning and return <null>
     *
     * @param filename
     * @return content of the File or <null>
     */
    @Nullable
    public static String readFile(String filename) {

        // logger.debug("Result filename: " + filename);

        String content = "";
        try {
            final File file = new File(filename);
            if (!file.exists()) {
                logger.warn("Cannot find File : " + filename);
                return null;
            }
            if (!file.canRead()) {
                logger.warn("Cannot read File : " + filename);
                return null;
            }
            {
                final FileInputStream fis = new FileInputStream(file);
                final byte[] data = new byte[(int) file.length()];
                fis.read(data);
                fis.close();

                content = new String(data, "UTF-8");
            }

        } catch (final FileNotFoundException e) {
            logger.error("An error occurred reading File: " + filename);
            logger.error(ExceptionHelper.compactStackTrace(e));
        } catch (final IOException e) {
            logger.error("An error occurred reading File: " + filename);
            logger.error(ExceptionHelper.compactStackTrace(e));
        }
        return content;
    }

    /**
     * Write content to File named filename.<br>
     * <p>
     * If needed the parent directories are created
     *
     * @param filename
     * @param content
     */
    public static void writeFile(String filename, String content) {
        createParentDirsForFile(filename);
        try {
            final FileWriter myWriter = new FileWriter(filename);
            myWriter.write(content);
            myWriter.close();
        } catch (final IOException e) {
            logger.error(ExceptionHelper.compactStackTrace(e));
        }
    }
}
