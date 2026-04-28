# Exception Handling Rules

## Exception Hierarchy

There are three exception types.
Use them according to the layer where the error occurs.

| Exception Class         | Occurrence Layer             | Meaning                                         |
| ----------------------- | ---------------------------- | ----------------------------------------------- |
| `DomainRuleException`   | `core/{domain}/domain/`      | Domain rule violation                           |
| `BusinessRuleException` | `core/{domain}/application/` | Business policy violation / unmet preconditions |
| `AdapterDataException`  | `outbound/{domain}/adapter/` | Missing data / infrastructure access failure    |

All follow the same creation pattern:

```java id="7r4nxt"
// Domain layer: domain rule violation
throw DomainRuleException.of(ErrorCode.ALREADY_DELETED);

// Application layer: business policy violation
throw BusinessRuleException.of(ErrorCode.REFRESH_TOKEN_INVALID);

// Adapter layer: missing data
throw AdapterDataException.of(ErrorCode.USER_NOT_FOUND);
```

---

## ErrorCode Usage Principles

* Do not hardcode error messages.
* Always use values from the `ErrorCode` enum.
* If a new error case appears, add it to `ErrorCode` first, then throw the exception.
* `ErrorCode.UNKNOWN_FAIL` should be temporary only and replaced with a specific code as soon as possible.

```java id="4k9vpd"
public enum ErrorCode {
    USER_NOT_FOUND("ERR.DVH.0014", "Logged-in user does not exist", UNAUTHORIZED),
    REFRESH_TOKEN_INVALID("ERR.DVH.0015", "Invalid token", UNAUTHORIZED),
    ALREADY_DELETED("ERR.DVH.0032", "User already withdrawn", BAD_REQUEST),
    ...
}
```

---

## API Response Rules

All API responses must be wrapped with `DataApiResponseDto<T>`.

```java id="9m2qxs"
// Success response with data
return ResponseEntity.ok(
    DataApiResponseDto.successWithData(SuccessCode.LOGIN_SUCCESS, data)
);

// Success response without data
return ResponseEntity.ok(
    DataApiResponseDto.successWithoutData(SuccessCode.LOGOUT_SUCCESS)
);
```

Do not manually create error responses inside controllers.
Throw exceptions and let `GlobalExceptionHandler` process them.

---

## Exception Patterns by Layer

### Domain Layer

```java id="3v8nka"
// Validate state inside domain methods and throw exceptions
public void withdraw() {
    if (this.deleted) {
        throw DomainRuleException.of(ErrorCode.ALREADY_DELETED);
    }
    this.deleted = true;
}

public void confirm(String code, LocalDateTime now) {
    boolean success = verify(code, now);
    if (!success) {
        throw DomainRuleException.of(ErrorCode.VERIFICATION_FAIL);
    }
}
```

---

### Application Layer

```java id="1t6qwr"
// Duplicate check: throw if exists
userCredentialRepository.findEmailUserCredentialByEmail(email)
        .ifPresent(c -> {
            throw BusinessRuleException.of(ErrorCode.UNKNOWN_FAIL);
        });

// Preconditions: token mismatch
if (savedRefreshToken == null || !savedRefreshToken.token().equals(refreshToken)) {
    throw BusinessRuleException.of(ErrorCode.REFRESH_TOKEN_INVALID);
}
```

---

### Adapter Layer

```java id="6p4xzm"
// Missing data: retrieval that guarantees existence
public User findByUserGuid(String userGuid) {
    return jpaUserRepository.findByUserGuid(userGuid)
            .orElseThrow(() -> AdapterDataException.of(ErrorCode.USER_NOT_FOUND));
}
```

---

## Optional Return vs Throwing Exception

Use two distinct patterns in adapter methods.

```java id="8n1vct"
// Data may not exist → return Optional
Optional<UserCredential> findEmailUserCredentialByEmail(String email);
Optional<UserCredential> findOAuthUserCredentialByOAuth(
        VerificationProvider provider,
        String oauthId
);

// Data must exist → throw exception if absent
User findByUserGuid(String userGuid);
Verification findByVerificationTarget(
        VerificationTarget verificationTarget
);
```
