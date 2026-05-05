# Project Status Memory

## Basic Project Information

* **Name**: DevHub Backend
* **Purpose**: Developer project matching and team-building platform
* **Stack**: Java 17 / Spring Boot 3.5.7 / Gradle / H2 (development)
* **Architecture**: Hexagonal Architecture (Ports & Adapters)
* **Branch Strategy**: `main` ← `dev` ← `feature/*`
* **Current Active Branch**: `feature/user`

---

## Domain Overview

| Domain         | Status           | Notes                               |
| -------------- | ---------------- | ----------------------------------- |
| `auth`         | Active           | Email + 4 OAuth providers           |
| `user`         | Active           | Signup, profile, withdrawal         |
| `project`      | Active (partial) | Service tests incomplete            |
| `board`        | Exists           | Detailed status unknown             |
| `file`         | Exists           | Detailed status unknown             |
| `application`  | Exists           | Detailed status unknown             |
| `terms`        | Active           | Terms agreement during signup       |
| `admin`        | Active (partial) | Authorization validation incomplete |
| `notification` | Exists           | Detailed status unknown             |

---

## Authentication Flow

```text id="xy7n2q"
Email Login:     POST /auth/login            → AuthFacade → UserCredentialService.authenticate()
OAuth Login:     GET /oauth/{provider}/callback → OAuthAuthFacade
User Signup:     POST /user/signup          → UserSignupFacade
Token Reissue:   POST /auth/reissue         → UserCredentialService.getUserForReissue()
```

---

## Incomplete Features (Currently Commented Out in Code)

| Feature                   | Location                              | Status                       |
| ------------------------- | ------------------------------------- | ---------------------------- |
| Password Change           | `User.changePassword()`               | Domain method commented out  |
| Password Change UseCase   | `UserProfileUseCase.updatePassword()` | Interface commented out      |
| Admin Authorization Check | `WebSecurityConfig:106`               | `/admin/**` is `permitAll()` |
| ProjectServiceTest        | `ProjectServiceTest.java`             | Entire file commented out    |

---

## Test Coverage Status

| Category             | Location               | Status                    |
| -------------------- | ---------------------- | ------------------------- |
| Unit Tests           | `src/test/.../small/`  | auth/user domain complete |
| Integration Tests    | `src/test/.../medium/` | auth adapters complete    |
| E2E Tests            | `src/test/.../large/`  | Not written               |
| Fake Implementations | `src/test/.../fake/`   | auth/user domain complete |

---

## Test Constants Location

```text id="b4lm8r"
src/test/java/teamdevhub/devhub/constant/UserTestConstant.java
```

Main constants: `TEST_USER_GUID_1`, `TEST_EMAIL_1`, `TEMP_TOKEN`

---

