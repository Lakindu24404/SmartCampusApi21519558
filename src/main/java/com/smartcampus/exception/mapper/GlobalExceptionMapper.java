// Author: W2151955/ 20241937 / Lakindu Jayathilaka
package com.smartcampus.exception.mapper;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> { // final catch for any errors

    @Override
    public Response toResponse(Throwable exception) {
        Map<String, String> errorBody = new HashMap<>();
        
        if (exception instanceof WebApplicationException) {
            return ((WebApplicationException) exception).getResponse();
        }

        errorBody.put("error", "INTERNAL_ERROR");
        errorBody.put("message", exception.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorBody).build();
    }
}
