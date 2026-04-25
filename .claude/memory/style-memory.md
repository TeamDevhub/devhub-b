# 코딩 스타일 메모리

## 클래스 설계 원칙

### 도메인 엔티티 (`core/{domain}/domain/`)
- `@Builder` 는 `private` — 외부에서 `new` 불가
- 정적 팩토리 메서드로만 생성: `createGeneralUser()`, `createOauthUser()`, `of()`
- 상태 변경은 도메인 메서드로만 (`withdraw()`, `changePositions()`)
- 복잡한 변경 결과는 inner `record`로 반환: `PositionChangeResult`, `SkillChangeResult`
- Spring 어노테이션 금지 (`@Service`, `@Component` 등)
- setter 금지

### 서비스 (`core/{domain}/application/service/`)
- `@Service` + `@RequiredArgsConstructor` + `@Transactional` (클래스 레벨)
- 포트 인터페이스만 주입 (JPA 직접 주입 금지)
- `private final` 필드만 사용

### Facade (`core/{domain}/port/in/facade/`)
- `@Service` + `@RequiredArgsConstructor` + `@Transactional`
- 여러 UseCase를 조합하여 단일 트랜잭션으로 처리
- 조합하는 UseCase가 2개 이상일 때 Facade 사용

### 컨트롤러 (`api/web/controller/`)
- Facade 또는 단일 UseCase만 주입 (Service 직접 주입 금지)
- `@RestController` + `@RequiredArgsConstructor`
- 반환: 항상 `DataApiResponseDto<T>`

---

## 값 객체 / 커맨드 (`record` 사용 기준)

`record` 사용:
- 불변 값 객체 (VO): `EmailUserCredential`, `OAuthUserCredential`, `AuthResult`
- 커맨드 객체: `SignupUserCommand`, `LoginCommand`, `UpdateBasicProfileCommand`
- 변경 결과: `UserPositionChangeResult`, `UserSkillChangeResult`

`class` 사용:
- 내부 상태 변화 필요, 컬렉션 조작, JPA 엔티티

---

## 예외 처리 계층

| 계층 | 예외 클래스 | 생성 방법 |
|---|---|---|
| 도메인 | `DomainRuleException` | `DomainRuleException.of(ErrorCode.XXX)` |
| 서비스/Facade | `BusinessRuleException` | `BusinessRuleException.of(ErrorCode.XXX)` |
| 어댑터 | `AdapterDataException` | `AdapterDataException.of(ErrorCode.XXX)` |

에러 메시지 하드코딩 금지. 반드시 `ErrorCode` enum 사용.

---

## 테스트 스타일

- 클래스명: `{대상클래스}Test`
- 메서드명: `{메서드명}_{시나리오}` (예: `signupEmailUser_success`)
- `@DisplayName`: 한국어, 언더스코어로 띄어쓰기 표현
- GWT 주석: `// given`, `// when`, `// then` 항상 작성
- Fake 구현체: `Fake{PortName}` (`FakeUserRepository`, `FakeRefreshTokenRepository`)
- Mockito 사용 금지

---

## 응답 형식

```java
// 데이터 있는 성공
DataApiResponseDto.successWithData(result)

// 데이터 없는 성공
DataApiResponseDto.successWithoutData()

// 실패 (GlobalExceptionHandler에서 처리)
DataApiResponseDto.failureWithoutData(errorCode)
```

---

## 어노테이션 컨벤션

- `@Autowired` 금지 → `@RequiredArgsConstructor` + `final` 사용
- `@Enumerated(EnumType.STRING)` — 모든 enum 컬럼
- `@Transactional` — 클래스 레벨에 선언, 메서드별 오버라이드 금지
- `@SpringBootTest` + `@Transactional` — 통합 테스트 표준 구성
