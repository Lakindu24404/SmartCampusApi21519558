package com.smartcampus.exception;

/**
 * Thrown when a POST reading is attempted on a Sensor whose status is "MAINTENANCE".
 * A sensor in maintenance mode is physically disconnected and cannot accept new data.
 *
 * Mapped to HTTP 403 Forbidden by SensorUnavailableExceptionMapper.
 */
public class SensorUnavailableException extends RuntimeException {

    private final String sensorId;
    private final String status;

    public SensorUnavailableException(String sensorId, String status) {
        super("Sensor '" + sensorId + "' is currently in '" + status
                + "' state and cannot accept new readings.");
        this.sensorId = sensorId;
        this.status = status;
    }

    public String getSensorId() {
        return sensorId;
    }

    public String getStatus() {
        return status;
    }
}
