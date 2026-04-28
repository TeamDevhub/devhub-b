# Skill: Safe Refactoring

## Purpose

Defines the process for improving code according to Hexagonal Architecture principles **while preserving existing behavior**.

---

# Step 1. Identify Target and List Problems

Read the target file and check the following:

* Is domain logic leaking into the service layer?
* Are there methods with 3 or more parameters?
* Does a service combine multiple domain UseCases?
* Are there invalid layer dependencies?

```bash id="f1x8kr"
Read src/main/java/teamdevhub/devhub/core/{domain}/application/service/{Class}.java
```

---

# Step 2. Check Impact Scope

Search all files referencing the target.

```bash id="k7m2vd"
Grep "{ClassName}" src/main/java --include="*.java"
Grep "{ClassName}" src/test/java --include="*.java"
```

---

# Step 3. Verify Existing Tests

Check whether tests already cover the behavior before refactoring.

```bash id="n4q7tp"
Glob src/test/java/teamdevhub/devhub/small/core/{domain}/**/*.java
```

If tests do not exist, create them first using the **/generate-tests** skill.

---

# Step 4. Select Refactoring Pattern

| Problem                       | Pattern                         |
| ----------------------------- | ------------------------------- |
| Domain logic inside service   | Extract domain method           |
| 3+ parameters                 | Introduce command `record`      |
| Multiple domain orchestration | Split into Facade               |
| Complex state change results  | Introduce `ChangeResult` record |
| Invalid layer dependency      | Invert through port interface   |

---

# Step 5. Apply Changes Incrementally

Refactor **one goal at a time**.

---

# Example: Extract Domain Method

### Before (domain logic leaked into service)

```java id="u8r3pk"
// UserService.java
public void withdraw(String userGuid) {

    User user = userRepository
            .findByGuid(userGuid)
            .orElseThrow(...);

    if (user.isDeleted()) {
        throw new BusinessRuleException(
                ErrorCode.ALREADY_WITHDRAWN
        );
    }

    user.setDeleted(true);

    userRepository.save(user);
}
```

### After (moved into domain)

```java id="m5x9cq"
// User.java
public void withdraw() {

    if (this.deleted) {
        throw new DomainRuleException(
                ErrorCode.ALREADY_WITHDRAWN
        );
    }

    this.deleted = true;
}

// UserService.java
public void withdraw(String userGuid) {

    User user = userRepository
            .findByGuid(userGuid)
            .orElseThrow(...);

    user.withdraw();

    userRepository.save(user);
}
```

---

# Example: Introduce Command Record

### Before

```java id="q1n6vd"
public void signupEmailUser(
        String email,
        String password,
        String name,
        String userGuid
) { ... }
```

### After

```java id="c8t2mr"
public record SignupEmailUserCommand(
        String email,
        String password,
        String name,
        String userGuid
) {}

public void signupEmailUser(
        SignupEmailUserCommand command
) { ... }
```

---

# Example: Introduce ChangeResult

```java id="z3p7kx"
// User.java
public record PositionChangeResult(
        List<String> added,
        List<String> removed
) {}

public PositionChangeResult changePositions(
        List<String> newPositions
) {
    List<String> added =
            newPositions.stream()
                    .filter(p ->
                            !this.positions.contains(p))
                    .toList();

    List<String> removed =
            this.positions.stream()
                    .filter(p ->
                            !newPositions.contains(p))
                    .toList();

    this.positions =
            new ArrayList<>(newPositions);

    return new PositionChangeResult(
            added,
            removed
    );
}
```

---

# Step 6. Never Change These

Do **not** modify:

* Port interface method signatures
* Test assertions (`assertThat(...)`)
* `ErrorCode` enum values or messages
* `DataApiResponseDto` structure

---

# Step 7. Forbidden Refactoring Patterns

Do **not** introduce:

* `setXxx()` methods
* Spring annotations on domain classes
  (`@Service`, `@Component`, etc.)
* `Mockito.mock()` or `@MockBean`
* Disabled code via `// TODO` or commented-out blocks

---

# Step 8. Compile and Test Validation

```bash id="r6v2pt"
./gradlew compileJava
./gradlew test --tests "teamdevhub.devhub.small.core.{domain}.*"
```

---

# Step 9. Refactoring Report Format

```text id="w9m4xd"
## Refactoring Summary

### Changed Files
- path/to/File.java — reason for change

### Applied Patterns
- Extract Domain Method:
  XxxService.someLogic()
  → Xxx.someMethod()

### Preserved Behavior
- All existing X tests passed

### Notes
- (Items requiring follow-up work)
```
