// Author: W2151955/ 20241937 / Lakindu Jayathilaka
package com.smartcampus.exception.mapper;

import com.smartcampus.exception.RoomNotEmptyException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> { // maps room not empty errors
    @Override
    public Response toResponse(RoomNotEmptyException exception) {
        return exception.getResponse();
    }
}
