# Spring User Management API

[![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.0-brightgreen?logo=spring)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?logo=postgresql)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?logo=docker)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-TODO-lightgrey)]()

A secure, production-oriented REST API for user management built with **Java 21** and **Spring Boot 4.1.0**.

It provides JWT-based authentication (Access + Refresh tokens), role-based authorization, pagination, filtering, OpenAPI documentation, and a fully Dockerized setup with PostgreSQL.

Designed as a portfolio project that demonstrates clean architecture, security best practices, comprehensive testing, and CI/CD.

---

## Overview

This API enables users to register, authenticate, and manage accounts under strict access control.  
Administrators have elevated privileges for user listing, deletion, and dashboard access.

The application follows a clear layered architecture, uses Flyway for database migrations, and includes unit, security, and integration tests.

---

## Features

- User registration and login
- JWT authentication with Access Token and Refresh Token
- Role-based authorization (`USER` / `ADMIN`)
- Current authenticated user endpoint
- Paginated user listing with optional email filtering
- Admin-only endpoints (user listing, deletion, dashboard)
- Global exception handling and validation error responses
- OpenAPI / Swagger documentation
- Spring Boot Actuator health checks
- Dockerized application + PostgreSQL via Docker Compose
- Unit tests, controller security tests, and integration tests
- GitHub Actions CI + Qodana code quality analysis

---

## Tech Stack

| Technology              | Purpose                        |
|-------------------------|--------------------------------|
| Java 21                 | Language                       |
| Spring Boot 4.1.0       | Application framework          |
| Spring Security         | Authentication & authorization |
| Spring Data JPA         | Data access                    |
| PostgreSQL 16           | Relational database            |
| Flyway                  | Database migrations            |
| JWT (jjwt 0.12.6)       | Token-based authentication     |
| Jakarta Bean Validation | Request validation             |
| springdoc-openapi       | OpenAPI / Swagger documentation|
| Spring Boot Actuator    | Health & monitoring            |
| Lombok                  | Boilerplate reduction          |
| Maven                   | Build tool                     |
| JUnit 5 + Mockito       | Unit testing                   |
| MockMvc                 | Controller & security testing  |
| Docker + Docker Compose | Containerization               |
| GitHub Actions          | CI pipeline                    |
| Qodana                  | Static code analysis           |

---

## Architecture

The project follows a classic layered architecture:

| Layer               | Responsibility                                      |
|---------------------|-----------------------------------------------------|
| **Controller**      | HTTP request/response handling                      |
| **Service**         | Business logic (`AuthService`, `UserService`, ...)  |
| **Repository**      | Data access via Spring Data JPA                     |
| **Entity**          | JPA entities (`User`, `Role`, `RefreshToken`, ...)  |
| **DTO**             | Request/response objects                            |
| **Security**        | JWT filter, security config, user details           |
| **Exception**       | Centralized error handling                          |
| **Migration**       | Flyway scripts (`classpath:db/migration`)           |
| **Config**          | JPA and OpenAPI configuration                       |

**Package root:** `com.example.usermanagement`

---

## Authentication & Authorization

Authentication is based on JWT:

| Token Type      | Lifetime              | Storage          |
|-----------------|-----------------------|------------------|
| Access Token    | 15 minutes (900000 ms)| Stateless (JWT)  |
| Refresh Token   | 7 days (604800000 ms) | Database         |

Protected endpoints require the header:

```http
Authorization: Bearer <access_token>
```

**Roles:**

- `USER` — Standard authenticated access
- `ADMIN` — Elevated privileges (admin endpoints, user deletion, etc.)

Role-based authorization is enforced at the endpoint level using Spring Security.

---

## API Endpoints

| Method | Endpoint                 | Auth           | Description                     |
|--------|--------------------------|----------------|---------------------------------|
| `POST` | `/api/auth/register`     | Public         | Register a new user             |
| `POST` | `/api/auth/login`        | Public         | Authenticate and receive tokens |
| `POST` | `/api/auth/refresh`      | Public         | Obtain a new access token       |
| `GET`  | `/api/users/me`          | Authenticated  | Get current authenticated user  |
| `GET`  | `/api/users`             | Authenticated  | List users (paginated + filter) |
| `GET`  | `/api/users/admin`       | Admin          | Admin user listing              |
| `DELETE` | `/api/users/{id}`      | Admin          | Delete a user by ID             |
| `GET`  | `/api/admin/dashboard`   | Admin          | Admin dashboard                 |
| `GET`  | `/actuator/health`       | Public         | Application health check        |

---

## API Usage

Examples using [HTTPie](https://httpie.io/).

### Register

```bash
http POST :8080/api/auth/register \
  email=jolia@example.com \
  password=password123 \
  firstName=Jolia \
  lastName=Example
```

### Login

```bash
http POST :8080/api/auth/login \
  email=jolia@example.com \
  password=password123
```

### Refresh Token

```bash
http POST :8080/api/auth/refresh \
  refreshToken="YOUR_REFRESH_TOKEN"
```

### Get Current User

```bash
http GET :8080/api/users/me \
  "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

### Get Users

```bash
http GET :8080/api/users \
  "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

### Admin Dashboard

```bash
http GET :8080/api/admin/dashboard \
  "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

### Delete User (Admin)

```bash
http DELETE :8080/api/users/1 \
  "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

---

## Pagination & Filtering

User listing supports pagination and optional email filtering:

```bash
# Pagination
http GET ":8080/api/users?page=0&size=10" \
  "Authorization: Bearer YOUR_ACCESS_TOKEN"

# Email filtering
http GET ":8080/api/users?email=jolia@example.com" \
  "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

---

## Error Handling

- Global exception handling via `GlobalExceptionHandler`
- Custom exceptions: `UserAlreadyExistsException`, `UserNotFoundException`
- Bean Validation errors returned in a consistent structure
- Proper HTTP status codes for authentication, authorization, and validation failures

---

## Testing

The project includes:

- **Unit tests** — `AuthServiceTest`, `UserServiceTest`
- **Controller security tests** — `AuthControllerTest`, `UserControllerSecurityTest`, `UserAdminControllerSecurityTest`, `AdminControllerSecurityTest`
- **Integration tests** — `AuthUserIntegrationTest`
- **Application context test**

Run all tests:

```bash
./mvnw clean test
```

---

## Docker

The application and PostgreSQL run together with Docker Compose.

```bash
docker compose up -d
```

| Service    | URL / Port              |
|------------|-------------------------|
| API        | http://localhost:8080   |
| PostgreSQL | localhost:5432          |

Inside the Docker network the application connects to the database using the hostname `postgres`.

---

## API Documentation

| Resource     | URL                                              |
|--------------|--------------------------------------------------|
| Swagger UI   | http://localhost:8080/swagger-ui/index.html      |
| OpenAPI JSON | http://localhost:8080/v3/api-docs                |

---

## Health Check

```bash
curl http://localhost:8080/actuator/health
```

Example response:

```json
{
  "status": "UP"
}
```

---

## CI/CD

GitHub Actions workflows:

| Workflow                    | Purpose                              |
|-----------------------------|--------------------------------------|
| `ci.yml`                    | Runs tests on every push and PR      |
| `qodana_code_quality.yml`   | Static code analysis with Qodana     |

---

## Project Structure

```text
src/main/java/com/example/usermanagement
├── config/
│   ├── JpaConfig.java
│   └── OpenApiConfig.java
├── controller/
│   ├── admin/
│   ├── auth/
│   └── user/
├── dto/
│   ├── auth/
│   ├── error/
│   └── user/
├── entity/
│   ├── enums/
│   ├── AuditLog.java
│   ├── BaseEntity.java
│   ├── RefreshToken.java
│   ├── Role.java
│   └── User.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── UserAlreadyExistsException.java
│   └── UserNotFoundException.java
├── mapper/
├── repository/
│   ├── RefreshTokenRepository.java
│   ├── RoleRepository.java
│   └── UserRepository.java
├── security/
│   ├── config/
│   ├── jwt/
│   ├── service/
│   └── user/
├── service/
│   ├── AuthService.java
│   ├── RefreshTokenService.java
│   └── UserService.java
├── util/
├── validation/
└── SpringUserManagementApiApplication.java
```

---

## Getting Started

### Prerequisites

- Java 21
- Docker & Docker Compose
- Maven Wrapper (`./mvnw`) is included

### Quick Start (Docker)

```bash
git clone https://github.com/ooam-iroo/Spring-User-Management-API.git
cd Spring-User-Management-API
docker compose up -d
```

The API will be available at **http://localhost:8080**

### Local Development

1. Start PostgreSQL (or use the Docker Postgres service)
2. Build the project:

   ```bash
   ./mvnw clean package
   ```

3. Run the application:

   ```bash
   ./mvnw spring-boot:run
   ```

4. Open Swagger UI: http://localhost:8080/swagger-ui/index.html

---

## Configuration

Key settings from `application.yml`:

| Property                           | Value / Default                                      | Description                |
|------------------------------------|------------------------------------------------------|----------------------------|
| `spring.datasource.url`            | `jdbc:postgresql://localhost:5432/user_management`   | Database connection        |
| `spring.jpa.hibernate.ddl-auto`    | `validate`                                           | Schema validation only     |
| `spring.flyway.enabled`            | `true`                                               | Enable Flyway migrations   |
| `app.jwt.secret`                   | *(change in production)*                             | JWT signing key            |
| `app.jwt.access-token-expiration`  | `900000` (15 min)                                    | Access token lifetime      |
| `app.jwt.refresh-token-expiration` | `604800000` (7 days)                                 | Refresh token lifetime     |
| `server.port`                      | `8080`                                               | Application port           |

In Docker, the datasource is automatically overridden to use the service name `postgres`.

---

## Future Improvements

The following features are **not** implemented yet and are planned for future iterations:

- Redis for token blacklisting / caching
- Rate limiting
- Email verification
- Password reset flow
- Full audit logging (entity already prepared)
- Testcontainers for integration tests
- Kubernetes deployment manifests

---

## License

TODO – License not yet defined.

