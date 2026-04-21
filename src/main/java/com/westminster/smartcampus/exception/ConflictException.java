// Author: 2151955/ 20241937 / Lakindu Jayathilaka

package com.westminster.smartcampus.exception;
public class ConflictException extends ApiException {
    public ConflictException(String message) {
        super("CONFLICT", message);
    }
}
