# Copilot instructions for springbootapi

Purpose: give future Copilot sessions the concrete commands, high-level architecture, and repository-specific conventions so suggestions and automation are accurate.

---

## Build, test, and lint (concrete commands)

- Build (skip tests):
  - ./mvnw clean package -DskipTests
- Build & run locally (development):
  - ./mvnw spring-boot:run
- Run full test suite + JaCoCo coverage report:
  - ./mvnw clean test jacoco:report
  - Coverage report output: target/site/jacoco/index.html
- Run a single test class or a single test method (useful for quick feedback):
  - Single class: ./mvnw -Dtest=PessoaControllerTest test
  - Single method: ./mvnw -Dtest=PessoaControllerTest#shouldReturn200WhenExists test
- Docker / CI related commands:
  - Bring up app + DB + newman: docker-compose up --build
  - Test-stage Docker build (fails if tests fail): docker build --target test -t springbootapi-test .
  - Run Newman E2E collection in container: docker-compose run --rm newman
- SBOM generation (CycloneDX plugin): runs during the generate-resources phase (mvn generate-resources) and is included in normal package lifecycle. The plugin config is in pom.xml.
- Static analysis / linter:
  - Qodana configuration exists (qodana.yaml). Use JetBrains Qodana JVM Docker image per Qodana docs to run local analysis, or run in CI if configured.

---

## High-level architecture (big picture)

- Project type: Spring Boot 4 application written for Java 25. Clean-architecture-like layering:
  - Security layer: WebSecurityConfig (JWT/RSA) for API, AdminSecurityConfig (form/basic) for the Admin UI.
  - Controllers: AuthController (login/token), PessoaController (CRUD for Pessoa).
  - Service/repository: TokenAuthenticationService, Spring Data JPA repositories (PessoaRepository).
  - Monitoring: Spring Actuator + Spring Boot Admin Server + CycloneDX SBOM integration.
  - Persistence: MariaDB in Docker for runtime; H2 used in tests.
- Important runtime/ops notes:
  - RSA keys (private/public) are expected under src/main/resources/certs/ for JWT signing/verification.
  - Virtual Threads and modern Java 25 features are enabled—expect code that uses records, SequencedCollections, and other JDK 25 APIs.
  - Docker uses multi-stage builds: separate test stage and a small runtime image produced with jlink.

---

## Key conventions and repository-specific patterns

- Java version: explicitly set to 25 in pom.xml; compiler uses --enable-preview and <release>25.
- Tests run against H2 (in-memory); avoid relying on external DB for unit tests.
- Unit/integration split: the Dockerfile contains a test stage to run tests during image build; CI may rely on it.
- Single-test invocation: use Maven Surefire's -Dtest flag as shown above for focused runs.
- SBOM: the cyclonedx-maven-plugin produces application.cdx into the build output. Running package or generate-resources will produce it.
- Admin UI vs API security:
  - The repo configures separate SecurityFilterChains with ordering to distinguish API JWT flows from Admin UI form/basic login.
  - Admin UI default credentials (used in docs/examples): admin/admin (used only for local/dev convenience).
- Swagger: accessible at /swagger-ui/index.html when app runs locally on port 8080.
- Newman / Postman automation: tests are included under newman/ and invoked via docker-compose; expect artifacts in newman/tests/newman/ after runs.

---

## Files and places to check (for Copilot decisions)

- pom.xml — build lifecycle, jacoco and cyclonedx plugins, compiler settings
- README.md and TECHNICAL_GUIDE.md — operational commands and architecture rationale
- src/main/resources/certs/ — RSA keypair used by JWT
- docker-compose.yml and Dockerfile — multi-stage build, test stage, runtime image
- qodana.yaml — Qodana analysis configuration

---

Notes for Copilot sessions
- Prefer invoking Maven wrapper (./mvnw) so environment-agnostic commands are used.
- When proposing quick-fix or code-gen changes that touch security (JWT keys, filters), reference SecurityFilterChain and the separate AdminSecurityConfig to avoid breaking the Admin UI flow.
- For test suggestions, recommend running the focused single-test command before suggesting broad test-suite runs.
- When suggesting CI changes, look for qodana.yaml and CycloneDX config to integrate static analysis and SBOM generation.

---

(Integrated from README.md and TECHNICAL_GUIDE.md — keep those in sync if you update commands or architecture.)
