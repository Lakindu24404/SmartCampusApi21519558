package com.smartcampus.resource;

import com.smartcampus.data.DataStore;
import com.smartcampus.exception.SensorUnavailableException;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.*;

/**
 * SensorReading Sub-Resource — Part 4
 *
 * Handles all operations on the reading history for a specific sensor.
 * This class is NOT registered as a top-level JAX-RS resource.
 * It is instantiated and returned by the sub-resource locator in SensorResource.
 *
 * Endpoints (relative to /api/v1/sensors/{sensorId}/readings):
 *   GET  /  → retrieve reading history for the sensor
 *   POST /  → append a new reading (and update sensor's currentValue)
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private final String sensorId;

    /**
     * Constructor called by the sub-resource locator in SensorResource.
     * The sensorId is injected so this class knows which sensor it's scoped to.
     */
    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    // ── GET /api/v1/sensors/{sensorId}/readings ───────────────────────────────────
    /**
     * Returns the full list of historical readings for this sensor.
     */
    @GET
    public Response getReadings() {
        List<SensorReading> history = DataStore.readings.getOrDefault(sensorId, Collections.emptyList());
        return Response.ok(history).build();
    }

    // ── POST /api/v1/sensors/{sensorId}/readings ──────────────────────────────────
    /**
     * Appends a new reading to the sensor's history.
     *
     * State Check: If the sensor is in "MAINTENANCE" status, the operation is
     * rejected with HTTP 403 (SensorUnavailableException).
     *
     * Side Effect: On success, the parent Sensor's currentValue is updated
     * to reflect the most recent reading — ensuring data consistency.
     */
    @POST
    public Response addReading(SensorReading reading) {
        Sensor sensor = DataStore.sensors.get(sensorId);

        // Enforce status constraint
        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())
                || "OFFLINE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException(sensorId, sensor.getStatus());
        }

        if (reading == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorBody("BAD_REQUEST", "Request body must be a valid SensorReading JSON."))
                    .build();
        }

        // Generate UUID and capture current timestamp
        reading.setId(UUID.randomUUID().toString());
        reading.setTimestamp(System.currentTimeMillis());

        // Append to history
        DataStore.readings.computeIfAbsent(sensorId, k -> new ArrayList<>()).add(reading);

        // Side Effect: Update parent sensor's currentValue for data consistency
        sensor.setCurrentValue(reading.getValue());

        URI location = URI.create("/api/v1/sensors/" + sensorId + "/readings/" + reading.getId());
        return Response.created(location).entity(reading).build();
    }

    // ── Helper ────────────────────────────────────────────────────────────────────
    private Map<String, String> errorBody(String error, String message) {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("error", error);
        body.put("message", message);
        return body;
    }
}
