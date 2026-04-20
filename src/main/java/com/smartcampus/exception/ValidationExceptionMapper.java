// Author: W2151955/ 20241937 / Lakindu Jayathilaka
package com.smartcampus.exception;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> { // maps bean validation errors

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("error", "VALIDATION_ERROR");
        
        Map<String, String> details = new HashMap<>();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            String property = violation.getPropertyPath().toString();
            // get the last part of the path
            if (property.contains(".")) {
                property = property.substring(property.lastIndexOf('.') + 1);
            }
            details.put(property, violation.getMessage());
        }
        
        errorBody.put("details", details);
        return Response.status(Response.Status.BAD_REQUEST).entity(errorBody).build();
    }
}
