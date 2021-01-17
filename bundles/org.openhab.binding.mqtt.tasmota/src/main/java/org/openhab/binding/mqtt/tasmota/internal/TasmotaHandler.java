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
import org.openhab.binding.mqtt.handler.SystemBrokerHandler;
import org.openhab.binding.mqtt.tasmota.internal.deviceState.TasmotaState;
import org.openhab.core.io.transport.mqtt.MqttBrokerConnection;
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
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {

        if (null == device) {
            logger.warn("Handling command without being initialized");
            return;
        }

        if (CHANNEL_SWITCH.equals(channelUID.getId())) {
            if (command instanceof RefreshType) {
                // TODO: handle data refresh
            } else {
                OnOffType wantedValue = (OnOffType) command;
                device.command("POWER", wantedValue.equals(OnOffType.ON) ? "ON" : "OFF");
            }

        } else if (CHANNEL_DIMMER.equals(channelUID.getId())) {
            if (command instanceof RefreshType) {
                // TODO: handle data refresh
            } else {
                PercentType wantedValue = (PercentType) command;
                device.command("DIMMER", "" + wantedValue.intValue());
            }
        }
    }

    @Override
    public void initialize() {
        logger.debug("Start initializing Bridge: {}, Thing: {}", this.getBridge(), this.getThing());
        config = getConfigAs(TasmotaConfiguration.class);

        Bridge bridge = getBridge();
        final SystemBrokerHandler brokerHandler = (SystemBrokerHandler) bridge.getHandler();
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

        logger.info("Have broker connection: {}", connection.toString());

        device = new Device(connection, config.deviceid, this);

        updateStatus(ThingStatus.UNKNOWN);

        device.update();

        logger.debug("Finished initializing!");
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
        }
        updateStatus(ThingStatus.ONLINE);
    }

    @Override
    public void processTelemetryMessage(String name, String payload) {

        updateStatus(ThingStatus.ONLINE);
    }

    @Override
    public void processState(TasmotaState state) {
        if (state.Dimmer != null) {
            processVariableState("DIMMER", "" + state.Dimmer);
        }
        if (state.POWER != null) {
            processVariableState("POWER", state.POWER);
        }
        updateStatus(ThingStatus.ONLINE);
    }
}
