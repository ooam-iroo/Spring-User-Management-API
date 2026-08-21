<div align="center">

# 🔐 Spring User Management API

### Secure & Production-Oriented REST API for User Management

<p>
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21">
  <img src="https://img.shields.io/badge/Spring_Boot-4.1.0-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white" alt="Spring Security">
  <img src="https://img.shields.io/badge/PostgreSQL-16-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL">
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker">
</p>

<p>
  <img src="https://img.shields.io/badge/JWT-Authentication-000000?style=flat-square&logo=jsonwebtokens&logoColor=white" alt="JWT">
  <img src="https://img.shields.io/badge/Flyway-Migrations-CC0200?style=flat-square" alt="Flyway">
  <img src="https://img.shields.io/badge/OpenAPI-Swagger-85EA2D?style=flat-square&logo=swagger&logoColor=black" alt="OpenAPI">
  <img src="https://img.shields.io/badge/Maven-C71A36?style=flat-square&logo=apachemaven&logoColor=white" alt="Maven">
</p>

</div>

---

## 📌 Overview

**Spring User Management API** is a secure, production-oriented REST API for user management built with **Java 21** and **Spring Boot 4.1.0**.

The API provides:

* 🔐 JWT authentication with Access & Refresh Tokens
* 👥 Role-based authorization
* 📄 Pagination & filtering
* 📚 OpenAPI / Swagger documentation
* 🐘 PostgreSQL database
* 🗃️ Flyway database migrations
* 🐳 Docker & Docker Compose
* 🧪 Unit, security & integration testing
* ⚙️ GitHub Actions CI
* 🔍 Qodana code quality analysis

The project follows a layered architecture and focuses on security, maintainability, testing, and clean separation of responsibilities.

---

## ✨ Features

### 🔐 Authentication

* User registration
* User login
* JWT Access Token
* JWT Refresh Token
* Refresh token persistence
* Current authenticated user endpoint

### 👤 User Management

* Get current user
* List users
* Pagination
* Email filtering
* User deletion
* Admin-only user management

### 🛡️ Authorization

Two roles are available:

```text
USER
ADMIN
```

`USER` provides standard authenticated access, while `ADMIN` provides elevated access to administrative endpoints.

### 🧯 Error Handling

* Global exception handling
* Custom exceptions
* Validation error responses
* Appropriate HTTP status codes

### 📖 API Documentation

The project includes:

* OpenAPI
* Swagger UI
* OpenAPI JSON specification

### ❤️ Health & Monitoring

Spring Boot Actuator provides application health checks.

---

## 🧰 Tech Stack

| Technology                    | Purpose                        |
| ----------------------------- | ------------------------------ |
| ☕ **Java 21**                 | Programming language           |
| 🍃 **Spring Boot 4.1.0**      | Application framework          |
| 🔐 **Spring Security**        | Authentication & authorization |
| 🗄️ **Spring Data JPA**       | Data access                    |
| 🐘 **PostgreSQL 16**          | Relational database            |
| 🗃️ **Flyway**                | Database migrations            |
| 🎫 **JJWT 0.12.6**            | JWT authentication             |
| ✅ **Jakarta Bean Validation** | Request validation             |
| 📚 **springdoc-openapi**      | OpenAPI / Swagger              |
| ❤️ **Spring Boot Actuator**   | Health & monitoring            |
| 🧩 **Lombok**                 | Boilerplate reduction          |
| 📦 **Maven**                  | Build tool                     |
| 🧪 **JUnit 5**                | Unit testing                   |
| 🧪 **Mockito**                | Mock-based testing             |
| 🌐 **MockMvc**                | Controller & security testing  |
| 🐳 **Docker**                 | Containerization               |
| 🔄 **Docker Compose**         | Multi-container environment    |
| ⚙️ **GitHub Actions**         | CI                             |
| 🔍 **Qodana**                 | Static code analysis           |

---

## 🏗️ Architecture

The application follows a classic layered architecture:

```text
                         ┌─────────────────────┐
                         │       Client        │
                         └──────────┬──────────┘
                                    │
                                    ▼
                         ┌─────────────────────┐
                         │     Controller      │
                         │   REST Endpoints    │
                         └──────────┬──────────┘
                                    │
                                    ▼
                         ┌─────────────────────┐
                         │      Service        │
                         │   Business Logic    │
                         └──────────┬──────────┘
                                    │
                                    ▼
                         ┌─────────────────────┐
                         │     Repository      │
                         │    Spring Data JPA  │
                         └──────────┬──────────┘
                                    │
                                    ▼
                         ┌─────────────────────┐
                         │    PostgreSQL DB    │
                         └─────────────────────┘
```

### Layers

| Layer          | Responsibility                   |
| -------------- | -------------------------------- |
| **Controller** | HTTP request & response handling |
| **Service**    | Business logic                   |
| **Repository** | Database access                  |
| **Entity**     | JPA entities                     |
| **DTO**        | Request & response models        |
| **Security**   | JWT & authentication             |
| **Exception**  | Centralized error handling       |
| **Migration**  | Flyway database migrations       |
| **Config**     | Application configuration        |

Package root:

```text
com.example.usermanagement
```

---

# 🔐 Authentication & Authorization

Authentication is implemented using **JWT**.

## Token Lifecycle

| Token            |   Lifetime | Storage       |
| ---------------- | ---------: | ------------- |
| 🔑 Access Token  | 15 minutes | Stateless JWT |
| ♻️ Refresh Token |     7 days | PostgreSQL    |

Protected endpoints require:

```http
Authorization: Bearer <access_token>
```

### Access Flow

```text
┌──────────┐
│  Login   │
└────┬─────┘
     │
     ▼
┌──────────────────┐
│ Access + Refresh │
│     Tokens       │
└────┬─────────────┘
     │
     ▼
┌──────────────────┐
│ Protected API    │
│ Bearer Token     │
└────┬─────────────┘
     │
     ▼
┌──────────────────┐
│   JWT Filter     │
└────┬─────────────┘
     │
     ▼
┌──────────────────┐
│ Spring Security  │
└──────────────────┘
```

---

# 👥 Roles

### `USER`

Standard authenticated access.

### `ADMIN`

Elevated privileges including:

* Admin user listing
* User deletion
* Admin dashboard

Role-based access is enforced at the endpoint level through Spring Security.

---

# 📡 API Endpoints

| Method   | Endpoint               | Authentication | Description                     |
| -------- | ---------------------- | -------------- | ------------------------------- |
| `POST`   | `/api/auth/register`   | 🌐 Public      | Register a new user             |
| `POST`   | `/api/auth/login`      | 🌐 Public      | Authenticate and receive tokens |
| `POST`   | `/api/auth/refresh`    | 🌐 Public      | Get a new access token          |
| `GET`    | `/api/users/me`        | 🔐 User        | Get current user                |
| `GET`    | `/api/users`           | 🔐 User        | Paginated user listing          |
| `GET`    | `/api/users/admin`     | 👑 Admin       | Admin user listing              |
| `DELETE` | `/api/users/{id}`      | 👑 Admin       | Delete user                     |
| `GET`    | `/api/admin/dashboard` | 👑 Admin       | Admin dashboard                 |
| `GET`    | `/actuator/health`     | 🌐 Public      | Health check                    |

---

# 🚀 API Usage

The examples below use **HTTPie**.

## Register

```bash
http POST :8080/api/auth/register \
  email=jolia@example.com \
  password=password123 \
  firstName=Jolia \
  lastName=Example
```

## Login

```bash
http POST :8080/api/auth/login \
  email=jolia@example.com \
  password=password123
```

## Refresh Token

```bash
http POST :8080/api/auth/refresh \
  refreshToken="YOUR_REFRESH_TOKEN"
```

## Current User

```bash
http GET :8080/api/users/me \
  "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

## Get Users

```bash
http GET :8080/api/users \
  "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

## Admin Dashboard

```bash
http GET :8080/api/admin/dashboard \
  "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

## Delete User

```bash
http DELETE :8080/api/users/1 \
  "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

---

# 📄 Pagination & Filtering

The user listing supports pagination and optional email filtering.

### Pagination

```bash
http GET ":8080/api/users?page=0&size=10" \
  "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

### Email Filtering

```bash
http GET ":8080/api/users?email=jolia@example.com" \
  "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

---

# 🧯 Error Handling

The API provides centralized error handling through:

```text
GlobalExceptionHandler
```

Custom exceptions include:

```text
UserAlreadyExistsException
UserNotFoundException
```

Validation errors are returned in a consistent structure with appropriate HTTP status codes.

---

# 🧪 Testing

The project includes several levels of testing.

### Unit Tests

```text
AuthServiceTest
UserServiceTest
```

### Controller Security Tests

```text
AuthControllerTest
UserControllerSecurityTest
UserAdminControllerSecurityTest
AdminControllerSecurityTest
```

### Integration Tests

```text
AuthUserIntegrationTest
```

### Application Context

The project also includes an application context test.

### Run Tests

```bash
./mvnw clean test
```

---

# 🐳 Docker

The application and PostgreSQL database can be started together using Docker Compose.

```bash
docker compose up -d
```

### Services

| Service       | Address          |
| ------------- | ---------------- |
| 🚀 API        | `localhost:8080` |
| 🐘 PostgreSQL | `localhost:5432` |

Inside the Docker network, the application connects to PostgreSQL through:

```text
postgres
```

---

# 📚 API Documentation

Once the application is running:

### Swagger UI

```text
http://localhost:8080/swagger-ui/index.html
```

### OpenAPI JSON

```text
http://localhost:8080/v3/api-docs
```

---

# ❤️ Health Check

Check application health:

```bash
curl http://localhost:8080/actuator/health
```

Expected response:

```json
{
  "status": "UP"
}
```

---

# ⚙️ CI/CD

The repository includes GitHub Actions workflows.

| Workflow                  | Purpose                                  |
| ------------------------- | ---------------------------------------- |
| `ci.yml`                  | Run tests on every push and pull request |
| `qodana_code_quality.yml` | Static code analysis with Qodana         |

---

# 📁 Project Structure

```text
src/main/java/com/example/usermanagement
│
├── config/
│   ├── JpaConfig.java
│   └── OpenApiConfig.java
│
├── controller/
│   ├── admin/
│   ├── auth/
│   └── user/
│
├── dto/
│   ├── auth/
│   ├── error/
│   └── user/
│
├── entity/
│   ├── enums/
│   ├── AuditLog.java
│   ├── BaseEntity.java
│   ├── RefreshToken.java
│   ├── Role.java
│   └── User.java
│
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── UserAlreadyExistsException.java
│   └── UserNotFoundException.java
│
├── mapper/
│
├── repository/
│   ├── RefreshTokenRepository.java
│   ├── RoleRepository.java
│   └── UserRepository.java
│
├── security/
│   ├── config/
│   ├── jwt/
│   ├── service/
│   └── user/
│
├── service/
│   ├── AuthService.java
│   ├── RefreshTokenService.java
│   └── UserService.java
│
├── util/
├── validation/
│
└── SpringUserManagementApiApplication.java
```

---

# 🚀 Getting Started

## Requirements

Before running the project, make sure you have:

* ☕ Java 21
* 🐳 Docker & Docker Compose
* 📦 Maven Wrapper

The repository already includes the Maven Wrapper.

---

## ⚡ Quick Start with Docker

Clone the repository:

```bash
git clone https://github.com/ooam-iroo/Spring-User-Management-API.git
```

Enter the project:

```bash
cd Spring-User-Management-API
```

Start the application:

```bash
docker compose up -d
```

The API will be available at:

```text
http://localhost:8080
```

---

# 💻 Local Development

### 1. Start PostgreSQL

Start PostgreSQL locally or use the PostgreSQL service provided by Docker.

### 2. Build the project

```bash
./mvnw clean package
```

### 3. Run the application

```bash
./mvnw spring-boot:run
```

### 4. Open Swagger

```text
http://localhost:8080/swagger-ui/index.html
```

---

# ⚙️ Configuration

Important settings from `application.yml`:

| Property                           | Default                                            | Description            |
| ---------------------------------- | -------------------------------------------------- | ---------------------- |
| `spring.datasource.url`            | `jdbc:postgresql://localhost:5432/user_management` | Database connection    |
| `spring.jpa.hibernate.ddl-auto`    | `validate`                                         | Schema validation      |
| `spring.flyway.enabled`            | `true`                                             | Flyway migrations      |
| `app.jwt.secret`                   | Change in production                               | JWT signing key        |
| `app.jwt.access-token-expiration`  | `900000`                                           | Access token lifetime  |
| `app.jwt.refresh-token-expiration` | `604800000`                                        | Refresh token lifetime |
| `server.port`                      | `8080`                                             | Application port       |

> ⚠️ Never use a default or exposed JWT secret in a production environment.

When running with Docker Compose, the database connection is configured to use the PostgreSQL service name:

```text
postgres
```

---

# 🗺️ Future Improvements

The following features are planned but are **not implemented yet**:

* [ ] Redis for token blacklisting / caching
* [ ] Rate limiting
* [ ] Email verification
* [ ] Password reset flow
* [ ] Full audit logging
* [ ] Testcontainers for integration tests
* [ ] Kubernetes deployment manifests

---

# 📜 License

License is not yet defined for this project.

---

<div align="center">

### 🔐 Secure APIs. Clean Architecture. Reliable Backend.

**Built with Java & Spring Boot.**

<br>

⭐ If you find this project useful, consider giving it a star.

</div>
