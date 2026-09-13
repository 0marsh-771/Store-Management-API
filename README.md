# Store Management API

A REST API for managing a store's **products** and **sales**, built with Spring Boot, Spring Data JPA and MySQL.
Access is protected with **Spring Security**: staff log in with HTTP Basic authentication, and what they can do depends on their role (**Cash
ier** or **Manager**).

## Features

- Full CRUD for products and sales (`GET`, `POST`, `PUT`, `PATCH`, `DELETE`)
- Partial updates with `PATCH`, so you only send the fields you want to change
- Role-based access control: cashiers can view data and record sales; managers can do everything
- Staff accounts and roles are stored in MySQL and loaded with `JdbcUserDetailsManager`
- Passwords are stored as BCrypt hashes, never as plain text
- Sample data is loaded automatically on startup from `data.sql`

## Tech Stack

- Language        java 26                             
- Framework       Spring Boot 4.1                     
- Web             Spring Web MVC (REST controllers)   
- Persistence     Spring Data JPA / Hibernate        
- Database        MySQL                               
- Security        Spring Security (HTTP Basic, BCrypt)
- Build tool      Maven (wrapper included)


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

> **Note:** `data.sql` clears the `products` and `sales` tables every time the app starts, so changes you make to them are reset after a rest
art.

 Username        | Password   | Role    |
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
