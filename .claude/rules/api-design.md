# API Design Rules

## Controller Structure

```java id="7d2mka"
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthFacade authFacade;   // Inject Facade only

    @PostMapping("/login")
    public ResponseEntity<DataApiResponseDto<TokenResponseDto>> login(
            @Valid @RequestBody LoginRequestDto loginRequestDto) {

        AuthResult authResult = authFacade.login(loginRequestDto.toLoginCommand());

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, authResult.toAuthorizationHeader())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(DataApiResponseDto.successWithData(
                        SuccessCode.LOGIN_SUCCESS,
                        TokenResponseDto.issueAccessToken(authResult.accessToken())
                ));
    }
}
```

* Controllers should inject only a `Facade`.
* Apply `@Valid` to request DTOs to enable Bean Validation.
* All responses must be wrapped with `DataApiResponseDto<T>`.
* Do not write business logic inside controllers.

---

## Request DTO

```java id="4v9xpt"
@Builder
public record SignupRequestDto(
        @NotBlank String email,
        @NotBlank String password,
        @NotBlank String username,
        @Size(min = 1) List<String> positionList,
        @Size(min = 1) List<String> skillList
) {
    public SignupUserCommand toSignupCommand() {
        return SignupUserCommand.builder()
                .email(this.email)
                ...
                .build();
    }
}
```

* Declare validation annotations on DTO fields:

    * `@NotBlank`
    * `@NotNull`
    * `@Size`
    * `@Pattern`
* DTOs should provide `to{Object}()` methods to convert into Commands or business objects.
* DTO classes should be declared as `record`.

---

## Response DTO

```java id="8n3wqy"
public record TokenResponseDto(String accessToken) {

    public static TokenResponseDto issueAccessToken(String accessToken) {
        return new TokenResponseDto(accessToken);
    }
}
```

* Use static factory methods for creation.
* Logic for converting domain objects into DTOs should be handled either:

    * inside DTO static methods, or
    * in the controller layer.

---

## Response Wrapper

```java id="6r1kcf"
// Success response with data
DataApiResponseDto.successWithData(SuccessCode.LOGIN_SUCCESS, data)

// Success response without data
DataApiResponseDto.successWithoutData(SuccessCode.LOGOUT_SUCCESS)

// Failure responses are automatically handled by GlobalExceptionHandler
```

If a required success code does not exist, add it to the `SuccessCode` enum first.

---

## JWT Authentication Flow

* **Access Token**: delivered through the header
  `Authorization: Bearer {token}`

* **Refresh Token**: delivered through cookie
  `Set-Cookie: refreshToken=...; HttpOnly`

* During reissue, receive the cookie value with `@CookieValue`

```java id="2m7vhs"
@PostMapping("/reissue")
public ResponseEntity<...> refresh(
        @CookieValue("refreshToken") String refreshToken) { ... }
```

---

## Getting Logged-in User Information

Use the `@LoginUser` annotation to receive the authenticated user's `UserCredential` in controller parameters.

```java id="9q4ldb"
@PostMapping("/logout")
public ResponseEntity<...> logout(
        @LoginUser UserCredential authenticatedUser) {

    authFacade.logout(authenticatedUser.userGuid());
    ...
}
```

---

## URL Design Rules

* Separate controllers by domain and define base paths using `@RequestMapping`.
* URLs must use lowercase + hyphen (`-`), not camelCase.
* Use resource-oriented design:

```text id="5y8nwr"
/user/profile
/auth/login
/projects/{projectGuid}
```

---

## Spring Security Configuration Principles

* `@EnableWebSecurity` must exist only once in:
  `shared/config/WebSecurityConfig`
* Register JWT filters before `UsernamePasswordAuthenticationFilter`
* Explicitly declare unauthenticated paths using `permitAll()`
* Keep `SessionCreationPolicy.STATELESS`
* Do not use sessions

### Current Incomplete Area

* `/admin/**` is currently configured as `permitAll()`
* Endpoints requiring admin privileges should use:

```java id="1t6xeg"
.hasRole("ADMIN")
```
