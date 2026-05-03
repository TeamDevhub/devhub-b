# Auth / User 도메인 코드 리뷰

> 리뷰 기준: `feature/user` 브랜치 현재 상태  
> 리뷰 대상: `core/auth`, `core/user`, `outbound/auth`, `outbound/user`, `api/auth`, `api/user`  
> 리뷰어 기준: 시니어 수준, 프로덕션 배포 가정

---

## 요약

최근 `lastLoginDateTime` 추적 위치를 credential 테이블에서 `users` 테이블로 통합한 리팩토링은 방향이 옳다. 로그인 정책을 `LoginPolicyService` 하나로 응집시키고, 이메일 로그인 / OAuth 로그인 두 경로 모두 일관된 순서(`validateLoginUser` → 토큰 발급 → `updateLastLoginDateTime`)를 따르는 구조는 유지보수성 측면에서 개선이다.

그러나 리팩토링 과정에서 생긴 데이터 불일치, null 버그, 아키텍처 경계 위반, 보안 설정 미비가 프로덕션 배포 전에 반드시 처리되어야 한다. 아래에 중요도 순서로 정리한다.

---

## 1. 보안 (즉시 수정 필요)

### 1-1. `CookieFactory.secure(false)` — 리프레시 토큰 평문 전송 가능

```java
// api/auth/controller/CookieFactory.java:17
return ResponseCookie.from(REFRESH_COOKIE_NAME, refreshToken)
        .httpOnly(true)
        .secure(false)   // ← HTTPS 없이 전송 허용
        .sameSite(SAME_SITE)
        ...
```

리프레시 토큰은 14일짜리 장기 세션 토큰이다. `secure(false)`는 HTTP 환경에서 쿠키가 평문으로 전달된다는 의미다. 중간자 공격으로 탈취되면 장기 세션 탈취가 가능하다.

**수정**: `secure(true)`로 변경. 개발 환경 분기가 필요하면 환경 변수(`spring.profiles.active`)로 제어한다.

---

### 1-2. `/admin/**` 전체 `permitAll()` — 어드민 엔드포인트 인증 우회

```java
// shared/config/WebSecurityConfig.java:106
// .requestMatchers("/admin/**").hasRole("ADMIN")   ← 주석 처리된 의도
.requestMatchers("/admin/**").permitAll()            // ← 현재 상태
```

어드민 API가 인증 없이 호출 가능하다. CLAUDE.md에도 미완성으로 표시되어 있으나, 실제 어드민 컨트롤러(`AdminUserController`, `AdminBoardController`, `BannerController`, `ApplicationFormController`, `CommonCodeController`)가 이미 존재한다. 코드가 있고 라우팅이 열려 있는 상태는 미완성이 아니라 취약점이다.

**수정**: `hasRole("ADMIN")` 주석을 해제하고 Spring Security role 체계를 완성한다.

---

### 1-3. OAuth 콜백 — localhost URL 하드코딩

```java
// api/auth/controller/OauthController.java:42-44
response.sendRedirect("http://localhost:5173/");
String redirectUrl = "http://localhost:5173/auth/signup" + "?token=" + oauthAuthResult.tempToken();
```

프로덕션에서 배포하면 모든 OAuth 로그인이 개발 PC `localhost`로 리다이렉트된다. 또한 CORS 설정(`WebSecurityConfig:63`)도 `localhost:3000`, `localhost:5173`만 허용하고 있어 같은 문제를 가진다.

**수정**: 리다이렉트 URL과 CORS 허용 오리진을 환경 변수(`${app.frontend-url}`)로 외부화한다.

---

## 2. 데이터 불일치 — 죽은 데이터와 죽은 코드 (높음)

### 2-1. credential 테이블의 `lastLoginDate` 컬럼 — 더 이상 갱신되지 않음

`lastLoginDateTime` 추적이 `users` 테이블로 이전된 후, credential 테이블의 `lastLoginDate` 컬럼은 아무도 갱신하지 않는다.

| 위치 | 컬럼 | 현재 상태 |
|---|---|---|
| `EmailCredentialEntity.lastLoginDate` | `user_email_credentials.lastLoginDate` | DB에 존재하지만 영구적으로 NULL |
| `OAuthCredentialEntity.lastLoginDate` | `user_oauth_credentials.lastLoginDate` | 동일 |
| `EmailUserCredential.lastLoginDate` | 도메인 객체 필드 | 조회는 되지만 갱신 경로 없음 |
| `OAuthUserCredential.lastLoginDate` | 도메인 객체 필드 | 동일 |

이 컬럼들을 읽는 쿼리나 비즈니스 로직이 현재 없다면 삭제해야 한다. 있다면 이 데이터가 항상 NULL임을 인식하고 의존하지 않아야 한다.

---

### 2-2. `markLoginSuccess()` — 호출자 없는 도메인 메서드

```java
// core/auth/domain/EmailUserCredential.java:44-46
public void markLoginSuccess() {
    this.lastLoginDate = LocalDateTime.now();  // 호출자 없음
}

// core/auth/domain/OAuthUserCredential.java:34-36
public void markLoginSuccess() {
    this.lastLoginDate = LocalDateTime.now();  // 호출자 없음
}
```

프로젝트 규칙에 따라 주석으로 남기지 말고 삭제해야 한다.

---

### 2-3. `UserBasicResponseDto.lastLoginDateTime` — 항상 null

```java
// core/user/port/in/facade/model/UserBasicResponseDto.java:28
private LocalDateTime lastLoginDateTime;

// fromDomain(User user) 에서 이 필드를 설정하지 않음
```

`User` 도메인 객체에는 `lastLoginDateTime` 필드가 없다. `lastLoginDateTime`은 `UserEntity`에만 있고 `@Modifying` 쿼리로만 갱신된다. 결과적으로 `GET /user` 응답의 `lastLoginDateTime`은 항상 null이다.

API 스펙상 이 필드를 노출해야 한다면 `UserRepository.findByUserGuid`가 반환하는 `User`에 `lastLoginDateTime`을 포함하거나, 별도 쿼리로 조회해야 한다. 노출이 불필요하다면 DTO에서 필드를 제거한다.

---

## 3. Null 버그 — OAuth 회원가입 시 `verificationProvider` 누락 (높음)

```java
// api/user/model/SignupOauthRequestDto.java:43-55
public SignupOauthUserCommand toSignupOauthUserCommand() {
    return SignupOauthUserCommand.builder()
            .tempToken(this.tempToken)
            // verificationProvider 설정 없음 → null
            .username(this.username)
            ...
            .build();
}
```

`SignupOauthUserCommand` 레코드는 `verificationProvider`를 두 번째 필드로 선언한다.

```java
// core/auth/port/in/command/oauth/SignupOauthUserCommand.java:11
public record SignupOauthUserCommand(String tempToken, VerificationProvider verificationProvider, ...)
```

DTO가 이 필드를 설정하지 않아 `verificationProvider`는 항상 `null`로 전달된다. `OauthResolveUseCase.extractOauthUser`가 이 값을 사용하는 경우 `NullPointerException` 또는 잘못된 분기가 발생한다.

또한 `OauthController.signup`에 `@Valid`가 누락되어 있어 DTO 유효성 검증이 수행되지 않는다.

```java
// api/auth/controller/OauthController.java:50
public ResponseEntity<...> signup(@RequestBody SignupOauthRequestDto signupOauthRequestDto) {
//                                 ↑ @Valid 누락
```

**수정**: DTO에 `verificationProvider` 필드를 추가하거나, temp token에서 provider를 역파싱하는 방식으로 해결한다. `@Valid`를 추가한다.

---

## 4. 아키텍처 경계 위반 (중간)

### 4-1. Response DTO가 `core` 레이어에 위치

```
core/user/port/in/facade/model/
├── UserBasicResponseDto.java    ← API 응답 DTO가 core에 있음
└── UserDetailResponseDto.java   ← 동일
```

CLAUDE.md의 디렉토리 규칙에 따르면 Response DTO는 `api/{domain}/model/response/`에 위치해야 한다. 현재 `core`에 있으므로:

- `core` 레이어가 HTTP 응답 형태를 알게 된다 (관심사 혼재).
- Facade의 반환 타입이 HTTP DTO에 직접 결합된다.

**수정**: `api/user/model/response/UserBasicResponseDto.java`, `api/user/model/response/UserDetailResponseDto.java`로 이동한다.

---

### 4-2. `LoginPolicyService` 위치 — 도메인 경계 교차

```java
// core/user/application/service/LoginPolicyService.java
// → implements core/auth/port/in/usecase/LoginPolicyUseCase
```

`user` 패키지의 서비스가 `auth` 패키지의 UseCase 인터페이스를 구현한다. 두 가지 해결책이 있다:

1. `LoginPolicyUseCase`를 `core/user/port/in/usecase/`로 이동 (더 나은 옵션).
2. `LoginPolicyService`를 `core/auth/application/service/`로 이동.

현재 상태는 패키지 구조만 보면 의존 방향이 명확하지 않다.

---

### 4-3. `UserProfileController` — 3개 도메인 혼재

```java
// api/user/controller/UserProfileController.java
private final UserProfileFacade userProfileFacade;
private final UserWithdrawFacade userWithdrawFacade;
private final BoardFacade boardFacade;      // board 도메인
private final ProjectFacade projectFacade;  // project 도메인
```

컨트롤러 레이어에서 user / board / project 세 도메인을 한 클래스에서 처리한다. 사용자 관련 게시글 조회(`GET /user/profile/boards`), 프로젝트 조회(`GET /user/projects`) 등은 별도 컨트롤러로 분리하는 것이 유지보수성 측면에서 낫다.

---

### 4-4. `UserSignupController` — 두 Facade 의존

```java
// api/user/controller/UserSignupController.java
private final UserSignupFacade userSignupFacade;
private final AuthFacade authFacade;
```

회원가입 후 자동 로그인 흐름(signup → login)을 컨트롤러에서 두 Facade를 직접 조합하여 처리한다. 아키텍처 원칙상 두 UseCase 이상의 조합은 Facade로 올려야 한다. 컨트롤러가 조합 책임을 가지는 것은 컨트롤러에 비즈니스 흐름 지식이 스며드는 것을 의미한다.

`UserSignupFacade.signup`이 완료된 후 `AuthFacade.login`을 호출하는 별도 Facade 메서드(`signupAndLogin`)로 래핑하는 것을 검토할 수 있다. 단, 이는 `lastLoginDateTime`이 이메일 회원가입 직후에도 갱신된다는 현재 동작을 변경하지 않는 방향으로 설계해야 한다.

---

## 5. Fake 구현체 규칙 위반 (중간)

### 5-1. `FakeUserCredentialRepository.findEmailCredentialByUserGuid` — null 반환

```java
// fake/pure/application/port/out/auth/FakeUserCredentialRepository.java:47-49
@Override
public EmailUserCredential findEmailCredentialByUserGuid(String userGuid) {
    return null;  // ← 규칙 위반: Fake는 null을 반환하지 않는다
}
```

CLAUDE.md 규칙: "Fake는 null을 반환하지 않는다. 데이터가 없으면 예외를 던지거나 Optional.empty()를 반환한다."

이 메서드를 테스트에서 실제로 호출하면 NPE가 발생해 테스트가 의도치 않은 방식으로 실패할 수 있다.

---

### 5-2. `FakeUserCredentialRepository.savePassword` — 빈 구현

```java
@Override
public void savePassword(EmailUserCredential emailUserCredential) {
    // 아무것도 하지 않음
}
```

비밀번호 변경을 테스트하는 케이스가 없어 현재는 문제가 없지만, `updatePassword` 기능이 완성되면 이 Fake가 침묵하는 버그를 만들 수 있다. `Map`에 저장하고 이후 검증 가능하도록 구현해야 한다.

---

### 5-3. `FakeUserRepository.findByUserGuid` — null 반환 가능

```java
@Override
public User findByUserGuid(String userGuid) {
    return store.get(userGuid);  // ← 없으면 null 반환
}
```

실제 `UserAdapter.findByUserGuid`는 존재하지 않으면 `AdapterDataException`을 던진다. Fake는 실제 구현과 동일한 실패 동작을 가져야 한다. null이 반환되면 테스트가 실제 프로덕션 동작과 다른 경로를 검증하게 된다.

---

## 6. 테스트 커버리지 공백 (중간)

### 현재 누락된 테스트 경로

| 경로 | 테스트 여부 |
|---|---|
| 이메일 로그인 (`AuthFacade.login`) | 단위 테스트 있음 |
| 이메일 회원가입 후 자동 로그인 | **없음** |
| OAuth 기존 유저 로그인 (`OauthAuthFacade.handleOAuthCallback`) | **없음** |
| OAuth 신규 유저 회원가입 (`UserSignupFacade.signupWithOauth`) | 단위 테스트 있음 |
| 토큰 재발급 | 단위 테스트 있음 |
| `OauthAuthFacade` 전체 | **단위 테스트 없음** |
| 컨트롤러 레이어 (`AuthController`, `OauthController`) | **없음** |
| `UserAdapter.updateLastLoginDateTime` | **어댑터 테스트 없음** |

`OauthAuthFacade`에 대한 `FakeOauthAuthFacadeTest`를 작성하면 OAuth 경로의 `validateLoginUser` → `updateLastLoginDateTime` 호출 순서를 검증할 수 있다.

---

## 7. 설계 관찰 — 버그는 아니지만 검토 필요

### 7-1. `createGeneralUser`와 `createOauthUser` 중복

```java
// core/user/domain/User.java
public static User createGeneralUser(CreateUserCommand generalCreateUserCommand) { ... }
public static User createOauthUser(CreateUserCommand oauthCreateUserCommand) { ... }
```

두 메서드의 구현이 완전히 동일하다. `userType`이 `CreateUserCommand`에서 전달되므로 메서드를 하나로 합칠 수 있다.

---

### 7-2. `UserProfileFacade` — 단순 위임만 하는 메서드들

```java
public UserBasicResponseDto getUserInfo(String userGuid) {
    User user = userProfileUseCase.getUserInfo(userGuid);
    return UserBasicResponseDto.fromDomain(user);
}
```

Facade 규칙에 따르면 "단순히 위임만 하는 메서드라면 Facade를 거칠 필요는 없다." `getUserInfo`, `getCurrentUserProfile`, `updateProfileImage`, `updateProfile`은 모두 UseCase 단일 위임이다. `updatePassword`만 `UserCredentialUseCase`를 추가로 사용한다.

현재 구조를 유지하더라도 큰 문제는 없지만, Facade를 통하는 이유가 명확하지 않으면 이후 유지보수자가 흐름을 파악하기 어렵다.

---

### 7-3. `OauthController.handleOauthCallback` — 액세스 토큰 응답 누락

OAuth 로그인 성공 시 리프레시 토큰 쿠키는 설정하지만, 액세스 토큰은 응답 헤더(`Authorization`)에 추가하지 않는다. 프론트엔드가 리다이렉트 후 액세스 토큰을 어떻게 얻는지 명확하지 않다. 리다이렉트 URL에 액세스 토큰을 포함하는 것은 보안상 좋지 않으므로, 이 흐름의 클라이언트 처리 방식을 문서화하거나 개선해야 한다.

---

## 수정 우선순위 요약

| 순위 | 항목 | 종류 |
|---|---|---|
| P0 | `CookieFactory.secure(true)` | 보안 |
| P0 | `/admin/**` hasRole("ADMIN") 복원 | 보안 |
| P0 | `SignupOauthRequestDto.verificationProvider` null 버그 수정 + `@Valid` 추가 | 버그 |
| P1 | `OauthController` localhost URL 환경변수화 + CORS 환경변수화 | 보안 |
| P1 | credential 테이블 `lastLoginDate` 컬럼 제거 또는 사용 목적 명확화 | 데이터 불일치 |
| P1 | `markLoginSuccess()` 두 곳 삭제 | 데드코드 |
| P1 | `UserBasicResponseDto.lastLoginDateTime` null 문제 해결 | 버그 |
| P2 | `UserBasicResponseDto`, `UserDetailResponseDto` → `api/` 레이어로 이동 | 아키텍처 |
| P2 | `FakeUserCredentialRepository.findEmailCredentialByUserGuid` null 반환 수정 | 테스트 신뢰성 |
| P2 | `FakeUserRepository.findByUserGuid` null 반환 수정 | 테스트 신뢰성 |
| P2 | `OauthAuthFacade` 단위 테스트 추가 | 커버리지 |
| P3 | `LoginPolicyUseCase` 패키지 위치 정리 | 아키텍처 |
| P3 | `UserProfileController` board/project 엔드포인트 분리 | 아키텍처 |
| P3 | `createGeneralUser`와 `createOauthUser` 통합 | 중복 제거 |
