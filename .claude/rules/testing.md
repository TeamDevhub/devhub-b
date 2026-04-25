# 테스트 코드 규칙

## 테스트 분류 및 패키지 구조

```
src/test/java/teamdevhub/devhub/
├── small/      # 단위 테스트: Spring Context 없이 실행
├── medium/     # 통합 테스트: @SpringBootTest 사용
├── large/      # E2E 테스트: TestRestTemplate 전체 흐름
├── fake/       # Fake 구현체 (테스트 더블)
└── constant/   # 테스트 상수 (UserTestConstant)
```

## 테스트 작성 형식

```java
@Test
@DisplayName("유효하지_않은_리프레시_토큰으로_재발급_요청하면_예외가_발생한다")
void getUserForReissue_tokenNotSaved_throwsException() {
    // given
    String invalidToken = "invalid-refresh-token";
    tokenParseProvider.givenRefreshToken(invalidToken, TEST_USER_GUID_1);

    // when, then
    assertThatThrownBy(() -> userCredentialService.getUserForReissue(invalidToken))
            .isInstanceOf(BusinessRuleException.class)
            .hasMessageContaining(ErrorCode.REFRESH_TOKEN_INVALID.getMessage());
}
```

- `@DisplayName`은 한국어로 작성하고, 공백은 언더스코어(`_`)로 표현한다.
- GWT 주석(`// given`, `// when`, `// then`, `// when, then`)을 항상 작성한다.
- AssertJ(`assertThat`, `assertThatThrownBy`)만 사용한다. JUnit의 `Assertions`는 쓰지 않는다.

## 단위 테스트 (small) 규칙

- `@SpringBootTest` 없이 순수 Java로 실행한다.
- Mockito를 사용하지 않는다. Fake 구현체로 대체한다.
- `@BeforeEach`에서 의존성을 직접 생성하고 주입한다.

```java
class UserCredentialServiceTest {

    private UserCredentialService userCredentialService;
    private FakeUserCredentialRepository userCredentialRepository;

    @BeforeEach
    void init() {
        userCredentialRepository = new FakeUserCredentialRepository();
        // ... 다른 Fake들 초기화

        userCredentialService = new UserCredentialService(
                tokenParseProvider,
                new FakeUuidIdentifierProvider(TEST_USER_GUID_1),
                noOpPasswordEncoder,
                new FakeAuthenticatedUserResolver(),
                userCredentialRepository,
                refreshTokenRepository
        );
    }
}
```

## 통합 테스트 (medium) 규칙

- `@SpringBootTest` + `@Transactional`을 선언한다.
- `@BeforeEach`에서 `jpaXxxRepository.deleteAll()`로 테스트 간 상태를 격리한다.
- 어댑터 테스트는 JPA 레포지토리를 직접 `@Autowired`로 주입받아 DB 상태를 검증한다.

```java
@SpringBootTest
@Transactional
class UserCredentialAdapterTest {

    @Autowired
    private UserCredentialAdapter userCredentialAdapter;

    @Autowired
    private JpaEmailCredentialRepository jpaEmailCredentialRepository;

    @BeforeEach
    void init() {
        jpaEmailCredentialRepository.deleteAll();
    }
}
```

## Fake 구현체 작성 규칙

### 위치

```
fake/pure/application/
├── port/
│   ├── in/usecase/{domain}/Fake{UseCase}.java
│   └── out/{domain}/Fake{Repository}.java
└── provider/Fake{Provider}.java
```

### 작성 원칙

- 내부 저장소는 `Map<String, T>`를 사용한다.
- 테스트 준비를 위한 `given*()` 메서드를 추가할 수 있다.
- `null`을 반환하지 않는다. 없으면 예외를 던지거나 `Optional.empty()`를 반환한다.
- 포트 인터페이스를 완전히 구현한다. 빈 메서드를 남기지 않는다.

```java
public class FakeUserCredentialRepository implements UserCredentialRepository {

    private final Map<String, UserCredential> emailByGuidStore = new HashMap<>();
    private final Map<String, UserCredential> emailByEmailStore = new HashMap<>();
    private final Map<String, UserCredential> oauthStore = new HashMap<>();

    @Override
    public void saveEmailUserCredential(UserCredential credential, String encryptedPassword) {
        emailByGuidStore.put(credential.userGuid(), credential);
        emailByEmailStore.put(credential.loginId(), credential);
    }

    @Override
    public Optional<UserCredential> findEmailUserCredentialByEmail(String email) {
        return Optional.ofNullable(emailByEmailStore.get(email));
    }
}
```

## 테스트 상수 사용

`UserTestConstant`에 이미 정의된 상수를 테스트 내에서 하드코딩하지 않는다.

```java
// ✅ 올바른 방식
import static teamdevhub.devhub.constant.UserTestConstant.*;

void someTest() {
    String email = TEST_EMAIL_1;   // 상수 사용
}

// ❌ 금지
void someTest() {
    String email = "user1@example.com";  // 하드코딩
}
```

새로운 상수가 필요하면 `UserTestConstant`에 먼저 추가한다.

## 테스트 대상별 가이드

### 도메인 테스트
- 생성 시나리오, 상태 변경, 도메인 규칙 위반 시 예외 발생을 검증한다.
- Fake나 Spring Context 불필요.

### 서비스 테스트
- Fake Repository + Fake Provider를 주입해 로직을 검증한다.
- 성공 케이스와 실패(예외) 케이스를 모두 작성한다.

### Facade 테스트
- Fake UseCase들을 주입해 호출 순서와 조합을 검증한다.

### 어댑터 테스트
- `@SpringBootTest` + `@Transactional` + 실제 JPA 레포지토리로 검증한다.
- 저장 후 JPA 레포지토리로 직접 조회하여 DB 상태를 확인한다.

### 컨트롤러 테스트
- `@SpringBootTest` + `@MockitoBean`으로 Facade를 Mock 처리한다.
- HTTP 상태 코드, 응답 바디, 헤더를 검증한다.
