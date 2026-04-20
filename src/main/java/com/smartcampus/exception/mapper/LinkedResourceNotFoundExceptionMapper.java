// Author: W2151955/ 20241937 / Lakindu Jayathilaka
package com.smartcampus.exception.mapper;

import com.smartcampus.exception.LinkedResourceNotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class LinkedResourceNotFoundExceptionMapper implements ExceptionMapper<LinkedResourceNotFoundException> { // maps missing links
    @Override
    public Response toResponse(LinkedResourceNotFoundException exception) {
        return exception.getResponse();
    }
}
