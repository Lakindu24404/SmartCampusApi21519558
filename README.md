# Smart Campus Sensor & Room Management API

## 1. Project Overview
This project is a JAX-RS RESTful API developed for the University of Westminster module **5COSC022W (Client-Server Architectures)**. It implements a management system for a "Smart Campus," allowing administrators to manage physical rooms, IoT sensors, and environmental reading history without a persistent database.

### Technical Stack
- **API Standard**: JAX-RS 2.1 (javax namespace)
- **Implementation**: Eclipse Jersey 2.41
- **Servlet Container**: Apache Tomcat 9/10
- **Database**: MySQL 8.0+
- **Persistence**: JPA 2.2 / Hibernate 5.6
- **Security**: JWT (JSON Web Tokens) with JJWT
- **Validation**: Bean Validation (Hibernate Validator)
- **Architecture**: Resource -> Service -> Repository

---

## 2. Setup & Execution Instructions

### Prerequisites
- Java JDK 11 or higher
- Apache Maven 3.8+
- **MySQL Server** (Running on `localhost:3306`)

### Database Setup
1. Create a database named `smart_campus`.
2. Configure your credentials in `src/main/resources/hibernate.cfg.xml`. 
   > Default: `root` / `password`
3. The schema will be auto-generated on the first run (`hbm2ddl.auto=update`).

### Build and Run
1. **Build the application**:
   ```bash
   mvn clean package
   ```
2. **Start the server**:
   ```bash
   mvn tomcat7:run
   ```
3. **Seed Data**: On startup, the system automatically creates:
   - `admin` (password: `password123`) -> Role: ADMIN
   - `student` (password: `password123`) -> Role: USER
   - Sample rooms and sensors.

---

## 3. API Documentation & Sample Usage

### Part 0: Authentication
Exchange credentials for a Bearer token.
- **Endpoint**: `POST /api/v1/auth/login`
- **Payload**: `{"username": "admin", "password": "password123"}`
- **Note**: Use the returned `token` in the `Authorization: Bearer <token>` header for all protected endpoints.

### Part 1: Discovery (HATEOAS)
Returns the entry point and navigational links.
- **Endpoint**: `GET /api/v1/`

### Part 2: Room Management
- **GET /api/v1/rooms**: List all rooms.
- **POST /api/v1/rooms**: Create a room (Admin only).
  ```json
  {"id": "LIB-301", "name": "Quiet Study", "capacity": 50}
  ```

### Part 3: Sensor Operations
- **GET /api/v1/sensors**: List all sensors.
- **POST /api/v1/sensors**: Create a sensor (Admin only).
  ```json
  {"id": "TEMP-001", "type": "Temperature", "status": "ACTIVE", "roomId": "LIB-301"}
  ```

### Part 4: Reading History
- **GET /api/v1/readings/{sensorId}**: Get history.
- **POST /api/v1/readings/{sensorId}**: Add reading (Admin/User).
  ```json
  {"value": 22.5}
  ```

---

## 4. Technical Report — Coursework Question Answers

### Part 1: Architecture & Foundation
**Q1.1: What are the benefits of a Service-Repository architecture over direct Resource-DataStore access?**  
This architecture promotes **Separation of Concerns**. The Resource layer handles HTTP, the Service layer handles business logic (validation, role checks), and the Repository layer handles persistence. This makes the code more testable, maintainable, and allows for swapping the DB implementation without touching the API endpoints.

**Q1.2: Why is HATEOAS considered a benchmark of RESTful maturity?**  
HATEOAS enables **discoverability**. By providing links in the response, the API becomes self-descriptive, allowing clients to navigate state transitions dynamically without hard-coding URIs.

### Part 2: Persistence & Validation
**Q2.1: How does JPA/Hibernate simplify data management compared to manual SQL?**  
JPA provides **Object-Relational Mapping (ORM)**, allowing us to interact with Java objects instead of rows and columns. It handles relationship management, dirty checking, and type safety, significantly reducing boilerplate code and SQL injection risks.

**Q2.2: Why is Bean Validation (@NotNull, @Min) better than manual if-statements?**  
Bean Validation provides a **declarative** and standardized way to define constraints. It centralizes validation rules in the Model, ensures consistency across the app, and allows JAX-RS to automatically reject invalid payloads before they reach the service layer.

### Part 3: Security & Performance
**Q3.1: What are the advantages of JWT over Session-based authentication in a REST API?**  
JWT is **stateless**. The server doesn't need to store session data in memory, making the API horizontally scalable. It also works better for mobile and cross-domain requests as it doesn't rely on cookies.

**Q3.2: Why is role-based access control (RBAC) essential for a campus API?**  
RBAC ensures that only authorized users (e.g., ADMIN) can perform destructive or administrative actions (like deleting a room or creating a sensor), while allowing standard users (e.g., STUDENT) to view data or report readings.
