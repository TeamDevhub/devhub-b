# Architecture Rules

## Hexagonal Architecture (Ports & Adapters)

This project follows Hexagonal Architecture in practice.
Dependency direction must always flow **from outer layers to inner layers**.

```text id="3p8vkd"
[api] → [core/port/in/facade] → [core/port/in/usecase]
                                        ↑
                              [core/application/service]
                                        ↓
                              [core/port/out (interfaces)]
                                        ↑
                              [outbound/adapter (implementations)]
                                        ↓
                              [outbound/persistence/JpaRepository]
```

---

## Layer Responsibilities

### `api/` — REST Layer

* Responsible only for:

    * receiving HTTP requests
    * converting DTOs
    * returning responses
* Must inject and call a `Facade`
* Must not inject `UseCase` or `Service` implementations directly
* Must not make business decisions

---

### `core/{domain}/domain/` — Domain Layer

* Contains:

    * domain entities
    * value objects (VO)
    * domain exceptions
* **No Spring dependencies**
* Do not use any Spring annotations such as:

    * `@Service`
    * `@Component`
* Encapsulate business rules inside methods

---

### `core/{domain}/application/` — Application Layer

* Contains `@Service` implementations of UseCase interfaces
* Must depend only on port interfaces (`port/out/`)
* Must not import JPA classes directly
* Loads domain objects, invokes domain methods, and delegates persistence to repositories

---

### `core/{domain}/port/in/` — Input Ports

* `usecase/`
  Interfaces representing feature entry points

* `facade/`
  Orchestrators combining multiple UseCases (`@Service`)

* `command/`
  Input command objects (`record`)

---

### `core/{domain}/port/out/` — Output Ports

* Contains only interfaces for:

    * repositories
    * external services
* No implementations here
* Implementations belong in `outbound/`

---

### `outbound/` — Adapter Layer

* `adapter/`
  `@Component` classes implementing port interfaces

* `persistence/`
  Interfaces extending `JpaRepository`

* `infrastructure/`
  Technical implementations such as:

    * JWT
    * OAuth
    * password encoding

---

## When to Use a Facade

Create a Facade when:

* Two or more UseCases must run sequentially inside one transaction

Example:

```text id="2q7nfm"
User Signup
= verification check
→ save credentials
→ save user profile
→ agree to terms
→ consume verification
```

---

## When Facade Is Not Required

The controller may call directly when:

* Only one UseCase is involved
* Simple read/query operations

---

## Dependency Direction Violations (Strictly Prohibited)

```java id="6m1xtr"
// Prohibited: Service directly uses JPA repository
@Service
public class UserProfileService {
    private final JpaUserRepository jpaUserRepository; // ❌ Direct outbound reference
}

// Prohibited: Controller directly injects Service implementation
@RestController
public class UserController {
    private final UserProfileService userProfileService; // ❌ Direct implementation injection
}

// Prohibited: Spring/JPA annotation in domain class
@Entity // ❌ JPA annotation on domain object
public class User { ... }
```
