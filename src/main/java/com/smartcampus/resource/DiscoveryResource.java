// Author: W2151955/ 20241937 / Lakindu Jayathilaka
package com.smartcampus.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.Map;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class DiscoveryResource { // api entry point with hateoas links

    @GET
    public Response getDiscoveryLinks(@Context UriInfo uriInfo) {
        String baseUri = uriInfo.getBaseUri().toString();
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Smart Campus API v1");
        response.put("version", "1.0.0");
        
        Map<String, String> admin = new HashMap<>();
        admin.put("name", "Lead Backend Architect");
        admin.put("email", "admin@smartcampus.westminster.ac.uk");
        response.put("admin_contact", admin);
        
        // build hateoas links
        Map<String, String> links = new HashMap<>();
        links.put("rooms", baseUri + "rooms");
        links.put("sensors", baseUri + "sensors");
        
        response.put("_links", links);
        
        return Response.ok(response).build();
    }
}
