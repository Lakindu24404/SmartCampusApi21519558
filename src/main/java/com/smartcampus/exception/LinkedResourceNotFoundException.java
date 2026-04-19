package com.smartcampus.exception;

/**
 * Thrown when a client tries to create a Sensor with a roomId that
 * does not exist in the system.
 *
 * Mapped to HTTP 422 Unprocessable Entity by LinkedResourceNotFoundExceptionMapper.
 *
 * 422 is preferred over 404 here because the request URI is valid — the
 * problem is a broken reference INSIDE the valid JSON payload.
 */
public class LinkedResourceNotFoundException extends RuntimeException {

    private final String field;
    private final String value;

    public LinkedResourceNotFoundException(String field, String value) {
        super("The referenced resource for field '" + field + "' with value '" + value + "' does not exist.");
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }
}
