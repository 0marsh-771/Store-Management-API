# CLAUDE.md

Project memory for Claude Code. Read this first in every session.

## What this is

**Store Management API**: a Spring Boot REST API for a store's **products** and **sales**, secured with HTTP Basic auth and two roles (**CASHIER**, **MANAGER**). It's a learning/portfolio project by the owner (git user `0marsh-771`), so keep things simple and easy to follow.

## Tech stack (these are new versions, so don't assume older APIs)

- **Java 26**, **Spring Boot 4.1.1** (parent POM), Maven wrapper (`./mvnw`)
- Spring Web MVC (`spring-boot-starter-webmvc`), Spring Data JPA / Hibernate, Spring Security
- **Jackson 3**: imports are `tools.jackson.databind...`, not `com.fasterxml.jackson.databind...`
- MySQL (`mysql-connector-j`), database `store_directory`
- springdoc-openapi 3.1.0 (Swagger UI), devtools
- Test starters: `spring-boot-starter-data-jpa-test`, `spring-boot-starter-webmvc-test` (Boot 4 split test starters)

## Commands

```bash
./mvnw spring-boot:run      # run (needs local MySQL running; the store_directory DB is auto-created via createDatabaseIfNotExist=true)
./mvnw test                 # tests (the only test is contextLoads, and it needs MySQL running)
./mvnw clean package        # build the jar
```

## Structure

Base package: `com.omarproject.storeapi`. Classic layered architecture, one package per layer:

```
Entity/    JPA entities: Product (table products), Sales (table sales)
DAO/       Spring Data repositories: XxxRepository extends JpaRepository<Entity, Integer>
Service/   Interface + Impl pairs: ProductService/ProductServiceImpl, SalesService/SalesServiceImpl
Rest/      @RestController classes: ProductsRestController (/api/product), SaleRestController (/api/sale)
Security/  DemoSecurityConfig: password encoder, JDBC users, URL/role rules
Exception/ ResourceNotFoundException + GlobalExceptionHandler (@RestControllerAdvice → ProblemDetail)
resources/ application.properties, data.sql (schema + seed data, runs on every startup)
```

Request flow: **Controller → Service interface → ServiceImpl → Repository → MySQL**. Controllers only talk to services, never to repositories.

## Code conventions (follow these when adding code)

- **Package names are capitalized** (`Entity`, `DAO`, `Service`, `Rest`, `Security`). This isn't standard Java style, but it's the project's convention. Keep new classes in these packages and don't rename the packages unless asked.
- **Naming:**
  - Entity: `Product`, `Sales`
  - Repository: `<Entity>Repository` in `DAO`
  - Service: `<Entity>Service` interface plus `<Entity>ServiceImpl` annotated with `@Service`
  - Controller: `<Name>RestController` in `Rest`
- **Parameter naming:** constructor and method parameters use a `the` prefix (`theProductService`, `theId`, `theProduct`), and locals use `tempX` or `dbX` (`tempProduct`, `dbProduct`).
- **Dependency injection:** constructor injection only, with `@Autowired` on the constructor. Never use field injection.
- **Entities:**
  - `@Entity` + `@Table(name=...)`, and every field gets an explicit `@Column(name=...)`
  - `int id` with `@GeneratedValue(strategy = GenerationType.IDENTITY)`
  - A no-arg constructor plus a constructor with all fields except `id`
  - Getters and setters written out by hand (no Lombok). Getters come first, then setters, under `// getters` / `// setters` comments.
  - Money is `BigDecimal`, dates are `LocalDate`
- **Service layer:** the interface exposes `findAll`, `findById`, `save` and `deleteById`. `findById` and `deleteById` throw `ResourceNotFoundException("<Name>", id)` for a missing id and never return null. Controllers don't null-check service results.
- **Errors:**
  - Exceptions live in the `Exception` package.
  - `GlobalExceptionHandler` (a `@RestControllerAdvice`) maps them to Spring's built-in `ProblemDetail` responses (RFC 9457).
  - For a new error type, add an exception class plus one `@ExceptionHandler` method. Don't return error bodies by hand from controllers.
- **REST controller pattern** (base path `/api/<entity>`, collection path `/<entities>`):
  - `GET /xs`: list all
  - `GET /xs/{id}`: get one
  - `POST /xs`: calls `setId(0)` first so the request always creates a new row, then `save`
  - `PUT /xs`: full update; the id goes in the body
  - `PATCH /xs/{id}`: body is a `Map<String, Object>`. Reject the request if it contains an `id` key, then merge the map into the entity with `jsonMapper.updateValue(entity, payload)` and save.
  - `DELETE /xs/{id}`: returns a plain confirmation string
- **Formatting:** 4-space indentation, and the opening brace goes on the same line as the declaration. `StoreapiApplication` is the only file indented with tabs, because it comes from the Spring Initializr template.
- **Security rules:** every new endpoint needs an explicit `requestMatchers(HttpMethod.X, "/path").hasRole(...)` line in `DemoSecurityConfig.filterChain`. Roles are stored as `ROLE_CASHIER`/`ROLE_MANAGER` in the DB and written as `"CASHIER"`/`"MANAGER"` in `hasRole`.

## Access matrix (keep in sync with README)

| Action                  | Cashier | Manager |
|-------------------------|:-------:|:-------:|
| GET products/sales      | ✅ | ✅ |
| POST sale               | ✅ | ✅ |
| POST product            | ❌ | ✅ |
| PUT/PATCH/DELETE any    | ❌ | ✅ |

## Security & data details

- Users are loaded by `JdbcUserDetailsManager` with custom queries against the **`Staff`** and **`Roles`** tables. These are not Spring's default `users`/`authorities` tables. The username is `Staff.name`, which contains spaces (e.g. `"John Smith"`).
- Passwords use `PasswordEncoderFactories.createDelegatingPasswordEncoder()`, so stored hashes need the `{bcrypt}` prefix. The demo password for every account is `store123`.
- CSRF is disabled (stateless API with HTTP Basic). `logging.level.org.springframework.security=DEBUG` is turned on.
- `data.sql` runs on every startup (`spring.sql.init.mode=always`). It **TRUNCATEs `products` and `sales`** and upserts Roles/Staff. `ddl-auto=update` is on as well. Some comments in `data.sql` are in Russian.
- Commit `b88b07c` fixed the 401 errors in three places: valid BCrypt hashes, the role query join, and the product URL matcher. Keep in mind that a broken hash, a wrong authorities query or a mismatched matcher path all show up as the same 401/403.

## Known issues / tech debt (don't fix unless asked, but be aware)

- **The DB password is hardcoded and committed** in `application.properties`. It should move to an env var (`${DB_PASSWORD}`).
- The PATCH "body can't contain id" check still throws a raw `RuntimeException`, so clients get a 500. It should be a 400, handled with its own exception in `GlobalExceptionHandler`.
- `PUT` with a missing or `0` id **creates** a new row instead of rejecting the request.
- There's no request validation (`jakarta.validation`), and entities are exposed directly without DTOs.
- The services have no `@Transactional`.
- Naming is inconsistent:
  - Entity `Product` (singular) vs `Sales` (plural)
  - Controller `ProductsRestController` vs `SaleRestController`
  - Path variable `{salesId}` in `getSale` vs `{saleId}` everywhere else
- `DemoSecurityConfig` has no `anyRequest()` rule, and the Swagger UI paths (`/swagger-ui/**`, `/v3/api-docs/**`) aren't explicitly permitted.
- Sales seed data uses cashier names (`John Doe`, `Jane Smith`, …) that don't match the Staff names, and `Sales.cashier` is a plain string rather than a link to Staff.
- The only test is `contextLoads`, and it needs a live MySQL.
- `README.md` was untracked as of 2026-09-24. Its "Future Improvements" section lists the planned roadmap: 404 handler, validation, sale→product link, pagination/filtering, env-var password, tests.

## How to write code here

- **Keep it simple and readable.** Choose the plainest solution that works: no speculative abstractions, no extra layers or patterns "for the future" unless there's a concrete need.
- **SOLID, applied lightly:**
  - Single responsibility per class: controllers handle HTTP, services hold business logic, repositories handle data access.
  - Depend on interfaces (`XxxService`), not implementations.
  - Adding a feature should mean adding a new class, not changing unrelated ones.
- **Match the surrounding code:**
  - Same package layout, naming, `the`-prefix parameters, constructor injection and explicit getters/setters
  - Comment density stays low: a short comment only where the *why* isn't obvious
- **Business rules go in the service layer, not in controllers.** Controllers stay thin.
- **When adding a new resource,** create all of these:
  - An Entity
  - A Repository
  - A Service interface and its Impl
  - A RestController
  - Security matchers
  - `data.sql` table and seed data, if needed
  - An update to the README endpoint table
- **Prefer standard Spring idioms** over custom plumbing, for example `@RestControllerAdvice`, `ResponseEntity`, Bean Validation and Spring Data derived queries.
- **Keep the README in sync** when endpoints, roles or setup steps change.
- **Don't commit secrets.** Don't reformat or rename existing code as a side effect of an unrelated change.
