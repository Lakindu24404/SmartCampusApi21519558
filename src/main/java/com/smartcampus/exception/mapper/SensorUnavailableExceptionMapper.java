package com.smartcampus.exception.mapper;

import com.smartcampus.exception.SensorUnavailableException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Maps SensorUnavailableException → HTTP 403 Forbidden.
 *
 * 403 is appropriate here: the client is authenticated and the endpoint is valid,
 * but the current state of the sensor FORBIDS the operation (maintenance lock).
 */
@Provider
public class SensorUnavailableExceptionMapper
        implements ExceptionMapper<SensorUnavailableException> {

    @Override
    public Response toResponse(SensorUnavailableException ex) {
        Map<String, String> error = new LinkedHashMap<>();
        error.put("error",    "SENSOR_UNAVAILABLE");
        error.put("message",  ex.getMessage());
        error.put("sensorId", ex.getSensorId());
        error.put("status",   ex.getStatus());

        return Response
                .status(Response.Status.FORBIDDEN)  // 403
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
