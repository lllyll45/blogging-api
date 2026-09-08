
# Blogging Platform API

A RESTful API for managing blog posts. Built as part of the [roadmap.sh](https://roadmap.sh/projects/blogging-platform-api) project challenge.

---

## Tech Stack

- **Java 21** — main programming language
- **Spring Boot 4.1.1** — web framework
- **PostgreSQL 16** — database
- **Hibernate 7.x** — ORM (JPA)
- **Maven 3.x** — build tool
- **Docker** — containerization
- **Swagger 2.6.0** — API documentation
- **JUnit 5 + Mockito** — testing

---

## Features

- Create a blog post (`POST /posts`)
- Get all blog posts (`GET /posts`) with pagination & sorting
- Get a single blog post (`GET /posts/{id}`)
- Update a blog post (`PUT /posts/{id}`)
- Delete a blog post (`DELETE /posts/{id}`)
- Search blog posts (`GET /posts?term=...`)
- Input validation
- Global exception handling
- Logging

---

## How to Run

### Option 1: Local Run (without Docker)

1. **Install Java 21 and PostgreSQL**
2. **Create database:**
   ```sql
   CREATE DATABASE blogdb;
   ```
3. **Configure `application.properties`:**
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/blogdb
   spring.datasource.username=postgres
   spring.datasource.password=1234
   ```
4. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

### Option 2: Run with Docker

1. **Install Docker Desktop**
2. **Run with one command:**
   ```bash
   docker-compose up --build
   ```
3. **Access the app at:** `http://localhost:8080`

---

## API Endpoints

GET /posts — get all posts
GET /posts?term=java — search posts by keyword
GET /posts?page=0&size=10 — get posts with pagination
GET /posts/{id} — get a single post by ID
POST /posts — create a new post
PUT /posts/{id} — update an existing post
DELETE /posts/{id} — delete a post by ID

---

### Example Request (POST /posts)

**Request:**
POST /posts
```json
{
  "title": "My First Blog Post",
  "content": "This is the content of my first blog post.",
  "category": "TECHNOLOGY",
  "tags": ["Tech", "Programming"]
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "title": "My First Blog Post",
  "content": "This is the content of my first blog post.",
  "category": "TECHNOLOGY",
  "tags": ["Tech", "Programming"],
  "createdAt": "2026-09-08T12:00:00Z",
  "updatedAt": "2026-09-08T12:00:00Z"
}
```

---

## Swagger Documentation

After starting the app, open in your browser:

```
http://localhost:8080/swagger-ui/index.html
```

---

## Running Tests

```bash
mvn clean test
```

---

## Docker Commands

Build the image:

```bash
docker build -t blogging-api .
```

Start containers:

```bash
docker-compose up --build
```

Stop containers:

```bash
docker-compose down
```

---

## CI/CD

Automated pipeline via **GitHub Actions**:

1. **Tests** — runs on every push
2. **Docker Image Build** — publishes to Docker Hub

---
