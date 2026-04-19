package com.smartcampus.resource;

import com.smartcampus.data.DataStore;
import com.smartcampus.exception.LinkedResourceNotFoundException;
import com.smartcampus.model.Sensor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Sensor Resource — Part 3
 *
 * Manages the /api/v1/sensors collection and acts as a locator
 * for the nested /api/v1/sensors/{sensorId}/readings sub-resource.
 *
 * Endpoints:
 *   POST   /api/v1/sensors                        → register a new sensor
 *   GET    /api/v1/sensors                        → list all sensors (filterable by ?type=)
 *   GET    /api/v1/sensors/{sensorId}             → get a single sensor
 *   (Sub-resource locator)
 *   ANY    /api/v1/sensors/{sensorId}/readings    → delegates to SensorReadingResource
 */
@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    // ── POST /api/v1/sensors ──────────────────────────────────────────────────────
    /**
     * Registers a new sensor.
     *
     * Referential Integrity Check: the provided roomId MUST reference an
     * existing room in DataStore.rooms, otherwise a 422 is returned.
     *
     * @Consumes(APPLICATION_JSON): If a client sends text/plain or XML,
     * JAX-RS returns 415 Unsupported Media Type automatically.
     */
    @POST
    public Response createSensor(Sensor sensor) {
        if (sensor == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorBody("BAD_REQUEST", "Request body must be a valid Sensor JSON object."))
                    .build();
        }

        // Validate that the specified room exists
        if (sensor.getRoomId() == null || !DataStore.rooms.containsKey(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException("roomId", sensor.getRoomId());
        }

        // Auto-generate ID if not provided
        if (sensor.getId() == null || sensor.getId().isBlank()) {
            sensor.setId("SENSOR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }

        // Reject duplicate sensor IDs
        if (DataStore.sensors.containsKey(sensor.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(errorBody("DUPLICATE_ID", "A sensor with ID '" + sensor.getId() + "' already exists."))
                    .build();
        }

        // Default status to ACTIVE if not specified
        if (sensor.getStatus() == null || sensor.getStatus().isBlank()) {
            sensor.setStatus("ACTIVE");
        }

        // Store sensor and link to its room
        DataStore.sensors.put(sensor.getId(), sensor);
        DataStore.readings.put(sensor.getId(), new ArrayList<>());
        DataStore.rooms.get(sensor.getRoomId()).addSensorId(sensor.getId());

        URI location = URI.create("/api/v1/sensors/" + sensor.getId());
        return Response.created(location).entity(sensor).build();
    }

    // ── GET /api/v1/sensors ───────────────────────────────────────────────────────
    /**
     * Lists all sensors, with optional filtering by type.
     *
     * Using @QueryParam for filtering (?type=CO2) is preferred over path-based
     * filtering (/sensors/type/CO2) because:
     *   - Query params are inherently optional — no special handling needed when omitted
     *   - Path segments should identify resources, not filter them
     *   - Easier to combine multiple filters (?type=CO2&status=ACTIVE)
     *   - Standard REST convention for search/filter operations
     */
    @GET
    public Response getAllSensors(@QueryParam("type") String type) {
        List<Sensor> sensorList = new ArrayList<>(DataStore.sensors.values());

        if (type != null && !type.isBlank()) {
            sensorList = sensorList.stream()
                    .filter(s -> s.getType().equalsIgnoreCase(type))
                    .collect(Collectors.toList());
        }

        return Response.ok(sensorList).build();
    }

    // ── GET /api/v1/sensors/{sensorId} ────────────────────────────────────────────
    /**
     * Fetches a single sensor by ID.
     * Returns 404 if not found.
     */
    @GET
    @Path("/{sensorId}")
    public Response getSensorById(@PathParam("sensorId") String sensorId) {
        Sensor sensor = DataStore.sensors.get(sensorId);
        if (sensor == null) {
            throw new NotFoundException("Sensor with ID '" + sensorId + "' was not found.");
        }
        return Response.ok(sensor).build();
    }

    // ── Sub-Resource Locator — Part 4 ─────────────────────────────────────────────
    /**
     * Sub-resource locator for /api/v1/sensors/{sensorId}/readings.
     *
     * NOTE: This method has NO HTTP verb annotation (@GET, @POST, etc.).
     * That is what makes it a "locator" — JAX-RS hands off further routing
     * to the returned SensorReadingResource instance.
     *
     * Architectural benefit: SensorReadingResource handles all reading-related
     * logic in isolation, keeping this class clean and single-responsibility.
     */
    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingsSubResource(@PathParam("sensorId") String sensorId) {
        // Verify the parent sensor exists before delegating
        if (!DataStore.sensors.containsKey(sensorId)) {
            throw new NotFoundException("Sensor with ID '" + sensorId + "' was not found.");
        }
        return new SensorReadingResource(sensorId);
    }

    // ── Helper ────────────────────────────────────────────────────────────────────
    private Map<String, String> errorBody(String error, String message) {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("error", error);
        body.put("message", message);
        return body;
    }
}
