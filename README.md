# SmartCampus API (JAX-RS)

## 📌 Overview

SmartCampus API is a RESTful web service built using JAX-RS for managing university rooms, sensors, and sensor readings. It follows REST principles including resource-based design, proper HTTP methods, and structured JSON responses.

This implementation strictly follows coursework requirements:

- ✅ JAX-RS (Jersey)
- ✅ In-memory storage (HashMap / ArrayList)
- ❌ No database used

---

## ⚙️ Setup Instructions

### Prerequisites

- Java JDK 8+
- Maven

### Steps

```bash
# Clone repository
git clone <your-repo-link>

# Navigate
cd SmartCampusApi21519558

# Build project
mvn clean install

# Run server
mvn exec:java
```

Server runs at:

```
http://localhost:8080/api/v1
```

---

## 🔗 API Endpoints

### 🔹 Discovery Endpoint

```bash
curl -X GET http://localhost:8080/api/v1
```

---

### 🔹 Rooms

#### Get all rooms

```bash
curl -X GET http://localhost:8080/api/v1/rooms
```

#### Create room

```bash
curl -X POST http://localhost:8080/api/v1/rooms \
-H "Content-Type: application/json" \
-d '{"id":"LIB-301","name":"Library","capacity":50}'
```

#### Get room by ID

```bash
curl -X GET http://localhost:8080/api/v1/rooms/LIB-301
```

#### Delete room

```bash
curl -X DELETE http://localhost:8080/api/v1/rooms/LIB-301
```

---

### 🔹 Sensors

#### Create sensor

```bash
curl -X POST http://localhost:8080/api/v1/sensors \
-H "Content-Type: application/json" \
-d '{"id":"TEMP-001","type":"Temperature","status":"ACTIVE","roomId":"LIB-301"}'
```

#### Get sensors

```bash
curl -X GET http://localhost:8080/api/v1/sensors
```

#### Filter sensors

```bash
curl -X GET "http://localhost:8080/api/v1/sensors?type=Temperature"
```

---

### 🔹 Sensor Readings

#### Add reading

```bash
curl -X POST http://localhost:8080/api/v1/sensors/TEMP-001/readings \
-H "Content-Type: application/json" \
-d '{"value":25.5}'
```

#### Get readings

```bash
curl -X GET http://localhost:8080/api/v1/sensors/TEMP-001/readings
```

---

## 4. Report — Answers to Coursework Questions

4. Report — Answers to Coursework Questions
   Part 1: Foundation

Q1.1: Lifecycle of a JAX-RS Resource
Resource objects in JAX-RS have a default lifecycle scope which is request-based, which means that for every single request a new instance of the resource will be created. In regards to our current project, due to the use of in-memory collections like HashMap, there was a need for data persistence through different requests. Therefore, we used a Singleton pattern approach (DataStore static field) to keep data across requests to ensure data created in previous requests will be available in future requests.

Q1.2: Advantages of HATEOAS
HATEOAS adds flexibility and convenience for APIs through the introduction of navigational links which make less likely that URLs would be hardcoded into the client application, allowing clients to discover the next available actions by following the links, and thus making it possible for an API to change its URL structure easily.

Part 2: Room Management

Q2.1: Identifier vs Full Object
Response size will be small due to return of only identifier; therefore, this approach is better. On the other hand, the client will need to make additional requests to receive full information about the room, leading to an increase in the number of API requests (N+1 problem). Returning a full object is better because, despite a slight increase in the amount of data being sent back, it allows fewer requests to be made and increases the performance of the application.

Q2.2: Idempotence of DELETE
DELETE operations can be said to be idempotent with regard to their result because the deletion will take place anyway irrespective of whether the request has been made once or several times. On the other hand, the responses returned by making these requests differ; the first one will be 204, while the second will be 404 Not Found.

Section 3: Sensors & Filters

Q3.1: Non-JSON Payloads
Whenever a non-JSON payload is sent to an endpoint that only allows JSON payloads (@Consumes(MediaType.APPLICATION_JSON)), then Jersey returns an error message saying that the media type is unsupported, i.e., a 415 error. Hence, this problem is handled by the framework itself.

Q3.2: Query Parameters vs. Path Segments
Query parameters should be preferred over path segments for filtering purposes because they can be optional and flexible. For example, ?type=CO2 clearly states the filter condition. On the other hand, path segments like /sensors/CO2 are more suited to be used where the purpose of the request is to fetch something rather than apply filters. Using query parameters will help us in the future to add more filters (such as status or date).

Part 4: Sub-resources

Q4.1: Advantages of Sub-Resource Locators
Sub-resource locators assist in making the code better organized through segregation of code in smaller segments. For example, the code begins with the validation of whether there is any sensor available in the main resource, and then the job is passed on to a supporting class named SensorReadingResource.

Part 5: Filters and Security

Q5.2: JAX-RS Filters vs Logging in Methods
Filters offer a more appropriate way to handle things than introducing logging statements throughout various methods. This is because filters help deal with crosscutting concerns like logging in one place only, making sure that there will be no code duplications in other parts. Thus, using filters allows us to avoid code redundancy and follow the DRY pattern.

Q5.4: Potential Problems with Showing Stack Trace
It is highly dangerous to display any information related to a stack trace to your clients. It may provide them with all sorts of information about the internal structure, such as classes and libraries used, and file paths. As a result, it is much better to hide such details from the client and just return a generic error message.

## ⚠️ Error Handling

Example error response:

```json
{
  "error": "Room not empty",
  "status": 409
}
```

---

## 🏁 Conclusion

This API demonstrates:

- RESTful design principles
- Proper HTTP usage
- Clean architecture
- Robust error handling
- In-memory data management as required

---

## 🎥 Video Demonstration

(Include your video link here)
