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
package org.openhab.binding.mqtt.tasmota.internal;

import static org.openhab.binding.mqtt.tasmota.internal.TasmotaBindingConstants.BINDING_ID;
import static org.openhab.binding.mqtt.tasmota.internal.TasmotaBindingConstants.CHANNEL_DIMMER;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.jetbrains.annotations.NotNull;
import org.openhab.binding.mqtt.handler.BrokerHandler;
import org.openhab.binding.mqtt.tasmota.utils.ExceptionHelper;
import org.openhab.binding.mqtt.tasmota.utils.FileUtils;
import org.openhab.core.io.transport.mqtt.MqttBrokerConnection;
import org.openhab.core.library.types.*;
import org.openhab.core.thing.*;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.thing.binding.ThingHandlerCallback;
import org.openhab.core.thing.binding.builder.ChannelBuilder;
import org.openhab.core.thing.binding.builder.ThingBuilder;
import org.openhab.core.thing.type.ChannelGroupTypeRegistry;
import org.openhab.core.thing.type.ChannelType;
import org.openhab.core.thing.type.ChannelTypeRegistry;
import org.openhab.core.thing.type.ChannelTypeUID;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link TasmotaHandlerImpl} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Daan Meijer - Initial contribution
 * @author Jörg Ostertag - Adaptions to compile with openhab-3.1.0-SNAPSHOT
 */
@NonNullByDefault
public class TasmotaHandlerImpl extends BaseThingHandler implements TasmotaHandler {

    private final Logger logger = LoggerFactory.getLogger(TasmotaHandlerImpl.class);

    private @Nullable TasmotaConfiguration config;

    protected @Nullable TasmotaSubscriber tasmotaSubscriber;

    private final ChannelTypeRegistry channelTypeRegistry;
    private Map<ChannelUID, MessageConfigItem> channelInternalConfig = new HashMap();

    public TasmotaHandlerImpl(Thing thing, ChannelTypeRegistry channelTypeRegistry) {
        super(thing);
        this.channelTypeRegistry = channelTypeRegistry;
        logger.debug("Init TasmotaHandler(Thing: {})", thing.getUID());
    }

    @Override
    public void initialize() {
        logger.debug("Start initializing Tasmota Handler -- Bridge: {}, Thing: {}", getBridge().getUID(),
                getThing().getUID());
        config = getConfigAs(TasmotaConfiguration.class);

        Bridge bridge = getBridge();
        BrokerHandler brokerHandler = (BrokerHandler) bridge.getHandler();
        MqttBrokerConnection connection = null;

        while (connection == null) {
            connection = brokerHandler.getConnection();
            if (null == connection) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
            }
        }

        logger.debug("initialize(): broker connection: {}", toConnectionString(connection));

        tasmotaSubscriber = new TasmotaSubscriber(connection, config.deviceid, this);

        updateStatus(ThingStatus.UNKNOWN);

        tasmotaSubscriber.triggerUpdate();

        if (this.getCallback() == null) {
            logger.error("Missing Callback in freshly created Thing {}\n" + //
                    "Stacktrace: \n" + //
                    "{}", this.getThing().getUID(), ExceptionHelper.compactStackTrace());
        }

        channelGroupTypeRegistry = getService(ChannelGroupTypeRegistry.class);

        logger.debug("Finished initializing. Type: {}, UID: {}", thing.getThingTypeUID(), thing.getUID());
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.debug("handleCommand({}, {})", channelUID, command);
        if (null == tasmotaSubscriber) {
            logger.warn("Handling command without being initialized");
            return;
        }

        {
            MessageConfigItem messageConfigItem = channelInternalConfig.get(channelUID);
            logger.debug("Thing: {}, Channel: {}, messageConfigItem: {}", thing, channelUID, messageConfigItem);
            if (null != messageConfigItem) {
                String actionCommandTopic = messageConfigItem.getActionCommandTopic();
                if (command instanceof RefreshType) {
                    // TODO: handle data refresh
                } else {
                    OnOffType wantedValue = (OnOffType) command;
                    String value = wantedValue.equals(OnOffType.ON) ? "ON" : "OFF";
                    tasmotaSubscriber.publishCommand(actionCommandTopic, value);
                }
            }
        }
        if (CHANNEL_DIMMER.equals(channelUID.getId())) {
            if (command instanceof RefreshType) {
                // TODO: handle data refresh
            } else {
                PercentType wantedValue = (PercentType) command;
                tasmotaSubscriber.publishCommand("DIMMER", "" + wantedValue.intValue());
            }
        }
    }

    private String toConnectionString(MqttBrokerConnection connection) {
        String connectionInfoString = connection.getHost();
        return connectionInfoString;
    }

    @Override
    public void processTelemetryMessage(String name, String payload) {
        logger.debug("UNHANDLED processTelemetryMessage(name: {}, payload: {})", name, new String(payload));
        updateStatus(ThingStatus.ONLINE);
    }

    private void updateState(String channelID, Double value) {
        logger.debug("updateState( Channel: {}, Thing: {}, Value: {})", getThing().getUID(), channelID, value);
        updateState(channelID, DecimalType.valueOf("" + value));
    }

    public boolean updateTasmotaChannel(String topic, String key, Object value) {
        ThingUID thingUID = this.getThing().getUID();

        MessageConfigItem configItem = TasmotaMessageItemConfig.getConfigItem(topic, key, value);

        @Nullable
        String channelName = configItem.getChannelName();
        if (null != channelName) {
            String channelDescription = configItem.getChannelDescription();
            logger.debug("update Channel(Thing: {}, Channel: {}, Description: {}, Value({}): {})", thingUID,
                    channelName, channelDescription, value.getClass().getSimpleName(), String.valueOf(value));

            ChannelUID channelUID = new ChannelUID(thing.getUID(), channelName);
            addChannelIfMissing(thing, channelName, channelDescription, configItem);
            try {
                State newChannelState = toOpenhabStateValue(value);

                updateState(channelUID, newChannelState);
            } catch (Exception ex) {
                logger.error("Error updating Channel(Thing: {}, Channel: {}, Value: {}): ex.Message: {}", thingUID,
                        channelName, String.valueOf(value), ex.getMessage());
            }
        } else {
            return false;
        }
        return true;
    }

    public boolean updateTasmotaProperty(String topic, String key, Object value) {
        ThingUID thingUID = this.getThing().getUID();

        MessageConfigItem configItem = TasmotaMessageItemConfig.getConfigItem(topic, key, value);

        @Nullable
        String propertyName = configItem.getPropertyName();

        if (null != propertyName) {
            logger.trace("Update Property {} to {}", propertyName, value);
            thing.setProperty(propertyName, String.valueOf(value));
        } else {
            return false;
        }
        return true;
    }

    @NotNull
    private State toOpenhabStateValue(Object value) {
        State newChannelState = StringType.valueOf("" + value);
        if (value.getClass().isInstance(Date.class)) {
            newChannelState = DateTimeType.valueOf("" + value);
        } else if (value.getClass().isInstance(Double.class)) {
            newChannelState = DecimalType.valueOf("" + value);
        } else if (value.getClass().isInstance(Integer.class)) {
            newChannelState = DecimalType.valueOf("" + value);
        } else if (value.getClass().isInstance(Number.class)) {
            newChannelState = DecimalType.valueOf("" + value);
        } else if (value.getClass().isInstance(String.class) //
                && (((String) value).equals("ON") || ((String) value).equals("OFF"))//
        ) {
            newChannelState = value.equals("ON") ? OnOffType.ON : OnOffType.OFF;
        } else {
            newChannelState = StringType.valueOf("" + value);
        }
        return newChannelState;
    }

    private void addChannelIfMissing(Thing thing, String name, String description,
            MessageConfigItem messageConfigItem) {
        logger.trace("addChannelIfMissing(Thing {},name: {}, description: {}) ", thing.getUID(), name, description);

        ChannelTypeUID channelTypeUID = new ChannelTypeUID(BINDING_ID, name);
        ChannelUID channelUID = new ChannelUID(thing.getUID(), name);
        @Nullable
        Channel existingChannel = thing.getChannel(channelUID);

        if (null != messageConfigItem) {
            channelInternalConfig.put(channelUID, messageConfigItem);
        }

        if (null != existingChannel) {
            logger.trace("Channel {} already exists", channelTypeUID);
        } else {

            // debugShowChannels("this.getThing()", this.getThing());
            try {
                String itemType = "String";

                // Check if ChannelType is already defined. (If print template for user to add to thing-types.xml)
                if (null != channelTypeRegistry) {
                    @Nullable
                    ChannelType channelType = channelTypeRegistry.getChannelType(channelTypeUID);
                    if (null == channelType) {
                        logger.debug("Missing channel-type({}): Suggestion to add to thing-type.xml", channelTypeUID);
                        String suggestion = "" //
                                + " <channel-type id=\"" + name + "\">" //
                                + " \t<item-type>" + itemType + "</item-type>" //
                                + " \t<label>" + description + "</label>" //
                                + " </channel-type>\n" //
                                + "";
                        FileUtils.fileAppend("/tmp/openhab/thing-types.xml-suggestion", suggestion);
                    } else {
                        @Nullable
                        String channelItemType = channelType.getItemType();
                        if (null != channelItemType) {
                            itemType = channelItemType;
                        }
                        String channelTypeDescription = channelType.getDescription();
                        if (null != channelTypeDescription) {
                            description = channelTypeDescription;
                        }
                    }
                }

                logger.debug("Create new Channel(Thing: {}, ChannelUID: {}, itemType: {}, description: {}, name: {}",
                        thing.getUID(), itemType, description, name, channelTypeUID);

                ChannelBuilder channelBuilder = ChannelBuilder//
                        .create(channelUID) //
                        // .withDescription(description)//
                        .withLabel(description) //
                        .withAcceptedItemType(itemType) //
                        .withType(channelTypeUID);
                Channel newChannel = channelBuilder.build();

                ThingBuilder thingBuilder = editThing();
                thingBuilder.withChannel(newChannel);
                Thing newThing = thingBuilder.build();
                updateThing(newThing);
                // debugShowChannels("new Thing", newThing);

            } catch (Exception ex) {
                logger.error("Error adding channel:{}, Message: {}, Cause: {}", name, ex.getMessage(), ex.getCause());
            }
        }
    }

    private void debugShowChannels(String message, Thing thing) {
        logger.debug("Channel List({}, Thing  {}):", message, thing.getUID());

        thing.getChannels().stream().forEach(channel -> {
            logger.debug("            Channel: UID {}, ChanneltypeUID: {}", channel.getUID(),
                    channel.getChannelTypeUID());
        });
    }

    @Override
    public void processMessage(String topic, byte[] payload) {
        logger.debug("processMessage(topic: {}, payload: {}", topic, new String(payload));

        Map<String, Object> stateMap = MqttMessageTransformer.transformMessage(topic, payload);

        for (Map.Entry<String, Object> entry : stateMap.entrySet()) {
            boolean done = false;
            done |= updateTasmotaProperty(topic, entry.getKey(), entry.getValue());
            done |= updateTasmotaChannel(topic, entry.getKey(), entry.getValue());
            if (!done) {
                logger.warn("No action for Entry {},{}", entry.getKey(), entry.getValue());
            }
        }

        updateStatus(ThingStatus.ONLINE);
    }

    @Override
    protected void updateState(String channelID, State state) {
        if (isLinked(channelID)) {
            @Nullable
            ThingHandlerCallback callback = this.getCallback();
            if (callback == null) {
                logger.error("updateState({},{}): Missing Callback in Thing {} {}\n" + //
                        "Stacktrace: \n" + //
                        "{}", channelID, state, this.getThing(), this.getThing().getUID(),
                        ExceptionHelper.compactStackTrace());
            } else {
                // logger.debug("Seen Callback '{}' in Thing {} {}\n" + //
                // "Stacktrace: \n" + //
                // "{}", callback.getClass().getName(), this.getThing(), this.getThing().getUID(),
                // ExceptionHelper.compactStackTrace());

                super.updateState(channelID, state);
            }
        }
    }

    @Override
    protected void updateStatus(ThingStatus status) {
        if (this.getCallback() == null) {
            logger.error("updateStatus({}): Missing Callback in Thing {} {}\n" + //
                    "Stacktrace: \n" + //
                    "{}", status, this.getThing(), this.getThing().getUID(), ExceptionHelper.compactStackTrace());
        } else {
            super.updateStatus(status);
        }
    }

    @Override
    public void setCallback(@Nullable ThingHandlerCallback thingHandlerCallback) {
        if (null == thingHandlerCallback) {
            // logger.warn("Setting Callback to null in Thing {}\n" + "Stacktrace:\n" + "{}", thing.getUID(),
            // ExceptionHelper.compactStackTrace());
        } else {
            // logger.debug("Setting Callback to {} in Thing {}\n" + "Stacktrace:\n" + "{}",
            // thingHandlerCallback.getClass().getSimpleName(), thing.getUID(),
            // ExceptionHelper.compactStackTrace());
        }
        super.setCallback(thingHandlerCallback);
    }
}
