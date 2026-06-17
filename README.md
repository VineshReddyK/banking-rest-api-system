# Banking REST API System

![CI](https://github.com/VineshReddyK/banking-rest-api-system/actions/workflows/ci.yml/badge.svg)
![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3-green)
![License](https://img.shields.io/badge/license-MIT-blue)

A production-ready Banking REST API built with Java, Spring Boot, Spring Security, JWT (JJWT), BCrypt, Hibernate, JPA, MySQL, Docker, and JUnit.

---

## Features

- User registration and login with **BCrypt password hashing**
- **JWT authentication** (JJWT 0.12.6) — signed tokens, expiry validation
- **Spring Security** filter chain with stateless session management
- Account creation with account type (SAVINGS / CHECKING)
- Deposit, withdrawal, and fund transfer with balance validation
- Transaction history per account
- **Input validation** on all request bodies (`@Valid`, `@NotBlank`, `@Email`, `@Positive`)
- **Structured JSON error responses** with timestamp, status, and message
- **Swagger / OpenAPI** documentation with JWT bearer auth support
- **Docker** multi-stage build (Maven build → lean JRE image)
- **GitHub Actions** CI pipeline with Maven dependency caching and test artifact upload
- Unit tests with **JUnit 5 + Mockito**

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 17 |
| Framework | Spring Boot 3.3 |
| Security | Spring Security + JJWT 0.12.6 |
| Password Hashing | BCrypt |
| ORM | Spring Data JPA + Hibernate |
| Database | MySQL 8 |
| API Docs | SpringDoc OpenAPI 2.5 / Swagger UI |
| Testing | JUnit 5 + Mockito |
| Build | Maven |
| Containerization | Docker (multi-stage) |
| CI/CD | GitHub Actions |

---

## Project Structure

```
src/main/java/com/vinesh/banking
├── BankingApplication.java
├── config/          SwaggerConfig.java
├── controller/      AuthController, AccountController, TransactionController
├── dto/             Request and Response DTOs with validation annotations
├── entity/          User, Account, Transaction
├── exception/       GlobalExceptionHandler, custom exceptions
├── repository/      Spring Data JPA repositories
├── security/        JwtUtil, JwtFilter, SecurityConfig
└── service/         UserService, AccountService, TransactionService
```

---

## API Endpoints

### Authentication

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/auth/register` | No | Register a new user |
| POST | `/api/auth/login` | No | Login and receive JWT token |

### Accounts

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/accounts/{userId}` | Yes | Create a new account |
| GET | `/api/accounts` | Yes | Get all accounts |

### Transactions

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/transactions/deposit` | Yes | Deposit funds |
| POST | `/api/transactions/withdraw` | Yes | Withdraw funds |
| POST | `/api/transactions/transfer` | Yes | Transfer between accounts |
| GET | `/api/transactions/{accountId}` | Yes | Get transaction history |

> Full request/response examples: [docs/api-endpoints.md](docs/api-endpoints.md)

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.9+
- MySQL 8+

### Local Setup

```bash
git clone https://github.com/VineshReddyK/banking-rest-api-system.git
cd banking-rest-api-system
```

Create the database:

```sql
CREATE DATABASE bankingdb;
```

Update `src/main/resources/application.properties` with your MySQL credentials, then run:

```bash
mvn spring-boot:run
```

| URL | Description |
|-----|-------------|
| `http://localhost:8080` | API base URL |
| `http://localhost:8080/swagger-ui/index.html` | Swagger UI |

### Run with Docker

```bash
docker build -t banking-api .
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/bankingdb \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=password \
  banking-api
```

### Run Tests

```bash
mvn clean test
```

---

## Security

- Passwords are hashed with **BCrypt** — never stored in plain text
- JWT tokens are signed with **HMAC-SHA** using a configurable secret key
- All endpoints except `/api/auth/**` require a valid `Authorization: Bearer <token>` header
- Tokens expire after 24 hours (configurable via `jwt.expiration`)

---

## Architecture

See [docs/architecture/system-architecture.md](docs/architecture/system-architecture.md) for the full architecture diagram and security flow.

---

## Future Enhancements

- Kafka event streaming for transaction notifications
- Redis caching for account balance reads
- Kubernetes deployment (AWS EKS)
- Monitoring with Prometheus and Grafana
- Email notifications on transactions
- Audit logging

---

## Author

**Vinesh Reddy Kankanalapally**

Software Engineer | Java | Spring Boot | AWS | Kubernetes | Terraform

- GitHub: [VineshReddyK](https://github.com/VineshReddyK)
- LinkedIn: [vinesh-reddy-kankanalapally](https://www.linkedin.com/in/vinesh-reddy-kankanalapally/)
