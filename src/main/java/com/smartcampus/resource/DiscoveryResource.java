package com.smartcampus.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Discovery Resource — Part 1 Task 2
 *
 * GET /api/v1
 * Returns API metadata: version, contact, and links to primary resource collections.
 *
 * This is the HATEOAS "entry point" for the API. Clients can discover all
 * available resource paths without needing external documentation.
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class DiscoveryResource {

    @GET
    public Response discover() {
        // Build the navigational links map (HATEOAS)
        Map<String, String> resources = new LinkedHashMap<>();
        resources.put("rooms",   "/api/v1/rooms");
        resources.put("sensors", "/api/v1/sensors");

        // Build the full response body
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("name",        "Smart Campus Sensor & Room Management API");
        response.put("version",     "1.0");
        response.put("description", "RESTful API for managing campus rooms and IoT sensors");
        response.put("contact",     "admin@smartcampus.ac.uk");
        response.put("_links",      resources);

        return Response.ok(response).build();
    }
}
