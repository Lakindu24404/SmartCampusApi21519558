// Author: 2151955/ 20241937 / Lakindu Jayathilaka

package com.westminster.smartcampus.exception;
public abstract class ApiException extends RuntimeException {
    private final String code;
    protected ApiException(String code, String message) {
        super(message);
        this.code = code;
    }
    // standard getter
    public String getCode() {
        return code;
    }
}
