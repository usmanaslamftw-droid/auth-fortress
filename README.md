# Auth Fortress

A comprehensive authentication and authorization sandbox built with Spring Boot, demonstrating JWT, OAuth2 concepts, RBAC (Role-Based Access Control), password hashing with BCrypt, and full audit logging.

## Tech Stack
- Java 21
- Spring Boot 3.5.16
- Spring Security + JWT
- H2 Database + Spring Data JPA
- Gradle 8.14

## Setup
1. Clone the repository
2. Run `./gradlew clean build`
3. Run `./gradlew bootRun`
4. The application will start on port `8084`

## API Documentation
Swagger UI is available at: `http://localhost:8084/swagger-ui.html`
H2 Console is available at: `http://localhost:8084/h2-console` (JDBC URL: `jdbc:h2:mem:authdb`, Username: `sa`)

## Seed User
On startup, an admin user is created:
- Username: `admin`
- Password: `admin123`
