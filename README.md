# Store Management API

A REST API for managing a store's **products** and **sales**, built with Spring Boot, Spring Data JPA and MySQL.
Access is protected with **Spring Security**: staff log in with HTTP Basic authentication, and what they can do depends on their role (**Cashier** or **Manager**).

## Features

- Full CRUD for products and sales (`GET`, `POST`, `PUT`, `PATCH`, `DELETE`)
- Partial updates with `PATCH`, so you only send the fields you want to change
- Role-based access control: cashiers can view data and record sales; managers can do everything
- Staff accounts and roles are stored in MySQL and loaded with `JdbcUserDetailsManager`
- Passwords are stored as BCrypt hashes, never as plain text
- Sample data is loaded automatically on startup from `data.sql`

## Tech Stack

| Layer          | Technology                          |
|----------------|-------------------------------------|
| Language       | Java 26                             |
| Framework      | Spring Boot 4.1                     |
| Web            | Spring Web MVC (REST controllers)   |
| Persistence    | Spring Data JPA / Hibernate         |
| Database       | MySQL                               |
| Security       | Spring Security (HTTP Basic, BCrypt)|
| Build tool     | Maven (wrapper included)            |

## Project Structure

```
src/main/java/com/omarproject/storeapi
├── Entity/      # JPA entities: Product, Sales
├── DAO/         # Spring Data repositories
├── Service/     # Business logic (interfaces + implementations)
├── Rest/        # REST controllers: /api/product, /api/sale
└── Security/    # DemoSecurityConfig: users, roles, endpoint rules

src/main/resources
├── application.properties   # Database connection and app settings
└── data.sql                 # Creates tables and inserts sample data
```

## Getting Started

### Prerequisites

- Java 26 (JDK)
- MySQL 8+

### 1. Create the database

```sql
CREATE DATABASE store_directory;
```

### 2. Configure the connection

Edit `src/main/resources/application.properties` with your own MySQL credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/store_directory
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

### 3. Run the application

```bash
./mvnw spring-boot:run
```

On Windows use `mvnw.cmd spring-boot:run`. The API starts at `http://localhost:8080`.

On startup, `data.sql` creates the tables and inserts sample products, sales, roles and staff.

> **Note:** `data.sql` clears the `products` and `sales` tables every time the app starts, so changes you make to them are reset after a restart.

## Authentication & Roles

All endpoints require HTTP Basic authentication. The sample staff accounts are:

| Username        | Password   | Role    |
|-----------------|------------|---------|
| `John Smith`    | `store123` | Cashier |
| `Emily Johnson` | `store123` | Cashier |
| `Michael Brown` | `store123` | Cashier |
| `Sarah Davis`   | `store123` | Manager |
| `David Wilson`  | `store123` | Manager |

> These are demo accounts for local testing only. Change the passwords before using this anywhere real.

### Response codes

- `401 Unauthorized`: you didn't log in, or the username/password is wrong
- `403 Forbidden`: you're logged in, but your role isn't allowed to do this

## API Endpoints

### Products: `/api/product`

| Method   | Endpoint                  | Description                | Cashier | Manager |
|----------|---------------------------|----------------------------|:-------:|:-------:|
| `GET`    | `/products`               | List all products          | ✅ | ✅ |
| `GET`    | `/products/{productId}`   | Get one product            | ✅ | ✅ |
| `POST`   | `/products`               | Create a product           | ❌ | ✅ |
| `PUT`    | `/products`               | Update a product           | ❌ | ✅ |
| `PATCH`  | `/products/{productId}`   | Update some fields         | ❌ | ✅ |
| `DELETE` | `/products/{productId}`   | Delete a product           | ❌ | ✅ |

### Sales: `/api/sale`

| Method   | Endpoint             | Description             | Cashier | Manager |
|----------|----------------------|-------------------------|:-------:|:-------:|
| `GET`    | `/sales`             | List all sales          | ✅ | ✅ |
| `GET`    | `/sales/{saleId}`    | Get one sale            | ✅ | ✅ |
| `POST`   | `/sales`             | Record a sale           | ✅ | ✅ |
| `PUT`    | `/sales`             | Update a sale           | ❌ | ✅ |
| `PATCH`  | `/sales/{saleId}`    | Update some fields      | ❌ | ✅ |
| `DELETE` | `/sales/{saleId}`    | Delete a sale           | ❌ | ✅ |

## Example Requests

**List all products (as a cashier):**

```bash
curl -u "John Smith:store123" http://localhost:8080/api/product/products
```

```json
[
  { "id": 1, "name": "Laptop Lenovo IdeaPad", "category": "Electronics", "price": 750.00, "quantity": 15 },
  { "id": 2, "name": "Wireless Mouse", "category": "Electronics", "price": 25.50, "quantity": 50 }
]
```

**Create a product (as a manager):**

```bash
curl -u "Sarah Davis:store123" -X POST http://localhost:8080/api/product/products \
     -H "Content-Type: application/json" \
     -d '{"name": "Keyboard", "category": "Accessories", "price": 45.00, "quantity": 30}'
```

**Change only the quantity of a product:**

```bash
curl -u "Sarah Davis:store123" -X PATCH http://localhost:8080/api/product/products/1 \
     -H "Content-Type: application/json" \
     -d '{"quantity": 10}'
```

**Record a sale (as a cashier):**

```bash
curl -u "John Smith:store123" -X POST http://localhost:8080/api/sale/sales \
     -H "Content-Type: application/json" \
     -d '{"cashier": "John Smith", "date": "2026-09-13", "total": 99.99}'
```

## Database Schema

| Table      | Columns                                              |
|------------|------------------------------------------------------|
| `products` | `id`, `name`, `category`, `price`, `quantity`        |
| `sales`    | `id`, `cashier`, `date`, `total`                     |
| `Roles`    | `id`, `name` (`ROLE_CASHIER`, `ROLE_MANAGER`)        |
| `Staff`    | `id`, `name`, `password` (BCrypt), `role_id` → `Roles.id` |

## Future Improvements

- Return proper `404 Not Found` responses with a global exception handler
- Validate request bodies (e.g. price and quantity must be positive)
- Link sales to the products that were sold
- Add pagination and filtering (by category, by date range)
- Load the database password from an environment variable
- Add unit and integration tests
