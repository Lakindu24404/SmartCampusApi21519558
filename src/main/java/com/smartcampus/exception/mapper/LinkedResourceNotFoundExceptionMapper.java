package com.smartcampus.exception.mapper;

import com.smartcampus.exception.LinkedResourceNotFoundException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Maps LinkedResourceNotFoundException → HTTP 422 Unprocessable Entity.
 *
 * 422 is semantically more accurate than 404 here because:
 *   - The request URI is perfectly valid (POST /api/v1/sensors is a known route)
 *   - The problem is a broken foreign-key reference INSIDE the payload (roomId)
 *   - 404 would be misleading — it implies the endpoint itself was not found
 */
@Provider
public class LinkedResourceNotFoundExceptionMapper
        implements ExceptionMapper<LinkedResourceNotFoundException> {

    @Override
    public Response toResponse(LinkedResourceNotFoundException ex) {
        Map<String, String> error = new LinkedHashMap<>();
        error.put("error",   "LINKED_RESOURCE_NOT_FOUND");
        error.put("message", ex.getMessage());
        error.put("field",   ex.getField());
        error.put("value",   ex.getValue());

        return Response
                .status(422)  // 422 Unprocessable Entity
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
