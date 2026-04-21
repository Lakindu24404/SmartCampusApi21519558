// Author: W2151955/ 20241937 / Lakindu Jayathilaka
package com.smartcampus.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class SensorUnavailableException extends WebApplicationException { // for 403s on sensor stuff
    public SensorUnavailableException(String message) {
        super(Response.status(Response.Status.FORBIDDEN)
                .entity(message)
                .build());
    }
}
