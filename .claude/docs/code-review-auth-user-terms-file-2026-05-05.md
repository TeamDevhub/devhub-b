# Code Review: Auth · User · Terms · File Domains

**Date:** 2026-05-05  
**Branch:** feature/user  
**Reviewer:** code-review-agent (via super-agent)  
**Scope:** `auth` · `user` · `terms` · `file`

---

## Executive Summary

The four domains follow hexagonal architecture consistently: port interfaces are respected, domain classes are pure Java with static factory methods, and Fake-based unit tests are used correctly throughout. However, **two correctness bugs block production use** (TermsAdapter saves domain objects directly to JPA, FileFacade has no transaction wrapping a multi-file upload), **one architecture inversion** exists in AdminUserFacade, and **a cluster of security issues** — hardcoded localhost redirects, insecure refresh-token cookies, wildcard CORS exposure header, an open unauthenticated `POST /terms` endpoint, and a path-traversal risk in LocalFileStorage — must be resolved before release. Several Fake implementations also violate the project's core no-null / no-RuntimeException rules, making those test doubles unreliable.

---

## Review Priority Order

1. Correctness / hidden bugs  
2. Security risks  
3. Transaction / concurrency issues  
4. Architecture boundary violations  
5. Maintainability / readability  
6. Test gaps  
7. Performance concerns  
8. Naming / style consistency  

---

## Findings

---

### AUTH DOMAIN

---

#### [CRITICAL] `OauthController` — Hardcoded `http://localhost:5173` redirect URLs

**Location:** `api/auth/controller/OauthController.java:74,76`

**Problem:**
```java
response.sendRedirect("http://localhost:5173/");
String redirectUrl = "http://localhost:5173/auth/signup" + "?token=" + oauthAuthResult.tempToken();
```
Both the post-login and post-OAuth-signup redirects are hardcoded development URLs.

**Why it matters:** This is a production-blocking defect. All social login completions redirect to the developer's localhost in any non-development environment.

**Fix:** Inject via `@Value("${app.frontend.base-url}")`.

---

#### [CRITICAL] `CookieFactory` — `secure=false` on the Refresh Token cookie

**Location:** `api/auth/controller/CookieFactory.java:21`

**Problem:**
```java
return ResponseCookie.from(REFRESH_COOKIE_NAME, refreshToken)
        .httpOnly(true)
        .secure(false)   // explicit false
        ...
```
The 14-day refresh token is transmitted over plain HTTP.

**Why it matters:** Network-level interception exposes the long-lived refresh credential. `httpOnly` alone is insufficient when `secure` is false.

**Fix:** Set `secure(true)`. Use a `@Value`-injected property to disable for local development only.

---

#### [CRITICAL] `WebSecurityConfig` — `addExposedHeader("*")` exposes all headers cross-origin

**Location:** `shared/config/WebSecurityConfig.java:67`

**Problem:**
```java
config.addExposedHeader("*");
```
Every response header — including `Authorization` — is exposed to JavaScript on allowed origins.

**Why it matters:** Exposes access tokens and any internal server headers to cross-origin scripts.

**Fix:** Replace with `config.addExposedHeader(HttpHeaders.AUTHORIZATION)`.

---

#### [HIGH] `OauthController.signup` — Missing `@Valid` on request body

**Location:** `api/auth/controller/OauthController.java:87`

**Problem:** `@RequestBody SignupOauthRequestDto` has no `@Valid`. Bean Validation constraints will not execute.

**Fix:** Add `@Valid` before `@RequestBody`.

---

#### [HIGH] `JwtTokenCodec` — `ZoneId.systemDefault()` for token expiry

**Location:** `outbound/auth/infrastructure/token/JwtTokenCodec.java:165-167`

**Problem:**
```java
private Date toDate(LocalDateTime ldt) {
    return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
}
```
Container timezone misconfiguration silently shifts all token expiry times.

**Fix:** Use `ZoneOffset.UTC` explicitly.

---

### USER DOMAIN

---

#### [CRITICAL] `UserReview.validateScoreStep` — Floating-point modulo corrupts step validation

**Location:** `core/user/domain/UserReview.java:71-74`

**Problem:**
```java
double normalized = score / SCORE_STEP;   // 3.0 / 0.5 = 6.0000000000000004 in IEEE 754
if (normalized % 1 != 0) {
    throw DomainRuleException.of(ErrorCode.REVIEW_SCORE_INVALID);
}
```
IEEE 754 floating-point division produces tiny fractional parts for valid inputs like `1.5` or `4.5`, causing the check to throw `REVIEW_SCORE_INVALID` on legitimate scores. It can also silently pass invalid scores near step boundaries.

**Why it matters:** Both outcomes corrupt the `mannerDegree` accumulation on every affected review — a silent, irreversible data integrity failure.

**Fix:** Use an explicit allowlist:
```java
Set.of(1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0).contains(score)
```
Or use integer arithmetic: `(int)(score * 2) * 0.5 != score`.

---

#### [CRITICAL] `AdminUserFacade` — Imports `api/` DTOs into the `core/` layer

**Location:** `core/user/port/in/facade/AdminUserFacade.java:6-7`

**Problem:**
```java
import teamdevhub.devhub.api.user.model.AdminReportResponseDto;
import teamdevhub.devhub.api.user.model.AdminUserDetailResponseDto;
```
`core/` depends on `api/`, inverting the required `api → core` dependency rule.

**Why it matters:** Couples domain orchestration to HTTP presentation concerns. Breaks hexagonal architecture boundaries.

**Fix:** Move those DTOs to `core/user/port/in/facade/model/` alongside the existing `UserBasicResponseDto` and `UserDetailResponseDto`, or perform the mapping in the controller.

---

#### [HIGH] `AdminUserController.banUser` — Missing `@Valid` on request body

**Location:** `api/user/controller/AdminUserController.java:87`

**Problem:** `@RequestBody AdminBanUserRequestDto` has no `@Valid`. A null `blockEndDate` reaches `User.ban(null)` setting an indefinite ban with no API-level contract.

**Fix:** Add `@Valid`. Document the `null = permanent ban` contract explicitly in the DTO or domain method.

---

#### [HIGH] `UserSignupController` — Raw password re-used across two facade calls

**Location:** `api/user/controller/UserSignupController.java:31-52`

**Problem:** The controller calls `userSignupFacade.signup()` then immediately `authFacade.login()` with credentials taken directly from the request DTO. If signup succeeds but login fails (transient error), the user is persisted but receives no token. The raw password also travels through the controller layer twice.

**Fix:** Move the post-signup auto-login into `UserSignupFacade.signup()` so it returns `AuthResult` directly, eliminating the dual-facade controller pattern.

---

#### [MEDIUM] `UserWithdrawService` / `UserAdapter` — `delete` method actually performs a soft-delete save

**Location:** `core/user/application/service/UserWithdrawService.java:18-21` and `outbound/user/adapter/UserAdapter.java:54-56`

**Problem:**
```java
// UserAdapter
public void delete(User user) {
    jpaUserRepository.save(UserMapper.toEntity(user)); // saves with deleted=true
}
```
Method is named `delete` but calls `save`. Future developers may introduce a real `deleteById` call, causing accidental hard-deletes.

**Fix:** Rename port method and adapter to `softDelete(User user)`.

---

#### [MEDIUM] `UserProfileService.updateProfile` — Three repository calls even for username-only updates

**Location:** `core/user/application/service/UserProfileService.java:48-64`

**Problem:** `getUserWithPositionsAndSkills` always fetches user + positions + skills regardless of which fields are changing.

**Fix:** Load positions and skills lazily — call the repository only when the corresponding change flag is true in the command.

---

#### [MEDIUM] `AdminUserFacade.getUserApplyProjects` — N+1 queries

**Location:** `core/user/port/in/facade/AdminUserFacade.java:77-84`

**Problem:** One `getProjectDetail` call per application record per page. 50 applications = 51 queries.

**Fix:** Add a batch `getProjectDetails(List<String> guids)` method to `ProjectUseCase` and perform a single `IN` query.

---

#### [LOW] `FakeUserProfileUseCase.updateProfileImage` — Empty method body

**Location:** `test/.../FakeUserProfileUseCase.java:51-53`

**Problem:** Empty `@Override` body violates the project rule prohibiting empty methods in Fake implementations.

**Fix:** Add an `imageCalled` tracking flag, set it `true` in the method body.

---

#### [LOW] `UserLoginServiceTest` — Test method name breaks naming convention

**Location:** `test/.../UserLoginServiceTest.java:89-90`

**Problem:** `updateLastLoginDateWhenLogin` does not follow the `method_condition_outcome` pattern used by all sibling tests.

**Fix:** Rename to `updateLastLoginDateTime_normalUser_updatesLastLoginDateTime`.

---

#### [LOW] `UserReviewServiceTest` — Missing self-review and score-step boundary tests

**Location:** `test/.../UserReviewServiceTest.java`

**Problem:** No test exercises `ErrorCode.REVIEW_SELF_NOT_ALLOWED` or `ErrorCode.REVIEW_SCORE_INVALID`.

**Fix:** Add `reviewMember_selfReview_throwsDomainException`, `reviewMember_scoreBelowMin_throwsDomainException`, `reviewMember_scoreInvalidStep_throwsDomainException`.

---

### TERMS DOMAIN

---

#### [CRITICAL] `TermsAdapter.saveTerms` — Saves domain object directly to `JpaRepository`

**Location:** `outbound/terms/adapter/TermsAdapter.java`

**Problem:** `jpaTermsRepository.save(terms)` where `terms` is a domain `Terms` object but the repository expects `TermsEntity`. `JpaTermsRepository` also re-declares `void save(Terms terms)` which shadows `JpaRepository.save()` and will never behave as a JPA persistence call. Every call to `termsFacade.registerTerms()` fails at runtime.

**Why it matters:** Terms registration is completely non-functional.

**Fix:** Call `TermsMapper.toEntity(terms)` before saving. Remove the illegal `void save(Terms terms)` re-declaration from `JpaTermsRepository`.

---

#### [CRITICAL] `WebSecurityConfig` — `POST /terms` is fully unauthenticated

**Location:** `shared/config/WebSecurityConfig.java:103` (`.requestMatchers("/terms/**").permitAll()`)

**Problem:** All HTTP methods on `/terms/**` are open. Anonymous callers can POST arbitrary terms with `isRequired=true`, polluting the terms table and forcing users to agree to fabricated content on next signup.

**Why it matters:** Privilege escalation / data integrity attack vector.

**Fix:** Change to `requestMatchers(HttpMethod.GET, "/terms/**").permitAll()`. Protect `POST /terms` with `.hasRole("ADMIN")`.

---

#### [CRITICAL] `TermsService.validateAllTermsExist` — Throws `ErrorCode.UNKNOWN_FAIL`

**Location:** `core/terms/application/TermsService.java:78`

**Problem:** Uses a placeholder error code explicitly prohibited by project rules.

**Fix:** Add a dedicated `ErrorCode` (e.g., `TERMS_NOT_FOUND`) and use it here.

---

#### [MAJOR] `TermsService` — Uses `jakarta.transaction.Transactional` instead of Spring's

**Location:** `core/terms/application/TermsService.java`

**Problem:** Import is `jakarta.transaction.Transactional`; everywhere else in the codebase uses `org.springframework.transaction.annotation.Transactional`. In some Spring proxy configurations, the Jakarta annotation is not intercepted by Spring's transaction infrastructure.

**Fix:** Change to `org.springframework.transaction.annotation.Transactional`.

---

#### [MAJOR] `TermsAdapter` — Implements two port interfaces in one class

**Location:** `outbound/terms/adapter/TermsAdapter.java`

**Problem:** `TermsAdapter` implements both `TermsRepository` and `TermsAgreementRepository`, coupling two distinct port interfaces into a single adapter and breaking isolation.

**Fix:** Split into `TermsAdapter` (implements `TermsRepository`) and `TermsAgreementAdapter` (implements `TermsAgreementRepository`).

---

#### [MAJOR] `CreateTermsRequestDto` — Not a record, exposes `isDeleted`, no validation

**Location:** `api/terms/model/CreateTermsRequestDto.java`

**Problem:**
1. POJO class instead of `record`.
2. Exposes `isDeleted` — a caller can POST `isDeleted: true` to delete a term on creation.
3. No `@NotBlank` on `title`/`content`; blank values reach a `nullable=false` DB column and throw `DataIntegrityViolationException`.

**Fix:** Convert to `record`. Add `@NotBlank` to `title` and `content`. Remove `isDeleted` from the DTO and `CreateTermsCommand` — the service must always set it `false`.

---

#### [MAJOR] `TermsMapper` — Dead code; both mappers missing private constructors

**Location:** `outbound/terms/adapter/mapper/TermsMapper.java`

**Problem:** `TermsMapper.toEntity(TermsAgreement)` is never called (the adapter uses `TermsAgreementMapper` instead). Neither mapper class has the `private ctor()` required by persistence rules.

**Fix:** Remove the dead method. Add `private TermsMapper() {}` and `private TermsAgreementMapper() {}`.

---

#### [MEDIUM] `FakeTermsAgreeUseCase.listTerms` — Returns `null`

**Location:** `test/.../FakeTermsAgreeUseCase.java:17`

**Problem:** Violates the project's Fake rule: "Do not return null from Fake objects."

**Fix:** Return `List.of()`.

---

#### [MEDIUM] `FakeTermsRepository.findByTermsGuid` — Returns `null` on miss

**Location:** `test/.../FakeTermsRepository.java:33`

**Problem:** `store.get(termsGuid)` returns `null` when absent; port contract expects either a value or an exception.

**Fix:** Throw `AdapterDataException.of(ErrorCode.TERMS_NOT_FOUND)` when absent.

---

#### [MEDIUM] `TermsResponseDto` — Lives in `core/` layer, is not a `record`

**Location:** `core/terms/port/in/facade/models/TermsResponseDto.java`

**Problem:** A response DTO for the API layer lives in `core/`. This bleeds HTTP presentation concerns into the domain. Also a POJO class instead of `record`.

**Fix:** Move to `api/terms/model/response/TermsResponseDto.java` and convert to `record`.

---

#### [MINOR] `TermsAgreementEntity` — No uniqueness constraint on `(user_guid, terms_guid)`

**Location:** `outbound/terms/adapter/entity/TermsAgreementEntity.java`

**Problem:** A user can agree to the same term repeatedly; no uniqueness constraint or service-level deduplication check.

**Fix:** Add `@UniqueConstraint(columnNames = {"user_guid", "terms_guid"})`. Add a service-level guard or handle `DataIntegrityViolationException` with a specific `ErrorCode`.

---

#### [MINOR] `AgreeTermsRequestDto` — `@NotNull` on primitive `boolean`

**Location:** `api/terms/model/AgreeTermsRequestDto.java`

**Problem:** `@NotNull` on a primitive `boolean` is a no-op. Primitives can never be `null`.

**Fix:** Remove `@NotNull`, or convert to `Boolean` (boxed) if null rejection is intentional.

---

### FILE DOMAIN

---

#### [CRITICAL] `LocalFileStorage` — No path traversal guard

**Location:** `outbound/file/infrastructure/LocalFileStorage.java`

**Problem:** `fileGuid` from HTTP path parameters is resolved directly into a filesystem path with no normalization check. A crafted value like `../../etc/passwd` can escape the storage root.

**Why it matters:** Arbitrary file read or delete from the server filesystem.

**Fix:**
```java
Path resolved = rootPath.resolve(prefix).resolve(fileGuid).normalize();
if (!resolved.startsWith(rootPath.normalize())) {
    throw ExternalServiceException.of(ErrorCode.FILE_INVALID);
}
```

---

#### [MAJOR] `FileFacade` — Missing `@Transactional`

**Location:** `core/file/port/in/facade/FileFacade.java`

**Problem:** `FileFacade` is `@Service` with no `@Transactional`. `upload` iterates files and calls `fileUseCase.upload()` in a loop — each in a separate transaction. A failure on file `n` leaves files `1..n-1` committed to the DB as orphaned metadata with no corresponding files on disk.

**Fix:** Add `@Transactional` at class level on `FileFacade`.

---

#### [MAJOR] `FileService.delete` — Deletes physical file before DB record

**Location:** `core/file/application/FileService.java`

**Problem:** `fileStorage.delete(fileGuid)` (filesystem) runs before `fileMetadataRepository.deleteByFileGuid(fileGuid)` (DB). If the DB delete fails, the physical file is gone but the DB record remains, pointing to a non-existent file.

**Fix:** Delete the DB metadata first (within the transaction), then delete the physical file after commit. Log orphaned paths for cleanup if the physical delete fails.

---

#### [MAJOR] `FileService` — Method-level `@Transactional` violates class-level-only convention

**Location:** `core/file/application/FileService.java:23,38`

**Problem:** `upload` and `find` have their own `@Transactional` annotations in addition to the class-level one. The project rule: "Declare `@Transactional` at the class level only."

**Fix:** Remove both method-level `@Transactional` annotations.

---

#### [MEDIUM] `FakeFileMetadataRepository` and `FakeFileUseCase` — Throw raw `RuntimeException`

**Location:** `test/.../FakeFileMetadataRepository.java:24` and `test/.../FakeFileUseCase.java:42`

**Problem:** Missing keys throw `new RuntimeException(fileGuid)` and `new RuntimeException(new FileNotFoundException(...))`. Test assertions that match `RuntimeException.class` will pass for the wrong reasons and mask future type changes in the real adapter.

**Fix:** Throw `AdapterDataException.of(ErrorCode.FILE_NOT_FOUND)` from both Fakes. Update test assertions to match the specific exception type and message.

---

#### [MEDIUM] `FileController.selectFile` — Wrong `SuccessCode`

**Location:** `api/file/controller/FileController.java:53`

**Problem:** `selectFile` (GET `/{fileGuid}/meta`) returns `SuccessCode.CREATE_SUCCESS` for a read operation.

**Fix:** Change to `SuccessCode.READ_SUCCESS`.

---

#### [MEDIUM] `FileController.view` / `download` — Bypass `DataApiResponseDto`

**Location:** `api/file/controller/FileController.java`

**Problem:** Binary streaming endpoints return `ResponseEntity<byte[]>` directly. The project rule requires `DataApiResponseDto<T>` wrapping for all API responses.

**Fix:** If binary streaming endpoints are intentionally exempted from the wrapper (which is architecturally reasonable), document the exemption explicitly. Otherwise, these endpoints are non-conforming.

---

#### [MEDIUM] `FileStorageProperties` — Public setter violates no-setter rule

**Location:** `outbound/file/infrastructure/FileStorageProperties.java`

**Problem:** A public `setRootPath(Path)` method allows arbitrary runtime changes to the storage root path. The project prohibits setters on domain and configuration classes.

**Fix:** Use constructor binding with `@ConstructorBinding` in Spring Boot 3.x. If a setter is required for `@ConfigurationProperties` binding, narrow its visibility.

---

#### [MINOR] `FileControllerTest` — Entirely commented out

**Location:** `test/.../FileControllerTest.java`

**Problem:** The entire test file is disabled via comments, violating the "do not disable code with comments" rule. The commented content also references a stale constructor that no longer matches the current `FileResponseDto` record.

**Fix:** Implement the controller test properly (use `@MockitoBean` for the facade as per testing rules) or delete the file entirely.

---

## Priority Fix List

### Block Release (Critical Security + Correctness)

1. **`TermsAdapter.saveTerms`** — Map domain object to entity before saving; remove the illegal JPA re-declaration. Terms registration is currently broken.
2. **`POST /terms` open to anonymous callers** — Apply `hasRole("ADMIN")` to the create endpoint; allow only `GET /terms/**` publicly.
3. **`LocalFileStorage` path traversal** — Add normalize + startsWith guard before all file system operations.
4. **`UserReview.validateScoreStep` floating-point bug** — Replace with allowlist or integer-based check; current code silently corrupts user reputation data.
5. **`OauthController` hardcoded `localhost:5173` redirects** — Inject from `@Value("${app.frontend.base-url}")`.
6. **`CookieFactory` `secure=false`** — Set `secure(true)` on the refresh token cookie.
7. **`WebSecurityConfig` `addExposedHeader("*")`** — Replace with `addExposedHeader(HttpHeaders.AUTHORIZATION)`.

### High Priority (Fix Before Merge)

8. **`AdminUserFacade` imports `api/` DTOs into `core/`** — Move to `core/user/port/in/facade/model/` or map in the controller.
9. **`FileFacade` missing `@Transactional`** — Wrap multi-file upload in a single transaction.
10. **`FileService.delete` ordering** — Delete DB record first, then physical file.
11. **`TermsService` Jakarta import** — Switch to `org.springframework.transaction.annotation.Transactional`.
12. **`CreateTermsRequestDto`** — Convert to `record`, add validation annotations, remove `isDeleted`.
13. **`validateAllTermsExist` throws `ErrorCode.UNKNOWN_FAIL`** — Replace with a specific `ErrorCode`.
14. **`UserSignupController` dual-facade raw-password pattern** — Move auto-login into `UserSignupFacade`.
15. **`OauthController.signup` and `AdminUserController.banUser` missing `@Valid`** — Add to both.
16. **`JwtTokenCodec.toDate`** — Use `ZoneOffset.UTC`.

### Medium Priority

17. `UserAdapter.delete` misleading name — rename to `softDelete`.
18. `UserProfileService.updateProfile` — lazy-load positions/skills.
19. `AdminUserFacade.getUserApplyProjects` N+1 — batch project lookup.
20. `TermsAdapter` split — one adapter per port interface.
21. `TermsMapper` dead code removal + private constructors on both mappers.
22. `FakeTermsAgreeUseCase.listTerms` — return `List.of()`.
23. `FakeTermsRepository.findByTermsGuid` — throw typed domain exception.
24. `FakeFileMetadataRepository` / `FakeFileUseCase` — throw typed domain exceptions.
25. `FileController.selectFile` wrong `SuccessCode` — use `READ_SUCCESS`.
26. `TermsResponseDto` — move to `api/` layer, convert to `record`.
27. `FileStorageProperties` public setter — use constructor binding.

### Minor / Cleanup

28. `FakeUserProfileUseCase.updateProfileImage` — add call-tracking flag.
29. `UserLoginServiceTest` test method rename to `method_condition_outcome` format.
30. Add `UserReviewServiceTest` cases: self-review, score boundary, invalid step.
31. `TermsAgreementEntity` — add unique constraint on `(user_guid, terms_guid)`.
32. `AgreeTermsRequestDto` — remove `@NotNull` from primitive `boolean`.
33. `FileControllerTest` — implement properly or delete.

---

## Strengths

- `User` domain class: private constructor, static factory methods per role (`createGeneralUser`, `createAdminUser`, `createOauthUser`), all state changes via domain methods, no setters, no Spring annotations.
- `UserReview.create()` encapsulates range-check and self-review validation inside the factory — invalid objects cannot be constructed.
- `UserWithdrawFacade` correctly wraps cross-usecase concerns (withdraw + token revocation) in a single facade transaction.
- `VerificationService.assertIssuable()` prevents re-sending a code while a valid unexpired one exists — a commonly missed guard.
- `OauthController.handleOauthCallback` implements CSRF state cookie validation correctly (cookie vs. query-param comparison, expires cookie on receipt).
- `UserCredentialService.getUserForReissue` performs two-stage token validation: parse JWT → lookup stored token → compare. Prevents token substitution attacks.
- `Terms` domain class: private `@Builder`, static factory methods (`of`, `createTerms`, `createAgreement`), all validation inside domain methods with specific `ErrorCode` values.
- `FileMetadata` uses a `record` with a static `create` factory that runs validation inline.
- `FileMetadataMapper` has a private constructor blocking instantiation and provides both `toEntity` and `toDomain` directions.
- `UploadFileResponseDto.from` uses `Map.copyOf` for defensive copying — correct immutability practice.
- All reviewed unit tests use Fake implementations, GWT structure, Korean `@DisplayName`, and `ErrorCode` enum references in assertions.

---

## Final Verdict

**Request Changes — Block Release**

Seven issues independently block release: the broken `TermsAdapter.saveTerms`, the open `POST /terms` endpoint, the path traversal in `LocalFileStorage`, the floating-point score validation bug, the hardcoded OAuth redirects, the insecure refresh-token cookie, and the wildcard CORS exposure header. The architecture inversion in `AdminUserFacade` and the non-atomic `FileFacade.upload` must also be resolved before merging to `dev`. All remaining items should be tracked as follow-up tasks.
