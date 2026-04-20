package com.smartcampus.resource;

import com.smartcampus.service.SensorReadingService;
import com.smartcampus.security.Secured;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("/readings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private final SensorReadingService readingService = new SensorReadingService();

    @GET
    @Path("/{sensorId}")
    public Response getReadingsBySensorId(@PathParam("sensorId") String sensorId) {
        return Response.ok(readingService.getReadingsBySensorId(sensorId)).build();
    }

    @POST
    @Path("/{sensorId}")
    @Secured({"ADMIN", "USER"}) // Allow users to record readings
    public Response addReading(@PathParam("sensorId") String sensorId, Map<String, Double> payload) {
        Double value = payload.get("value");
        if (value == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Missing 'value' in payload")
                    .build();
        }
        readingService.addReading(sensorId, value);
        return Response.status(Response.Status.CREATED).build();
    }
}
