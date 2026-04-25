# 코드 컨벤션

## 의존성 주입

`@Autowired`는 사용하지 않는다. 모든 의존성은 생성자 주입으로 처리한다.

```java
// 올바른 방식
@Service
@RequiredArgsConstructor
public class UserProfileService implements UserProfileUseCase {
    private final UserRepository userRepository;           // final + @RequiredArgsConstructor
    private final UserPositionRepository userPositionRepository;
}
```

## 트랜잭션

- `@Transactional`은 클래스 레벨에 선언한다. 메서드별로 따로 붙이지 않는다.
- `readOnly = true`는 명시적인 성능상 이유가 있을 때만 사용한다.

```java
@Service
@Transactional          // ✅ 클래스 레벨
@RequiredArgsConstructor
public class UserSignupService implements UserSignupUseCase { ... }
```

## 도메인 객체 생성 패턴

도메인 클래스의 생성자는 `private`이다. 외부에서는 반드시 정적 팩토리 메서드를 사용한다.

```java
@Getter
public class User {

    @Builder                          // 빌더는 클래스 내부에서만 사용
    private User(...) { ... }         // private 생성자

    // 정적 팩토리 메서드로만 생성
    public static User createGeneralUser(CreateUserCommand command) {
        return User.builder()
                .userGuid(command.userGuid())
                .userRole(UserRole.USER)
                ...
                .build();
    }

    public static User of(...) { ... }  // DB에서 복원할 때 사용
}
```

## 불변 값 객체

상태가 변하지 않는 값 객체는 `record`로 선언한다.

```java
// VO: record 사용
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

// Result: record + @Builder + 정적 팩토리
@Builder
public record AuthResult(String accessToken, String refreshToken) {
    public static AuthResult of(String accessToken, String refreshToken) {
        return new AuthResult(accessToken, refreshToken);
    }
}
```

## 도메인 필드 불변성

도메인 엔티티에서 식별자와 역할처럼 변하지 않아야 하는 필드는 `final`로 선언한다.

```java
public class User {
    private final String userGuid;     // 식별자: 불변
    private final UserRole userRole;   // 역할: 불변

    private String username;           // 프로필: 변경 가능
    private boolean deleted;           // 상태: 변경 가능
}
```

## Null 처리

- `null` 반환 대신 `Optional<T>` 또는 예외를 반환한다.
- 컬렉션 필드는 `null` 대신 빈 컬렉션으로 초기화한다.

```java
// 도메인 생성자에서 null 방어
this.positions = Objects.requireNonNullElseGet(positions, HashSet::new);

// 포트에서 Optional 반환
Optional<UserCredential> findEmailUserCredentialByEmail(String email);

// 어댑터에서 존재 보장 조회는 예외
User findByUserGuid(String userGuid); // 없으면 AdapterDataException
```

## Stream과 컬렉션

- `Collectors.toUnmodifiableSet()`이나 `Set.copyOf()`를 적극 활용하여 불변 컬렉션을 반환한다.
- `.stream().map().collect(Collectors.toUnmodifiableSet())` 패턴을 도메인/서비스 내에서 사용한다.

## 주석

주석은 작성하지 않는다. 코드 자체가 의도를 설명해야 한다. 주석이 필요하다는 느낌이 들면, 메서드나 변수 이름을 더 명확하게 바꾸는 것을 먼저 시도한다.

## import 순서

- 사용하지 않는 import는 남기지 않는다.
- 와일드카드 import(`import java.util.*`)는 사용하지 않는다.
