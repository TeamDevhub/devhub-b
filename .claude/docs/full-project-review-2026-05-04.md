# Code Review Result — Full Project

## Review Target

Full project — all domains and layers (auth, user, project, board, application, notification, file, terms, admin, report)

## Executive Summary

The codebase has matured since the initial review: the most glaring security gaps (OAuth CSRF, LoggingAspect password leak) have been partially addressed, and the hexagonal structure is largely intact. However, several correctness bugs remain unresolved, including `ProjectAdapter.getProjectDetail()` still calling `.get()` without `orElseThrow`, inline production comments that expose unfinished design decisions, and multiple controllers that accept `@RequestBody` input without `@Valid`. A deeper new finding is that `UserReviewService` crosses domain boundaries by directly depending on `ProjectRepository` and `ProjectMemberRepository` from the project domain, violating single-domain service responsibility. Two services (`UserSignupService`, `TermsService`) use `jakarta.transaction.Transactional` instead of `org.springframework.transaction.annotation.Transactional`, silently bypassing Spring's transaction proxy in most configurations. The project is not release-ready: correctness and security issues require resolution first.

---

## Findings

### Auth Domain

#### `CustomAuthenticationProvider` — `orElseThrow()` with no argument exposes stack trace
- **Severity**: Major
- **Location**: `outbound/auth/adapter/CustomAuthenticationProvider.java:30` — `findByEmail(email).orElseThrow()`
- **Problem**: `orElseThrow()` with no argument throws `NoSuchElementException`, which propagates uncaught through Spring Security and falls through to `GlobalExceptionHandler`, which returns HTTP 400 with the raw exception message. The exception message is "No value present," which leaks that the email does not exist.
- **Why it matters**: Distinguishable "email not found" vs "wrong password" responses are an enumeration vector. Also, NoSuchElementException is not a BusinessRuleException, so the handler wraps it as `UNKNOWN_FAIL` exposing that raw message.
- **Fix**: Replace with `.orElseThrow(() -> AdapterDataException.of(ErrorCode.AUTH_FAIL))`, and ensure `CustomAuthenticationProvider` throws `BadCredentialsException` on failure (not a domain exception) so Spring Security handles it uniformly.

---

#### `TokenParseProvider` port interface imports outbound infrastructure types
- **Severity**: Major
- **Location**: `core/auth/port/out/token/TokenParseProvider.java:3-4`
- **Problem**: The port interface directly imports `teamdevhub.devhub.outbound.auth.infrastructure.token.vo.AccessTokenInfo` and `TempTokenInfo`. A core port must not reference outbound types; this creates a hard dependency from the hexagonal core to the adapter layer, defeating the entire point of the port.
- **Why it matters**: Any change to the outbound token VO forces a recompile of the core port. Services and fakes that implement this interface must know about the outbound package.
- **Fix**: Move `AccessTokenInfo` and `TempTokenInfo` to `core/auth/domain/vo/` or a new `core/auth/port/out/token/vo/` package. Similarly, `AuthResult` and `OAuthAuthResult` import `outbound.auth.infrastructure.token.TokenPrefix` — `TokenPrefix` must be moved to `core` or `shared`.

---

#### `RefreshTokenAdapter.save()` — mutation of JPA entity outside transaction
- **Severity**: Major
- **Location**: `outbound/auth/adapter/RefreshTokenAdapter.java:20-24`
- **Problem**: `ifPresentOrElse` calls `refreshTokenEntity.rotate(newToken)` to mutate the entity in place. This relies on JPA dirty-checking within the active transaction. However, `RefreshTokenAdapter` is a `@Component` with no `@Transactional`; it is called from `AuthenticationService` which is `@Transactional`. While this works when called correctly, if `save()` is ever called from a non-transactional context the mutation is silently lost — no `jpaRepository.save()` is called in that branch.
- **Why it matters**: Silent data loss. The "rotate existing token" branch does not call `jpaRefreshTokenRepository.save(refreshTokenEntity)` — it relies entirely on dirty checking. This is an implicit contract that is not obvious and breaks if the calling context changes.
- **Fix**: Explicitly call `jpaRefreshTokenRepository.save(refreshTokenEntity)` in the `ifPresent` branch to make persistence explicit and not dependent on the ambient transaction's dirty-checking behavior.

---

### User Domain

#### `UserSignupService` uses `jakarta.transaction.Transactional` instead of Spring's
- **Severity**: Major
- **Location**: `core/user/application/service/UserSignupService.java:3`
- **Problem**: `import jakarta.transaction.Transactional` is used instead of `org.springframework.transaction.annotation.Transactional`. In Spring, only `org.springframework.transaction.annotation.Transactional` participates fully in Spring's transaction management (supports `readOnly`, propagation attributes, rollback rules defined in Spring). `jakarta.transaction.Transactional` is supported but with limited semantics and no `readOnly` support.
- **Why it matters**: `UserSignupFacade` (which is `@Transactional` with Spring's annotation) wraps this service. If the JTA `@Transactional` is not correctly bridged, the signup flow may run in multiple transactions instead of one, breaking atomicity between credential and profile saves.
- **Fix**: Change to `import org.springframework.transaction.annotation.Transactional` in both `UserSignupService` and `TermsService`.

---

#### `TermsService` uses `jakarta.transaction.Transactional` instead of Spring's
- **Severity**: Major
- **Location**: `core/terms/application/TermsService.java:3`
- **Problem**: Same issue as above — `jakarta.transaction.Transactional` used. The `UserSignupFacade.signup()` method is Spring `@Transactional` and calls `termsUseCase.saveTermsAgreement()`. If the JTA annotation does not correctly join the Spring transaction, terms agreement may be saved in a separate transaction that does not roll back with the rest of signup.
- **Why it matters**: A failed signup (e.g., position save error) may leave orphaned `TermsAgreement` rows because they were committed in a separate transaction.
- **Fix**: Change to `org.springframework.transaction.annotation.Transactional`.

---

#### `UserReviewService` crosses domain boundaries
- **Severity**: Major
- **Location**: `core/user/application/service/UserReviewService.java:9-10`
- **Problem**: `UserReviewService` (in the user domain) directly injects `ProjectRepository` and `ProjectMemberRepository` from the project domain. This is a cross-domain dependency from service layer to another domain's port, which violates hexagonal architecture's domain isolation rule (same violation as the known `BoardService`/`BoardQueryService` issue for `UserRepository`).
- **Why it matters**: The user domain now has a compile-time dependency on the project domain's port interfaces. Changes to project ports can break user services. Test isolation becomes difficult — fakes for two domains needed in one unit test.
- **Fix**: Move the project validation logic (is the project completed? is the user a member?) to `UserReviewFacade`, which sits at the orchestration layer where cross-domain calls are appropriate. `UserReviewFacade` already orchestrates `UserReviewUseCase` and `UserProfileUseCase` — add `ProjectUseCase` and `ProjectMemberUseCase` injections there, call them before delegating to `userReviewUseCase.reviewMember()`.

---

#### `User.applyReviewScore()` — score bias formula always grows mannerDegree
- **Severity**: Major
- **Location**: `core/user/domain/User.java:228`
- **Problem**: `this.mannerDegree += (score - 3.0)`. At a score of 3.0, mannerDegree is unchanged. At 1.0, it decreases by 2.0. At 5.0, it increases by 2.0. There is no cap or floor on `mannerDegree`. A user reviewed 100 times at 5.0 has a `mannerDegree` of 236.5 — a meaningless number. There is also no atomic / concurrent review protection.
- **Why it matters**: Business logic is broken: the initial value of 36.5 has no semantic ceiling, resulting in display values that have no meaning to users.
- **Fix**: Define explicit min/max bounds (e.g., 0.0–100.0) and clamp the value. Consider storing a running average rather than a cumulative delta.

---

#### `UserSignupService.initializeAdminUser()` — `existsByUserRole(ADMIN)` is a race condition
- **Severity**: Minor
- **Location**: `core/user/application/service/UserSignupService.java:39-49`
- **Problem**: The check `if (existsByUserRole())` followed by creating the admin user is not atomic. If the endpoint is called concurrently at startup, two admin users can be created.
- **Why it matters**: Unlikely in practice (called once at startup), but the DB has no unique constraint on `userRole=ADMIN` to prevent this.
- **Fix**: Add a unique index on `(userRole)` or use `INSERT IF NOT EXISTS` / `UPSERT` semantics at the DB level, or protect the check+insert with a distributed lock.

---

### Project Domain

#### `ProjectService` imports `outbound` Mapper directly
- **Severity**: Major
- **Location**: `core/project/application/ProjectService.java:31`
- **Problem**: `import teamdevhub.devhub.outbound.project.adapter.mapper.ProjectMapper` — a service in the `core` layer directly imports and calls `ProjectMapper` from the `outbound` layer. This is a direct violation of hexagonal dependency direction: `core` must never depend on `outbound`.
- **Why it matters**: The `core` service is now coupled to the outbound persistence implementation. Changing the mapper (e.g., introducing a different storage backend) requires changing `ProjectService`. Unit tests of `ProjectService` must bring in the outbound mapper class.
- **Fix**: Move the methods used (`toMapSkill`, `toMapRequirement`, `toMapLikeCount`, `toProjectDetail`) to a utility or domain service in `core/project/`, or refactor so the mapping happens inside the adapter layer and the service receives already-assembled domain objects.

---

#### `ProjectFacade.getUserApplyProjects()` — uses `requirementGuid` as `projectGuid`
- **Severity**: Critical (Correctness Bug)
- **Location**: `core/project/port/in/facade/ProjectFacade.java:211`
- **Problem**: `Project project = projectUseCase.getProjectDetail(projectApply.getRequirementGuid())`. The code incorrectly passes `requirementGuid` (the GUID of a project requirement row) to `getProjectDetail()`, which expects a `projectGuid`. This will either load a completely wrong project or throw `NoSuchElementException` (actually `NoSuchElementException` from `.get()` in `ProjectAdapter`).
- **Why it matters**: The "my applied projects" endpoint (`GET /user/projects/applications`) returns incorrect data or crashes at runtime for every invocation.
- **Fix**: Change to `projectUseCase.getProjectDetail(projectApply.getRequirementGuid())` → look up the project GUID from the application's associated project: the `ApplicationRepository` should expose `projectGuid` on `ProjectApplication`, or look it up via the requirement.

---

#### `ProjectFacade.getUserProjects()` — N+1 query pattern
- **Severity**: Major
- **Location**: `core/project/port/in/facade/ProjectFacade.java:180-190`
- **Problem**: For each project in the paged result, `getProjectDetail()` is called individually: `projectUseCase.getProjectDetail(item.getProjectGuid())`. This issues one DB query per project in the list — a classic N+1 problem.
- **Why it matters**: For a page of 10 projects, this issues 10 separate project detail queries plus the applications query for each. Under any meaningful load this will cause timeout issues.
- **Fix**: Either extend `ProjectRepository` to support batch detail loading by a list of GUIDs, or rethink the use case — if the paged list already contains the full project data, there is no need to call `getProjectDetail()` again.

---

#### `ProjectFacade` — inline comments in production code
- **Severity**: Minor
- **Location**: `core/project/port/in/facade/ProjectFacade.java:105`, `121-123`, `208`
- **Problem**: Comments like `// 프로젝트 지원자 조회 후 지원자가 있으면 return...`, `// 인증테이블 분리에 따라 추후 변경 필요`, and `// 작업 예정` indicate deferred or unfinished design.
- **Why it matters**: Violates the project principle "do not disable code with comments" / "do not leave TODOs unmanaged." The comment on line 121-123 indicates the email is incorrectly set to the `userGuid` field value: `String email = userProfileUseCase.getUserInfo(project.getUserGuid()).getUserGuid()` — this is assigning a GUID to an `email` variable, which is clearly wrong data.
- **Fix**: Fix the `email = getUserInfo().getUserGuid()` bug (should be fetching the email from credentials, not the user profile). Track remaining deferred work in the issue tracker, not source comments.

---

#### `Project.getRecruitStatus()` — NPE when dates are null (previously documented — still present)
- **Severity**: Critical (Correctness)
- **Location**: `core/project/domain/Project.java:96-105`
- **Problem**: `this.recruitmentStartDate` and `this.recruitmentEndDate` can be null (they are not marked non-null and `createProject` does not require them). `now.isBefore(null)` throws `NullPointerException`.
- **Why it matters**: Any list display of projects where a project has no recruitment dates set crashes the call.
- **Fix**: Add null guards before the date comparisons; return `WAITING` if dates are null, or enforce non-null in the factory method.

---

### Board Domain

#### `BoardController.deleteBoard()` — no authentication guard, no ownership check
- **Severity**: Critical (Security)
- **Location**: `api/board/controller/BoardController.java:115`
- **Problem**: `POST /boards/delete` accepts a `@RequestBody DeleteBoardRequestDto` (list of GUIDs) with no `@LoginUser` parameter and no authorization check. Any unauthenticated caller can delete any set of boards by GUID.
- **Why it matters**: An unauthenticated attacker can delete all boards by guessing or collecting GUIDs. There is zero access control on this endpoint.
- **Fix**: Add `@LoginUser AuthenticatedUser authenticatedUser` to the method signature, verify the caller is an admin or owns the boards, and add the ownership/role check in the service/facade layer.

---

#### `BoardController.createBoard()` and `updateBoard()` — missing `@Valid`
- **Severity**: Major
- **Location**: `api/board/controller/BoardController.java:57, 98`
- **Problem**: `createBoard` and `updateBoard` accept `@RequestBody` DTOs but neither has `@Valid`. Any bean validation annotations on the DTO fields are silently ignored.
- **Why it matters**: Invalid input (e.g., empty title, null category) reaches the service without validation, causing opaque downstream failures or data corruption.
- **Fix**: Add `@Valid` before `@RequestBody` on both methods, and verify that `CreateBoardRequestDto` and `UpdateBoardRequestDto` carry the appropriate validation annotations.

---

#### `BoardService.detailBoard()` — inline comment in production code
- **Severity**: Minor
- **Location**: `core/board/application/BoardService.java:62-64`
- **Problem**: A multi-line block comment `/** 인증테이블 분리에 따라 추후 변경 필요 */` appears inside the method body.
- **Why it matters**: Violates the "no comments" and "no TODOs" conventions. Indicates deferred design work not tracked in the issue system.
- **Fix**: Remove the comment. If the change is genuinely needed, file an issue.

---

### Application Domain

#### `ProjectApplicationService` — class-level `@Transactional(readOnly = true)` with method-level overrides is correct, but domain object construction bypasses factory
- **Severity**: Minor
- **Location**: `core/application/application/ProjectApplicationService.java:32-57`
- **Problem**: `ProjectApplication` is built with `ProjectApplication.builder()` directly in the service, bypassing any factory method pattern. `ProjectApplication` has a public `@Builder` which allows arbitrary construction. The same applies to `ProjectApplicationAnswer.builder()`.
- **Why it matters**: The project convention requires domain objects to be created only via static factory methods. Direct builder usage in services means invariant checks that should live in the domain (e.g., validating `statusCd`) are absent.
- **Fix**: Add a `ProjectApplication.create(...)` static factory that validates required fields; call it from the service.

---

#### `ProjectApplicationFacade` — imports API response types inside the core layer
- **Severity**: Major
- **Location**: `core/application/port/in/facade/ProjectApplicationFacade.java:8-16`
- **Problem**: This facade in `core/` directly imports `teamdevhub.devhub.api.application.model.response.*` — response DTO classes from the API layer. The core layer must not depend on the API layer; dependency must flow only inward (api → core).
- **Why it matters**: This is an inverted dependency in the hexagonal model. Changing API response DTOs can break core services. The facade cannot be tested independently of the Spring MVC layer.
- **Fix**: The facade should return core model objects or inner result records. The controller or a dedicated converter at the API layer should map to the response DTOs. Move `ProjectApplicationListResponseDto`, `ProjectApplicationDetailWrapperResponseDto`, etc. to be assembled in the controller or an API-layer assembler.

---

#### `ApplicationFormFacade` (core/application/port/in/facade) — same API import violation
- **Severity**: Major
- **Location**: `core/application/port/in/facade/ApplicationFormFacade.java:8-16`
- **Problem**: Also imports `teamdevhub.devhub.api.web.model.response.DataListApiResponseDto` and `PageResponseDto`. Core facades must not return API wrapper types.
- **Why it matters**: Same architectural violation — core depends on API layer.
- **Fix**: Return `PageResult<ApplicationForm>` from the facade method; let the controller wrap it in `DataListApiResponseDto`.

---

#### `BoardFacade` — same API import violation
- **Severity**: Major
- **Location**: `core/board/port/in/Facade/BoardFacade.java:9-10`
- **Problem**: Imports and returns `DataApiResponseDto` and `DataListApiResponseDto` from the API layer. The facade wraps responses in `DataApiResponseDto.successWithoutData(SuccessCode.CREATE_SUCCESS)` — these are HTTP-layer concerns.
- **Why it matters**: Same pattern as above. The core facade is coupled to HTTP response serialization details.
- **Fix**: Facades should return domain objects or void. Controllers are responsible for wrapping in response DTOs.

---

### Security / Access Control

#### `ProjectController.deleteProject()` — no ownership or role check
- **Severity**: Critical (Security)
- **Location**: `api/project/controller/ProjectController.java:120-126`
- **Problem**: `DELETE /projects/{projectGuid}` accepts any authenticated user's request without verifying that the caller is the project owner. The `authenticatedUser` is not even passed to `projectFacade.deleteProject(projectGuid)`.
- **Why it matters**: Any authenticated user can delete any project by GUID.
- **Fix**: Pass `authenticatedUser.userGuid()` to the facade/service and verify `project.getUserGuid().equals(callerGuid)` before deletion.

---

#### `ProjectController.updateProject()` — no ownership check
- **Severity**: Critical (Security)
- **Location**: `api/project/controller/ProjectController.java:103-112`
- **Problem**: Same pattern — `updateProject` receives `@LoginUser AuthenticatedUser authenticatedUser` but passes only `projectGuid` and `updateProjectCommand` to the facade. The `authenticatedUser.userGuid()` is not forwarded and the service does not verify ownership.
- **Why it matters**: Any authenticated user can update any project.
- **Fix**: Forward `authenticatedUser.userGuid()` to the service, compare with `project.getUserGuid()`.

---

#### `ApplicationController.approveApplication()` — no project ownership check
- **Severity**: Critical (Security)
- **Location**: `api/application/controller/ApplicationController.java:36-44`
- **Problem**: `PUT /projects/applications/{applicationGuid}/approve` accepts `approved` from any authenticated user. The `approverGuid` is the authenticated user's GUID but there is no check that this user is the project owner/leader before allowing approval.
- **Why it matters**: Any authenticated user can approve or reject any project's applications.
- **Fix**: In `ProjectApplicationFacade.approveApplication()`, load the application, get its project, and verify that `approverGuid.equals(project.getUserGuid())`.

---

#### `CookieFactory` — `secure=false` on both refresh and OAuth state cookies (previously documented, still present)
- **Severity**: Critical (Security)
- **Location**: `api/auth/controller/CookieFactory.java:21,31`
- **Problem**: Both `createRefreshTokenCookie` and `createOAuthStateCookie` use `.secure(false)`.
- **Why it matters**: Refresh tokens and OAuth state cookies will be transmitted over HTTP, making them vulnerable to interception.
- **Fix**: Set `.secure(true)` and configure a HTTPS-only deployment, or make secure configurable via `@Value`.

---

#### CORS allows `*` exposed headers with credentials
- **Severity**: Major
- **Location**: `shared/config/WebSecurityConfig.java:65-66`
- **Problem**: `config.addExposedHeader("*")` with `config.setAllowCredentials(true)`. Browsers reject wildcard exposed headers when `allowCredentials` is true (CORS specification error), and when this is silently allowed by older browsers, it leaks all response headers including `Set-Cookie` and `Authorization` to cross-origin JavaScript.
- **Why it matters**: Exposes sensitive headers to cross-origin requests; may cause silent CORS failures in strict browsers.
- **Fix**: Explicitly list only the headers that cross-origin clients need (e.g., `Authorization`). Remove the wildcard.

---

#### `boardView` view-count cookie is not `HttpOnly` and not sanitized
- **Severity**: Major
- **Location**: `api/board/controller/BoardController.java:72-93`
- **Problem**: The `boardView` cookie is set via `javax.servlet.http.Cookie` (not `ResponseCookie`) with no `HttpOnly`, no `SameSite`, and no sanitization of the `boardGuid` value concatenated into the cookie value. If a `boardGuid` could ever contain `]` or `_` characters, the parsing logic `cookie.getValue().contains("[" + boardGuid + "]")` could be manipulated.
- **Why it matters**: The cookie is readable by JavaScript (no HttpOnly), enabling session-level tracking by XSS payloads. The string-based "list in a cookie" approach is fragile and unbounded in size.
- **Fix**: Use `ResponseCookie` with `httpOnly(true)` and `sameSite("Lax")`. Consider a server-side view deduplication approach (e.g., Redis TTL key) instead of storing state in a cookie.

---

### Architecture Violations

#### `ProjectFacade` is not annotated with `@Transactional`
- **Severity**: Major
- **Location**: `core/project/port/in/facade/ProjectFacade.java`
- **Problem**: The class has no `@Transactional` annotation. Several methods perform multiple write operations (e.g., `createProject` calls `applicationFormUseCase.saveApplicationForms` and `projectUseCase.createProject`). If either fails after the first commit, data is in an inconsistent state.
- **Why it matters**: Partial failures in multi-step operations leave orphaned `ApplicationForm` rows linked to a project that was never saved (or vice versa).
- **Fix**: Add `@Transactional` to `ProjectFacade`, consistent with all other facade classes in the project.

---

#### Core services directly use `outbound` exception types
- **Severity**: Major
- **Location**: `core/notification/application/selector/CompositeMessageSenderSelector.java:6`
- **Problem**: `import teamdevhub.devhub.outbound.common.exception.ExternalServiceException` — a `@Component` in the core layer imports an exception type from `outbound.common`. Core must not depend on outbound.
- **Why it matters**: `ExternalServiceException` is an outbound infrastructure concern. The core layer should use a core exception (like `BusinessRuleException`) or define its own exception for external communication failure.
- **Fix**: Move `ExternalServiceException` to `core/common/exception/` or use an existing core exception type.

---

#### `AuthResult` and `OAuthAuthResult` import outbound `TokenPrefix`
- **Severity**: Major
- **Location**: `core/auth/application/service/AuthResult.java:4`, `OAuthAuthResult.java:6`
- **Problem**: These core domain result objects import `outbound.auth.infrastructure.token.TokenPrefix` to build the `Authorization` header string. This inverts the dependency: core depends on outbound infrastructure.
- **Why it matters**: `TokenPrefix` is a formatting detail. The core should not know about HTTP header formatting.
- **Fix**: Move `TokenPrefix` (or an equivalent constant) to `shared/` or `core/auth/domain/`. Alternatively, move `toAuthorizationHeader()` to the controller or a utility in the API layer.

---

#### `OAuthResolveService` imports outbound infrastructure VO
- **Severity**: Major
- **Location**: `core/auth/application/service/oauth/OAuthResolveService.java:7`
- **Problem**: `import teamdevhub.devhub.outbound.auth.infrastructure.token.vo.TempTokenInfo` — core application service depends on an outbound VO.
- **Why it matters**: Same dependency inversion as above. The core should not know about the JWT implementation's VO structure.
- **Fix**: Move `TempTokenInfo` to `core/auth/port/out/token/vo/` or define an equivalent core record returned by `TokenParseProvider.getTempTokenInfo()`.

---

### Naming / Conventions

#### Package name `Facade` uses uppercase — inconsistent with Java conventions
- **Severity**: Minor
- **Location**: `core/board/port/in/Facade/` (directory name)
- **Problem**: Java package names must be all lowercase. `Facade` with an uppercase `F` is invalid by convention and will cause issues on case-sensitive filesystems.
- **Fix**: Rename to `core/board/port/in/facade/`.

---

#### `UserUserLoginService` — doubled word in class name
- **Severity**: Nitpick
- **Location**: `core/user/application/service/UserUserLoginService.java`
- **Problem**: The class is named `UserUserLoginService`. The doubled "User" is an obvious typo/error.
- **Fix**: Rename to `UserLoginService`.

---

#### `ReviewUserRequestDto` — redundant parameter in `toReviewUserCommand()`
- **Severity**: Nitpick
- **Location**: `api/project/model/ReviewUserRequestDto.java:22`
- **Problem**: The method signature is `toReviewUserCommand(String projectGuid, String userGuid, ReviewUserRequestDto reviewUserRequestDto, AuthenticatedUser authenticatedUser)` where `reviewUserRequestDto` is `this` — it is passed as a parameter to itself. The call site `reviewUserRequestDto.toReviewUserCommand(..., reviewUserRequestDto, ...)` passes the same object as a parameter.
- **Fix**: Remove the `reviewUserRequestDto` parameter from the method and use `this.score` directly inside the method.

---

#### `UserQueryService` — `@Transactional` on a read-only query service should be `readOnly = true`
- **Severity**: Minor
- **Location**: `core/user/application/admin/UserQueryService.java`
- **Problem**: `@Transactional` with no `readOnly = true` on a service that only reads data.
- **Fix**: Change to `@Transactional(readOnly = true)`.

---

### Transaction / Concurrency Issues

#### `ProjectMemberAdapter.isApprovedApplicant()` — `PageRequest.of(0, Integer.MAX_VALUE)` is dangerous
- **Severity**: Major
- **Location**: `outbound/project/adapter/ProjectMemberAdapter.java:45`
- **Problem**: `PageRequest.of(0, Integer.MAX_VALUE)` is used to load all applications at once. This effectively disables pagination and loads every application for every requirement into memory for a simple membership check.
- **Why it matters**: A project with thousands of applicants will load all of them into the JVM heap on every review attempt. Under concurrent load this causes OOM.
- **Fix**: Replace with a direct `EXISTS` query: `jpaProjectApplicationRepository.existsByRequirementGuidInAndApplicantGuidAndStatusCdAndCanceledFalse(requirementGuids, userGuid, APPROVED.getCode())`.

---

#### `ProjectFacade.getUserProjects()` uses `new PageCommand(0, Integer.MAX_VALUE)` for all applications
- **Severity**: Major
- **Location**: `core/project/port/in/facade/ProjectFacade.java:185`
- **Problem**: Same pattern — `new PageCommand(0, Integer.MAX_VALUE)` loads all applications per project. Combined with the N+1 project detail query, this is compounded: for a page of 10 projects, 10 × MAX_VALUE application loads occur.
- **Why it matters**: Guaranteed OOM under any meaningful production load.
- **Fix**: Refactor `getUserProjects` to load applicant counts in batch via a GROUP BY query, not by loading all applications.

---

### Correctness Issues

#### `ProjectAdapter.getProjectDetail()` — `.get()` without `orElseThrow` (previously documented, still present)
- **Severity**: Critical
- **Location**: `outbound/project/adapter/ProjectAdapter.java:31`
- **Problem**: `jpaProjectRepository.findById(projectGuid).get()` — if the project does not exist, this throws unchecked `NoSuchElementException` which bypasses `GlobalExceptionHandler`'s typed catch blocks and results in HTTP 400 with raw error message.
- **Fix**: `jpaProjectRepository.findById(projectGuid).orElseThrow(() -> AdapterDataException.of(ErrorCode.PROJECT_NOT_FOUND))`.

---

#### `GlobalExceptionHandler` — `AdapterDataException` and `ExternalServiceException` are not handled
- **Severity**: Major
- **Location**: `shared/exception/GlobalExceptionHandler.java`
- **Problem**: The handler has specific handlers for `DomainRuleException`, `BusinessRuleException`, and `MethodArgumentNotValidException`. But `AdapterDataException` and `ExternalServiceException` (both extending `RuntimeException`) fall through to the catch-all `Exception` handler, which returns HTTP 400. `AdapterDataException` represents data-not-found (should be 4xx) and `ExternalServiceException` represents infrastructure failure (should be 500).
- **Why it matters**: File upload failures, email send failures, and missing data all return HTTP 400 instead of appropriate codes.
- **Fix**: Add explicit `@ExceptionHandler(AdapterDataException.class)` and `@ExceptionHandler(ExternalServiceException.class)` handlers that return the HTTP status from `e.getErrorCode().getStatus()`. The existing ErrorCode enum already has the correct HTTP statuses configured.

---

#### `TermsService.validateAllTermsExist()` uses `ErrorCode.UNKNOWN_FAIL`
- **Severity**: Minor
- **Location**: `core/terms/application/TermsService.java:78`
- **Problem**: `throw BusinessRuleException.of(ErrorCode.UNKNOWN_FAIL)` when terms GUIDs don't match. A specific `INVALID_TERMS` error code exists (`ERR.DVH.0067`).
- **Fix**: Use `ErrorCode.INVALID_TERMS`.

---

#### `UserSignupController.signup()` — two facades called without shared transaction
- **Severity**: Major
- **Location**: `api/user/controller/UserSignupController.java:42-43`
- **Problem**: `userSignupFacade.signup(...)` and then `authFacade.login(...)` are called sequentially in the controller, each in their own independent transaction. If `authFacade.login()` fails (e.g., token storage failure), the user exists in the DB but has no session — the signup appears to succeed from the client's perspective but the returned tokens are absent.
- **Why it matters**: User is created but the client receives an error, leading to confusion (user exists, cannot sign up again, but also has no token).
- **Fix**: Either combine signup + login into a single `UserSignupFacade.signupAndLogin()` method wrapped in one transaction, or handle the partial failure gracefully with a clear user-facing error message distinguishing "signup succeeded, login failed — please try logging in."

---

### Miscellaneous

#### `FileStorageProperties.resolve()` — no path traversal guard
- **Severity**: Major
- **Location**: `outbound/file/infrastructure/FileStorageProperties.java:22-27`
- **Problem**: `resolve(fileGuid)` takes a `fileGuid` and uses `fileGuid.substring(0, 2)` as a subdirectory prefix. If `fileGuid` is somehow set to a value containing `../` or path separators (even after URL encoding), `rootPath.resolve(prefix).resolve(fileGuid)` could escape the root directory.
- **Why it matters**: Although `fileGuid` is generated internally, if any code path allows external control of `fileGuid` values (e.g., API parameters flowing through to file reads), this is a path traversal vulnerability.
- **Fix**: After resolving the path, verify `path.normalize().startsWith(rootPath)` and throw if not.

---

#### `CustomAccessDeniedHandler.handle()` — leaks exception message in response
- **Severity**: Major
- **Location**: `outbound/security/handler/CustomAccessDeniedHandler.java:21`
- **Problem**: `DataApiResponseDto.failureFromThrowable(accessDeniedException)` serializes the raw `accessDeniedException.getMessage()` into the response. Spring Security's `AccessDeniedException` message is "Access Denied", but this pattern means any security exception message is exposed directly.
- **Why it matters**: If the exception message changes or is customized to include internal details, they leak to clients.
- **Fix**: Use `DataApiResponseDto.failureWithoutData(ErrorCode.AUTH_INVALID)` with a fixed error code instead of `failureFromThrowable`.

---

## Priority Fix List

### Immediate (Block Release — Critical)

1. **`ProjectController.deleteProject()`** — no ownership check; any authenticated user can delete any project.
2. **`ProjectController.updateProject()`** — no ownership check; any authenticated user can update any project.
3. **`ApplicationController.approveApplication()`** — no project ownership check; any user can approve applications.
4. **`BoardController.deleteBoard()`** — no authentication or authorization; unauthenticated users can delete boards.
5. **`ProjectFacade.getUserApplyProjects()`** — passes `requirementGuid` as `projectGuid`; endpoint crashes at runtime for every call.
6. **`CookieFactory`** — `secure=false` on all cookies in production scope.
7. **`ProjectAdapter.getProjectDetail()`** — raw `NoSuchElementException` from `.get()` bypasses exception handling.
8. **`Project.getRecruitStatus()`** — NPE when recruitment dates are null, crashes any project list that includes such projects.

### High Priority

9. **`UserSignupService` + `TermsService`** — wrong `@Transactional` import (`jakarta` vs `org.springframework`), breaks signup atomicity.
10. **`UserReviewService`** — cross-domain dependency on project ports; must move validation to `UserReviewFacade`.
11. **`ProjectService`** — directly imports and uses `outbound` mapper; violates hexagonal boundary.
12. **`TokenParseProvider` port** — imports outbound VOs; move VOs to core.
13. **`AuthResult` + `OAuthAuthResult`** — import outbound `TokenPrefix`; move to shared/core.
14. **`OAuthResolveService`** — imports outbound `TempTokenInfo`; move to core.
15. **`ProjectApplicationFacade` + `ApplicationFormFacade` + `BoardFacade`** — return/import API layer types from core; invert the dependency.
16. **`GlobalExceptionHandler`** — does not handle `AdapterDataException` or `ExternalServiceException` (returns 400 instead of appropriate codes).
17. **`RefreshTokenAdapter.save()`** — token rotation via dirty-check only; add explicit `.save()` call.
18. **`ProjectMemberAdapter.isApprovedApplicant()`** — `PageRequest.of(0, Integer.MAX_VALUE)` is an OOM risk.
19. **`ProjectFacade.getUserProjects()`** — N+1 queries + `PageCommand(0, MAX_VALUE)` combination.
20. **`BoardController.createBoard()/updateBoard()`** — missing `@Valid` on request bodies.
21. **`CustomAuthenticationProvider.orElseThrow()`** — no argument throws `NoSuchElementException` with an ambiguous error.
22. **`UserSignupController.signup()`** — two separate transactions for signup and login; partial failure leaves orphaned user.
23. **CORS wildcard exposed headers** with credentials enabled.
24. **`boardView` cookie** — no `HttpOnly`, no `SameSite`, unbounded size, fragile parsing.

### Later Cleanup

25. `UserQueryService` — should be `@Transactional(readOnly = true)`.
26. `User.applyReviewScore()` — no floor/ceiling on `mannerDegree`.
27. `Package name board/port/in/Facade` — uppercase violates Java convention; rename to `facade`.
28. `UserUserLoginService` — rename to `UserLoginService`.
29. `ReviewUserRequestDto.toReviewUserCommand()` — remove redundant self-reference parameter.
30. `TermsService.validateAllTermsExist()` — use `INVALID_TERMS` instead of `UNKNOWN_FAIL`.
31. `FileStorageProperties.resolve()` — add path traversal guard.
32. `CustomAccessDeniedHandler` — use fixed error code instead of `failureFromThrowable`.
33. Inline comments and `// 작업 예정` in `ProjectFacade` and `BoardService`.
34. `UserSignupService.initializeAdminUser()` — race condition at startup (low risk, but correctness issue).

---

## Strengths

- The hexagonal structure is largely present and consistently applied in the auth, user, and terms domains.
- `User` domain model is well-designed: private constructor, static factory methods, immutable identity fields, domain methods that encapsulate state changes (`withdraw`, `ban`, `unban`, `assertActive`).
- `UserReview` domain correctly validates score range and step (0.5 increments) inside the domain object.
- `VerificationService` flow (issue → confirm → assert → consume) is clean and correctly scoped.
- `LoggingAspect` now includes sensitive field masking by name — a real improvement over the previous version.
- `TraceIdMDCFilter` now validates the incoming `X-Trace-Id` header against a safe regex before trusting it — the log injection issue has been substantially mitigated.
- `JwtTokenCodec` correctly validates token type on every parse operation, preventing access tokens from being used as refresh tokens.
- `RefreshTokenAdapter.save()` correctly implements refresh token rotation.
- The `OAuthController.handleOAuthCallback()` now validates the state cookie before processing, resolving the previously documented OAuth CSRF issue.
- `WebSecurityConfig` now has `hasRole("ADMIN")` on `/admin/**`, resolving the previously documented open admin route.

---

## Final Verdict

**Request Changes — Block Release**

Three critical security vulnerabilities (unauthenticated board deletion, any-user project deletion/update, any-user application approval) must be fixed before any production deployment. A runtime-crashing bug (`getUserApplyProjects` uses wrong GUID) makes a public endpoint non-functional. Seven other correctness or security issues are release blockers. The architecture violations (core depending on outbound) are not immediate runtime risks but will actively prevent safe refactoring and testing going forward and should be resolved in the next sprint.
