// Author: 2151955/ 20241937 / Lakindu Jayathilaka

package com.westminster.smartcampus.api;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
@JsonInclude(JsonInclude.Include.NON_NULL)
    // main class here
public class ApiError {
    private String code;
    private String message;
    private Integer status;
    private String path;
    private String timestamp;
    public ApiError() {
        this.timestamp = Instant.now().toString();
    }
    public ApiError(String code, String message, Integer status, String path) {
        this.code = code;
        this.message = message;
        this.status = status;
        this.path = path;
        this.timestamp = Instant.now().toString();
    }
    // standard getter
    public String getCode() {
        return code;
    }
    // setting data
    public void setCode(String code) {
        this.code = code;
    }
    // standard getter
    public String getMessage() {
        return message;
    }
    // setting data
    public void setMessage(String message) {
        this.message = message;
    }
    // standard getter
    public Integer getStatus() {
        return status;
    }
    // setting data
    public void setStatus(Integer status) {
        this.status = status;
    }
    // standard getter
    public String getPath() {
        return path;
    }
    // setting data
    public void setPath(String path) {
        this.path = path;
    }
    // standard getter
    public String getTimestamp() {
        return timestamp;
    }
    // setting data
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
