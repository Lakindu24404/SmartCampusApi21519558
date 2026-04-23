// Author: 2151955/ 20241937 / Lakindu Jayathilaka

package com.westminster.smartcampus.filter;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Logger;

@Provider
public class RequestLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {
    private static final Logger LOGGER = Logger.getLogger(RequestLoggingFilter.class.getName());
    private static final String START_NANOS = "smartcampus.startNanos";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        requestContext.setProperty(START_NANOS, System.nanoTime());
        LOGGER.info("--> " + requestContext.getMethod() + " " + requestContext.getUriInfo().getRequestUri());
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        long start = 0L;
        Object prop = requestContext.getProperty(START_NANOS);
        if (prop instanceof Long) {
            start = (Long) prop;
        }
        long elapsedMs = start == 0L ? -1L : (System.nanoTime() - start) / 1_000_000L;
        LOGGER.info("<-- " + requestContext.getMethod() + " " +
                requestContext.getUriInfo().getRequestUri() + " " +
                responseContext.getStatus() + " (" + elapsedMs + "ms)");
    }
}
