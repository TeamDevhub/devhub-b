# Code Review Result

## Review Target

Domains: `auth` · `user` · `terms` · `file`

Layers reviewed:
- `core/{domain}/domain/` — User, Terms, File
- `core/{domain}/application/service/` — all services in scope
- `core/{domain}/port/in/facade/` — AuthFacade, OAuthAuthFacade, UserProfileFacade, UserReviewFacade, UserSignupFacade, TermsFacade, FileFacade
- `api/` — AuthController, OAuthController, UserProfileController, UserSignupController, TermsController, FileController
- `outbound/` — UserAdapter, FileMetadataAdapter, LocalFileStorage, TermsAdapter
- `shared/` — WebSecurityConfig, GlobalExceptionHandler, LoggingAspect, TraceIdMDCFilter, CookieFactory

Review date: 2026-05-05

---

## Executive Summary

The codebase has seen meaningful security improvements since the last review (2026-05-03): OAuth CSRF state validation is now implemented, LoggingAspect masks sensitive fields, TraceIdMDCFilter validates incoming trace IDs, FileResponseFactory sanitizes Content-Disposition headers, and the admin authorization gap in WebSecurityConfig is closed.

Three new issues require attention before release:

1. `FileMetadataAdapter.find()` throws a raw `IllegalArgumentException` with a hardcoded message instead of the project-standard `AdapterDataException`. This bypasses the exception handler contract and leaks an internal error message.
2. `GlobalExceptionHandler` still maps all unhandled exceptions to HTTP 400 — infrastructure failures appear as client errors, making incident diagnosis difficult.
3. `CookieFactory` still defaults `secure=false`, leaving production deployments at risk of misconfiguration.

The rest of the codebase is structurally sound: hexagonal boundaries are respected, domain logic is properly encapsulated, and the Facade orchestration layer is clean.

---

## Findings

---

### F-01 — Critical

**Location:** `outbound/file/adapter/FileMetadataAdapter.java:27` — `find()`

**Problem:**
```java
.orElseThrow(() -> new IllegalArgumentException("File not found"));
```
Throws a raw `IllegalArgumentException` with a hardcoded string instead of `AdapterDataException.of(ErrorCode.FILE_READ_FAIL)`.

**Why it matters:**
- `GlobalExceptionHandler` has no handler for `IllegalArgumentException` — it falls through to the generic `Exception.class` handler which returns HTTP 400 with the raw exception message exposed in the response body.
- Hardcoded message text violates the project rule "Do not hardcode error messages — use the ErrorCode enum."
- The Fake (`FakeFileMetadataRepository`) and the real adapter now have divergent exception types — `FakeFileMetadataRepository.find()` throws `AdapterDataException.of(FILE_READ_FAIL)` while the real adapter throws `IllegalArgumentException`. Tests pass but production behavior differs.

**Recommended fix:**
```java
.orElseThrow(() -> AdapterDataException.of(ErrorCode.FILE_READ_FAIL));
```

---

### F-02 — Major

**Location:** `shared/exception/GlobalExceptionHandler.java:64` — `handleException()`

**Problem:**
```java
@ExceptionHandler(Exception.class)
@ResponseStatus(BAD_REQUEST)
public ResponseEntity<DataApiResponseDto<?>> handleException(Exception e) {
    ...
    return ResponseEntity.badRequest()
            .body(DataApiResponseDto.failureFromThrowable(e));
}
```
All unhandled exceptions — including `NullPointerException`, `IllegalStateException`, `DataIntegrityViolationException`, unchecked infrastructure exceptions — return HTTP 400 BAD_REQUEST.

**Why it matters:**
- HTTP 400 signals a bad client request. Returning 400 for a database failure or uncaught NPE misleads the client and corrupts monitoring (SLAs calculated on 5xx become invisible).
- `failureFromThrowable(e)` likely exposes raw exception messages to the client, leaking internal implementation details.
- On-call engineers diagnosing a production incident will see a wall of 400s with no 500s, making impact assessment incorrect.

**Recommended fix:**
```java
@ExceptionHandler(Exception.class)
@ResponseStatus(INTERNAL_SERVER_ERROR)
public ResponseEntity<DataApiResponseDto<?>> handleException(Exception e) {
    logException(e);
    return ResponseEntity.internalServerError()
            .body(DataApiResponseDto.failureWithoutData(ErrorCode.UNKNOWN_FAIL));
}
```

---

### F-03 — Major

**Location:** `api/auth/controller/CookieFactory.java:19`

**Problem:**
```java
@Value("${app.cookie.secure:false}")
private boolean secureCookie;
```
The default value for `app.cookie.secure` is `false`. If the production deployment omits this property (misconfiguration, missing env var, outdated config), refresh tokens are sent over plain HTTP.

**Why it matters:**
- Refresh tokens issued over non-HTTPS connections are interceptable on the network. An attacker who captures a refresh token has persistent access until expiry (14 days).
- `secure=false` is correct for local development but the wrong default for a shared configuration.

**Recommended fix:**
Invert the default: `@Value("${app.cookie.secure:true}")`. Developers who need HTTP locally must explicitly opt-out. Add a startup warning if `app.cookie.secure=false` is detected in a non-dev profile.

---

### F-04 — Major

**Location:** `core/auth/application/service/UserCredentialService.java` — `signupEmailUser()`

**Problem:**
Duplicate email signup throws `ErrorCode.SIGNUP_FAIL` instead of a specific duplicate-email error code.

**Why it matters:**
- The frontend receives a generic "signup failed" message and cannot distinguish a duplicate email from a system error, preventing appropriate UX feedback (e.g., "This email is already registered — try logging in").
- `SIGNUP_FAIL` is semantically incorrect: the domain rule violation is a duplicate credential, not a generic failure.

**Recommended fix:**
Add `ErrorCode.EMAIL_DUPLICATED` (or use the closest existing code) and throw it specifically on the `ifPresent` duplicate check.

---

### F-05 — Minor

**Location:** `outbound/terms/adapter/TermsAdapter.java:44` — `findByTermsGuid()`

**Problem:**
```java
.orElseThrow(() -> AdapterDataException.of(ErrorCode.UNKNOWN_FAIL));
```
Uses `UNKNOWN_FAIL` as the error code when a terms record is not found.

**Why it matters:**
Per `error-handling.md`: "`ErrorCode.UNKNOWN_FAIL` should be temporary only and replaced with a specific code as soon as possible." A terms-not-found scenario is predictable and deserves its own error code for correct HTTP status mapping and client-side interpretation.

**Recommended fix:**
Add `ErrorCode.TERMS_NOT_FOUND` to the `ErrorCode` enum and use it here.

---

### F-06 — Minor

**Location:** `shared/config/WebSecurityConfig.java:103` — security filter chain

**Problem:**
```java
.requestMatchers("/auth/**").permitAll()
```
This `permitAll()` rule covers `/auth/logout`, `/auth/reissue`, and `/auth/password` — endpoints that require an authenticated user. Authentication for these endpoints is enforced only at the application layer via `@LoginUser` / `AuthenticatedUser`, not at the security filter level.

**Why it matters:**
- Defense-in-depth is reduced: if the `@LoginUser` resolver is misconfigured or bypassed, unauthenticated requests reach the business logic.
- The security config implies these are public endpoints, which is misleading.

**Recommended fix:**
Split the auth rules to expose only the truly public endpoints as `permitAll()`:
```java
.requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
.requestMatchers(HttpMethod.POST, "/auth/reissue").permitAll()
.requestMatchers("/auth/oauth/**").permitAll()
// logout and password change: leave as authenticated()
```

---

### F-07 — Minor

**Location:** `core/user/domain/User.java:228` — `applyReviewScore()`

**Problem:**
```java
public void applyReviewScore(double score) {
    this.mannerDegree += (score - 3.0);
}
```
No floor or ceiling is applied to `mannerDegree`. The service layer clamps the incoming `score` to `[1.0, 5.0]`, but the cumulative effect on `mannerDegree` is unbounded: a user with many negative reviews could reach arbitrarily low values.

**Why it matters:**
- If initial value is 36.5 (body temperature metaphor), reaching -50 has no semantic meaning and might cause unexpected rendering or comparison behavior downstream.
- Bounds enforcement belongs in the domain, not the service layer.

**Recommended fix:**
```java
public void applyReviewScore(double score) {
    this.mannerDegree = Math.max(0.0, Math.min(100.0, this.mannerDegree + (score - 3.0)));
}
```
The exact bounds are a product decision; the domain should enforce whatever they are.

---

### F-08 — Minor

**Location:** `core/user/application/service/UserSignupService.java:39` — `initializeAdminUser()`

**Problem:**
```java
if (existsByUserRole()) {
    return;
}
// ... create admin user
```
Check-then-act race condition: two concurrent startup threads can both pass the `existsByUserRole()` check and both attempt to insert an admin user.

**Why it matters:**
In a multi-instance deployment without startup synchronization, the race produces a unique constraint violation that surfaces as an unhandled exception. In practice the risk is low (startup typically serializes in practice), but the code assumes rather than handles it.

**Recommended fix:**
Wrap the insert in a try/catch for `DataIntegrityViolationException` (attempt insert, catch duplicate), or rely exclusively on the DB unique constraint as the enforcer.

---

### F-09 — Nitpick

**Location:** `api/file/controller/FileController.java:53` — `selectFile()`

**Problem:**
```java
DataApiResponseDto.successWithData(
        SuccessCode.CREATE_SUCCESS,  // wrong for a GET
        fileFacade.selectFileObject(fileGuid)
)
```

**Recommended fix:** Change to `SuccessCode.READ_SUCCESS`.

---

### F-10 — Nitpick

**Location:** `shared/logging/LoggingAspect.java:50` — `logAround()`

**Problem:**
The pointcut covers all methods in `api`, `core`, and `outbound` packages. The `maskSensitiveFields()` method uses reflection (`field.setAccessible(true)`) to walk all fields of every method argument on every invocation.

**Why it matters:**
At scale, reflection on every service call adds measurable overhead. Consider profiling under load and narrowing the pointcut if needed.

---

## Previously Reported — Now Fixed

The following issues from the 2026-05-03 full-project-review have been resolved:

| Issue | Status |
|---|---|
| `OAuthController` — OAuth CSRF: no state parameter validation | **FIXED** — cookie state compared against request param |
| `LoggingAspect` — serializes all params including passwords | **FIXED** — SENSITIVE_FIELDS masking list implemented |
| `TraceIdMDCFilter` — X-Trace-Id injected without sanitization | **FIXED** — regex `[a-zA-Z0-9\\-]{8,36}` validation added |
| `FileResponseFactory.attachment()` — header injection via filename | **FIXED** — `replaceAll("[\r\n\"\\\\;]", "_")` sanitization added |
| `CookieFactory` — Refresh Token cookie `secure=false` | **PARTIALLY FIXED** — now configurable via `app.cookie.secure`, but default remains `false` (see F-03) |
| `OAuthController` — hardcoded `http://localhost:5173` | **FIXED** — uses `${app.frontend.base-url}` |
| `WebSecurityConfig` — `/admin/**` was `permitAll()` | **FIXED** — `.hasRole("ADMIN")` applied |
| `AuthController.login()` — `@Valid` missing on `LoginRequestDto` | **FIXED** — `@Valid` is present |

---

## Priority Fix List

1. **Immediate** — `FileMetadataAdapter.find()`: change `IllegalArgumentException` → `AdapterDataException.of(ErrorCode.FILE_READ_FAIL)`. Single-line fix; divergence between fake and real adapter breaks the test-as-contract guarantee.
2. **Immediate** — `GlobalExceptionHandler`: change generic handler from HTTP 400 → HTTP 500 and stop exposing raw exception messages in the response body.
3. **Before release** — `CookieFactory`: invert default to `secure=true`.
4. **Before release** — `UserCredentialService.signupEmailUser()`: replace `SIGNUP_FAIL` with a specific duplicate-credential error code.
5. **Backlog** — `TermsAdapter.findByTermsGuid()`: replace `UNKNOWN_FAIL` with `TERMS_NOT_FOUND`.
6. **Backlog** — `WebSecurityConfig`: narrow `/auth/**` permitAll to specific public endpoints only.
7. **Backlog** — `User.applyReviewScore()`: add floor/ceiling enforcement on `mannerDegree`.
8. **Backlog** — `FileController.selectFile()`: fix `CREATE_SUCCESS` → `READ_SUCCESS`.

---

## Strengths

- `OAuthAuthFacade.handleOAuthCallback()` correctly handles both the login path and the signup-required path; state cookie is expired before redirect in both cases.
- `TraceIdMDCFilter` cleanup in `finally` block guarantees MDC does not leak across requests in thread-pool environments.
- `User.assertActive()` centralizes the deleted/blocked guard in one place; both the email and OAuth login paths call it before issuing tokens.
- `LocalFileStorage.safeResolve()` performs path traversal prevention (`!resolved.startsWith(root)`) — correctly defends against `../` injection in file GUIDs.
- `User` domain uses `@Builder` with `private` constructor and static factory methods throughout — construction bypass from outside the domain is impossible.
- `LoggingAspect` sensitive field masking covers all token and password field name variants including nested objects via reflection.
- `AuthFacade.login()` correctly sequences: authenticate → validate login eligibility → issue tokens → update last login datetime, in a single transaction.

---

## Final Verdict

**Request Changes**

F-01 and F-02 are must-fix before release: F-01 creates a behavioral divergence between tests and production that makes file-not-found untestable, and F-02 has been a known issue long enough that it should not ship. Both are small changes. F-03 requires a deployment config audit.

Once items 1–4 in the Priority Fix List are resolved, this scope is releasable.
