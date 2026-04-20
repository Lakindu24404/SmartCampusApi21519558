# Smart Campus IoT API
<!-- Author: W2151955/ 20241937 / Lakindu Jayathilaka -->

## 1. Overview
This is a JAX-RS API for managing campus rooms and sensors. Built with Jersey and Hibernate.

### Tech Stack
- JAX-RS (Jersey)
- Hibernate / JPA
- MySQL
- JWT Security
- Bean Validation

---

## 2. Setup
### Prerequisites
- Java 11
- Maven
- MySQL

### Database
1. Create `smart_campus` db.
2. Update `hibernate.cfg.xml` with your mysql username/password.

### Run
1. `mvn clean package`
2. `mvn tomcat7:run`

### Testing
- **Swagger**: `http://localhost:8080/swagger-ui/`
- **Postman**: Import `SmartCampusAPI.postman_collection.json` from the `postman` folder.

---

## 3. Endpoints
- **Auth**: `POST /api/v1/auth/login` (admin/password123)
- **Rooms**: `GET /api/v1/rooms`
- **Sensors**: `GET /api/v1/sensors`
- **Readings**: `GET /api/v1/readings/{id}`
