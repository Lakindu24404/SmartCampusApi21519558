// Author: W2151955/ 20241937 / Lakindu Jayathilaka
package com.smartcampus.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class LinkedResourceNotFoundException extends WebApplicationException { // for 404s when linking stuff
    public LinkedResourceNotFoundException(String message) {
        super(Response.status(Response.Status.NOT_FOUND)
                .entity(message)
                .build());
    }
}
