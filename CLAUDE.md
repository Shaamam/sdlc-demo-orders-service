# orders-service

Spring Boot 4.1 / Java 21 REST API for a small shop: orders, products, customers, coupons.
In-memory H2 database, re-created and seeded from `src/main/resources/data.sql` on every start.

## Commands

| What | Command |
|---|---|
| Full local check (format + tests + architecture rule + coverage ≥ 80%) | `./mvnw -B verify` |
| Fix formatting (google-java-format via Spotless), run before committing | `./mvnw -B spotless:apply` |
| Tests only | `./mvnw -B test` |
| One test class | `./mvnw -B test -Dtest=OrderApiTest` |
| Run the app on http://localhost:8080 | `./mvnw spring-boot:run` |

In Windows PowerShell use `.\mvnw.cmd`; in Git Bash `./mvnw` works.

## Layout

Package-by-feature under `com.example.orders`: `order`, `product`, `customer`, `coupon`, plus `common`
(exceptions, error handler, clock). Each feature has an entity, a Spring Data repository, a service,
a controller and request/response records.

## Conventions

- Controllers call services; services call repositories. Controllers never touch repositories
  (`ArchitectureTest` enforces this).
- Errors: throw `NotFoundException` (→ 404) or `BusinessRuleException(code, message)` (→ 409).
  `GlobalExceptionHandler` turns them into RFC 9457 `ProblemDetail`s; bean-validation failures become
  400 with an `errors` map. Don't build error responses inside controllers.
- Request and response bodies are Java records validated with `jakarta.validation` annotations.
- Money is `BigDecimal`, scale 2, `RoundingMode.HALF_UP`.
- Time comes from the injected `java.time.Clock` (see `ClockConfig`). Never call `Instant.now()` or
  `LocalDate.now()` directly. Timestamps are UTC `Instant`s.
- Constructor injection only.
- Business-rule error codes are UPPER_SNAKE_CASE, prefixed with the feature (`ORDER_…`, `PRODUCT_…`).

## Tests

- API behaviour: annotate the class with `@ApiTest` (full context + MockMvc, each test rolled back).
  Example: `order/OrderApiTest`.
- Pure logic: plain JUnit 5 + AssertJ. Example: `order/OrderPricingTest`.
- Seed data is present in tests: products `BOOK-001`…`TOYS-007` (ids 1–30), customers 1–2
  (`asha@example.com`, `ben@example.com`), coupons `WELCOME10`, `SUMMER25` and the expired `SPRING15`.
- Time-dependent tests: replace the `Clock` bean with `Clock.fixed(...)` via `@TestConfiguration`.
