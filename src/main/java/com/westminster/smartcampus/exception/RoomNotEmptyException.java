// Author: 2151955/ 20241937 / Lakindu Jayathilaka

package com.westminster.smartcampus.exception;

public class RoomNotEmptyException extends ApiException {
    public RoomNotEmptyException(String message) {
        super("CONFLICT", message);
    }
}
