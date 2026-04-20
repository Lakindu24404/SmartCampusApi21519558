package com.smartcampus;

import com.smartcampus.filter.ApiLoggingFilter;
import com.smartcampus.security.JwtFilter;
import com.smartcampus.resource.*;
import com.smartcampus.exception.*;

import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * JAX-RS Application entry point.
 * Professional Smart Campus API with OpenAPI 3.0 documentation.
 */
@ApplicationPath("/api/v1")
@OpenAPIDefinition(
    info = @Info(
        title = "Smart Campus IoT API",
        version = "1.0.0",
        description = "Enterprise-grade RESTful API for managing rooms, sensors, and reading history across the Smart Campus site. Features JWT security, JPA persistence, and Bean Validation.",
        license = @License(name = "University of Westminster", url = "https://www.westminster.ac.uk/")
    ),
    servers = @Server(url = "/api/v1")
)
@io.swagger.v3.oas.annotations.security.SecurityScheme(
    name = "bearerAuth",
    type = io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class SmartCampusApplication extends Application {


    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();

        // ── Resource classes ──────────────────────────────────────
        classes.add(DiscoveryResource.class);
        classes.add(RoomResource.class);
        classes.add(SensorResource.class);
        classes.add(SensorReadingResource.class);
        classes.add(AuthResource.class);

        // ── Exception Mappers ─────────────────────────────────────
        classes.add(ValidationExceptionMapper.class);
        classes.add(RoomNotEmptyException.class); // JAX-RS will use its built-in or custom mapper

        // ── Filters ───────────────────────────────────────────────
        classes.add(ApiLoggingFilter.class);
        classes.add(JwtFilter.class);

        // ── Swagger Resources ─────────────────────────────────────
        classes.add(OpenApiResource.class);
        classes.add(AcceptHeaderOpenApiResource.class);

        return classes;
    }
}
