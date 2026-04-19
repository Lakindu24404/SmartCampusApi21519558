package com.smartcampus.exception.mapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Global "catch-all" Exception Mapper — Part 5 Task 4
 *
 * Catches any Throwable not handled by a more specific mapper.
 * Returns a generic HTTP 500 response — NEVER exposes stack traces to clients.
 *
 * Security rationale: stack traces reveal class names, method names, line numbers,
 * library versions, and internal architecture — all useful to an attacker for
 * crafting targeted exploits (path traversal, injection, etc.).
 */
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable ex) {
        // Log full details server-side so developers can debug
        LOGGER.log(Level.SEVERE, "Unhandled exception caught by GlobalExceptionMapper", ex);

        // Return only a safe, generic message to the client
        Map<String, String> error = new LinkedHashMap<>();
        error.put("error",   "INTERNAL_SERVER_ERROR");
        error.put("message", "An unexpected error occurred. Please contact support.");

        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)  // 500
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
