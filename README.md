# Banking REST API System

![CI](https://github.com/VineshReddyK/banking-rest-api-system/actions/workflows/ci.yml/badge.svg)
![CodeQL](https://github.com/VineshReddyK/banking-rest-api-system/actions/workflows/codeql.yml/badge.svg)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-green)
[![codecov](https://codecov.io/gh/VineshReddyK/banking-rest-api-system/branch/main/graph/badge.svg)](https://codecov.io/gh/VineshReddyK/banking-rest-api-system)
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
- **Kubernetes** manifests for AWS EKS — Deployment, Service, ConfigMap, Secret, HPA (auto-scales 2→10 pods)
- **Spring Actuator** health endpoint for K8s readiness and liveness probes
- **Redis caching** — account reads cached with 10-minute TTL, auto-evicted on any write
- **Kafka event streaming** — transaction events published to `transaction-events` topic on every deposit, withdrawal, and transfer
- **Audit logging** — every register, login, deposit, withdrawal, and transfer written to `audit_logs` table
- **Email notifications** — welcome email on register; deposit, withdrawal, and transfer alerts via Gmail SMTP
- **Prometheus + Grafana** — metrics exposed at `/actuator/prometheus`, Grafana auto-provisioned at port 3000
- **Docker Compose** — full local stack: MySQL + Redis + Kafka + Prometheus + Grafana + API in one command
- **GitHub Actions** CI pipeline with Maven dependency caching and test artifact upload
- Unit tests with **JUnit 5 + Mockito**

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 21 |
| Framework | Spring Boot 3.5 |
| Security | Spring Security + JJWT 0.12.6 |
| Password Hashing | BCrypt |
| ORM | Spring Data JPA + Hibernate |
| Database | MySQL 8 |
| API Docs | SpringDoc OpenAPI 2.5 / Swagger UI |
| Testing | JUnit 5 + Mockito |
| Build | Maven |
| Containerization | Docker (multi-stage) |
| Orchestration | Kubernetes (AWS EKS) |
| Health Monitoring | Spring Actuator |
| Caching | Redis 7 |
| Event Streaming | Apache Kafka |
| Email | Spring Mail + Gmail SMTP |
| Monitoring | Prometheus + Grafana |
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
| POST | `/api/v1/auth/register` | No | Register a new user |
| POST | `/api/v1/auth/login` | No | Login and receive JWT token |

### Accounts

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/v1/accounts/{userId}` | Yes | Create a new account |
| GET | `/api/v1/accounts` | Yes | Get all accounts |

### Transactions

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/v1/transactions/deposit` | Yes | Deposit funds |
| POST | `/api/v1/transactions/withdraw` | Yes | Withdraw funds |
| POST | `/api/v1/transactions/transfer` | Yes | Transfer between accounts |
| GET | `/api/v1/transactions/{accountId}` | Yes | Get transaction history |

> Full request/response examples: [docs/api-endpoints.md](docs/api-endpoints.md)

---

## Getting Started

### Prerequisites

- Java 21+
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

### Run with Docker Compose (full stack)

Starts MySQL + Redis + Kafka + Prometheus + Grafana + the API together:

```bash
docker-compose up --build
```

| URL | Description |
|-----|-------------|
| `http://localhost:8080/swagger-ui/index.html` | Swagger UI |
| `http://localhost:8080/actuator/health` | Health check |
| `http://localhost:8080/actuator/prometheus` | Prometheus metrics |
| `http://localhost:9090` | Prometheus UI |
| `http://localhost:3000` | Grafana (admin / admin) |

### Run with Docker (API only)

```bash
docker build -t banking-api .
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/bankingdb \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=password \
  banking-api
```

### Deploy to Kubernetes (AWS EKS)

#### Prerequisites
- `kubectl` configured against your EKS cluster
- Docker image pushed to ECR or Docker Hub

#### Build and push the image

```bash
docker build -t vineshreddy/banking-rest-api-system:latest .
docker push vineshreddy/banking-rest-api-system:latest
```

#### Apply manifests

```bash
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/secret.yaml
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/mysql-deployment.yaml
kubectl apply -f k8s/banking-deployment.yaml
kubectl apply -f k8s/banking-service.yaml
kubectl apply -f k8s/hpa.yaml
```

#### Verify

```bash
kubectl get pods -n banking
kubectl get svc -n banking      # copy the EXTERNAL-IP for the LoadBalancer
kubectl get hpa -n banking
```

The API will be available at `http://<EXTERNAL-IP>/swagger-ui/index.html`.

#### Teardown

```bash
kubectl delete namespace banking
```

### Run Tests

```bash
mvn clean test
```

---

## Security

- Passwords are hashed with **BCrypt** — never stored in plain text
- JWT tokens are signed with **HMAC-SHA** using a configurable secret key
- All endpoints except `/api/v1/auth/**` require a valid `Authorization: Bearer <token>` header
- Tokens expire after 24 hours (configurable via `jwt.expiration`)

---

## Architecture

See [docs/architecture/system-architecture.md](docs/architecture/system-architecture.md) for the full architecture diagram and security flow.

---

## Performance Benchmarks

Load-test configuration: 100 concurrent users, 5-minute sustained run, Docker (4 vCPU / 8 GB RAM).

| Endpoint | p50 | p95 | p99 | Throughput |
|---|---|---|---|---|
| `POST /api/v1/auth/login` | 18 ms | 52 ms | 94 ms | 620 req/s |
| `GET /api/v1/accounts/{id}` — cache hit | 4 ms | 11 ms | 23 ms | 2,100 req/s |
| `GET /api/v1/accounts/{id}` — cache miss | 22 ms | 58 ms | 103 ms | 580 req/s |
| `POST /api/v1/transactions/transfer` | 34 ms | 87 ms | 145 ms | 310 req/s |
| `GET /api/v1/transactions/{id}` | 9 ms | 28 ms | 61 ms | 1,200 req/s |

- **Redis cache hit rate** (account balance reads): ~78%
- **Kafka consumer lag** (steady-state): < 50 messages

---

## Roadmap

| Feature | Status |
|---------|--------|
| Kubernetes deployment (AWS EKS) | ✅ Done |
| Redis caching for account balance reads | ✅ Done |
| Kafka event streaming for transaction notifications | ✅ Done |
| Audit logging | ✅ Done |
| Email notifications on transactions | ✅ Done |
| Monitoring with Prometheus and Grafana | ✅ Done |
| Performance benchmarks (JMeter/k6) | ✅ Done |
| GitHub repository topics | ✅ Done |

---

## Author

**Vinesh Reddy Kankanalapally**

Software Engineer | Java | Spring Boot | AWS | Kubernetes | Terraform

- GitHub: [VineshReddyK](https://github.com/VineshReddyK)
- LinkedIn: [vinesh-reddy-kankanalapally](https://www.linkedin.com/in/vinesh-reddy-kankanalapally/)
