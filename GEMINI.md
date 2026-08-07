# Project Overview: Springboot API 🚀

A modern, high-performance REST API built with **Java 25 (LTS)** and **Spring Boot 4.1.0**. This project demonstrates best practices in back-end development, including JWT authentication, comprehensive testing, and containerized deployment.

## Key Technologies
- **Java 25:** Leverages modern features like **Virtual Threads** (Project Loom) for high throughput and **Sequenced Collections** for predictable iteration order.
- **Spring Boot 4.1.0:** Core framework for the API and monitoring.
- **Security:** JWT-based authentication using **RSA signatures** (Private/Public keys in `src/main/resources/certs/`).
- **Database:** **MariaDB** for production/development and **H2** for in-memory testing.
- **Observability:** Integrated **Spring Boot Admin** for monitoring and **CycloneDX** for SBOM (Software Bill of Materials) generation.
- **Testing:** **JaCoCo** for code coverage and **Newman (Postman)** for automated E2E tests.
- **Deployment:** **Docker** with multi-stage builds and a minimal JRE via `jlink`.

## Building and Running

### Prerequisites
- **Java 25** (recommended to manage via [SDKMAN](https://sdkman.io/))
- **Docker & Docker Compose**

### Commands
- **Build (Local):** `./mvnw clean package -DskipTests`
- **Run (Local):** `./mvnw spring-boot:run`
- **Run (Docker):** `docker-compose up --build`
- **Test & Coverage:** `./mvnw clean test jacoco:report` (Report at `target/site/jacoco/index.html`)
- **E2E Tests (Newman/Docker):** `docker-compose run --rm newman`
- **Load Tests (JMeter/Docker):** `docker-compose run --rm jmeter` (Generates interactive HTML report in `jmeter/reports/index.html`)

### Endpoints
- **API Base:** `http://localhost:8080`
- **Swagger UI:** `http://localhost:8080/swagger-ui/index.html`
- **Spring Boot Admin:** `http://localhost:8080/admin` (User/Pass: `admin`/`admin`)
- **Health Check:** `http://localhost:8080/actuator/health`

## Architecture
The project follows a simplified **Clean Architecture** with distinct layers:
- **Security Layer:** Handles JWT validation (API) and Form/Basic Login (Admin UI).
- **Controller Layer:** Exposes REST endpoints (`PessoaController`, `AuthController`).
- **Logic & Data Layer:** Contains Services (`TokenAuthenticationService`) and Repositories (`PessoaRepository`).
- **Persistence Layer:** Manages data storage in MariaDB/H2.
- **Monitoring Layer:** Spring Actuator and Spring Boot Admin Server.

## Development Conventions
- **Modern Java:** Use **Java Records** for DTOs and **Sequenced Collections** for API returns.
- **Virtual Threads:** Enabled by default (`spring.threads.virtual.enabled=true`).
- **Validation:** Use **Jakarta Bean Validation** (`@NotBlank`, `@Size`, etc.) in entities and `@Valid` in controllers.
- **Security:** JWT tokens are signed using RSA. Ensure keys are present in `src/main/resources/certs/`.
- **Documentation:** Use **Javadoc in Markdown** (`///`) for clear code documentation.
- **SBOM:** Automatically generated during build for security auditing.
- **Testing:** New features MUST include unit tests and, where applicable, be added to the Newman collection in `newman/tests/`. For performance-critical updates, verify compatibility with the JMeter load test suite in `jmeter/springbootapi.jmx` using `docker-compose run --rm jmeter`.
