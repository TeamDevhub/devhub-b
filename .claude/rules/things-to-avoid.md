# 지양해야 할 사항

이 목록은 이 프로젝트 코드베이스에서 실제로 관찰된 패턴을 기반으로 작성됐다.

## 아키텍처 위반

### 컨트롤러에서 Service/UseCase 직접 주입 금지

```java
// ❌ 금지
@RestController
public class UserController {
    private final UserProfileService userProfileService;  // 구현체 직접 주입
    private final UserSignupUseCase userSignupUseCase;    // UseCase 직접 주입
}

// ✅ 올바른 방식
@RestController
public class UserController {
    private final UserSignupFacade userSignupFacade;      // Facade만 주입
}
```

### 서비스에서 JPA Repository 직접 주입 금지

```java
// ❌ 금지
@Service
public class UserProfileService {
    private final JpaUserRepository jpaUserRepository;  // outbound 직접 참조
}

// ✅ 올바른 방식
@Service
public class UserProfileService {
    private final UserRepository userRepository;        // 포트 인터페이스 참조
}
```

### 도메인 클래스에 Spring/JPA 어노테이션 금지

```java
// ❌ 금지
@Entity                      // JPA
@Service                     // Spring
@Transactional               // Spring
public class User { ... }

// ✅ 도메인 클래스는 순수 Java
@Getter
public class User { ... }
```

## 객체 생성 패턴 위반

### setter 추가 금지

도메인 엔티티에 `@Setter`나 set 메서드를 추가하지 않는다. 상태 변경은 의미 있는 도메인 메서드로 표현한다.

```java
// ❌ 금지
user.setDeleted(true);
user.setUsername("newName");

// ✅ 올바른 방식
user.withdraw();
user.updateBasicProfile(updateCommand);
```

### 도메인 생성자 직접 호출 금지

```java
// ❌ 금지 (빌더가 public이어도 직접 호출 지양)
User user = new User(...);
User user = User.builder().userGuid("...").build();  // 빌더가 private이라 불가

// ✅ 정적 팩토리 메서드 사용
User user = User.createGeneralUser(command);
User user = User.of(...);
```

## 테스트 코드 위반

### 단위 테스트에서 Mockito 사용 금지

```java
// ❌ 금지 (단위 테스트에서)
@ExtendWith(MockitoExtension.class)
class UserSignupServiceTest {
    @Mock
    private UserRepository userRepository;
}

// ✅ Fake 사용
class UserSignupServiceTest {
    private FakeUserRepository userRepository = new FakeUserRepository();
}
```

### 테스트 내 값 하드코딩 금지

```java
// ❌ 금지
String userGuid = "USER1a1b2c3d4e5f6g7h8i9j10k11l12";
String email = "user1@example.com";

// ✅ 상수 사용
import static teamdevhub.devhub.constant.UserTestConstant.*;
String userGuid = TEST_USER_GUID_1;
String email = TEST_EMAIL_1;
```

### Fake에서 null 반환 금지

```java
// ❌ 금지
@Override
public User findByUserGuid(String userGuid) {
    return null;  // 없을 때 null 반환
}

// ✅ Optional 또는 예외
@Override
public Optional<User> findByUserGuid(String userGuid) {
    return Optional.ofNullable(store.get(userGuid));
}
```

## 코드 스타일 위반

### @Autowired 사용 금지

```java
// ❌ 금지
@Autowired
private UserRepository userRepository;

// ✅ 생성자 주입
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
}
```

### 에러 메시지 하드코딩 금지

```java
// ❌ 금지
throw new RuntimeException("사용자를 찾을 수 없습니다");
throw BusinessRuleException.of(ErrorCode.UNKNOWN_FAIL);  // 구체적인 코드가 있다면

// ✅ ErrorCode 사용
throw AdapterDataException.of(ErrorCode.USER_NOT_FOUND);
```

### 주석으로 코드 비활성화 금지

```java
// ❌ 금지: 사용하지 않는 코드를 주석으로 남기기
// public void oldMethod() {
//     doSomething();
// }

// ✅ 불필요하면 삭제
```

## 응답 처리 위반

### DataApiResponseDto 없이 응답 금지

```java
// ❌ 금지
return ResponseEntity.ok("success");
return ResponseEntity.ok(user);

// ✅ 반드시 래퍼 사용
return ResponseEntity.ok(
    DataApiResponseDto.successWithData(SuccessCode.SUCCESS, userDto)
);
```

### 컨트롤러에서 에러 응답 직접 생성 금지

```java
// ❌ 금지
try {
    ...
} catch (Exception e) {
    return ResponseEntity.badRequest()
        .body(DataApiResponseDto.failureWithoutData(ErrorCode.USER_NOT_FOUND));
}

// ✅ 예외를 던지면 GlobalExceptionHandler가 처리
// 컨트롤러는 정상 흐름만 작성
```

## 기타

- **와일드카드 import 금지**: `import java.util.*`
- **빈 메서드 남기기 금지**: `@Override public void doSomething() {}` — 구현하거나 해당 메서드를 인터페이스에서 제거
- **TODO 방치 금지**: 작성 즉시 처리하거나 이슈로 관리
- **System.out.println 사용 금지**: `log.info()` 등 SLF4J 로거 사용
