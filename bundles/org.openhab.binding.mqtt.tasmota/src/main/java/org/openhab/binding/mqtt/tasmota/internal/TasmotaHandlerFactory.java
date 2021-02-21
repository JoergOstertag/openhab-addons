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

import static org.openhab.binding.mqtt.tasmota.internal.TasmotaBindingConstants.TASMOTA_MQTT_THING;

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
import org.openhab.core.thing.type.ChannelGroupTypeRegistry;
import org.openhab.core.thing.type.ChannelTypeRegistry;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
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

    private final Logger logger = LoggerFactory.getLogger(new Object() {
    }.getClass().getEnclosingClass());

    private static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Stream.of(TASMOTA_MQTT_THING)
            .collect(Collectors.toSet());

    private ChannelTypeRegistry channelTypeRegistry;
    private ChannelGroupTypeRegistry channelGroupTypeRegistry;

    @Activate
    public TasmotaHandlerFactory( //
            @Reference ChannelTypeRegistry channelTypeRegistry, //
            @Reference ChannelGroupTypeRegistry channelGroupTypeRegistry //
    ) {
        this.channelTypeRegistry = channelTypeRegistry;
        this.channelGroupTypeRegistry = channelGroupTypeRegistry;
    }

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        boolean contains = SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
        // logger.debug("supportsThingType(ThingTypeUID {}) ==> {}", thingTypeUID, contains);
        return contains;
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        logger.info("createHandler ThingTypeUID({}) Thing: {}", thingTypeUID, thing.getUID().toString());

        if (TASMOTA_MQTT_THING.equals(thingTypeUID)) {
            return new TasmotaHandlerImpl(thing, channelTypeRegistry);
        }

        logger.error("createHandler failed: ThingTypeUID({}) is not a known Type for thing: {}", thingTypeUID, thing);

        return null;
    }
}
