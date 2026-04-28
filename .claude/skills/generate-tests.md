# Skill: Test Code Generation

## Purpose

Defines the process for generating test code that follows this project's conventions and standards.

---

# Step 1. Analyze the Target

Read the target class and identify:

* List of public methods
* Success scenarios for each method
* Failure scenarios for each method (exception cases)
* External dependencies (port interfaces)

```bash id="n4t8xq"
# Read target class
Read src/main/java/teamdevhub/devhub/core/{domain}/application/service/{Class}.java
```

---

# Step 2. Decide Test Type

| Target Class                           | Test Type | Package                                    |
| -------------------------------------- | --------- | ------------------------------------------ |
| Domain Entity (`User`, `Verification`) | small     | `small/core/{domain}/domain/`              |
| Service (`XxxService`)                 | small     | `small/core/{domain}/application/service/` |
| Facade (`XxxFacade`)                   | small     | `small/core/{domain}/port/facade/`         |
| Adapter (`XxxAdapter`)                 | medium    | `medium/outbound/{domain}/adapter/`        |
| Controller (`XxxController`)           | medium    | `medium/api/{domain}/controller/`          |
| DTO Validation (`XxxRequestDto`)       | medium    | `medium/api/{domain}/model/request/`       |

---

# Step 3. Check Required Fakes and Create If Needed

Verify whether required Fake implementations already exist under `fake/`.

```bash id="k7p3vm"
# Search existing Fake classes
Glob src/test/java/teamdevhub/devhub/fake/**/*.java
```

If missing, create one first using this template:

```java id="r2x8nc"
package teamdevhub.devhub.fake.pure.application.port.out.{domain};

public class Fake{PortName}
        implements {PortInterface} {

    private final Map<String, T> store =
            new HashMap<>();

    // given*() method for test setup
    public void given{Data}({Type} value) {
        store.put(key, value);
    }

    @Override
    public Optional<T> findBy{X}(String x) {
        return Optional.ofNullable(store.get(x));
    }
}
```

---

# Step 4. Write Test Class

# Unit Test Template

```java id="m5q1zt"
package teamdevhub.devhub.small.core.{domain}.application.service;

import static org.assertj.core.api.Assertions.*;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class {ClassName}Test {

    private {TargetClass} target;

    // Fake dependencies
    private Fake{Port1} fake{Port1};

    @BeforeEach
    void init() {
        fake{Port1} = new Fake{Port1}();

        target = new {TargetClass}(
                fake{Port1}
        );
    }

    @Test
    @DisplayName("{시나리오_한국어_설명}")
    void {methodName}_{scenario}() {

        // given
        {설정 코드}

        // when
        {실행 코드}

        // then
        assertThat({결과}).{검증};
    }

    @Test
    @DisplayName("{실패_시나리오_한국어_설명}")
    void {methodName}_{scenario}_throwsException() {

        // given
        {설정 코드}

        // when, then
        assertThatThrownBy(() ->
                {실행 코드})
                .isInstanceOf(
                        {ExceptionClass}.class
                )
                .hasMessageContaining(
                        {ErrorCode}.getMessage()
                );
    }
}
```

---

# Integration Test Template (Adapter)

```java id="v8t4kp"
@SpringBootTest
@Transactional
class {AdapterClass}Test {

    @Autowired
    private {AdapterClass} adapter;

    @Autowired
    private Jpa{Domain}Repository jpaRepository;

    @BeforeEach
    void init() {
        jpaRepository.deleteAll();
    }

    @Test
    @DisplayName("{시나리오}")
    void {method}_{scenario}() {

        // given:
        // save directly via JPA
        // or call adapter.save()

        // when:
        // call adapter query/update method

        // then:
        // verify DB state via JPA repository
    }
}
```

---

# Step 5. Scenario Checklist

For every public method, confirm these scenarios are covered:

* [ ] Happy path
* [ ] Missing required input (`null`, empty)
* [ ] Duplicate data handling
* [ ] Data not found
* [ ] Domain rule violation

---

# Step 6. Compile Validation

```bash id="p9x2rm"
./gradlew compileTestJava
```

If there are no compilation errors, the task is complete.
