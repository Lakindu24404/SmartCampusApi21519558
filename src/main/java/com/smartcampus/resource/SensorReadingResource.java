// Author: W2151955/ 20241937 / Lakindu Jayathilaka
package com.smartcampus.resource;

import com.smartcampus.service.SensorReadingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("/readings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Reading History", description = "Monitor and record historical sensor data")
public class SensorReadingResource { // endpoints for sensor readings

    private final SensorReadingService readingService = new SensorReadingService();

    @GET
    @Path("/{sensorId}")
    @Operation(summary = "Get reading history", description = "Returns a chronological log of all measurements recorded by a specific sensor.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved reading history")
    @ApiResponse(responseCode = "404", description = "Sensor not found")
    public Response getReadingsBySensorId(@PathParam("sensorId") String sensorId) {
        return Response.ok(readingService.getReadingsBySensorId(sensorId)).build();
    }

    @POST
    @Path("/{sensorId}")
    @Operation(summary = "Record a measurement", description = "Appends a new value to the reading history and updates the sensor's current state.")
    @ApiResponse(responseCode = "201", description = "Reading successfully recorded")
    @ApiResponse(responseCode = "400", description = "Missing value in payload")
    @ApiResponse(responseCode = "404", description = "Sensor not found")
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
