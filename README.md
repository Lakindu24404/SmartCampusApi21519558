# Smart Campus Sensor & Room Management API

## 1. Project Overview
This project is a JAX-RS RESTful API developed for the University of Westminster module **5COSC022W (Client-Server Architectures)**. It implements a management system for a "Smart Campus," allowing administrators to manage physical rooms, IoT sensors, and environmental reading history without a persistent database.

### Technical Stack
- **API Standard**: JAX-RS 2.1 (javax namespace)
- **Implementation**: Eclipse Jersey 2.41
- **Servlet Container**: Apache Tomcat 9/10 (via maven-tomcat-plugin)
- **Data Serialization**: JSON (Jackson)
- **Development Tooling**: Maven, Postman

---

## 2. Setup & Execution Instructions

### Prerequisites
- Java JDK 11 or higher
- Apache Maven 3.8+

### Build and Run
1. **Navigate to the project root** and build the application:
   ```bash
   mvn clean package
   ```
2. **Start the embedded Tomcat server**:
   ```bash
   mvn tomcat7:run
   ```
3. **Access the API**:
   The Discovery endpoint will be available at: `http://localhost:8080/api/v1`

---

## 3. API Documentation & Sample Usage

### Part 1: Discovery (HATEOAS)
Returns the entry point and navigational links for the API.
- **Endpoint**: `GET /api/v1/`
- **Sample Request**:
  ```bash
  curl -X GET http://localhost:8080/api/v1/
  ```

### Part 2: Room Management
CRUD operations for physical campus locations.
- **Endpoint**: `GET /api/v1/rooms` (List all rooms)
- **Sample Create**:
  ```bash
  curl -X POST http://localhost:8080/api/v1/rooms \
    -H "Content-Type: application/json" \
    -d '{"name": "Lecture Theatre 1", "capacity": 150}'
  ```

### Part 3: Sensor Operations
Manage IoT devices across the campus.
- **Endpoint**: `GET /api/v1/sensors?type=CO2` (Filtered search)
- **Sample Create**:
  ```bash
  curl -X POST http://localhost:8080/api/v1/sensors \
    -H "Content-Type: application/json" \
    -d '{"roomId": "ROOM_ID_HERE", "type": "CO2", "status": "ACTIVE"}'
  ```

### Part 4: Reading History (Sub-Resources)
Manage environmental readings for a specific sensor.
- **Endpoint**: `GET /api/v1/sensors/{sensorId}/readings`
- **Sample Append**:
  ```bash
  curl -X POST http://localhost:8080/api/v1/sensors/{sensor_id}/readings \
    -H "Content-Type: application/json" \
    -d '{"value": 415.2}'
  ```

---

## 4. Technical Report — Coursework Question Answers

### Part 1: Architecture & Foundation
**Q1.1: What is the default lifecycle of a JAX-RS Resource? How does this affect in-memory data?**  
The default lifecycle is **request-scoped**; a new instance is created for every request. Since this project uses in-memory data (Maps/Lists), data would be lost between requests if stored in instance variables. Therefore, we use a **Singleton Pattern** or **static data stores** to ensure persistence throughout the application's runtime.

**Q1.2: Why is HATEOAS considered a benchmark of RESTful maturity?**  
HATEOAS (Hypermedia as the Engine of Application State) enables **discoverability**. By providing links (`_links`) in the response, the API becomes self-descriptive. Clients can navigate the API dynamically without having to hard-code every possible URI, decoupling the client from the server’s URI structure.

### Part 2: Resource Granularity
**Q2.1: What are the trade-offs of returning nested objects vs flat IDs in a collection?**  
Returning **full objects** (Nested) reduces the number of requests (avoiding the N+1 problem) but increases bandwidth and latency for massive collections. Returning **IDs only** (Flat) is bandwidth-efficient but forces the client to make multiple round-trips to get data, increasing total network overhead.

**Q2.2: Is your DELETE implementation idempotent? Justify.**  
Yes. Idempotence means multiple identical requests have the same **effect** on the server. In our implementation, deleting a room removes it. Subsequent identical `DELETE` calls will find no room to delete; the server state remains "room is gone," satisfying the idempotency requirement of the HTTP specification.

### Part 3: Query & Exception Handling
**Q3.1: How does JAX-RS handle a request with an unsupported Content-Type?**  
JAX-RS automatically returns an **HTTP 415 Unsupported Media Type** status if a client sends a payload format (e.g., XML) that is not included in the resource method’s `@Consumes` annotation.

**Q3.2: Why are Query Parameters (`?type=...`) preferred over Path Segments (`/type/...`) for filtering?**  
Path segments are semantically used to identify a **specific resource** or sub-collection. Query parameters are used to **modify the presentation** of a resource (filter, sort, or paginate). Using query params is more flexible and allows for optional filtering without breaking the URI hierarchy.

### Part 4: Hierarchical Patterns
**Q4.1: What are the benefits of the Sub-Resource Locator pattern in a hierarchical API?**  
Sub-resource locators enable **logical separation**. Instead of one massive class, we delegate sub-path logic (like readings) to a dedicated `SensorReadingResource`. This improves maintainability and allows for context-specific validation (e.g., verifying a sensor exists before allowing reading access).

### Part 5: Filters & Security
**Q5.1: What are the security implications of exposing stack traces in an API?**  
Exposing stack traces reveals internal implementation details, such as file paths, class names, line numbers, and library versions. This information acts as a **vulnerability map** for attackers, making it easier for them to craft targeted exploits against the system.

**Q5.2: Why is HTTP 422 semantically more appropriate than 404 when a referenced resource ID in a POST body is missing?**  
HTTP **404 Not Found** implies the URI itself is wrong. HTTP **422 Unprocessable Entity** indicates that the request was well-formed (valid JSON) but contains semantic errors (a non-existent `roomId`), making it the more accurate status for referential integrity failures.

**Q5.3: What are the advantages of using JAX-RS Filters (AOP) for logging over manual logging?**  
Filters follow the **DRY (Don't Repeat Yourself)** principle. They centralize cross-cutting concerns (logging, auth, metrics) outside of the business logic. This ensures every request/response is logged consistently without polluting resource methods with repetitive code.
