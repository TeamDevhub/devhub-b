# CLAUDE.md

## 프로젝트 개요

DevHub는 개발자 프로젝트 매칭 및 팀 빌딩을 위한 백엔드 서비스다. Spring Boot 3.5.7 / Java 17 기반이며, 헥사고날 아키텍처(Ports & Adapters)를 채택하고 있다.

- **빌드 도구**: Gradle
- **인증 방식**: JWT Stateless (액세스 토큰 Bearer + 리프레시 토큰 HTTP-only 쿠키)
- **OAuth 제공자**: Google, GitHub, Kakao, Naver
- **데이터베이스**: H2 (개발/테스트), TCP 모드
- **주요 의존성**: Spring Security, Spring Data JPA, QueryDSL 5.1.0, Lombok

---

## 아키텍처 원칙

이 프로젝트는 헥사고날 아키텍처를 실제로 지키는 것을 최우선으로 한다. 규칙을 반드시 따라야 한다.

### 계층 의존 방향

```
Controller → Facade → UseCase(Port) ← Service(구현체)
                                     ← Repository(Port) ← Adapter(구현체)
                                                         ← JpaRepository
```

- **도메인 계층**은 Spring에 의존하지 않는다. 어노테이션도 없다.
- **포트(인터페이스)**는 `core` 패키지 안에 정의한다.
- **어댑터(구현체)**는 `outbound` 패키지 안에 정의한다.
- 컨트롤러는 `Facade`만 주입받는다. 서비스를 직접 주입받지 않는다.
- 서비스는 포트 인터페이스만 주입받는다. JPA 레포지토리를 직접 주입받지 않는다.

### Facade 역할

Facade는 여러 UseCase를 조합하는 오케스트레이터다. 복잡한 흐름(회원가입 → 자격증명 저장 → 약관 동의 → 인증 소비)을 하나의 트랜잭션으로 묶는 곳이다. 단순히 위임만 하는 메서드라면 Facade를 거칠 필요는 없지만, 두 개 이상의 UseCase가 협력하는 경우 반드시 Facade를 통한다.

---

## 디렉토리/레이어 역할

```
src/main/java/teamdevhub/devhub/
├── api/                      # REST 계층: Controller, DTO (Request/Response)
├── core/                     # 비즈니스 핵심
│   ├── {domain}/
│   │   ├── domain/           # 도메인 엔티티, VO, 도메인 예외
│   │   ├── application/      # UseCase 구현체 (Service)
│   │   └── port/
│   │       ├── in/
│   │       │   ├── command/  # Input 커맨드 객체
│   │       │   ├── facade/   # Facade 클래스
│   │       │   └── usecase/  # UseCase 인터페이스
│   │       └── out/          # Repository, Provider 인터페이스
├── outbound/                 # 어댑터 계층
│   ├── auth/
│   │   ├── adapter/          # JPA 어댑터 + Mapper + Entity
│   │   ├── infrastructure/   # JWT, OAuth, Password, Verification 구현체
│   │   └── persistence/      # JpaRepository 인터페이스
│   └── user/
│       ├── adapter/
│       └── persistence/
└── shared/                   # 공통: 설정, ErrorCode, SuccessCode, 유틸
```

---

## 코드 컨벤션

### 일반 규칙

- `@Autowired` 사용 금지. 생성자 주입만 사용하며, `@RequiredArgsConstructor` + `final` 필드로 선언한다.
- 클래스 레벨에 `@Transactional`을 선언한다. 메서드별로 개별 선언하지 않는다.
- `@Service`는 Application 계층 구현체에, `@Component`는 어댑터에 사용한다.
- 주석은 작성하지 않는다. 코드가 의도를 설명해야 한다.
- 불필요한 `public` 접근 제어자를 남발하지 않는다. 도메인 생성자는 `private`이다.

### 네이밍 규칙

| 대상 | 규칙 | 예시 |
|---|---|---|
| UseCase 인터페이스 | `{역할}UseCase` | `UserSignupUseCase` |
| 서비스 구현체 | `{역할}Service` | `UserSignupService` |
| 어댑터 | `{도메인}Adapter` | `UserCredentialAdapter` |
| JPA 레포지토리 | `Jpa{도메인}Repository` | `JpaEmailCredentialRepository` |
| 포트 인터페이스 | `{도메인}Repository` | `UserCredentialRepository` |
| Command 객체 | `{동사}{대상}Command` | `SignupUserCommand`, `UpdateProfileCommand` |
| Mapper 클래스 | `{도메인}Mapper` | `UserMapper`, `VerificationMapper` |
| 컨트롤러 | `{도메인}{역할}Controller` | `UserSignupController` |

### 레코드(record) vs 클래스

- **불변 값 객체(VO)**: `record` 사용. `UserPosition`, `UserSkill`, `EmailUserCredential`, `OAuthUserCredential`, `AuthResult` 등이 해당.
- **상태가 변하는 도메인 엔티티**: `@Getter` + `@Builder(access = PRIVATE)` 클래스 사용. `User`, `Verification` 등이 해당.
- 커맨드 객체는 `record` + `@Builder`로 선언한다.

---

## 도메인 계층 규칙

### 도메인 엔티티 설계

- `@Builder`는 `private`으로 선언한다. 외부에서 직접 빌더를 호출하지 못하게 막는다.
- 생성 로직은 반드시 **정적 팩토리 메서드**로 제공한다: `createAdminUser()`, `createGeneralUser()`, `issue()`, `of()` 등.
- 도메인 식별자(`userGuid` 등)는 `final`로 선언하여 불변을 보장한다.
- 비즈니스 상태 변경은 도메인 메서드로만 수행한다. `user.withdraw()`, `verification.confirm()` 등.
- 도메인 규칙 위반 시 `DomainRuleException.of(ErrorCode.XXX)`를 던진다.

### 도메인 검증 패턴

```java
// 올바른 방식: 도메인 메서드 내에서 규칙 위반을 던짐
public void withdraw() {
    if (this.deleted) {
        throw DomainRuleException.of(ErrorCode.ALREADY_DELETED);
    }
    this.deleted = true;
}

// 금지: 서비스 계층에서 도메인 상태를 직접 비교하여 예외를 던짐
// if (user.isDeleted()) { throw ... }
```

### VO 설계

- 값 객체는 불변 `record`로 선언한다.
- 생성 시 유효성 검증이 필요하면 `of()` 팩토리 메서드 안에서 처리한다.
- 도메인 변경 결과는 `UserPositionChangeResult`, `UserSkillChangeResult`처럼 별도 결과 record로 반환한다. 서비스에서 이 결과를 보고 후속 처리한다.

---

## 애플리케이션 계층 규칙

### Service 규칙

- 서비스는 포트 인터페이스(`UserRepository`, `UserCredentialRepository` 등)에만 의존한다.
- 도메인 객체를 생성하거나 가져와서 도메인 메서드를 호출하고, 결과를 저장소에 저장하는 순서를 따른다.
- 비즈니스 정책 위반 시 `BusinessRuleException.of(ErrorCode.XXX)`를 던진다.
- 도메인 규칙이 아닌 애플리케이션 수준의 중복 검사, 사전 조건 확인은 서비스에서 처리한다.

```java
// 올바른 예: 서비스가 포트만 사용
userCredentialRepository.findEmailUserCredentialByEmail(email)
    .ifPresent(c -> { throw BusinessRuleException.of(ErrorCode.UNKNOWN_FAIL); });
```

### Facade 규칙

- Facade는 `@Service` + `@Transactional`로 선언한다.
- 여러 UseCase 호출의 순서와 조합만 담당한다. 비즈니스 로직을 직접 작성하지 않는다.
- Facade 안에서 도메인 객체나 JPA를 직접 다루지 않는다.

### 응답 객체 반환

- 서비스는 도메인 객체나 Result record를 반환한다.
- 컨트롤러 DTO로의 변환은 컨트롤러 또는 DTO의 정적 팩토리 메서드에서 수행한다.

---

## 어댑터 계층 규칙

### 구조

- 어댑터는 `@Component`로 선언하고 포트 인터페이스를 구현한다.
- JPA 엔티티 ↔ 도메인 변환은 반드시 **정적 메서드만 가진 Mapper 클래스**를 통한다.
- 어댑터 메서드에서 데이터를 찾지 못하면 `AdapterDataException.of(ErrorCode.XXX)`를 던진다.
- `Optional`을 반환하는 메서드와 존재를 보장하는 메서드를 명확히 분리한다.

```java
// 존재를 보장: 없으면 예외
public User findByUserGuid(String userGuid) {
    return jpaUserRepository.findByUserGuid(userGuid)
            .orElseThrow(() -> AdapterDataException.of(ErrorCode.USER_NOT_FOUND));
}

// Optional 반환: 없을 수도 있음
public Optional<UserCredential> findEmailUserCredentialByEmail(String email) {
    return jpaEmailCredentialRepository.findByEmail(email).map(this::toUserCredential);
}
```

### Mapper 규칙

- 인스턴스를 만들지 않는다. 모든 메서드는 `public static`이다.
- `toEntity(DomainObject)`, `toDomain(EntityObject)` 네이밍을 따른다.
- Mapper가 비즈니스 로직을 포함하지 않도록 한다. 단순 필드 매핑만 수행한다.

---

## 영속성 계층 규칙

### JPA 엔티티 설계

- JPA 엔티티는 `outbound` 패키지 안에만 존재한다.
- 엔티티 클래스는 `@Entity`, `@Getter`, `@Builder`, `@NoArgsConstructor(access = PROTECTED)`를 선언한다.
- 기본키가 `String` 타입(GUID)인 경우 `@Id` + `@Column(length = 32, nullable = false, unique = true)`를 사용한다.
- `boolean` 필드는 `@Column` 레벨에서 기본값을 설정한다. 도메인 생성 시에도 명시적으로 초기화한다.
- Enum 필드는 `@Enumerated(EnumType.STRING)`을 사용한다.
- `@ManyToOne`, `@OneToMany` 등 연관관계 어노테이션은 최소화한다. 연관 데이터는 GUID로 참조한다.

### JpaRepository 네이밍

- `Jpa{도메인}Repository`로 명명하며 `JpaRepository<Entity, Long>`을 확장한다.
- 식별자가 `String`(GUID)인 테이블은 `JpaRepository<Entity, String>`을 사용한다.
- 스프링 데이터 쿼리 메서드 네이밍을 최대한 활용하며, 복잡한 쿼리는 QueryDSL 구현체를 별도로 만든다.

---

## 예외 처리 규칙

### 예외 계층 구조

세 가지 예외 타입이 있으며, 발생 위치에 따라 구분한다.

| 예외 | 발생 위치 | 의미 |
|---|---|---|
| `DomainRuleException` | 도메인 계층 | 도메인 규칙 위반 |
| `BusinessRuleException` | 애플리케이션 계층 | 비즈니스 정책 위반, 전제 조건 미충족 |
| `AdapterDataException` | 어댑터 계층 | 데이터 없음, DB 접근 실패 |

- 세 예외 모두 `of(ErrorCode errorCode)` 정적 팩토리 메서드로 생성한다.
- `ErrorCode`에 없는 에러 메시지를 하드코딩하지 않는다. 반드시 `ErrorCode` enum에 추가한다.

### API 응답 규칙

- 모든 응답은 `DataApiResponseDto<T>`로 감싼다.
- 성공 응답: `DataApiResponseDto.successWithData(SuccessCode, data)` 또는 `successWithoutData(SuccessCode)`
- 실패 응답: `GlobalExceptionHandler`가 예외를 잡아 `failureWithoutData(ErrorCode)`로 응답한다.
- 컨트롤러에서 직접 에러 응답을 만들지 않는다.

---

## 테스트 코드 규칙

### 테스트 분류

테스트는 세 가지 패키지로 분류한다.

| 패키지 | 종류 | 설명 |
|---|---|---|
| `small` | 단위 테스트 | 도메인, 서비스, Facade, Command 테스트. Spring Context 없이 실행. |
| `medium` | 통합 테스트 | 컨트롤러, JPA 어댑터 테스트. `@SpringBootTest` 사용. |
| `large` | E2E 테스트 | 전체 흐름 시나리오. `TestRestTemplate` 사용. |

### 단위 테스트 규칙

- Mockito를 쓰지 않는다. `fake/` 패키지의 Fake 구현체를 사용한다.
- `@BeforeEach`에서 의존성을 직접 생성하고 주입한다.
- 테스트 픽스처는 `UserTestConstant` 상수를 사용한다. 테스트 내부에 하드코딩하지 않는다.

### 통합 테스트 규칙

- `@SpringBootTest` + `@Transactional`을 선언한다.
- `@BeforeEach`에서 `jpaXxxRepository.deleteAll()`로 테스트 간 상태를 격리한다.
- 어댑터 테스트는 실제 JPA 레포지토리를 `@Autowired`로 주입받아 데이터를 검증한다.

### Fake 구현체 규칙

- Fake는 `fake/pure/application/port/` 구조 아래에 위치한다.
- `Map<String, T>`를 내부 저장소로 사용하며, 실제 포트 인터페이스를 구현한다.
- 테스트 준비를 위한 `given*()` 메서드나 public setter를 Fake에 추가할 수 있다.
- Fake는 `null`을 반환하지 않는다. 데이터가 없으면 예외를 던지거나 `Optional.empty()`를 반환한다.

### 테스트 작성 형식

```java
@Test
@DisplayName("인증코드가_만료되면_검증에_실패한다")  // 한국어, 언더스코어로 공백 표현
void confirm_expired_code_throwsException() {
    // given
    ...

    // when, then
    assertThatThrownBy(() -> ...)
            .isInstanceOf(DomainRuleException.class);
}
```

- `@DisplayName`은 한국어로 작성하며, 공백은 언더스코어(`_`)로 표현한다.
- AssertJ(`assertThat`, `assertThatThrownBy`)만 사용한다. JUnit 기본 `Assertions`는 쓰지 않는다.
- GWT 주석(`// given`, `// when`, `// then`)을 반드시 작성한다.

---

## 리팩토링 가이드

### 도메인 메서드 추출 기준

서비스 계층에서 도메인 상태를 읽고 조건 분기를 하고 있다면, 그 로직을 도메인 메서드로 옮기는 것을 검토한다.

```java
// 리팩토링 전 (서비스에 도메인 로직 누출)
if (!user.isDeleted() && user.getMannerDegree() < 0) { ... }

// 리팩토링 후 (도메인 메서드로 캡슐화)
user.validateActive();
```

### 새로운 도메인 개념 추가 기준

상태 변경 결과가 복잡한 경우 `XxxChangeResult` record를 만들어 반환한다. 서비스가 여러 도메인 상태를 직접 비교하지 않도록 결과 객체로 추상화한다.

---

## 지양해야 할 사항

- **setter 추가 금지**: 도메인 엔티티에 `@Setter`나 set 메서드를 추가하지 않는다. 상태 변경은 의미 있는 도메인 메서드로 표현한다.
- **JPA 엔티티를 도메인으로 직접 사용 금지**: `UserEntity`를 서비스 계층에 넘기지 않는다. 반드시 도메인 객체로 변환한다.
- **컨트롤러에서 서비스 직접 주입 금지**: 컨트롤러는 Facade만 주입받는다.
- **서비스에서 JPA 레포지토리 직접 주입 금지**: 포트 인터페이스를 통해서만 접근한다.
- **Mockito 사용 금지 (단위 테스트)**: Fake 구현체로 대체한다. `@SpringBootTest` 없는 테스트에서 `@MockitoBean`, `@Mock`을 사용하지 않는다.
- **`@Transactional(readOnly = true)` 남발 금지**: 명시적인 이유가 없으면 클래스 레벨 `@Transactional`로 통일한다.
- **하드코딩된 에러 메시지 금지**: 에러 메시지는 반드시 `ErrorCode` enum을 통한다.
- **ResponseEntity 반환 타입에 `?` 사용 금지**: 명시적인 제네릭 타입을 지정한다.
- **주석으로 코드 비활성화 금지**: 불필요해진 코드는 삭제한다. 주석으로 남기지 않는다.

---

## 신규 기능 추가 방법

새로운 도메인 기능을 추가하는 순서는 다음과 같다.

1. **도메인 모델 정의** (`core/{domain}/domain/`): 엔티티 또는 VO를 만든다. 정적 팩토리 메서드와 비즈니스 메서드를 작성한다.
2. **커맨드 정의** (`core/{domain}/port/in/command/`): 입력값을 담는 `record` 커맨드를 만든다.
3. **UseCase 인터페이스 정의** (`core/{domain}/port/in/usecase/`): 기능의 진입점이 될 인터페이스를 작성한다.
4. **출력 포트 정의** (`core/{domain}/port/out/`): 필요한 Repository나 Provider 인터페이스를 작성한다.
5. **서비스 구현** (`core/{domain}/application/`): UseCase를 구현한다. 포트 인터페이스에만 의존한다.
6. **Facade 작성** (`core/{domain}/port/in/facade/`): 여러 UseCase 조합이 필요하면 Facade를 작성한다.
7. **JPA 엔티티 및 레포지토리** (`outbound/{domain}/adapter/entity/`, `outbound/{domain}/persistence/`): 엔티티와 `JpaRepository`를 만든다.
8. **Mapper 작성** (`outbound/{domain}/adapter/mapper/`): 도메인 ↔ 엔티티 변환 정적 메서드를 작성한다.
9. **어댑터 구현** (`outbound/{domain}/adapter/`): 포트 인터페이스를 구현한다.
10. **컨트롤러 및 DTO** (`api/{domain}/`): Request/Response DTO와 컨트롤러를 작성한다.
11. **테스트 작성**: Fake 구현체 → 단위 테스트(small) → 어댑터 통합 테스트(medium) 순서로 작성한다.

---

## Claude가 이 프로젝트에 기여하는 방식

### 코드 생성 시 준수 사항

- 도메인 클래스 생성 시 `@Builder(access = AccessLevel.PRIVATE)` + 정적 팩토리 메서드 패턴을 따른다.
- 새 서비스를 만들 때 반드시 대응하는 UseCase 인터페이스를 먼저 만든다.
- 새 어댑터를 만들 때 Mapper 클래스를 분리하여 작성한다.
- 테스트 코드는 Fake 구현체를 활용한 단위 테스트를 기본으로 작성한다.
- 컨트롤러 응답은 반드시 `DataApiResponseDto<T>`로 감싸고, `SuccessCode`를 사용한다.

### 분석 요청 시

- 현재 코드와 다른 패턴을 제안할 때에는, 기존 코드베이스와 일치하지 않는다는 점을 명시한다.
- 주석처리된 코드(`changePassword`, `updatePassword` 등)는 미완성 기능으로 간주한다. 복원 여부는 확인 후 결정한다.
- `@admin/**` permitAll 보안 설정은 미완성 상태다. 수정 제안 시 이 점을 언급한다.

### 테스트 작성 시

- 기존 Fake 구현체(`FakeUserRepository`, `FakeTokenParseProvider` 등)를 최대한 재사용한다.
- 새로운 Fake가 필요하면 `fake/pure/application/port/out/` 또는 `fake/pure/application/provider/` 하위에 작성한다.
- `UserTestConstant`에 이미 정의된 상수를 새로 하드코딩하지 않는다.
- 통합 테스트는 `@SpringBootTest` + `@Transactional` + `@BeforeEach deleteAll()` 조합을 기본으로 한다.
