# Coding Style Memory

## Class Design Principles

### Domain Entities (`core/{domain}/domain/`)

* `@Builder` must be `private` — direct external `new` construction is not allowed.
* Create instances only through static factory methods such as:

    * `createGeneralUser()`
    * `createOAuthUser()`
    * `of()`
* State changes must occur only through domain methods:

    * `withdraw()`
    * `changePositions()`
* Complex mutation results should be returned as inner `record` types:

    * `PositionChangeResult`
    * `SkillChangeResult`
* Spring annotations are prohibited (`@Service`, `@Component`, etc.)
* No setters allowed.

### Services (`core/{domain}/application/service/`)

* Use `@Service` + `@RequiredArgsConstructor` + `@Transactional` (class level)
* Inject only port interfaces (direct JPA repository injection is prohibited)
* Use only `private final` fields

### Facades (`core/{domain}/port/in/facade/`)

* Use `@Service` + `@RequiredArgsConstructor` + `@Transactional`
* Combine multiple UseCases into a single transaction
* Use a Facade when composing two or more UseCases

### Controllers (`api/web/controller/`)

* Inject only a Facade or a single UseCase (direct Service injection is prohibited)
* Use `@RestController` + `@RequiredArgsConstructor`
* Always return `DataApiResponseDto<T>`

---

## Value Objects / Commands (`record` Usage Rules)

Use `record` for:

* Immutable value objects (VO)

    * `EmailUserCredential`
    * `OAuthUserCredential`
    * `AuthResult`
* Command objects

    * `SignupUserCommand`
    * `LoginCommand`
    * `UpdateBasicProfileCommand`
* Mutation result objects

    * `UserPositionChangeResult`
    * `UserSkillChangeResult`

Use `class` for:

* Mutable internal state
* Collection manipulation
* JPA entities

---

## Exception Layering

| Layer            | Exception Class         | Creation Method                           |
| ---------------- | ----------------------- | ----------------------------------------- |
| Domain           | `DomainRuleException`   | `DomainRuleException.of(ErrorCode.XXX)`   |
| Service / Facade | `BusinessRuleException` | `BusinessRuleException.of(ErrorCode.XXX)` |
| Adapter          | `AdapterDataException`  | `AdapterDataException.of(ErrorCode.XXX)`  |

* Hardcoded error messages are prohibited.
* Always use the `ErrorCode` enum.

---

## Test Style

* Class name: `{TargetClass}Test`
* Method name: `{methodName}_{scenario}`
  Example: `signupEmailUser_success`
* `@DisplayName`: Korean text with underscores for spaces
* GWT comments must always be included:

    * `// given`
    * `// when`
    * `// then`
* Fake implementation naming:

    * `Fake{PortName}`
    * `FakeUserRepository`
    * `FakeRefreshTokenRepository`
* Mockito is prohibited

---

## Response Format

```java
// Success with data
DataApiResponseDto.successWithData(result)

// Success without data
DataApiResponseDto.successWithoutData()

// Failure (handled by GlobalExceptionHandler)
DataApiResponseDto.failureWithoutData(errorCode)
```

---

## Annotation Conventions

* `@Autowired` prohibited
  → Use `@RequiredArgsConstructor` + `final`
* `@Enumerated(EnumType.STRING)` for all enum columns
* `@Transactional` declared at class level only
  → Method-level overrides prohibited
* `@SpringBootTest` + `@Transactional` is the standard setup for integration tests
