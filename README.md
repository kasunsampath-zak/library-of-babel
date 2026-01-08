
# 📚 Library Management REST API

A simple, clean, and production-ready Library Management REST API built using **Java 17** and **Spring Boot**.  
This application allows managing borrowers and books, including borrowing and returning books while enforcing core domain rules.

---

## 🚀 Features

### Library Management
- Register new borrowers
- Register new books (supports multiple copies)
- List all books in the library

### Borrowing Operations
- Borrow a book for a borrower
- Return a borrowed book
- Ensures only one borrower can borrow a book copy at a time

### Technical Highlights
- Java 17
- Spring Boot
- RESTful API design
- DTO-based request/response model
- Centralized global error handling
- Unit tests with JUnit 5 & Mockito
- Environment-based configuration
- Clean architecture & best practices

---

## 🏗️ Architecture Overview

```
Controller  →  Service  →  Repository  →  Database
    ↓           ↓
    DTOs     Business Rules
```

**Design principles used:**
- Separation of concerns
- Thin controllers, rich services
- Explicit domain rules
- Testable business logic
- Fail-fast validation

---

## 🧩 Tech Stack

| Category      | Technology                        |
|---------------|-----------------------------------|
| Language      | Java 17                           |
| Framework     | Spring Boot                       |
| Build Tool    | Maven                             |
| ORM           | Spring Data JPA                   |
| Database      | PostgreSQL (prod), H2 (dev/test)  |
| Testing       | JUnit 5, Mockito                  |
| API Docs      | Swagger / OpenAPI                 |
| Containerization | Docker (optional)              |

---

## 🗄️ Database Choice & Justification

**PostgreSQL** is used for production because:
- ACID compliance
- Strong relational consistency
- Widely adopted in enterprise environments
- Suitable for enforcing domain rules (e.g., borrowing constraints)

**H2** is used for local development and testing to enable fast setup and isolated tests.

---

## 📦 Data Models

### Borrower
- `id` (unique)
- `name`
- `email` (unique)

### Book
- `id` (unique – represents a physical copy)
- `isbn`
- `title`
- `author`
- `status` (`AVAILABLE` / `BORROWED`)

---

## 📘 ISBN Rules Enforced

- Books with the same ISBN must have the same title and author
- Books with the same title and author but different ISBNs are different books
- Multiple copies of the same ISBN are allowed (each copy has a unique ID)

---

## 🌐 REST API Endpoints

### Borrowers

| Method | Endpoint                                      | Description            |
|--------|-----------------------------------------------|------------------------|
| POST   | `/borrowers`                                  | Register a new borrower|
| GET    | `/borrowers`                                  | List all borrowers     |
| POST   | `/borrowers/{borrowerId}/borrow/{bookId}`     | Borrow a book          |
| POST   | `/borrowers/{borrowerId}/return/{bookId}`     | Return a book          |

### Books

| Method | Endpoint      | Description            |
|--------|--------------|------------------------|
| POST   | `/books`     | Register a new book copy|
| GET    | `/books`     | List all books         |

---

## 🧪 Testing

- Unit tests focus on service layer
- Business rules tested thoroughly
- Fast, isolated tests using mocks
- High coverage for critical logic

**Run tests:**

```sh
mvn clean test
```

---

## ⚠️ Error Handling

All errors are handled via a Global Exception Handler:

- **404 Not Found** → Resource not found
- **400 Bad Request** → Business rule violation or validation failure
- **500 Internal Server Error** → Unexpected errors

Error responses are consistent and client-friendly.

---

## ⚙️ Configuration & Environments

Supports multiple environments using Spring profiles:

- `application-dev.yml` → PostgreSQL (local)
- `application-prod.yml` → PostgreSQL

Configuration follows [12-Factor App](https://12factor.net/) principles:

- Externalized configuration
- Environment-specific profiles
- Stateless service design

---

## ▶️ Running the Application

### Local (Dev)

```sh
mvn spring-boot:run
```

**Swagger UI:**  
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### 🐳 Docker (Optional)

**Build image:**

```sh
docker build -t library-api .
```

**Run with Docker Compose:**

```sh
docker-compose up
```

---

## 📄 Assumptions

- Authentication & authorization are out of scope
- Borrow history is not persisted
- Each Book ID represents a physical copy
- One book copy can be borrowed by only one borrower at a time
- No reservation or wait-list functionality

_All assumptions are documented intentionally due to unclear requirements._

---

## 🔮 Future Improvements

- Borrowing history tracking
- Pagination & filtering
- Authentication (JWT / OAuth2)
- Rate limiting
- Kubernetes deployment
- CI/CD pipeline with GitHub Actions

---

## 👨‍💻 Author

**Kasun Sampath** (ksf.kaunsampath@gmail.com)  
Senior Software Engineer  
Java • Spring Boot • Backend Systems

---

## ✅ Final Notes

This project intentionally prioritizes:

- Clarity over over-engineering
- Business rules over framework complexity
- Testability over shortcuts
