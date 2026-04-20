package com.smartcampus.resource;

import com.smartcampus.service.AuthService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    private final AuthService authService = new AuthService();

    @POST
    @Path("/login")
    public Response login(Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        if (username == null || password == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorBody("Missing credentials"))
                    .build();
        }

        try {
            String token = authService.authenticate(username, password);
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return Response.ok(response).build();
        } catch (NotAuthorizedException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(errorBody(e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/register")
    public Response register(Map<String, String> details) {
        String username = details.get("username");
        String password = details.get("password");
        String role = details.getOrDefault("role", "USER");

        try {
            authService.register(username, password, role);
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorBody("Could not register user: " + e.getMessage()))
                    .build();
        }
    }

    private Map<String, String> errorBody(String message) {
        Map<String, String> body = new HashMap<>();
        body.put("error", message);
        return body;
    }
}
