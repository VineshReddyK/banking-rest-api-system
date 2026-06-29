# System Architecture

## Overview

The Banking REST API follows a standard layered architecture pattern with a stateless JWT-based security model.

## Architecture Diagram

```
┌─────────────────────────────────────────────────┐
│                  Client / Swagger UI             │
└─────────────────────┬───────────────────────────┘
                      │ HTTP Request + Bearer Token
                      ▼
┌─────────────────────────────────────────────────┐
│              Spring Security Filter Chain        │
│         JwtFilter → validates JWT token          │
└─────────────────────┬───────────────────────────┘
                      │ Authenticated Request
                      ▼
┌─────────────────────────────────────────────────┐
│              REST Controllers Layer              │
│  AuthController | AccountController |            │
│  TransactionController                           │
│  (Input validation via @Valid)                   │
└─────────────────────┬───────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────┐
│                 Service Layer                    │
│  UserService | AccountService |                  │
│  TransactionService                              │
│  (Business logic, BCrypt, JWT generation)        │
└─────────────────────┬───────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────┐
│              Repository Layer                    │
│  UserRepository | AccountRepository |            │
│  TransactionRepository                           │
│  (Spring Data JPA)                               │
└─────────────────────┬───────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────┐
│           MySQL Database (via Hibernate)          │
│  Tables: users | accounts | transactions         │
└─────────────────────────────────────────────────┘
```

## Security Flow

```
POST /api/auth/login
        │
        ▼
  Validate credentials
  (BCrypt password match)
        │
        ▼
  Generate JWT (JJWT 0.12.6)
  Signed with HMAC-SHA secret
        │
        ▼
  Return token to client
        │
        ▼
  Client sends: Authorization: Bearer <token>
        │
        ▼
  JwtFilter extracts + validates token
        │
        ▼
  Set authentication in SecurityContext
        │
        ▼
  Request proceeds to Controller
```

## Package Structure

```
com.vinesh.banking
├── BankingApplication.java
├── config
│   ├── RedisConfig.java             # Cache manager, 10-min TTL
│   └── SwaggerConfig.java           # OpenAPI + JWT bearer scheme
├── controller
│   ├── AuthController.java          # /api/v1/auth/**
│   ├── AccountController.java       # /api/v1/accounts/**
│   └── TransactionController.java   # /api/v1/transactions/**
├── dto
│   ├── RegisterRequest.java
│   ├── LoginRequest.java
│   ├── LoginResponse.java
│   ├── AccountRequest.java
│   ├── AccountResponse.java
│   └── TransactionRequest.java
├── entity
│   ├── User.java
│   ├── Account.java
│   ├── Transaction.java
│   └── AuditLog.java
├── exception
│   ├── ResourceNotFoundException.java
│   ├── DuplicateResourceException.java
│   └── GlobalExceptionHandler.java  # Structured JSON error responses
├── kafka
│   ├── TransactionProducer.java     # Publishes to transaction-events topic
│   ├── TransactionConsumer.java
│   └── TransactionEvent.java
├── repository
│   ├── UserRepository.java
│   ├── AccountRepository.java
│   ├── TransactionRepository.java
│   └── AuditLogRepository.java
├── security
│   ├── JwtUtil.java                 # Token generation + validation
│   ├── JwtFilter.java               # OncePerRequestFilter
│   └── SecurityConfig.java          # Filter chain + BCrypt bean
└── service
    ├── UserService.java
    ├── AccountService.java
    ├── TransactionService.java
    ├── AuditLogService.java
    └── EmailNotificationService.java
```

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 21 |
| Framework | Spring Boot 3.5 |
| Security | Spring Security + JWT (JJWT 0.12.6) |
| Password Hashing | BCrypt |
| ORM | Spring Data JPA + Hibernate |
| Database | MySQL 8 |
| Caching | Redis 7 |
| Messaging | Apache Kafka |
| Email | Spring Mail (Gmail SMTP) |
| API Docs | SpringDoc OpenAPI 2.5 / Swagger UI |
| Monitoring | Prometheus + Grafana |
| Testing | JUnit 5 + Mockito |
| Build | Maven |
| Containerization | Docker (multi-stage build) |
| Orchestration | Kubernetes (AWS EKS) |
| CI/CD | GitHub Actions |

