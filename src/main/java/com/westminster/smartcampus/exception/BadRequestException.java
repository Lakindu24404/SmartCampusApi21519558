// Author: 2151955/ 20241937 / Lakindu Jayathilaka

package com.westminster.smartcampus.exception;
public class BadRequestException extends ApiException {
    public BadRequestException(String message) {
        super("BAD_REQUEST", message);
    }
}
