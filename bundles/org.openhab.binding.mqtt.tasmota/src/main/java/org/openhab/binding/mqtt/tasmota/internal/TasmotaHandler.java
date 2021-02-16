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

import static org.openhab.binding.mqtt.tasmota.internal.TasmotaBindingConstants.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.jetbrains.annotations.NotNull;
import org.openhab.binding.mqtt.handler.BrokerHandler;
import org.openhab.binding.mqtt.tasmota.internal.deviceState.Dht11DTO;
import org.openhab.binding.mqtt.tasmota.internal.deviceState.Ds18B20DTO;
import org.openhab.binding.mqtt.tasmota.internal.deviceState.EnergyDTO;
import org.openhab.binding.mqtt.tasmota.internal.deviceState.TasmotaStateDTO;
import org.openhab.core.io.transport.mqtt.MqttBrokerConnection;
import org.openhab.core.library.types.*;
import org.openhab.core.thing.*;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.thing.binding.builder.ChannelBuilder;
import org.openhab.core.thing.binding.builder.ThingBuilder;
import org.openhab.core.thing.type.ChannelType;
import org.openhab.core.thing.type.ChannelTypeRegistry;
import org.openhab.core.thing.type.ChannelTypeUID;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link TasmotaHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Daan Meijer - Initial contribution
 * @author Jörg Ostertag - Adaptions to compile with openhab-3.1.0-SNAPSHOT
 */
@NonNullByDefault
public class TasmotaHandler extends BaseThingHandler implements TasmotaListener {

    private final Logger logger = LoggerFactory.getLogger(TasmotaHandler.class);

    private @Nullable TasmotaConfiguration config;

    protected @Nullable Device device;

    private final ChannelTypeRegistry channelTypeRegistry;

    public TasmotaHandler(Thing thing, ChannelTypeRegistry channelTypeRegistry) {
        super(thing);
        this.channelTypeRegistry = channelTypeRegistry;
        logger.debug("Init TasmotaHandler({})", thing);
    }

    @Override
    public void initialize() {
        logger.debug("Start initializing Tsmota Handler -- Bridge: {}, Thing: {}", getBridge(), getThing());
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

        logger.debug("Have broker connection: {}", toConnectionString(connection));

        device = new Device(connection, config.deviceid, this);

        updateStatus(ThingStatus.UNKNOWN);

        device.triggerUpdate();

        logger.debug("Finished initializing. Type: {}, UID: {}", thing.getThingTypeUID(), thing.getUID());
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.debug("handleCommand({}, {})", channelUID, command);

        if (null == device) {
            logger.warn("Handling command without being initialized");
            return;
        }

        if (CHANNEL_SWITCH.equals(channelUID.getId())) {
            if (command instanceof RefreshType) {
                // TODO: handle data refresh
            } else {
                OnOffType wantedValue = (OnOffType) command;
                device.publishCommand("POWER", wantedValue.equals(OnOffType.ON) ? "ON" : "OFF");
            }

        } else if (CHANNEL_DIMMER.equals(channelUID.getId())) {
            if (command instanceof RefreshType) {
                // TODO: handle data refresh
            } else {
                PercentType wantedValue = (PercentType) command;
                device.publishCommand("DIMMER", "" + wantedValue.intValue());
            }
        }
    }

    private String toConnectionString(MqttBrokerConnection connection) {
        String connectionInfoString = connection.getHost();
        return connectionInfoString;
    }

    @Override
    public void processVariableState(String name, String payload) {

        switch (name) {
            case "POWER":
                addChannelIfMissing(thing, "switch", "switch");
                updateState(CHANNEL_SWITCH, payload.equals("ON") ? OnOffType.ON : OnOffType.OFF);
                break;

            case "DIMMER":
                addChannelIfMissing(thing, "dimmer", "Dimmer");
                updateState(CHANNEL_DIMMER, PercentType.valueOf(payload));
                break;

            default: {
                logger.error("processVariableState(): Unknown topic name {}", name);
            }
                break;
        }

        updateStatus(ThingStatus.ONLINE);
    }

    @Override
    public void processTelemetryMessage(String name, String payload) {
        logger.debug("UNHANDLED processTelemetryMessage(name: {}, payload: {})", name, new String(payload));
        updateStatus(ThingStatus.ONLINE);
    }

    @Override
    public void processState(TasmotaStateDTO tasmotaState) {
        updateStatus(ThingStatus.ONLINE);
        updatePropertiesFromTasmotaState(tasmotaState);
        updateChannelsFromTasmotaState(tasmotaState);

        if (tasmotaState.Dimmer != null) {
            processVariableState("DIMMER", "" + tasmotaState.Dimmer);
        }

        // Power
        if (tasmotaState.POWER != null) {
            processVariableState("POWER", tasmotaState.POWER);
        }
        if (tasmotaState.POWER1 != null) {
            processVariableState("POWER1", tasmotaState.POWER1);
        }
        if (tasmotaState.POWER2 != null) {
            processVariableState("POWER2", tasmotaState.POWER2);
        }
        if (tasmotaState.POWER3 != null) {
            processVariableState("POWER3", tasmotaState.POWER3);
        }
        if (tasmotaState.POWER4 != null) {
            processVariableState("POWER4", tasmotaState.POWER4);
        }
        // DS18B20
        Ds18B20DTO ds18B20DTO = tasmotaState.DS18B20;
        if (null == ds18B20DTO && tasmotaState.StatusSNS != null) {
            ds18B20DTO = tasmotaState.StatusSNS.DS18B20;

        }
        if (null != ds18B20DTO) {
            if (ds18B20DTO.Temperature != null) {
                updateState(CHANNEL_TEMPERATURE, ds18B20DTO.Temperature);
            }
        }

        // DHT11
        Dht11DTO dht11DTO = tasmotaState.Dht11;
        if (null == dht11DTO && tasmotaState.StatusSNS != null) {
            dht11DTO = tasmotaState.StatusSNS.DHT11;
        }
        if (dht11DTO != null) {
            if (dht11DTO.Temperature != null) {
                updateState(CHANNEL_TEMPERATURE, dht11DTO.Temperature);
            }
            if (dht11DTO.Humidity != null) {
                updateState(CHANNEL_HUMIDITY, dht11DTO.Humidity);
            }
            if (dht11DTO.DewPoint != null) {
                updateState(CHANNEL_DEWPOINT, dht11DTO.DewPoint);
            }

        }

        // ENERGY
        EnergyDTO energyDTO = tasmotaState.ENERGY;
        if (energyDTO != null) {
            if (energyDTO.Voltage != null) {
                updateState(CHANNEL_VOLTAGE, energyDTO.Voltage);
            }
            if (energyDTO.Power != null) {
                updateState(CHANNEL_POWER_LOAD, energyDTO.Power);
            }

        }

        updateStatus(ThingStatus.ONLINE);
    }

    public void updateExistingThing(TasmotaStateDTO tasmotaStateDTO) {
        updatePropertiesFromTasmotaState(tasmotaStateDTO);
        updateChannelsFromTasmotaState(tasmotaStateDTO);
        updateStatus(ThingStatus.ONLINE);
    }

    private void updateState(String channelID, Double value) {
        logger.debug("updateState( Channel: {}, Thing: {}, Value: {})", getThing().getUID(), channelID, value);
        updateState(channelID, DecimalType.valueOf("" + value));
    }

    public void updatePropertiesFromTasmotaState(TasmotaStateDTO tasmotaState) {
        if (TasmotaBindingConstants.skipPropertyUpdateForDebugging) {
            logger.warn("skip PropertyUpdate For Easier Debugging");
        } else {
            Map<String, String> propertiesString = getPropertiesStringMap(tasmotaState);
            try {
                updateProperties(propertiesString);
            } catch (Exception ex) {
                logger.error("Error while updating Propperties: {}", ex.getMessage());
            }
        }
    }

    @NotNull
    private Map<String, String> getPropertiesStringMap(TasmotaStateDTO tasmotaState) {
        Map<String, Object> properties = DeviceStateParser.stateToHashMap(tasmotaState);
        Map<String, String> propertiesString = new HashMap<>();
        for (Entry<String, Object> property : properties.entrySet()) {
            String propertyName = property.getKey();
            Object propertyValue = property.getValue();
            if (null != propertyValue) {
                propertiesString.put(propertyName, String.valueOf(propertyValue));
                logger.trace("updateProperty({},{})", propertyName, String.valueOf(propertyValue));
            }
        }
        return propertiesString;
    }

    public void updateChannelsFromTasmotaState(TasmotaStateDTO tasmotaState) {
        logger.trace("updateChannelsFromTasmotaState ...");
        Map<String, Object> properties = DeviceStateParser.stateToHashMap(tasmotaState);
        for (Entry<String, Object> property : properties.entrySet()) {
            String key = property.getKey();
            Object value = property.getValue();
            ThingUID thingUID = this.getThing().getUID();
            if ((!TasmotaBindingConstants.addChannelsForConfigValues) && (key.startsWith("Config."))) {
                logger.trace("Thing: {}\tupdateChannel: Ignore Config Value( {}, {})", thingUID, key, value);
            } else {
                if (null != value) {
                    if (key.startsWith("deviceTypeKnown")) {
                        continue;
                    }
                    String name = key.replaceAll("\\.", "_");
                    String description = key.replaceAll("\\.", " ");
                    logger.debug("update Channel(Thing: {}, Channel: {}, Description: {}, Value({}): {})", thingUID,
                            name, description, value.getClass().getSimpleName(), String.valueOf(value));

                    ChannelUID channelUID = new ChannelUID(thing.getUID(), name);
                    addChannelIfMissing(thing, name, description);
                    try {
                        if (value.getClass().isInstance(Date.class)) {
                            updateState(channelUID, DateTimeType.valueOf(("" + value)));
                        } else if (value.getClass().isInstance(Double.class)) {
                            updateState(channelUID, DecimalType.valueOf(("" + value)));
                        } else if (value.getClass().isInstance(Number.class)) {
                            updateState(channelUID, DecimalType.valueOf(("" + value)));
                        } else {
                            updateState(channelUID, StringType.valueOf("" + value));
                        }
                    } catch (Exception ex) {
                        logger.error(
                                "Error updating Channel(Thing: {}, Channel: {}, Description: {}, Value: {}) ==> Message: {}",
                                thingUID, name, description, String.valueOf(value), ex.getMessage());
                    }

                }
            }
        }
    }

    private void addChannelIfMissing(Thing thing, String name, String description) {
        logger.trace("addChannelIfMissing(Thing {},name: {}, description: {}) ", thing.getUID(), name, description);

        ChannelTypeUID channelTypeUID = new ChannelTypeUID(BINDING_ID, name);
        ChannelUID channelUID = new ChannelUID(thing.getUID(), name);
        @Nullable
        Channel existingChannel = thing.getChannel(channelUID);

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
                        System.out.println("" //
                                + " <channel-type id=\"" + name + "\">\n" //
                                + " \t<item-type>" + itemType + "</item-type>\n" //
                                + " \t<label>" + description + "</label>\n" //
                                + " </channel-type>\n" //
                                + "");
                        System.out.println();
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
                logger.error("Error adding channel:{}, {}, {} {}", name, ex.getMessage(), ex.getCause(), "");
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

    /**
     * @param topic A topic like "tele/office/STATE"
     * @param payload
     * @return Returns the "office" part of the example
     */
    public static @Nullable String extractDeviceID(String topic, byte[] payload) {
        String[] strings = topic.split("/");
        if (strings.length > 2) {
            return strings[1];
        }
        return null;
    }
}
