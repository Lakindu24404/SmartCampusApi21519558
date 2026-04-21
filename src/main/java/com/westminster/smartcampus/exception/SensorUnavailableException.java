// Author: 2151955/ 20241937 / Lakindu Jayathilaka

package com.westminster.smartcampus.exception;
public class SensorUnavailableException extends ApiException {
    public SensorUnavailableException(String message) {
        super("FORBIDDEN", message);
    }
}
