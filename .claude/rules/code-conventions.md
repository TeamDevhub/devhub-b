# Code Conventions

## Dependency Injection

Do not use `@Autowired`.
All dependencies must be injected via constructor injection.

```java id="1k9xzm"
// Correct approach
@Service
@RequiredArgsConstructor
public class UserProfileService implements UserProfileUseCase {
    private final UserRepository userRepository;           // final + @RequiredArgsConstructor
    private final UserPositionRepository userPositionRepository;
}
```

---

## Transactions

* Declare `@Transactional` at the class level only. Do not apply it per method.
* Use `readOnly = true` only when there is a clear performance reason.

```java id="6f3qdp"
@Service
@Transactional          // ✅ Class-level declaration
@RequiredArgsConstructor
public class UserSignupService implements UserSignupUseCase { ... }
```

---

## Domain Object Creation Pattern

Constructors of domain classes must be `private`.
Objects must be created through static factory methods.

```java id="8z4nwr"
@Getter
public class User {

    @Builder                          // Builder used internally only
    private User(...) { ... }         // private constructor

    // Create via static factory method only
    public static User createGeneralUser(CreateUserCommand command) {
        return User.builder()
                .userGuid(command.userGuid())
                .userRole(UserRole.USER)
                ...
                .build();
    }

    public static User of(...) { ... }  // Used for reconstruction from DB
}
```

---

## Immutable Value Objects

Use `record` for immutable value objects.

```java id="3m7qks"
// VO: use record
public record EmailUserCredential(
                String userGuid,
                String email,
                String password,
                UserRole userRole
        ) {}

// Command: record + @Builder
@Builder
public record SignupUserCommand(
        String email,
        String password,
        String username,
        ...
) {}

// Result: record + @Builder + static factory
@Builder
public record AuthResult(String accessToken, String refreshToken) {
    public static AuthResult of(String accessToken, String refreshToken) {
        return new AuthResult(accessToken, refreshToken);
    }
}
```

---

## Domain Field Immutability

Fields that must not change (e.g., identifiers, roles) should be declared as `final`.

```java id="5t2lcn"
public class User {
    private final String userGuid;     // Identifier: immutable
    private final UserRole userRole;   // Role: immutable

    private String username;           // Profile: mutable
    private boolean deleted;           // State: mutable
}
```

---

## Null Handling

* Do not return `null`. Use `Optional<T>` or throw exceptions.
* Initialize collection fields with empty collections instead of `null`.

```java id="2p8vra"
// Null safety in constructor
this.positions = Objects.requireNonNullElseGet(positions, HashSet::new);

// Return Optional from port
Optional<UserCredential> findEmailUserCredentialByEmail(String email);

// Guaranteed retrieval in adapter throws exception if not found
User findByUserGuid(String userGuid); // throws AdapterDataException if not found
```

---

## Streams and Collections

* Use immutable collections such as:

    * `Collectors.toUnmodifiableSet()`
    * `Set.copyOf()`
* Common pattern:

```java id="9d1xqs"
.stream()
.map(...)
.collect(Collectors.toUnmodifiableSet())
```

Use this pattern within domain and service layers.

---

## Comments

Do not write comments.
The code itself should clearly express intent.

If you feel the need to add a comment, first try improving method or variable naming.

---

## Import Rules

* Do not leave unused imports.
* Do not use wildcard imports (e.g., `import java.util.*`).
