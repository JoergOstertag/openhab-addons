package org.openhab.binding.mqtt.tasmota.test;

public enum ResultFileType {

    SEEN_RESULTS("seenResults"), //
    EXPECTED_RESULTS("expectedResults"), //
    ERROR_RESULTS("errorResults");

    String value;

    ResultFileType(String value) {
        this.value = value;
    }

    public String asValue() {
        return value;
    }
}
