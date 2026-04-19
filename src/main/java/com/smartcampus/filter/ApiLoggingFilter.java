package com.smartcampus.filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * API Logging Filter — Part 5 Task 5
 *
 * Implements both ContainerRequestFilter and ContainerResponseFilter to log
 * every incoming request and every outgoing response in one class.
 *
 * Using a filter for cross-cutting concerns like logging is far superior to
 * manually inserting Logger.info() into every resource method because:
 *   - It follows the DRY (Don't Repeat Yourself) principle
 *   - Logging logic is centralised and version-controlled in one place
 *   - Resource methods remain focused on business logic only
 *   - Filters can be toggled, extended, or replaced without touching resources
 *   - This is the standard AOP (Aspect-Oriented Programming) pattern
 */
@Provider
public class ApiLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger LOGGER = Logger.getLogger(ApiLoggingFilter.class.getName());

    /**
     * Logs every incoming HTTP request: method and URI.
     */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        LOGGER.info(String.format("[REQUEST]  %s %s",
                requestContext.getMethod(),
                requestContext.getUriInfo().getRequestUri()));
    }

    /**
     * Logs the HTTP status code of every outgoing response.
     */
    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) throws IOException {
        LOGGER.info(String.format("[RESPONSE] %d %s",
                responseContext.getStatus(),
                responseContext.getStatusInfo().getReasonPhrase()));
    }
}
