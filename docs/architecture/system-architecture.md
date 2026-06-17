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
│   └── SwaggerConfig.java          # OpenAPI + JWT bearer scheme
├── controller
│   ├── AuthController.java          # /api/auth/**
│   ├── AccountController.java       # /api/accounts/**
│   └── TransactionController.java   # /api/transactions/**
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
│   └── Transaction.java
├── exception
│   ├── ResourceNotFoundException.java
│   ├── DuplicateResourceException.java
│   └── GlobalExceptionHandler.java  # Structured JSON error responses
├── repository
│   ├── UserRepository.java
│   ├── AccountRepository.java
│   └── TransactionRepository.java
├── security
│   ├── JwtUtil.java                 # Token generation + validation
│   ├── JwtFilter.java               # OncePerRequestFilter
│   └── SecurityConfig.java          # Filter chain + BCrypt bean
└── service
    ├── UserService.java
    ├── AccountService.java
    └── TransactionService.java
```

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 17 |
| Framework | Spring Boot 3.3 |
| Security | Spring Security + JWT (JJWT 0.12.6) |
| Password Hashing | BCrypt |
| ORM | Spring Data JPA + Hibernate |
| Database | MySQL 8 |
| API Docs | SpringDoc OpenAPI / Swagger UI |
| Testing | JUnit 5 + Mockito |
| Build | Maven |
| Containerization | Docker (multi-stage build) |
| CI/CD | GitHub Actions |

