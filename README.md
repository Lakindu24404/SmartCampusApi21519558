# Smart Campus Sensor & Room Management API

## 1. API Overview
This RESTful API is designed for managing campus rooms and IoT sensors as part of the student coursework for Module 5COSC022W. It provides full CRUD operations for rooms and sensors, including a hierarchical sub-resource for sensor reading history.

### Tech Stack
- **Core Framework**: JAX-RS (Jersey 3.x)
- **Servlet Container**: Apache Tomcat 10+
- **Dependency Management**: Maven
- **Serialization**: Jackson (JSON)
- **Logging**: Java Util Logging (JUL)

---

## 2. How to Build & Run
Ensure you have Maven and a Java 17+ JDK installed.

1. **Build the Project**:
   ```bash
   mvn clean package
   ```
2. **Run with Tomcat**:
   ```bash
   mvn tomcat7:run
   ```
   *The API will be available at `http://localhost:8080/api/v1`*

---

## 3. Sample curl Commands

### Discovery (Part 1)
```bash
curl -X GET http://localhost:8080/api/v1
```

### Create a Room (Part 2)
```bash
curl -X POST http://localhost:8080/api/v1/rooms \
     -H "Content-Type: application/json" \
     -d '{"name": "Lab 5.01", "capacity": 30}'
```

### List Sensors by Type (Part 3)
```bash
curl -X GET "http://localhost:8080/api/v1/sensors?type=CO2"
```

### Post a New Reading (Part 4)
```bash
curl -X POST http://localhost:8080/api/v1/sensors/{sensor_id}/readings \
     -H "Content-Type: application/json" \
     -d '{"value": 450.5}'
```

### Trigger 500 Error (Part 5 - Security Test)
```bash
# Attempting to access non-existent/invalid logic to verify GlobalExceptionMapper
curl -X GET http://localhost:8080/api/v1/debug/error
```

---

## 4. Report — Answers to Coursework Questions

### Part 1: Foundation
**Q1.1: Life-cycle of a JAX-RS Resource**  
By default, JAX-RS resources follow a **request-scoped** lifecycle, meaning a new instance is created for every HTTP request. Since this project uses in-memory data structures (`HashMap`), we must use a **Singleton** pattern (or static members in a `DataStore`) to ensure that data persisted in one request is available to subsequent requests.

**Q1.2: Benefits of HATEOAS**  
HATEOAS (Hypermedia as the Engine of Application State) provides **discoverability**. By including navigational links in responses (like the Discovery endpoint), clients can discover API capabilities dynamically. This reduces hard-coding of URLs on the client side and allows the server to change URI structures without breaking clients.

### Part 2: Room Management
**Q2.1: Room IDs vs Full Objects**  
Returning only **IDs** reduces bandwidth but requires the client to make "N+1" requests to get details for a list of items. Returning **Full Objects** increases the size of the initial payload but significantly improves performance by reducing network round-trips and latency.

**Q2.2: Idempotency of DELETE**  
In this implementation, `DELETE` is idempotent in its *effect* (the room is gone), but not in its *response*. The first call returns `204 No Content`, while subsequent calls return `404 Not Found`. While some argue 204 should be returned always, 404 is semantically useful to tell the client the resource no longer exists.

### Part 3: Sensors & Filtering
**Q3.1: Non-JSON Payloads**  
If a client sends a non-JSON payload (e.g., XML) to an endpoint annotated with `@Consumes(MediaType.APPLICATION_JSON)`, Jersey will automatically return an **HTTP 415 Unsupported Media Type** error.

**Q3.2: Query Params vs Path Segments**  
Query parameters (`?type=CO2`) are superior for **filtering** because they are optional by nature and denote a subset of a collection. Path segments (`/sensors/CO2`) are intended to identify a **specific resource**. Using query params keeps the URI structure clean as more filters (status, date, etc.) are added.

### Part 4: Sub-resources
**Q4.1: Benefits of Sub-Resource Locators**  
Locators allow for **hierarchical logic** and **modular code**. They enable us to verify that a parent resource (Sensor) exists before delegating reading operations to a specialized `SensorReadingResource`. This keeps the main `SensorResource` lightweight and enforces referential integrity at the routing level.

### Part 5: Filters & Security
**Q5.2: JAX-RS Filters vs Manual Logging**  
Filters centralize **cross-cutting concerns**. Using a filter ensures that every request/response is logged consistently without duplicating `Logger` calls in every resource method. This adheres to the **DRY (Don't Repeat Yourself)** principle and keeps business logic clean.

**Q5.4: Risks of Exposing Stack Traces**  
Exposing stack traces is a **security vulnerability**. It reveals internal implementation details, such as library versions, class names, and file paths. Attackers can use this information to identify specific vulnerabilities or gain a map of the internal system structure to craft more effective exploits.
