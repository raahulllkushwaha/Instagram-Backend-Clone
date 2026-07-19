# Instaclone — Instagram Clone Backend

A production-style, feature-complete backend for an Instagram-like social media application, built with **Spring Boot**. This project goes beyond a typical CRUD app — it uses a **polyglot persistence** architecture (PostgreSQL + Neo4j), **event-driven notifications** (Kafka), **real-time chat** (WebSocket/STOMP), and is fully **containerized with Docker**.

---

## Features

- **Authentication & Security** — JWT-based signup/login, custom `JwtAuthFilter`, Spring Security, rate limiting on auth endpoints (Bucket4j)
- **Profiles** — view/update profile, bio, profile picture
- **Follow System** — built on **Neo4j** graph database for followers/following and mutual-connections queries
- **Posts** — create/read/delete posts with real image uploads to **Cloudinary**
- **Feed** — paginated feed combining a user's own posts and posts from people they follow (Postgres + Neo4j working together)
- **Likes & Comments** — like/unlike posts, add/view/delete comments
- **Notifications** — event-driven notifications (like, comment, follow) via **Apache Kafka** producer/consumer
- **Stories** — 24-hour auto-expiring stories with a **scheduled cleanup job** (`@Scheduled`) that also removes expired media from Cloudinary
- **Real-time Chat** — one-to-one messaging over **WebSocket (STOMP)** with JWT-authenticated handshake, plus REST endpoints for chat history
- **API Documentation** — interactive docs via **Swagger / OpenAPI**
- **Testing** — unit tests for service-layer logic using **JUnit 5 & Mockito**
- **Containerization** — full stack (app + Postgres + Kafka + Zookeeper) runs with a single `docker compose up`

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language / Framework | Java 21, Spring Boot 3 |
| Relational DB | PostgreSQL |
| Graph DB | Neo4j (follow relationships, mutual connections) |
| Messaging | Apache Kafka (notifications) |
| Real-time | WebSocket + STOMP |
| Media Storage | Cloudinary |
| Security | Spring Security, JWT (jjwt), Bucket4j (rate limiting) |
| API Docs | springdoc-openapi (Swagger UI) |
| Testing | JUnit 5, Mockito |
| Containerization | Docker, Docker Compose |
| Build Tool | Maven |

---

## Architecture Highlights

- **Polyglot Persistence** — user/post/comment/like data lives in PostgreSQL, while the social graph (follows, mutual connections) lives in Neo4j for efficient graph traversal.
- **Event-Driven Notifications** — actions like `like`, `comment`, and `follow` publish events to Kafka; a separate consumer persists notifications asynchronously, decoupling the write path from notification delivery.
- **Feed Generation** — combines the follow graph (Neo4j) with paginated post queries (Postgres `Pageable`) to build a personalized, paginated feed.
- **Scheduled Jobs** — stories automatically expire after 24 hours; a `@Scheduled` job periodically purges expired stories from both the database and Cloudinary.
- **Real-Time Messaging** — a custom WebSocket handshake interceptor authenticates users via JWT (passed as a query parameter) before establishing the STOMP session, enabling per-user private message queues.
- **Layered Design** — consistent Controller → Service → Repository structure per feature module, with DTOs and mappers separating persistence entities from API responses.

---

## Project Structure

```
src/main/java/com/rahul/instagram/
├── auth/            # Signup, login, JWT utils, JWT filter
├── user/            # Profile, user repository/service
├── follow/          # Neo4j-based follow system
├── post/            # Post CRUD
├── media/           # Cloudinary upload/delete service
├── feed/            # Paginated feed generation
├── like/            # Like/unlike
├── comment/         # Comments
├── notification/    # Kafka producer/consumer, notifications
├── story/           # Auto-expiring stories + cleanup scheduler
├── chat/            # WebSocket chat (entity, service, controller)
├── config/          # Security, WebSocket, JPA/Neo4j transaction config
└── common/          # Global exception handling, generic API response wrapper
```

---

## Getting Started

### Prerequisites
- Java 21
- Maven
- Docker & Docker Compose
- A Neo4j instance (local install or container)
- A Cloudinary account (for media uploads)

### Environment Variables
Create a `.env` file in the project root:
```
JWT_SECRET=your-secret-key
CLOUDINARY_CLOUD_NAME=your-cloud-name
CLOUDINARY_API_KEY=your-api-key
CLOUDINARY_API_SECRET=your-api-secret
```

### Run with Docker (recommended)
```bash
docker compose up -d --build
```
This spins up the Spring Boot app, PostgreSQL, Kafka, and Zookeeper together.

### Run locally (IDE)
1. Start Postgres, Neo4j, Kafka, and Zookeeper (via `docker compose up -d` for the infra services, or your own setup).
2. Set the environment variables above in your run configuration.
3. Run `InstagramApplication` from your IDE, or `mvn spring-boot:run`.

### API Documentation
Once running, visit:
```
http://localhost:8081/swagger-ui.html
```

---

## Testing
Run unit tests with:
```bash
mvn test
```

---

## Roadmap / Upcoming

- [ ] **Redis caching** — cache feed and profile reads to reduce repeated Postgres/Neo4j lookups
- [ ] **CI/CD with GitHub Actions** — automated build, test, and Docker image pipeline on every push

---

## Author
Built by Rahul Kushwaha as a placement-focused, from-scratch backend engineering project.