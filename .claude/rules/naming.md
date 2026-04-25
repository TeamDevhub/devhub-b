# 네이밍 규칙

## 클래스 네이밍

| 종류 | 패턴 | 예시 |
|---|---|---|
| 도메인 엔티티 | `{도메인}` | `User`, `Verification` |
| UseCase 인터페이스 | `{역할}UseCase` | `UserSignupUseCase`, `AuthenticationUseCase` |
| Service 구현체 | `{역할}Service` | `UserSignupService`, `UserCredentialService` |
| Facade | `{도메인}{역할}Facade` | `UserSignupFacade`, `AuthFacade`, `OauthAuthFacade` |
| 출력 포트 (Repository) | `{도메인}Repository` | `UserCredentialRepository`, `VerificationRepository` |
| 출력 포트 (Provider) | `{기능}Provider` | `TokenIssueProvider`, `EncodedPasswordProvider` |
| JPA Repository | `Jpa{도메인}Repository` | `JpaEmailCredentialRepository`, `JpaUserRepository` |
| 어댑터 | `{도메인}Adapter` | `UserCredentialAdapter`, `EmailUserCredentialAdapter` |
| Mapper | `{도메인}Mapper` | `UserMapper`, `VerificationMapper` |
| JPA 엔티티 | `{도메인}Entity` | `UserEntity`, `EmailCredentialEntity` |
| 컨트롤러 | `{도메인}{역할}Controller` | `UserSignupController`, `AuthController` |
| Request DTO | `{동작}{대상}RequestDto` | `SignupRequestDto`, `LoginRequestDto` |
| Response DTO | `{대상}ResponseDto` | `TokenResponseDto`, `UserDetailResponseDto` |
| 커맨드 객체 | `{동사}{대상}Command` | `SignupUserCommand`, `UpdateProfileCommand`, `LoginCommand` |
| Result 객체 | `{동사}{대상}Result` or `{대상}Result` | `AuthResult`, `OauthAuthResult`, `OauthUserResult` |
| 변경 결과 객체 | `{대상}ChangeResult` | `UserPositionChangeResult`, `UserSkillChangeResult` |

## 메서드 네이밍

### 도메인 엔티티 메서드

- **생성**: `create{역할}()` — `createAdminUser()`, `createGeneralUser()`, `issue()`
- **복원**: `of(...)` — 기존 데이터를 도메인 객체로 복원할 때
- **상태 변경**: 의미 있는 동사 — `withdraw()`, `confirm()`, `updateBasicProfile()`, `changePositions()`
- **검증**: `assertXxx()` — `assertValid()`

### 서비스/Facade 메서드

- 생성: `save{대상}()`, `signup()`, `create{대상}()`
- 조회: `get{대상}()`, `find{대상}()`
- 수정: `update{대상}()`
- 삭제: `delete{대상}()`, `revoke()`, `consume()`
- 인증: `authenticate()`, `login()`, `logout()`

### 어댑터/레포지토리 메서드

- JPA 쿼리 메서드 네이밍 규칙을 그대로 따른다.
- 존재를 보장하는 조회: `findBy{조건}()` (없으면 예외)
- Optional 반환: `findBy{조건}()` (반환 타입으로 구분)
- 저장: `save{대상}()`, `saveAll()`

## Fake 테스트 더블 네이밍

- `Fake{클래스명}` — `FakeUserRepository`, `FakeTokenParseProvider`
- `Stub{클래스명}` — 항상 예외를 던지는 더블 — `StubOauthClient`

## 패키지 네이밍

- 도메인 패키지 이름은 단수형으로 한다: `user`, `auth`, `project`, `board`
- 하위 패키지는 역할을 명확하게 나타낸다: `domain`, `application`, `port`, `adapter`, `persistence`

## 상수 네이밍

- 테스트 상수는 `{도메인}TestConstant`에 모아서 관리한다.
- 상수명은 `TEST_{대상}_{구분}` 패턴: `TEST_USER_GUID_1`, `TEST_EMAIL_1`, `TEMP_TOKEN`
