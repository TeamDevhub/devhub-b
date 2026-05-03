# Things to Avoid

This list is based on patterns actually observed in this project codebase.

---

# Architecture Violations

## Do Not Inject Service / UseCase Directly into Controllers

```java
// ❌ Forbidden
@RestController
public class UserController {

    private final UserProfileService userProfileService;
    private final UserSignupUseCase userSignupUseCase;
}

// ✅ Correct
@RestController
public class UserController {

    private final UserSignupFacade userSignupFacade;
}
```

Controllers must inject **Facade only**.

---

## Do Not Inject JPA Repository Directly into Services

```java
// ❌ Forbidden
@Service
public class UserProfileService {

    private final JpaUserRepository jpaUserRepository;
}

// ✅ Correct
@Service
public class UserProfileService {

    private final UserRepository userRepository;
}
```

Services must depend on **port interfaces**, not outbound implementations.

---

## Do Not Put Spring / JPA Annotations on Domain Classes

```java
// ❌ Forbidden
@Entity
@Service
@Transactional
public class User { ... }

// ✅ Correct
@Getter
public class User { ... }
```

Domain classes must remain **pure Java**.

---

# Object Creation Violations

## Do Not Add Setters

Do not add `@Setter` or public `setXxx()` methods to domain entities.

```java
// ❌ Forbidden
user.setDeleted(true);
user.setUsername("newName");

// ✅ Correct
user.withdraw();
user.updateBasicProfile(updateCommand);
```

State changes must be expressed through meaningful domain methods.

---

## Do Not Call Domain Constructors Directly

```java
// ❌ Forbidden
User user = new User(...);
User user = User.builder()
        .userGuid("...")
        .build();

// ✅ Correct
User user = User.createGeneralUser(command);
User user = User.of(...);
```

Use static factory methods only.

---

# Test Code Violations

## Do Not Use Mockito in Unit Tests

```java
// ❌ Forbidden
@ExtendWith(MockitoExtension.class)
class UserSignupServiceTest {

    @Mock
    private UserRepository userRepository;
}

// ✅ Correct
class UserSignupServiceTest {

    private FakeUserRepository userRepository =
            new FakeUserRepository();
}
```

Use Fake implementations instead.

---

## Do Not Hardcode Values in Tests

```java
// ❌ Forbidden
String userGuid =
        "USER1a1b2c3d4e5f6g7h8i9j10k11l12";

String email =
        "user1@example.com";

// ✅ Correct
import static teamdevhub.devhub.constant.UserTestConstant.*;

String userGuid = TEST_USER_GUID_1;
String email = TEST_EMAIL_1;
```

Use shared constants.

---

## Do Not Return null from Fake Objects

```java
// ❌ Forbidden
@Override
public User findByUserGuid(String userGuid) {
    return null;
}

// ✅ Correct
@Override
public Optional<User> findByUserGuid(
        String userGuid
) {
    return Optional.ofNullable(
            store.get(userGuid)
    );
}
```

Use `Optional.empty()` or throw exceptions.

---

# Code Style Violations

## Do Not Use @Autowired

```java
// ❌ Forbidden
@Autowired
private UserRepository userRepository;

// ✅ Correct
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
}
```

Use constructor injection only.

---

## Do Not Hardcode Error Messages

```java
// ❌ Forbidden
throw new RuntimeException("User not found");

throw BusinessRuleException.of(
        ErrorCode.UNKNOWN_FAIL
);

// ✅ Correct
throw AdapterDataException.of(
        ErrorCode.USER_NOT_FOUND
);
```

Always use `ErrorCode`.

---

## Do Not Disable Code by Commenting It Out

```java
// ❌ Forbidden
// public void oldMethod() {
//     doSomething();
// }

// ✅ Correct
// Delete unused code completely
```

---

# Response Handling Violations

## Do Not Return Responses Without DataApiResponseDto

```java
// ❌ Forbidden
return ResponseEntity.ok("success");
return ResponseEntity.ok(user);

// ✅ Correct
return ResponseEntity.ok(
        DataApiResponseDto.successWithData(
                SuccessCode.SUCCESS,
                userDto
        )
);
```

All API responses must use the wrapper.

---

## Do Not Build Error Responses in Controllers

```java
// ❌ Forbidden
try {
    ...
} catch (Exception e) {
    return ResponseEntity.badRequest()
            .body(
                DataApiResponseDto.failureWithoutData(
                    ErrorCode.USER_NOT_FOUND
                )
            );
}

// ✅ Correct
// Throw exception
// GlobalExceptionHandler handles response
```

Controllers should contain only normal flow logic.

---

# Other Things to Avoid

* Do not use wildcard imports
  `import java.util.*`

* Do not leave empty methods

```java
@Override
public void doSomething() {}
```

Implement it properly or remove it from the interface.

* Do not leave TODOs unmanaged
  Resolve immediately or track via issue.

* Do not use `System.out.println()`
  Use SLF4J logging:

```java
log.info("message");
```
