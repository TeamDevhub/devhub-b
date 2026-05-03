# Code Review Result

> 작성일: 2026-05-03  
> 대상: DevHub 백엔드 전체 프로젝트  
> 브랜치: `feature/user`  
> 리뷰어: super-agent → code-review-agent (all scope)

---

## Review Target

전체 도메인 — `auth`, `user`, `project`, `board`, `admin`, `application`, `notification`, `file`, `terms`  
전체 레이어 — `api`, `core`, `outbound`, `shared`  
전체 테스트 — `small`, `medium`, `fake`

---

## Executive Summary

헥사고날 아키텍처를 의도한 프로젝트이며 전반적인 구조 방향성은 올바르다.  
그러나 다음 세 가지 영역에서 **릴리스를 블로킹할 수준의 결함**이 존재한다.

1. **보안**: OAuth CSRF 취약점, 로그 인젝션, Content-Disposition 헤더 인젝션, secure=false 쿠키
2. **정합성**: `ProjectAdapter.getProjectDetail()` 미확인 `.get()`, `deleteProject()` 순서 역전으로 인한 데이터 손실
3. **아키텍처**: `Board` 서비스의 `User` 도메인 직접 의존, `Project` 도메인 공개 `@Builder`

단순 코드 스타일 문제가 아니라 운영 환경에서 즉시 장애로 연결될 수 있는 결함들이다.

---

## Findings

### CRITICAL — 즉시 수정 필요

---

**[C-1] ProjectAdapter — `.get()` without `orElseThrow()`**

- **위치**: `outbound/project/adapter/ProjectAdapter.java:31`
- **문제**: `jpaProjectRepository.findById(projectGuid).get()` — `Optional`을 검증 없이 `.get()` 호출
- **영향**: 해당 GUID의 프로젝트가 존재하지 않으면 `NoSuchElementException`이 던져지고, `GlobalExceptionHandler`가 이를 400 BAD_REQUEST로 처리해 클라이언트에 원인 불명 에러 반환
- **수정**:
  ```java
  jpaProjectRepository.findById(projectGuid)
      .orElseThrow(() -> AdapterDataException.of(ErrorCode.PROJECT_NOT_FOUND));
  ```

---

**[C-2] TraceIdMDCFilter — 클라이언트 제어 헤더가 MDC에 비위생 주입**

- **위치**: `shared/logging/TraceIdMDCFilter.java:30-36`
- **문제**: `X-Trace-Id` 헤더 값을 검증 없이 MDC에 직접 저장
  ```java
  traceId = httpServletRequest.getHeader("X-Trace-Id");
  MDC.put(TRACE_ID_KEY, traceId);
  ```
- **영향**: 공격자가 `\n`, `\r` 등 개행 문자를 포함한 값으로 로그를 위조할 수 있다 (Log Injection). 예: `X-Trace-Id: abc\n[ERROR] 2026-05-03 FAKE_ALERT`
- **수정**: 헤더 값이 UUID 패턴(`[a-fA-F0-9]{16,32}`)에 맞는지 검증하거나, 일치하지 않으면 서버 생성 ID 사용
  ```java
  if (traceId == null || !traceId.matches("[a-zA-Z0-9\\-]{8,36}")) {
      traceId = identifierProvider.generateIdentifier().substring(0, 16);
  }
  ```

---

**[C-3] FileResponseFactory — Content-Disposition 헤더 인젝션**

- **위치**: `api/file/controller/FileResponseFactory.java:21`
- **문제**: 클라이언트가 제어할 수 있는 `fileResource.originalName()`을 헤더에 직접 연결
  ```java
  "attachment; filename=\"" + fileResource.originalName() + "\""
  ```
- **영향**: 파일명에 `"` 또는 `\r\n`가 포함되면 HTTP 헤더를 분리하거나 추가 헤더를 주입할 수 있다
- **수정**: RFC 5987 인코딩 또는 파일명에서 허용 문자 이외 제거
  ```java
  String safeName = fileResource.originalName().replaceAll("[^\\w.\\-]", "_");
  "attachment; filename=\"" + safeName + "\""
  ```

---

**[C-4] CookieFactory — Refresh Token 쿠키 `secure=false`**

- **위치**: `api/auth/controller/CookieFactory.java:16`
- **문제**: `.secure(false)` — Refresh Token이 HTTP 평문으로 전송 가능
- **영향**: 네트워크 스니핑, 공유 Wi-Fi 등 환경에서 Refresh Token 탈취 가능. Token Hijacking → 세션 도용
- **수정**: 운영 환경에서는 반드시 `true`. 개발/운영 프로파일을 분리해 설정값 주입
  ```java
  @Value("${cookie.secure:true}")
  private boolean cookieSecure;
  // ...
  .secure(cookieSecure)
  ```

---

**[C-5] OauthController — OAuth state 파라미터 없음 (CSRF 취약점)**

- **위치**: `api/auth/controller/OauthController.java:51-65`
- **문제**: `handleOauthCallback()`이 `code` 파라미터를 수신할 때 `state` 값을 검증하지 않음. `createAuthorizationUrl()`도 `state`를 생성하지 않음
- **영향**: 공격자가 피해자 브라우저에서 자신의 인가 코드로 콜백을 강제 실행 → Authorization Code Injection → 공격자 계정으로 강제 로그인 (OAuth CSRF)
- **수정**:
  1. `redirectToProvider` 시 `state` 값을 세션 또는 쿠키에 저장
  2. `handleOauthCallback` 에서 `state` 파라미터 수신 및 저장값과 일치 여부 검증
  3. 불일치 시 요청 거부

---

**[C-6] OauthController — 하드코딩된 localhost URL**

- **위치**: `api/auth/controller/OauthController.java:60, 62`
- **문제**:
  ```java
  response.sendRedirect("http://localhost:5173/");
  response.sendRedirect("http://localhost:5173/auth/signup" + "?token=" + ...);
  ```
- **영향**: 운영 환경에서 배포 즉시 OAuth 로그인이 localhost로 리다이렉트되어 완전 불능
- **수정**: `@Value("${app.frontend.url}")` 등 외부 설정으로 분리

---

**[C-7] LoggingAspect — 비밀번호 포함 모든 파라미터를 로그에 직렬화**

- **위치**: `shared/logging/LoggingAspect.java:44`
- **문제**: `execution(* teamdevhub.devhub.core..*(..))` 범위로 모든 서비스 메서드 파라미터를 JSON 직렬화하여 로그 출력
- **영향**: `UserCredentialService.signupEmailUser(SignupUserCommand)` 호출 시 `password` 필드가 INFO 로그에 평문으로 출력됨. 로그 수집 시스템(ELK 등)에 비밀번호 노출
- **수정**: 민감 필드 마스킹 처리 또는 Command 객체에 `@JsonIgnore` / 커스텀 `toString()` 적용. 또는 Aspect 범위를 Controller 레이어로만 제한

---

**[C-8] ProjectService.deleteProject() — 삭제 후 조회로 항상 빈 목록 반환**

- **위치**: `core/project/application/ProjectService.java:122-129`
- **문제**:
  ```java
  projectApplicationFormRepository.deleteByProjectGuid(projectGuid); // 먼저 삭제
  return projectApplicationFormRepository.findAllGuidByProjectGuid(projectGuid); // 이미 삭제됨
  ```
- **영향**: 삭제 후 조회이므로 반환 목록은 항상 비어 있음. 이 목록을 사용하는 연계 처리(파일 삭제 등)가 누락되어 고아 데이터 발생
- **수정**: 조회를 삭제 전에 수행
  ```java
  List<String> guids = projectApplicationFormRepository.findAllGuidByProjectGuid(projectGuid);
  projectApplicationFormRepository.deleteByProjectGuid(projectGuid);
  return guids;
  ```

---

**[C-9] GlobalExceptionHandler — 모든 미처리 예외를 400으로 반환**

- **위치**: `shared/exception/GlobalExceptionHandler.java:64-70`
- **문제**: `@ExceptionHandler(Exception.class)`가 `ResponseEntity.badRequest()`(400)를 반환. DB 연결 실패, NPE, 인프라 장애 등 서버 에러가 클라이언트 에러로 위장됨
- **영향**: 클라이언트는 서버 내부 장애를 자신의 요청 오류로 오해. 모니터링 알람도 왜곡됨
- **수정**: `Exception.class` 핸들러는 HTTP 500 반환

---

### MAJOR — 이번 스프린트 내 수정

---

**[M-1] BoardService / BoardQueryService — User 도메인 직접 의존**

- **위치**: `core/board/application/BoardService.java:31`, `core/board/application/BoardQueryService.java:29`
- **문제**: Board 서비스가 `UserRepository`를 직접 주입받아 사용자 이름 조회
- **영향**: Board와 User 도메인 간 양방향 의존 위험. 아키텍처 경계 위반
- **수정**: 게시글 생성 시 작성자명을 Board 데이터에 포함하거나, Board-User 간 별도 ReadModel 도입

---

**[M-2] Project 도메인 — `@Builder` public 노출**

- **위치**: `core/project/domain/Project.java:15`
- **문제**: `@Builder`가 클래스 레벨에 선언되어 빌더가 `public` 접근성을 가짐. `Project.builder().build()`로 검증 없이 빈 객체 생성 가능
- **영향**: 팩토리 메서드 우회 가능. 테스트에서도 이미 `Project.builder()`를 직접 사용 중 (`UserReviewServiceTest`)
- **수정**: `@Builder(access = AccessLevel.PRIVATE)` 또는 생성자를 `private`으로 변경

---

**[M-3] JWT와 쿠키 만료 기간 불일치**

- **위치**: `outbound/auth/infrastructure/token/JwtTokenCodec.java:63`, `api/auth/controller/CookieFactory.java:10`
- **문제**: JWT Refresh Token은 7일 만료, 쿠키는 14일 maxAge. 7일 이후 쿠키는 존재하지만 JWT는 만료됨
- **영향**: 7-14일 사이 재발급 시도 시 `TOKEN_EXPIRED` 에러 발생. 의도한 `REFRESH_TOKEN_INVALID`와 다른 에러 코드 반환으로 사용자에게 혼란
- **수정**: 두 값을 동일하게 맞추거나 외부 설정으로 단일 관리

---

**[M-4] ProjectService — 다른 도메인 포트 직접 주입**

- **위치**: `core/project/application/ProjectService.java:43`
- **문제**: `ProjectApplicationFormRepository`는 `application` 도메인 포트인데 `project` 도메인 서비스가 주입받음
- **영향**: 도메인 간 경계 혼재. Project 도메인이 Application 도메인을 직접 인식
- **수정**: Facade 계층에서 두 UseCase를 조합하거나 도메인 간 이벤트로 분리

---

**[M-5] Project.getRecruitStatus() — null이면 NPE**

- **위치**: `core/project/domain/Project.java:96-105`
- **문제**: `this.recruitmentStartDate`, `this.recruitmentEndDate`가 null인 경우 NPE 발생
- **영향**: 모집 기간이 설정되지 않은 프로젝트 조회 시 500 에러
- **수정**: null 방어 또는 도메인 생성 시 필수 값 검증 추가

---

**[M-6] AuthController.login() — `@Valid` 누락**

- **위치**: `api/auth/controller/AuthController.java:41`
- **문제**: `@RequestBody LoginRequestDto loginRequestDto` — `@Valid` 없음
- **영향**: Bean Validation이 동작하지 않아 빈 이메일/비밀번호로 로그인 시도 가능
- **수정**: `@Valid @RequestBody LoginRequestDto loginRequestDto`

---

**[M-7] BoardService.likeBoard() — null 반환 포트 의존**

- **위치**: `core/board/application/BoardService.java:87-90`
- **문제**: `boardLikeRepository.likeBoard()`가 좋아요가 없으면 `null` 반환. 프로젝트 규칙 위반
- **영향**: 포트 계약이 null 반환을 포함. 향후 Fake 구현에서 실수 유발
- **수정**: `Optional<BoardLike>` 반환으로 변경

---

**[M-8] BoardService.deleteBoard() — 소유권 검증 없음**

- **위치**: `core/board/application/BoardService.java:98-100`
- **문제**: `deleteBoard(List<String> boardGuids)` — 요청자가 해당 게시글의 작성자인지 검증 없음
- **영향**: GUID를 아는 사람이면 누구든 타인의 게시글을 삭제 가능
- **수정**: `userGuid`를 파라미터로 추가하고 각 GUID에 대한 소유권 확인

---

**[M-9] Board 도메인 — `fill*Subquery()` 메서드는 사실상 setter**

- **위치**: `core/board/domain/Board.java:65-79`
- **문제**: `fillSummarySubquery()`, `fillDetailSubquery()`가 도메인 필드를 직접 변경. 실질적으로 다중 setter
- **영향**: 도메인 불변성 훼손. 서비스 레이어에서 도메인 상태를 외부에서 주입하는 패턴
- **수정**: 별도 ReadModel(DTO)을 서비스/Facade에서 조합. Board 도메인은 저장 데이터만 보유

---

**[M-10] ProjectService.updateProject() — TODO 수준 주석이 운영 코드에 존재**

- **위치**: `core/project/application/ProjectService.java:137`
- **문제**:
  ```java
  // 신청양식 변경되면 어떻게???
  // 일단 삭제 후 생성
  ```
- **영향**: 미결정 비즈니스 로직이 운영 코드에 주석으로 남아 있음. 프로젝트 규칙 위반
- **수정**: 결정 후 주석 제거. 의사결정 내용은 PR/이슈 트래킹

---

**[M-11] UserReviewUseCase — 반환 계약 미문서화**

- **위치**: `core/user/port/in/usecase/UserReviewUseCase.java:7`
- **문제**: `double reviewMember(ReviewUserCommand command)` — 반환값이 raw 점수(1.0~5.0)인지 delta인지 인터페이스에서 알 수 없음
- **영향**: 이전 이중차감 버그의 근본 원인. 동일 실수 재발 위험
- **수정**: JavaDoc으로 명시: `@return raw 점수 (1.0 ~ 5.0)`

---

**[M-12] UserReviewServiceTest — 도메인 생성 규칙 위반**

- **위치**: `small/core/user/application/service/UserReviewServiceTest.java:44-58`
- **문제**: `Project.builder()` 직접 호출로 팩토리 메서드 우회
- **영향**: `Project.createProject()` 검증 로직을 거치지 않는 테스트 픽스처
- **수정**: `Project`에 테스트용 팩토리 메서드 추가(`forTest()` 등)

---

**[M-13] UserCredentialService.updatePassword() — CLAUDE.md 미완 기능과 불일치**

- **위치**: `core/auth/application/service/UserCredentialService.java:85-90`
- **문제**: `updatePassword()` 구현은 존재하나 CLAUDE.md는 "commented out"으로 기록. `AuthController`에서는 API가 노출되어 있음
- **영향**: 문서와 실제 코드 불일치. 기능이 완료된 것인지 미완인지 불명확
- **수정**: CLAUDE.md 업데이트 또는 기능 완성 여부 재확인

---

### MINOR — 다음 스프린트 정리

---

**[N-1] Project 도메인 — `createUpateProject()` 오타**

- **위치**: `core/project/domain/Project.java:69`
- `createUpateProject` → `createUpdateProject`

---

**[N-2] Board 패키지 — `Facade` 대문자 패키지명**

- **위치**: `core/board/port/in/Facade/`
- Java 패키지명 컨벤션은 소문자. `facade`로 수정 필요

---

**[N-3] Project 도메인 — 혼재된 탭/스페이스 들여쓰기**

- **위치**: `core/project/domain/Project.java` 전반
- 일부 라인이 탭, 다른 라인이 스페이스로 들여쓰기됨. 편집기 설정 통일 필요

---

**[N-4] ProjectAdapter.update() — 15개 파라미터 쿼리 직접 호출**

- **위치**: `outbound/project/adapter/ProjectAdapter.java:61-64`
- JPA 엔티티를 저장하지 않고 네이티브 쿼리 수준의 호출로 15개 필드 나열
- Command 객체를 Entity로 변환하여 `save()` 호출로 단순화 필요

---

**[N-5] CookieFactory.SAME_SITE = "Lax"**

- **위치**: `api/auth/controller/CookieFactory.java:8`
- 인증 쿠키는 `Strict`가 더 안전. `Lax`는 일부 CSRF 시나리오에서 취약

---

**[N-6] ProjectService — `@Transactional` 클래스 레벨이나 `getUserProjects()` 등 읽기 전용 메서드 포함**

- `readOnly=true` 옵션 미적용으로 읽기 전용 호출에서도 불필요한 write lock 가능성

---

## Priority Fix List

### 1. 즉시 수정 (배포 전 필수)

| 우선순위 | 이슈 | 위치 |
|---------|------|------|
| 1 | OAuth CSRF — state 파라미터 누락 [C-5] | `OauthController` |
| 2 | 비밀번호 로그 노출 [C-7] | `LoggingAspect` |
| 3 | Log Injection via X-Trace-Id [C-2] | `TraceIdMDCFilter` |
| 4 | OAuth 하드코딩 localhost [C-6] | `OauthController` |
| 5 | `ProjectAdapter.get()` 미검증 [C-1] | `ProjectAdapter` |
| 6 | `deleteProject()` 조회/삭제 순서 역전 [C-8] | `ProjectService` |
| 7 | Content-Disposition 헤더 인젝션 [C-3] | `FileResponseFactory` |
| 8 | `secure=false` 쿠키 [C-4] | `CookieFactory` |
| 9 | 모든 예외 400 반환 [C-9] | `GlobalExceptionHandler` |

### 2. 이번 스프린트 (1주 내)

| 우선순위 | 이슈 |
|---------|------|
| 10 | Board → User 도메인 직접 의존 [M-1] |
| 11 | `@Valid` 누락 [M-6] |
| 12 | Board 게시글 삭제 소유권 미검증 [M-8] |
| 13 | JWT 7일 / Cookie 14일 불일치 [M-3] |
| 14 | `Project.@Builder` 공개 노출 [M-2] |
| 15 | `Project.getRecruitStatus()` NPE [M-5] |

### 3. 다음 스프린트 정리

- Board 도메인 fill*Subquery setter 제거 [M-9]
- UserReviewUseCase 계약 문서화 [M-11]
- `createUpateProject()` 오타 수정 [N-1]
- 패키지명 소문자 통일 [N-2]
- 인라인 TODO 주석 제거 [M-10]
- ProjectService 도메인 경계 정리 [M-4]

---

## Test Quality Assessment

### 현재 강점

- `auth` 도메인 서비스 레이어: `UserCredentialServiceTest`, `OauthAuthenticationServiceTest` 존재
- `user` 도메인: `UserReviewServiceTest`, `UserReviewTest`, `UserWithdrawFacadeTest` 등 핵심 흐름 커버
- `terms`, `board` 도메인: 도메인 유닛 테스트 존재 (`TermsTest`, `BoardTest`, `BoardFacadeTest`)
- Fake 구현체: 프로젝트 규칙 준수 (Mockito 사용 없음)

### 누락된 시나리오

| 도메인 | 누락 테스트 |
|--------|------------|
| `project` | `ProjectService`, `ProjectQueryService` 서비스 단위 테스트 없음 (`ProjectServiceTest` 전체 주석 처리) |
| `board` | `BoardService`, `BoardQueryService` 단위 테스트 없음 |
| `user` | `UserReviewFacade` — `updateUserMannerDegree`에 전달되는 실제 값 검증 없음 |
| `file` | `FileResponseFactory` 단위 테스트 없음 (Content-Disposition 인젝션 테스트 포함) |
| `auth` | OAuth CSRF state 검증 테스트 없음 (해당 로직 자체가 없어서) |
| `admin` | Admin 엔드포인트 통합 테스트 없음 |
| 공통 | `TraceIdMDCFilter` 악의적 헤더 입력 테스트 없음 |

### 권장 추가 테스트

1. `UserReviewFacadeTest` — `FakeUserProfileUseCase`가 받은 `reviewScore` 인자를 캡처하여 4.0임을 단언
2. `ProjectServiceTest` — 주석 해제 또는 재작성 (현재 블로킹 이슈 해소 후)
3. `BoardServiceTest` — 소유권 검증 테스트 포함
4. `FileResponseFactoryTest` — 파일명에 `"`, `\r\n` 포함 시 동작 검증

---

## Security Assessment

| 위험 | 심각도 | 상태 |
|-----|--------|------|
| OAuth CSRF (state 파라미터 없음) | Critical | 미수정 |
| Log Injection (X-Trace-Id MDC 비위생 입력) | Critical | 미수정 |
| 비밀번호 로그 노출 (LoggingAspect 전 범위) | Critical | 미수정 |
| Content-Disposition 헤더 인젝션 | Critical | 미수정 |
| Refresh Token 쿠키 `secure=false` | Critical | 미수정 |
| JWT/Cookie 만료 기간 불일치 | Major | 미수정 |
| `/admin/**` — `hasRole("ADMIN")` | Major | 수정 완료 ✓ |
| CORS `allowedHeaders(List.of("*"))` + `addExposedHeader("*")` | Minor | 운영 전 검토 필요 |
| `SameSite=Lax` (Strict 권장) | Minor | 미수정 |

**CORS 비고**: `setAllowedHeaders(List.of("*"))`는 개발 환경에서 허용 가능하나, 운영 환경에서는 허용 헤더를 명시적으로 제한해야 한다.

---

## Architecture Assessment

### 경계 위반

| 위반 위치 | 내용 |
|-----------|------|
| `BoardService` → `UserRepository` | Board 서비스가 User 출력 포트 직접 의존 |
| `BoardQueryService` → `UserRepository` | 동일 패턴 |
| `ProjectService` → `ProjectApplicationFormRepository` | Project 서비스가 Application 도메인 포트 직접 주입 |

### 설계 문제

| 위치 | 문제 |
|------|------|
| `Project` 도메인 | `@Builder` public 노출 — 팩토리 메서드 우회 가능 |
| `Board` 도메인 | `fill*Subquery()` 패턴이 사실상 multi-setter |
| `BoardFacade` | `core/board/port/in/Facade/`에 위치 — Facade가 port/in 하위에 있는 것은 비표준 위치 |
| `UserReviewFacade` | Facade가 `port/in/facade/`에 있는 것은 올바름 — 이 패턴으로 일치 필요 |

### 올바르게 구현된 부분

- `auth` 도메인: Service → UseCase → Port 계층 명확
- `user` 도메인: `UserProfileFacade` → `UserProfileUseCase` → `UserProfileService` 계층 명확
- 예외 계층 (DomainRuleException / BusinessRuleException / AdapterDataException) 일관된 사용
- Fake 구현체가 실 포트 인터페이스를 완전 구현 (Mockito 금지 규칙 준수)

---

## Domain Health Status

| 도메인 | 상태 | 주요 이슈 |
|--------|------|----------|
| `auth` (email) | 🟡 | 비밀번호 로그 노출, JWT/Cookie 불일치 |
| `auth` (oauth) | 🔴 | OAuth CSRF, 하드코딩 URL |
| `auth` (verification) | 🟢 | 구조 건전 |
| `user` (profile) | 🟢 | 정상 |
| `user` (review) | 🟡 | 반환 계약 미문서화, Facade 테스트 부재 |
| `user` (withdraw) | 🟢 | 정상 |
| `project` | 🔴 | `.get()` NPE, deleteProject 순서 버그, 주석 TODO, 테스트 전무 |
| `board` | 🔴 | User 도메인 의존, setter 패턴, 소유권 미검증, 테스트 없음 |
| `admin` | 🟡 | `/admin/**` 인가 설정됨, 통합 테스트 없음 |
| `file` | 🟡 | Content-Disposition 인젝션 |
| `notification` | 🟡 | 평가 범위 밖 (어댑터 코드 미확인) |
| `terms` | 🟢 | 도메인 테스트 존재, 구조 건전 |
| `shared/logging` | 🔴 | 비밀번호 노출, Log Injection |
| `shared/security` | 🔴 | secure=false 쿠키 |

---

## Strengths (실제 관찰된 강점)

- **예외 계층 설계**: `DomainRuleException`, `BusinessRuleException`, `AdapterDataException` 삼계층 일관 적용
- **도메인 팩토리 메서드**: `User.createGeneralUser()`, `User.createAdminUser()` 등 명확한 생성 의도 표현
- **Change Result 패턴**: `UserPositionChangeResult`, `UserSkillChangeResult` — 서비스가 도메인 내부 상태를 직접 비교하지 않고 변경 결과만 수신
- **Fake 구현체 체계**: Mockito 없이 `FakeUserReviewRepository`, `FakeProjectRepository` 등 완전한 인메모리 구현
- **ErrorCode 중앙화**: 하드코딩된 에러 메시지 없음, enum으로 일관 관리
- **`User.applyReviewScore()` 책임 단일화**: 중립값(3.0) 계산이 오직 이 메서드 한 곳에서만 수행 (최근 리팩토링 결과)

---

## Final Verdict

**Request Changes — Block Release (보안 이슈 해소 전 배포 불가)**

아홉 개의 Critical 이슈 중 다섯 개가 보안 취약점이다 (OAuth CSRF, Log Injection, Content-Disposition 인젝션, 비밀번호 로그 노출, 쿠키 `secure=false`). 이 다섯 개는 운영 배포 전 반드시 수정되어야 한다.

정합성 측면에서도 `ProjectService.deleteProject()`의 순서 역전 버그와 `ProjectAdapter.get()` 미검증은 운영 데이터 손실 및 500 에러를 직접 유발할 수 있다.

아키텍처 방향성은 올바르며 일부 도메인(`auth/email`, `user/profile`, `terms`)은 설계 원칙을 잘 따르고 있다. `board`와 `project` 도메인이 가장 취약하며 집중적인 보완이 필요하다.
