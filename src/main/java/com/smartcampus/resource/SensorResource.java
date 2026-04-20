package com.smartcampus.resource;

import com.smartcampus.model.Sensor;
import com.smartcampus.service.SensorService;
import com.smartcampus.security.Secured;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
public class SensorResource {

    private final SensorService sensorService = new SensorService();

    @GET
    @Operation(summary = "List all sensors", description = "Returns a complete list of all sensors deployed across all rooms.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved sensor list")
    public Response getAllSensors() {
        return Response.ok(sensorService.getAllSensors()).build();
    }

    @POST
    @Secured({"ADMIN"})
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Register a new sensor", description = "Adds a new sensor to the system and links it to a room. Requires ADMIN role.")
    @ApiResponse(responseCode = "201", description = "Sensor successfully registered")
    @ApiResponse(responseCode = "400", description = "Invalid input or room not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
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
            throw new NotFoundException("Sensor with ID " + sensorId + " not found");
        }
        return Response.ok(sensor).build();
    }

    @DELETE
    @Path("/{sensorId}")
    @Secured({"ADMIN"})
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Decommission a sensor", description = "Removes a sensor from the system. Requires ADMIN role.")
    @ApiResponse(responseCode = "204", description = "Sensor successfully removed")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public Response deleteSensor(@PathParam("sensorId") String sensorId) {
        sensorService.deleteSensor(sensorId);
        return Response.noContent().build();
    }
}
