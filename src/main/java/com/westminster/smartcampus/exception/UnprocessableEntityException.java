// Author: 2151955/ 20241937 / Lakindu Jayathilaka

package com.westminster.smartcampus.exception;
public class UnprocessableEntityException extends ApiException {
    public UnprocessableEntityException(String message) {
        super("UNPROCESSABLE_ENTITY", message);
    }
}
