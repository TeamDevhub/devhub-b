# Skill: README Writing

## Purpose

Defines the process for writing an accurate README for the DevHub Backend **based only on the actual source code**.

Do not guess. Read the code first and document facts only.

---

# Step 1. Scan Current Codebase

```bash id="k4m8xp"
# Identify controllers
Glob src/main/java/teamdevhub/devhub/api/**/*Controller.java

# Check runtime configuration
Read src/main/resources/application.yml
Read src/main/resources/application-dev.yml   (if exists)

# Check dependencies
Read build.gradle
```

---

# Step 2. Review Existing README

```bash id="p7t2vd"
Read README.md
```

* If it exists, identify outdated sections
* If not, create a new README

---

# Step 3. Extract API Endpoints

From each controller, extract the following:

| Item                    | Source                              |
| ----------------------- | ----------------------------------- |
| HTTP Method             | `@GetMapping`, `@PostMapping`, etc. |
| URL Path                | `@RequestMapping` + method mapping  |
| Authentication Required | Presence of `@LoginUser` parameter  |
| Request Body            | `@RequestBody` type                 |
| Response Body           | Return type                         |
| Description             | Method name or `@Operation`         |

---

# Step 4. Extract Environment Variables

```bash id="v1q6rm"
Grep "System.getenv\|@Value\|getenv" src/main/java --include="*.java"
```

Also extract `${...}` placeholders from config files.

Organize results in `.env.example` style.

---

# Step 5. Write README Structure

```markdown id="n8x3tk"
# DevHub Backend

## Project Overview

## Tech Stack

| Category | Technology |
|---|---|
| Framework | Spring Boot 3.x |
| Language | Java 17 |
| Build | Gradle |
| Database | H2 (Local), MySQL (Production) |
| Authentication | JWT (Bearer + HttpOnly Cookie) |

## Architecture

Hexagonal Architecture (Ports & Adapters)

api/ → facade → usecase ← service
→ port.out ← adapter ← JPA

## Run Locally

1. Start H2 TCP server (if required)
2. Configure environment variables
3. Run:

./gradlew bootRun --args='--spring.profiles.active=dev'

## Environment Variables

| Variable | Description | Example |
|---|---|---|
| JWT_SECRET | JWT signing key | any-secret-string |

## API Documentation

### Auth

| Method | URL | Auth | Description |
|---|---|---|---|
| POST | /auth/login | No | Email login |

## Run Tests

./gradlew test
./gradlew test --tests "teamdevhub.devhub.small.*"

## Current Development Status

### Completed Features
- ...

### Incomplete Features
- Password change (`changePassword`)
- ...
```

---

# Step 6. Criteria for Marking Incomplete Features

Search for these patterns in code:

```bash id="m5r9qp"
Grep "TODO\|FIXME\|주석\|미구현" src/main/java --include="*.java" -i
```

Also treat **commented-out public methods** as incomplete.

---

# Step 7. Final Report Format

After completion, report using:

```text id="z2v7nc"
## README Completed

### Added Sections
- ...

### Removed Outdated Content
- ...

### Marked as Incomplete
- ...

### Needs Confirmation
- (Environment variables whose real values are unknown)
```
