// Author: W2151955/ 20241937 / Lakindu Jayathilaka
package com.smartcampus.exception.mapper;

import com.smartcampus.exception.SensorUnavailableException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException> { // maps sensor unavailable errors
    @Override
    public Response toResponse(SensorUnavailableException exception) {
        return exception.getResponse();
    }
}
