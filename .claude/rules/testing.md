# Test Code Rules

## Test Classification and Package Structure

```text id="4n8xkr"
src/test/java/teamdevhub/devhub/
├── small/      # Unit tests: run without Spring Context
├── medium/     # Integration tests: use @SpringBootTest
├── large/      # E2E tests: full flow with TestRestTemplate
├── fake/       # Fake implementations (test doubles)
└── constant/   # Test constants (UserTestConstant)
```

---

## Test Writing Format

```java id="7m2qvd"
@Test
@DisplayName("유효하지_않은_리프레시_토큰으로_재발급_요청하면_예외가_발생한다")
void getUserForReissue_tokenNotSaved_throwsException() {
    // given
    String invalidToken = "invalid-refresh-token";
    tokenParseProvider.givenRefreshToken(
            invalidToken,
            TEST_USER_GUID_1
    );

    // when, then
    assertThatThrownBy(() ->
            userCredentialService.getUserForReissue(invalidToken))
            .isInstanceOf(BusinessRuleException.class)
            .hasMessageContaining(
                    ErrorCode.REFRESH_TOKEN_INVALID.getMessage()
            );
}
```

### Rules

* `@DisplayName` must be written in Korean
* Represent spaces using underscores (`_`)
* Always include GWT comments:

    * `// given`
    * `// when`
    * `// then`
    * `// when, then`
* Use AssertJ only:

    * `assertThat`
    * `assertThatThrownBy`
* Do not use JUnit `Assertions`

---

## Unit Test (`small`) Rules

* Run as pure Java without `@SpringBootTest`
* Do not use Mockito
* Replace dependencies with Fake implementations
* Create and inject dependencies manually in `@BeforeEach`

```java id="1p6xtr"
class UserCredentialServiceTest {

    private UserCredentialService userCredentialService;
    private FakeUserCredentialRepository userCredentialRepository;

    @BeforeEach
    void init() {
        userCredentialRepository =
                new FakeUserCredentialRepository();

        // initialize other fakes...

        userCredentialService =
                new UserCredentialService(
                        tokenParseProvider,
                        new FakeUuidIdentifierProvider(
                                TEST_USER_GUID_1
                        ),
                        noOpPasswordEncoder,
                        new FakeAuthenticatedUserResolver(),
                        userCredentialRepository,
                        refreshTokenRepository
                );
    }
}
```

---

## Integration Test (`medium`) Rules

* Use:

```text id="8v3mcf"
@SpringBootTest
@Transactional
```

* In `@BeforeEach`, isolate state using:

```java id="5k1qpd"
jpaXxxRepository.deleteAll();
```

* Adapter tests may inject JPA repositories directly using `@Autowired` to verify DB state

```java id="9n7xra"
@SpringBootTest
@Transactional
class UserCredentialAdapterTest {

    @Autowired
    private UserCredentialAdapter userCredentialAdapter;

    @Autowired
    private JpaEmailCredentialRepository
            jpaEmailCredentialRepository;

    @BeforeEach
    void init() {
        jpaEmailCredentialRepository.deleteAll();
    }
}
```

---

## Fake Implementation Rules

## Location

```text id="2r8ltk"
fake/pure/application/
├── port/
│   ├── in/usecase/{domain}/Fake{UseCase}.java
│   └── out/{domain}/Fake{Repository}.java
└── provider/Fake{Provider}.java
```

---

## Principles

* Use internal `Map<String, T>` storage
* May add `given*()` methods for test setup
* Do not return `null`
* If absent:

    * throw exception
    * or return `Optional.empty()`
* Fully implement the port interface
* Do not leave empty methods

```java id="6m4xzs"
public class FakeUserCredentialRepository
        implements UserCredentialRepository {

    private final Map<String, UserCredential>
            emailByGuidStore = new HashMap<>();

    private final Map<String, UserCredential>
            emailByEmailStore = new HashMap<>();

    private final Map<String, UserCredential>
            oauthStore = new HashMap<>();

    @Override
    public void saveEmailUserCredential(
            UserCredential credential,
            String encryptedPassword
    ) {
        emailByGuidStore.put(
                credential.userGuid(),
                credential
        );

        emailByEmailStore.put(
                credential.loginId(),
                credential
        );
    }

    @Override
    public Optional<UserCredential>
    findEmailUserCredentialByEmail(String email) {
        return Optional.ofNullable(
                emailByEmailStore.get(email)
        );
    }
}
```

---

## Test Constant Usage

Do not hardcode values already defined in `UserTestConstant`.

```java id="3t9vkp"
// ✅ Correct
import static teamdevhub.devhub.constant.UserTestConstant.*;

void someTest() {
    String email = TEST_EMAIL_1;
}

// ❌ Prohibited
void someTest() {
    String email = "user1@example.com";
}
```

If a new constant is needed, add it to `UserTestConstant` first.

---

## Guidance by Test Target

## Domain Tests

* Verify creation scenarios
* Verify state changes
* Verify exceptions on domain rule violations
* No Fake or Spring Context needed

---

## Service Tests

* Inject Fake repositories + Fake providers
* Cover both:

    * success cases
    * failure / exception cases

---

## Facade Tests

* Inject Fake UseCases
* Verify:

    * call order
    * orchestration behavior

---

## Adapter Tests

* Use:

```text id="7q2nwd"
@SpringBootTest
@Transactional
```

* Verify using real JPA repositories
* Save through adapter, then query directly through JPA repository

---

## Controller Tests

* Use:

```text id="1m8xrc"
@SpringBootTest
@MockitoBean
```

* Mock the Facade
* Verify:

    * HTTP status code
    * response body
    * headers
