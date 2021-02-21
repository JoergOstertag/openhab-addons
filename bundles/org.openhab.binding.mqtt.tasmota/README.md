# TasmotaMQTTBinding Binding

This binding is for discovering and handling devices flashed with [Tasmota Firmware](https://tasmota.github.io/docs/)

## Supported Things

So after discovery you can (most likely) see all your tasmota devices. But The only thing you would see is the device
itself and a lot of properties in this device. The only Devices which have working channels currently are Sonoff-Basic,
Sonoff-Pow, Wemos-DHT11

I opened up a completely separated testing environment here. And as already said I would currently really recommend to
do the same if someone wants to have a look at the current state.

My current setup for the start contains:

    Sonoff-Pow
    Sonoff-Basic
    Wemos-D1Minit with DHT11
    Wemos-D1Minit with DS1820

The Idea was to get those working as a start. And when those 4 are working I can add my other devices:

    Sonoff-4CH
    Sonoff S20
    Sonoff S55
    Sonoff Touch with 1 Switch
    Sonoff Touch with 2 Switches
    Sonoff Touch with 3 Switches
    Sonoff RF-Shield (with RF-switches, RF-Movement, RF-Door Sensor, RF-DoorBell)
    Wemos-D1-Mini with Button-Shield
    Wemos-D1-Mini with Buzzer-Shield
    Wemos-D1-Mini with IR-Shield
    Wemos-D1-Mini with LED-Matrix-Shield
    Wemos-D1-Mini with OLED Display-Shield
    Wemos-D1-Mini with Relay-Shield
    Wemos-D1-Mini with SHT-Shield
    Wemos-D1-Mini with RGB-Shield
    Wemos-D1-Mini with RFID-Reader
    RGB-Strips
    Water-Relay

Already well known open points with top Priority:

- Get the Code cleaned up so that someone else can do things in the code too -Find Co-coders :slight_smile:

- I think the binding triggers an additional Homematic subscribe. So I have to find where this is triggered and get rid
  of it.

- Find where I put a date value instead of a Double (Temperature/Humidity/DewPoint sometimes gets a date instead of a
  Double)

- I have a lot of ,messages with “although the handler was already disposed.” This probably leads to not updating the
  values when this message triggers.

- Add more config options to tell the binding how to handle what it discovered

- I have a lot of channels, but some channels are only for device management (RSSI,uptime, …) and others carry the real
  productive Values. So I want to introduce channel-groups to separate between config, sensors, actors.

- Find a good naming scheme for all the channels (about 70 Channels per Device)

- Adapt my widgets to the new channel names.

_Note that it is planned to generate some part of this based on the XML files
within ```src/main/resources/ESH-INF/thing``` of your binding._

## Discovery

Discovery is done by subscribing to the following topics:

At the current development state I am discovering tasmota devices by their mqtt messages:

    tele/+/STATE<x>
    tele/+/STATUS
    tasmota/discovery/#

This might change in a later release. I am currently thinking about only listening to the tasmota/discovery/# in the
initial Discovery and later dynamically adding channels to them by listening to the other topics.

## Binding Configuration

This is planed for later.

## Thing Configuration

Currently you can not configure a Tasmota Thing manually.

## Channels

_Here you should provide information about available channel types, what their meaning is and how they can be used._

_Note that it is planned to generate some part of this based on the XML files
within ```src/main/resources/ESH-INF/thing``` of your binding._

| channel  | type   | description                  |
|----------|--------|------------------------------|
| control  | Switch | This is the control channel  |

## Full Example

_Provide a full usage example based on textual configuration files (*.things, *.items, *.sitemap)._
