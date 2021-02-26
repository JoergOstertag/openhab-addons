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
package org.openhab.binding.mqtt.tasmota.internal;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * @author Jörg Ostertag - Initial contribution
 */
@NonNullByDefault
class MessageConfigItem {

    private String key;

    @Nullable
    private String propertyName;

    @Nullable
    private String channelName;

    private String channelDescription = "";

    /**
     * The 3rd part of the topic "cmnd/Device-Id/POWER" to send a command to change the value in the tasmota device
     */
    private String actionCommandTopic = "";

    public void setChannelDescription(String channelDescription) {
        this.channelDescription = channelDescription;
    }

    public MessageConfigItem(String topic, String key) {
        key = getUnifiedKey(topic, key);
        this.key = key;
    }

    public static String getUnifiedKey(String topic, String key) {
        String subTopics[] = topic.toLowerCase().split("/");
        key = key.toLowerCase();
        key = key.replaceAll(".*\\.status([^\\.]{3,3})", "\\$.status.$1");
        if (topic.startsWith("cmnd/")) { // cmnd/OLED-0/DisplayText ===> $.cmnd.displaytext+.<key>
            key = "$.cmnd." + subTopics[2] + "." + key;
        } else if (topic.startsWith("stat/") && topic.endsWith("/RESULT")) { // stat/Pow-3/RESULT ===> $.result.<key>
            key = "$.result." + key.replace("$.", "");
        } else if (key.equals("$")) { // cmnd/Pow-2/POWER $ ===> $.power
            key = "$." + subTopics[2];
        }
        return key;
    }

    @Nullable
    public String getPropertyName() {
        return propertyName;
    }

    public MessageConfigItem setPropertyName(String propertyName) {
        this.propertyName = propertyName;
        return this;
    }

    @Nullable
    public String getChannelName() {
        return channelName;
    }

    public MessageConfigItem setChannelName(String channelName) {
        channelName = channelName.replaceAll("\\.", "_");
        this.channelName = channelName;
        return this;
    }

    public String getKey() {
        return key;
    }

    public String getChannelDescription() {
        if (StringUtils.isEmpty(channelDescription)) {
            if (null != channelName && StringUtils.isNotEmpty(channelName)) {
                channelDescription = (@NonNull String) channelName;
            }
            if (StringUtils.isEmpty(channelDescription)) {
                channelDescription = key;
            }
            channelDescription = channelDescription.replaceAll("\\.", " ");
        }
        return channelDescription;
    }

    public void setActionCmndTopic(String actionCommandTopic) {
        this.actionCommandTopic = actionCommandTopic;
    }

    public String getActionCommandTopic() {
        return actionCommandTopic;
    }

    public void setActionCommandTopic(String actionCommandTopic) {
        this.actionCommandTopic = actionCommandTopic;
    }
}
