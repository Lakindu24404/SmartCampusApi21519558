// Author: W2151955/ 20241937 / Lakindu Jayathilaka
package com.smartcampus.filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Logger;

@Provider
public class ApiLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger LOGGER = Logger.getLogger(ApiLoggingFilter.class.getName());

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String method = requestContext.getMethod();
        String uri = requestContext.getUriInfo().getRequestUri().toString();
        LOGGER.info("Incoming request: " + method + " " + uri);
    }

    @Override
    public void filter(ContainerRequestContext requestContext, 
                      ContainerResponseContext responseContext) throws IOException {
        int status = responseContext.getStatus();
        LOGGER.info("Outgoing response with status code: " + status);
    }
}
