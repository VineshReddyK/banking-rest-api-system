# API Endpoints

Base URL: `http://localhost:8080`

Swagger UI: `http://localhost:8080/swagger-ui/index.html`

> All endpoints except `/api/v1/auth/**` require a JWT Bearer token in the `Authorization` header.

---

## Authentication

### Register

```
POST /api/v1/auth/register
```

**Request Body:**
```json
{
  "username": "vinesh",
  "email": "vinesh@example.com",
  "password": "password123"
}
```

**Response:** `201 Created`
```
User registered successfully
```

---

### Login

```
POST /api/v1/auth/login
```

**Request Body:**
```json
{
  "email": "vinesh@example.com",
  "password": "password123"
}
```

**Response:** `200 OK`
```json
{
  "token": "<jwt-token>"
}
```

---

## Accounts

### Create Account

```
POST /api/accounts/{userId}
Authorization: Bearer <token>
```

**Request Body:**
```json
{
  "accountType": "SAVINGS",
  "initialDeposit": 1000.00
}
```

**Response:** `201 Created`
```json
{
  "accountNumber": "A1B2C3D4E5F6",
  "accountType": "SAVINGS",
  "balance": 1000.00
}
```

---

### Get All Accounts

```
GET /api/accounts
Authorization: Bearer <token>
```

**Response:** `200 OK`
```json
[
  {
    "accountNumber": "A1B2C3D4E5F6",
    "accountType": "SAVINGS",
    "balance": 1000.00
  }
]
```

---

## Transactions

### Deposit

```
POST /api/transactions/deposit
Authorization: Bearer <token>
```

**Request Body:**
```json
{
  "accountId": 1,
  "amount": 500.00
}
```

**Response:** `200 OK`
```
Deposit Successful. New balance: 1500.0
```

---

### Withdraw

```
POST /api/transactions/withdraw
Authorization: Bearer <token>
```

**Request Body:**
```json
{
  "accountId": 1,
  "amount": 200.00
}
```

**Response:** `200 OK`
```
Withdrawal Successful. New balance: 1300.0
```

---

### Transfer

```
POST /api/transactions/transfer
Authorization: Bearer <token>
```

**Request Body:**
```json
{
  "accountId": 1,
  "targetAccountId": 2,
  "amount": 300.00
}
```

**Response:** `200 OK`
```
Transfer Successful. New balance: 1000.0
```

---

### Transaction History

```
GET /api/transactions/{accountId}
Authorization: Bearer <token>
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "amount": 500.00,
    "transactionType": "DEPOSIT",
    "transactionDate": "2026-06-17T10:30:00"
  }
]
```

---

## Error Responses

All errors return a consistent JSON structure:

```json
{
  "timestamp": "2026-06-17T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with id: 99"
}
```

| Status | Meaning |
|--------|---------|
| 400 | Validation error or bad input |
| 401 | Missing or invalid JWT token |
| 404 | Resource not found |
| 409 | Duplicate resource (e.g. email already in use) |
| 500 | Unexpected server error |

