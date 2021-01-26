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

import static org.openhab.binding.mqtt.tasmota.internal.TasmotaBindingConstants.*;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.mqtt.handler.BrokerHandler;
import org.openhab.binding.mqtt.tasmota.internal.deviceState.TasmotaState;
import org.openhab.core.io.transport.mqtt.MqttBrokerConnection;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.binding.BaseThingHandler;
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

    public TasmotaHandler(Thing thing) {
        super(thing);
        logger.debug("Init TasmotaHandler({})", thing);
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

    @Override
    public void initialize() {
        logger.debug("Start initializing Tsmota Handler -- Bridge: {}, Thing: {}", this.getBridge(), this.getThing());
        config = getConfigAs(TasmotaConfiguration.class);

        Bridge bridge = getBridge();
        final BrokerHandler brokerHandler = (BrokerHandler) bridge.getHandler();
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

        logger.info("Have broker connection: {}", toConnectionString(connection));

        device = new Device(connection, config.deviceid, this);

        updateStatus(ThingStatus.UNKNOWN);

        device.update();

        logger.debug("Finished initializing. Type: {}, UID: {}", thing.getThingTypeUID(), thing.getUID());
    }

    private String toConnectionString(MqttBrokerConnection connection) {
        String connectionInfoString = connection.getHost();
        return connectionInfoString;
    }

    @Override
    public void processVariableState(String name, String payload) {
        switch (name) {

            case "POWER":
                updateState(CHANNEL_SWITCH, payload.equals("ON") ? OnOffType.ON : OnOffType.OFF);
                break;

            case "DIMMER":
                updateState(CHANNEL_DIMMER, PercentType.valueOf(payload));
                break;

            default:
                logger.error("Unknown name {}", name);
                break;
        }
        updateStatus(ThingStatus.ONLINE);
    }

    @Override
    public void processTelemetryMessage(String name, String payload) {

        updateStatus(ThingStatus.ONLINE);
    }

    @Override
    public void processState(TasmotaState tasmotaState) {
        if (tasmotaState.Dimmer != null) {
            processVariableState("DIMMER", "" + tasmotaState.Dimmer);
        }
        if (tasmotaState.POWER != null) {
            processVariableState("POWER", tasmotaState.POWER);
        }
        if (tasmotaState.Dht11 != null) {
            if (tasmotaState.Dht11.Temperature != null) {
                updateState(CHANNEL_TEMPERATURE, tasmotaState.Dht11.Temperature);
            }
            if (tasmotaState.Dht11.Humidity != null) {
                updateState(CHANNEL_HUMIDITY, tasmotaState.Dht11.Humidity);
            }
            if (tasmotaState.Dht11.DewPoint != null) {
                updateState(CHANNEL_DEWPOINT, tasmotaState.Dht11.DewPoint);
            }
        }

        if (tasmotaState.StatusSNS != null) {
            if (tasmotaState.StatusSNS.DHT11 != null) {
                if (tasmotaState.StatusSNS.DHT11.Temperature != null) {
                    updateState(CHANNEL_TEMPERATURE, tasmotaState.StatusSNS.DHT11.Temperature);
                }
                if (tasmotaState.StatusSNS.DHT11.Humidity != null) {
                    updateState(CHANNEL_HUMIDITY, tasmotaState.StatusSNS.DHT11.Humidity);
                }
            }

        }
        if (tasmotaState.ENERGY != null) {
            if (tasmotaState.ENERGY.Voltage != null) {
                updateState(CHANNEL_VOLTAGE, tasmotaState.ENERGY.Voltage);
            }
            if (tasmotaState.ENERGY.Power != null) {
                updateState(CHANNEL_POWER_LOAD, tasmotaState.ENERGY.Power);
            }
        }

        updateStatus(ThingStatus.ONLINE);
    }

    private void updateState(String channelID, Double value) {
        logger.debug("updateChannel({}, {}, {})", this.getThing().getUID(), channelID, value);
        updateState(channelID, DecimalType.valueOf("" + value));
    }
}
