// Author: 2151955/ 20241937 / Lakindu Jayathilaka

package com.westminster.smartcampus.mapper;
import com.westminster.smartcampus.api.ApiError;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
@Provider
    // main class here
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {
    @Context
    private UriInfo uriInfo;
    @Override
    // api response
    public Response toResponse(Throwable exception) {
        String path = uriInfo != null && uriInfo.getRequestUri() != null ? uriInfo.getRequestUri().getPath() : null;
        ApiError body = new ApiError("INTERNAL_SERVER_ERROR", "An unexpected error occurred", 500, path);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(body)
                .build();
    }
}
