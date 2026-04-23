// Author: 2151955/ 20241937 / Lakindu Jayathilaka

package com.westminster.smartcampus.exception;

public class LinkedResourceNotFoundException extends ApiException {
    public LinkedResourceNotFoundException(String message) {
        super("UNPROCESSABLE_ENTITY", message);
    }
}
