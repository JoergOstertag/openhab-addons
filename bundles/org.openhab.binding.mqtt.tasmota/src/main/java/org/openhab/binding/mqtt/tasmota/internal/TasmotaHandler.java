/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
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
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.PercentType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.eclipse.smarthome.io.transport.mqtt.MqttBrokerConnection;
import org.openhab.binding.mqtt.handler.SystemBrokerHandler;
import org.openhab.binding.mqtt.tasmota.internal.Device.TasmotaState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link TasmotaHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Daan Meijer - Initial contribution
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

        if (device == null) {
            logger.warn("Handling command without being initialized");
            return;
        }

        if (SWITCH.equals(channelUID.getId())) {
            if (command instanceof RefreshType) {
                // TODO: handle data refresh
            } else {
                OnOffType wantedValue = (OnOffType) command;
                device.command("POWER", wantedValue.equals(OnOffType.ON) ? "ON" : "OFF");
            }

        } else if (DIMMER.equals(channelUID.getId())) {
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
        // logger.debug("Start initializing!");
        config = getConfigAs(TasmotaConfiguration.class);

        final SystemBrokerHandler brokerHandler = (SystemBrokerHandler) getBridge().getHandler();
        MqttBrokerConnection connection = null;

        while (connection == null) {
            connection = brokerHandler.getConnection();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {

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
                updateState(SWITCH, payload.equals("ON") ? OnOffType.ON : OnOffType.OFF);
                break;
            case "DIMMER":
                updateState(DIMMER, PercentType.valueOf(payload));
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
