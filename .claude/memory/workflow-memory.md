# Workflow Memory

## New Feature Development Order

When adding a new domain feature, always follow this sequence:

```text id="w91fkr"
1. Create domain entity / VO          (core/{domain}/domain/)
2. Create port interfaces            (core/{domain}/port/in/usecase/, port/out/)
3. Create service                    (core/{domain}/application/service/)
4. Create facade (if needed)         (core/{domain}/port/in/facade/)
5. Create JPA entity + repository    (outbound/persistence/)
6. Create adapter                    (outbound/{domain}/adapter/)
7. Create controller + DTO           (api/web/controller/, model/)
8. Write tests                       (small/ + medium/)
```

---

## Fake Implementation Management

Fake implementations should provide in-memory `Map`-based versions of port interfaces.

**Location:**

```text id="8v2mqt"
src/test/java/teamdevhub/devhub/fake/pure/application/port/out/{domain}/
```

**Naming Convention:**

* `Fake{PortInterfaceName}`
  Examples:
* `FakeUserRepository`
* `FakeRefreshTokenRepository`

**Rules:**

* Use `given{Data}()` methods for preloading test data
* Use `HashMap` as the backing store
* Simulate exceptions through configurable `given*()` methods

---

## Branching and Commit Strategy

```text id="m3xk4z"
feature/{feature-name} → dev → main
```

**Commit Message Format:**

```text id="v7q2np"
[feat] Feature description
[fix] Bug fix description
[test] Test code changes
[refactor] Refactoring description
[docs] Documentation changes
```

---

## Build and Validation

| Stage             | Command                               | Timing                         |
| ----------------- | ------------------------------------- | ------------------------------ |
| Quick Validation  | `./gradlew compileJava`               | Immediately after code changes |
| Test Compilation  | `./gradlew compileTestJava`           | After Fake/test changes        |
| Unit Tests        | `./gradlew test --tests "*.small.*"`  | After service/domain changes   |
| Integration Tests | `./gradlew test --tests "*.medium.*"` | After adapter changes          |
| Full Build        | `./gradlew build`                     | Final validation before PR     |

If QueryDSL Q-class regeneration is needed:

```text id="n4rj6a"
./gradlew clean compileJava
```

---

## Common Issues and Fixes

### Build Failure After Adding Port Methods

When a new method is added to a port interface, it must also be implemented in:

* Fake implementations
* Adapters

### Integration Test Data Contamination

If `jpaRepository.deleteAll()` is missing in `@BeforeEach`, tests may interfere with each other.

### H2 Schema Mismatch

Check the test environment `application.yml`:

```text id="c8tp1s"
spring.jpa.hibernate.ddl-auto=create-drop
```

---

## Agent Usage Guide

| Situation                      | Recommended Agent / Skill                                |
| ------------------------------ | -------------------------------------------------------- |
| Writing tests for new features | `agents/test-agent.md` + `skills/generate-tests.md`      |
| Improving code structure       | `agents/refactor-agent.md` + `skills/refactor-safely.md` |
| Updating documentation         | `agents/readme-agent.md` + `skills/write-readme.md`      |
| Notion synchronization         | `agents/notion-agent.md` + `skills/notion-sync.md`       |
| Diagnosing build failures      | `agents/build-agent.md` + `skills/build-verify.md`       |
