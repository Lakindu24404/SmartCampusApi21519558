package com.smartcampus.exception.mapper;

import com.smartcampus.exception.RoomNotEmptyException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Maps RoomNotEmptyException → HTTP 409 Conflict.
 *
 * Triggered when a client attempts to DELETE a room that still has sensors
 * assigned to it. Returns a structured JSON error body.
 */
@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {

    @Override
    public Response toResponse(RoomNotEmptyException ex) {
        Map<String, String> error = new LinkedHashMap<>();
        error.put("error",   "ROOM_NOT_EMPTY");
        error.put("message", ex.getMessage());
        error.put("roomId",  ex.getRoomId());

        return Response
                .status(Response.Status.CONFLICT)  // 409
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
