// Author: W2151955/ 20241937 / Lakindu Jayathilaka
package com.smartcampus.resource;

import com.smartcampus.model.Sensor;
import com.smartcampus.service.SensorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Sensor Management", description = "CRUD operations for IoT campus sensors")
public class SensorResource { // api endpoints for sensors

    private final SensorService sensorService = new SensorService();

    @GET
    @Operation(summary = "List all sensors", description = "Returns a complete list of all sensors deployed across all rooms. Supports optional filtering by type.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved sensor list")
    public Response getAllSensors(@QueryParam("type") String type) {
        return Response.ok(sensorService.getAllSensors(type)).build();
    }

    @POST
    @Operation(summary = "Register a new sensor", description = "Adds a new sensor to the system and links it to a room.")
    @ApiResponse(responseCode = "201", description = "Sensor successfully registered")
    @ApiResponse(responseCode = "400", description = "Invalid input or room not found")
    public Response createSensor(@Valid Sensor sensor) {
        sensorService.createSensor(sensor);
        return Response.created(URI.create("/api/v1/sensors/" + sensor.getId()))
                .entity(sensor)
                .build();
    }

    @GET
    @Path("/{sensorId}")
    @Operation(summary = "Get sensor by ID", description = "Fetches metadata for a specific sensor.")
    @ApiResponse(responseCode = "200", description = "Sensor found")
    @ApiResponse(responseCode = "404", description = "Sensor not found")
    public Response getSensorById(@PathParam("sensorId") String sensorId) {
        Sensor sensor = sensorService.getSensorById(sensorId);
        if (sensor == null) {
            throw new NotFoundException("no sensor with id " + sensorId);
        }
        return Response.ok(sensor).build();
    }

    @DELETE
    @Path("/{sensorId}")
    @Operation(summary = "Decommission a sensor", description = "Removes a sensor from the system.")
    @ApiResponse(responseCode = "204", description = "Sensor successfully removed")
    public Response deleteSensor(@PathParam("sensorId") String sensorId) {
        sensorService.deleteSensor(sensorId);
        return Response.noContent().build();
    }

    @Path("/{sensorId}/readings")
    @Operation(summary = "Sensor Readings Sub-resource", description = "Locator for reading history operations.")
    public SensorReadingResource getReadingsResource(@PathParam("sensorId") String sensorId) {
        // According to JAX-RS Sub-resource locator pattern, return the instantiated class or the class type
        // In our case, returning the initialized instance works dynamically.
        return new SensorReadingResource();
    }
}
