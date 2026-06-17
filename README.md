# Banking REST API System

![CI](https://github.com/VineshReddyK/banking-rest-api-system/actions/workflows/ci.yml/badge.svg)

## Project Status

✅ Complete

### Completed

* Spring Boot Project Structure
* JPA Entities
* DTOs
* Controllers
* Services
* Repositories
* Exception Handling
* JWT Security (with filter chain integration)
* Swagger/OpenAPI Configuration
* Dockerfile
* GitHub Actions CI Pipeline
* Unit Tests (JUnit & Mockito)

### Upcoming

* MySQL Integration Testing
* AWS Deployment
* Kubernetes Deployment

---

## Overview

A production-ready banking backend application built using Java, Spring Boot, Spring Security, JWT, Hibernate, JPA, MySQL, Docker, and Swagger/OpenAPI.

This project demonstrates modern backend development practices including layered architecture, RESTful API development, authentication, exception handling, containerization, and enterprise-grade project structure.

---

## Features

* User Registration
* User Authentication using JWT
* Role-Based Access Control
* Account Creation
* Account Management
* Deposit Transactions
* Withdrawal Transactions
* Fund Transfers
* Transaction History
* Global Exception Handling
* Swagger/OpenAPI Documentation
* JPA Entity Management
* Repository Pattern
* RESTful API Design
* Docker Containerization
* Scalable Layered Architecture

---

## Tech Stack

### Backend

* Java 17
* Spring Boot 3
* Spring Security
* Spring Data JPA
* Hibernate

### Database

* MySQL

### API Documentation

* Swagger / OpenAPI

### Testing

* JUnit
* Mockito

### DevOps

* Docker
* GitHub Actions

### Build Tool

* Maven

---

## Project Architecture

```text
Client
   │
   ▼
REST Controllers
   │
   ▼
Service Layer
   │
   ▼
Repository Layer
   │
   ▼
Hibernate / JPA
   │
   ▼
MySQL Database

Security Layer
(JWT Authentication)

Documentation Layer
(Swagger/OpenAPI)

Deployment Layer
(Docker)
```

---

## API Endpoints

### Authentication

```http
POST /api/auth/register
POST /api/auth/login
```

### Accounts

```http
POST /api/accounts
GET  /api/accounts
```

### Transactions

```http
POST /api/transactions/deposit
POST /api/transactions/withdraw
POST /api/transactions/transfer
```

---

## Project Structure

```text
src/main/java/com/vinesh/banking

├── config
├── controller
├── dto
├── entity
├── exception
├── repository
├── security
├── service
└── BankingApplication.java
```

---

## Documentation

* Architecture Diagram: docs/architecture/system-architecture.md
* API Documentation: docs/api-endpoints.md

---

## How to Run Locally

```bash
git clone https://github.com/VineshReddyK/banking-rest-api-system.git
cd banking-rest-api-system
mvn clean install
mvn spring-boot:run
```

Application URL:

```text
http://localhost:8080
```

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

---

## Future Enhancements

* Kafka Event Streaming
* Event-Driven Architecture
* Kubernetes Deployment
* AWS EKS Deployment
* Monitoring using Prometheus and Grafana
* Redis Caching
* Email Notifications
* Audit Logging

---

## Author

**Vinesh Reddy Kankanalapally**

Software Engineer | Java | Spring Boot | AWS | Kubernetes | Terraform

GitHub: https://github.com/VineshReddyK

LinkedIn: https://www.linkedin.com/in/vinesh-reddy-kankanalapally/
