// Author: W2151955/ 20241937 / Lakindu Jayathilaka
package com.smartcampus.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class RoomNotEmptyException extends WebApplicationException { // for 409s when deleting rooms
    public RoomNotEmptyException(String roomId) {
        super(Response.status(Response.Status.CONFLICT)
                .entity("room " + roomId + " has sensors in it")
                .build());
    }
}
