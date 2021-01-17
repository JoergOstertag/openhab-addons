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

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.binding.BaseThingHandlerFactory;
import org.openhab.core.thing.binding.ThingHandler;
import org.openhab.core.thing.binding.ThingHandlerFactory;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link TasmotaHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Daan Meijer - Initial contribution
 */
@NonNullByDefault
@Component(configurationPid = "binding.tasmota", service = ThingHandlerFactory.class)
public class TasmotaHandlerFactory extends BaseThingHandlerFactory {

    private final Logger logger = LoggerFactory.getLogger(TasmotaHandlerFactory.class);

    private static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Stream
            .of(TASMOTA_MQTT_SWITCH, TASMOTA_MQTT_DIMMER).collect(Collectors.toSet());

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        boolean contains = SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
        return contains;
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (TASMOTA_MQTT_SWITCH.equals(thingTypeUID) //
                || TASMOTA_MQTT_DIMMER.equals(thingTypeUID)) {
            return new TasmotaHandler(thing);
        }

        logger.error("createHandler failed: ThingTypeUID({}) is not a known Type for thing: {}", thingTypeUID, thing);

        return null;
    }
}
