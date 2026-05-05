# Admin 회원 관리 API 코드 리뷰

> 리뷰 대상: `feature/user` 브랜치 — Admin 회원 관리 API 신규 구현 (10개 엔드포인트)
> 리뷰 일자: 2026-04-29

---

## 요약

| 심각도 | 건수 |
|--------|------|
| 🔴 Critical | 1 |
| 🟠 Major | 3 |
| 🟡 Minor | 5 |
| **합계** | **9** |

---

## 🔴 Critical

### 1. `AdminUserFacade`가 `api/` 레이어 DTO를 직접 임포트 — 헥사고날 아키텍처 의존성 방향 위반

**위치:** `src/main/java/teamdevhub/devhub/core/user/port/in/facade/AdminUserFacade.java`

**문제:**
```java
import teamdevhub.devhub.api.user.model.AdminReportResponseDto;
import teamdevhub.devhub.api.user.model.AdminUserDetailResponseDto;
```
`core/` 레이어에 속하는 Facade가 `api/` 레이어의 DTO를 임포트하고 있다.
헥사고날 아키텍처의 의존성 방향은 항상 **외부 → 내부** (`api` → `core`)여야 하며, 반대 방향은 엄격히 금지된다.

**중요한 이유:**
이 구조가 유지되면 `api/` 레이어의 변경이 `core/` 레이어를 오염시킨다.
도메인 로직과 표현 계층이 결합되어 테스트 독립성이 무너지고, 아키텍처 경계가 사실상 무의미해진다.

**권장 수정 방법:**
`AdminReportResponseDto`와 `AdminUserDetailResponseDto`를 `core/user/port/in/facade/model/` 패키지로 이동한다.
컨트롤러에서는 해당 경로로 임포트하도록 수정한다.

```
core/user/port/in/facade/model/
├── AdminUserDetailResponseDto.java  ← api/user/model/ 에서 이동
└── AdminReportResponseDto.java      ← api/user/model/ 에서 이동
```

---

## 🟠 Major

### 2. OAuth 사용자에게 비밀번호 초기화 호출 시 예외 발생

**위치:** `src/main/java/teamdevhub/devhub/core/auth/application/service/UserCredentialService.java` — `resetUserPassword()`

**문제:**
```java
@Override
public void resetUserPassword(String userGuid, String newPassword) {
    EmailUserCredential emailUserCredential =
            userCredentialRepository.findEmailCredentialByUserGuid(userGuid);
    // OAuth 사용자는 이메일 크리덴셜이 없으므로 AdapterDataException 발생
    ...
}
```
`findEmailCredentialByUserGuid()`는 이메일 크리덴셜이 없으면 `AdapterDataException(USER_NOT_FOUND)`를 던진다.
OAuth로만 가입한 사용자에게 이 API를 호출하면 500 수준의 에러가 발생한다.

**중요한 이유:**
관리자가 OAuth 사용자의 비밀번호를 초기화하려 할 때 의미 없는 에러 메시지를 받게 되며, 실제 원인을 파악하기 어렵다.

**권장 수정 방법:**
`Optional`을 반환하는 조회 메서드를 사용하거나, 전용 `ErrorCode`를 추가하여 명확한 예외 메시지를 제공한다.

```java
@Override
public void resetUserPassword(String userGuid, String newPassword) {
    userCredentialRepository.findEmailCredentialByUserGuidOptional(userGuid)
            .orElseThrow(() -> BusinessRuleException.of(ErrorCode.EMAIL_CREDENTIAL_NOT_FOUND));
    ...
}
```

---

### 3. `getUserApplyProjects()`에서 잘못된 GUID 사용 — 기존 버그 전파

**위치:** `src/main/java/teamdevhub/devhub/core/user/port/in/facade/AdminUserFacade.java:80`

**문제:**
```java
Project project = projectUseCase.getProjectDetail(application.getRequirementGuid());
```
`application.getRequirementGuid()`는 프로젝트 모집 요건(Requirement)의 GUID이며, 프로젝트(Project)의 GUID가 아니다.
`getProjectDetail()`은 프로젝트 GUID를 인자로 받으므로, 이 호출은 항상 잘못된 프로젝트를 조회하거나 `AdapterDataException`을 발생시킨다.

**중요한 이유:**
지원 프로젝트 목록 조회 API가 실제로는 동작하지 않는다.
동일한 버그가 기존 `ProjectFacade`에도 존재하며, Admin Facade에서 그대로 복사되었다.

**권장 수정 방법:**
`ProjectApplication` 도메인에 `projectGuid` 필드가 있는지 확인하고 올바른 필드를 사용한다.
없다면 도메인 설계를 검토하여 연관 프로젝트 GUID를 조회할 수 있는 경로를 확보해야 한다.

---

### 4. `User.ban()`이 과거 날짜를 검증하지 않음

**위치:** `src/main/java/teamdevhub/devhub/core/user/domain/User.java` — `ban(LocalDateTime blockEndDate)`

**문제:**
```java
public void ban(LocalDateTime blockEndDate) {
    if (this.deleted) throw DomainRuleException.of(ErrorCode.USER_WITHDRAWN);
    if (this.blocked) throw DomainRuleException.of(ErrorCode.USER_ALREADY_BANNED);
    this.blocked = true;
    this.blockEndDate = blockEndDate;
    // blockEndDate가 과거 날짜여도 정지 처리됨
}
```
또한 `assertActive()` 메서드는 `blocked` 불리언만 확인하며 `blockEndDate`가 이미 지났는지는 검사하지 않는다.
정지 기간이 만료된 사용자도 차단된 상태로 남아 있을 수 있다.

**중요한 이유:**
관리자가 실수로 과거 날짜를 입력해도 정지 처리가 완료된다.
만료된 blockEndDate를 가진 사용자가 시스템에 접근하지 못하는 버그로 이어질 수 있다.

**권장 수정 방법:**
```java
public void ban(LocalDateTime blockEndDate) {
    if (this.deleted) throw DomainRuleException.of(ErrorCode.USER_WITHDRAWN);
    if (this.blocked) throw DomainRuleException.of(ErrorCode.USER_ALREADY_BANNED);
    if (blockEndDate != null && !blockEndDate.isAfter(LocalDateTime.now())) {
        throw DomainRuleException.of(ErrorCode.INVALID_BAN_END_DATE);
    }
    this.blocked = true;
    this.blockEndDate = blockEndDate;
}
```
`assertActive()`도 `blockEndDate` 만료 여부를 함께 검사하도록 수정한다.

---

## 🟡 Minor

### 5. `updateUser()` — 변경 없을 때도 항상 `save()` 호출

**위치:** `src/main/java/teamdevhub/devhub/core/user/application/service/AdminUserManagementService.java` — `updateUser()`

**문제:**
```java
user.updateBasicProfile(new UpdateUserCommand(...));
userRepository.save(user);  // 변경 여부와 무관하게 항상 저장
```
`updateBasicProfile()`이 아무 변경을 하지 않아도 `save()`가 호출된다.
감사 로그(audit trail)가 오염되고 불필요한 DB 쓰기가 발생한다.

**권장 수정 방법:**
`updateBasicProfile()`이 변경 여부를 나타내는 boolean 또는 `ChangeResult`를 반환하도록 수정하거나,
변경 전후 값을 비교하여 실제 변경이 있을 때만 `save()`를 호출한다.

---

### 6. `ReportEntity.commentGuid` — `nullable = false` 제약이 실제 도메인과 불일치

**위치:** `src/main/java/teamdevhub/devhub/outbound/board/adapter/entity/ReportEntity.java`

**문제:**
```java
@Column(nullable = false)
private String commentGuid;
```
게시판 신고 시 댓글이 없는 경우에도 `commentGuid`가 필수값으로 설정되어 있다.
게시글 자체에 대한 신고는 `commentGuid`를 가질 수 없으므로 DDL 제약과 실제 데이터가 충돌한다.

**권장 수정 방법:**
```java
@Column(nullable = true)
private String commentGuid;
```
신고 대상이 댓글인 경우에만 `commentGuid`가 존재하도록 도메인 규칙과 엔티티 제약을 일치시킨다.

---

### 7. `AdminUserController.banUser()` — `@RequestBody`에 `@Valid` 누락

**위치:** `src/main/java/teamdevhub/devhub/api/user/controller/AdminUserController.java` — `banUser()`

**문제:**
```java
public ResponseEntity<...> banUser(
        @PathVariable String userGuid,
        @RequestBody AdminBanUserRequestDto requestDto) {  // @Valid 없음
```
`AdminBanUserRequestDto`의 Bean Validation 어노테이션이 실제로 동작하지 않는다.

**권장 수정 방법:**
```java
public ResponseEntity<...> banUser(
        @PathVariable String userGuid,
        @Valid @RequestBody AdminBanUserRequestDto requestDto) {
```

---

### 8. `AdminUserFacade` — 쓰기 메서드가 포함된 UseCase를 조회 목적으로 주입

**위치:** `src/main/java/teamdevhub/devhub/core/user/port/in/facade/AdminUserFacade.java`

**문제:**
```java
private final ProjectApplicationUseCase projectApplicationUseCase;
```
`ProjectApplicationUseCase`는 지원서 생성/수정 등 쓰기 메서드를 포함하고 있으나, Facade에서는 `findByApplicantGuid()` 조회만 사용한다.
불필요하게 넓은 권한의 인터페이스를 주입받고 있다.

**권장 수정 방법:**
`findByApplicantGuid()`를 `ProjectApplicationQueryUseCase` 인터페이스로 분리하고, Facade는 이 조회 전용 인터페이스를 주입받는다.

---

### 9. `FakeReportQueryRepository` — 페이지네이션 미구현으로 테스트 사각지대 발생

**위치:** `src/test/java/teamdevhub/devhub/fake/pure/application/port/out/report/FakeReportQueryRepository.java`

**문제:**
```java
@Override
public PageResult<Report> findByReportedUser(String userGuid, PageCommand pageCommand) {
    List<Report> results = store.values().stream()
            .filter(r -> r.getReportedUserGuid().equals(userGuid))
            .toList();
    return PageResult.of(results, ...);  // PageCommand 무시
}
```
`PageCommand`의 `page`, `size`를 무시하고 항상 전체 결과를 반환한다.
다중 페이지 시나리오에서의 동작은 테스트로 검증되지 않는다.

**권장 수정 방법:**
`PageCommand.size()`와 `PageCommand.page()`를 적용하여 결과를 슬라이싱하는 로직을 추가한다.
또는 명시적으로 "이 Fake는 단일 페이지만 지원한다"는 제약을 문서화하고, 실제 페이지네이션 동작은 어댑터 통합 테스트에서 검증한다.

---

## 잘 된 점

- 도메인 메서드(`ban()`, `unban()`)를 통한 상태 변경 — setter 미사용
- `ErrorCode` 열거형을 통한 에러 메시지 관리 (`USER_ALREADY_BANNED`, `USER_NOT_BANNED` 추가)
- Fake 구현체를 활용한 단위 테스트 — Mockito 미사용
- `AdminUserManagementServiceTest`, `UserTest` 테스트 커버리지 충실
- Report 도메인을 기존 `ReportEntity`를 재활용하면서 `core/` 레이어로 올바르게 분리

