# API 설계 규칙

## 컨트롤러 구조

```java
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthFacade authFacade;   // Facade만 주입

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

- 컨트롤러는 `Facade`만 주입받는다.
- 요청 DTO에 `@Valid`를 붙여 Bean Validation을 활성화한다.
- 모든 응답은 `DataApiResponseDto<T>`로 감싼다.
- 컨트롤러에서 비즈니스 로직을 작성하지 않는다.

## Request DTO

```java
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

- 검증 어노테이션(`@NotBlank`, `@NotNull`, `@Size`, `@Pattern`)을 DTO 필드에 선언한다.
- DTO에서 Command 또는 비즈니스 객체로 변환하는 `to{Object}()` 메서드를 제공한다.
- DTO 클래스는 `record`로 선언한다.

## Response DTO

```java
public record TokenResponseDto(String accessToken) {

    public static TokenResponseDto issueAccessToken(String accessToken) {
        return new TokenResponseDto(accessToken);
    }
}
```

- 정적 팩토리 메서드로 생성한다.
- 도메인 객체를 DTO로 변환하는 로직은 DTO 클래스의 정적 메서드나 컨트롤러 레이어에서 처리한다.

## 응답 래퍼

```java
// 데이터 있는 성공 응답
DataApiResponseDto.successWithData(SuccessCode.LOGIN_SUCCESS, data)

// 데이터 없는 성공 응답
DataApiResponseDto.successWithoutData(SuccessCode.LOGOUT_SUCCESS)

// 실패 응답은 GlobalExceptionHandler가 자동으로 처리
```

`SuccessCode`에 없는 코드가 필요하면 `SuccessCode` enum에 먼저 추가한다.

## JWT 인증 흐름

- **액세스 토큰**: `Authorization: Bearer {token}` 헤더로 전달
- **리프레시 토큰**: `Set-Cookie: refreshToken=...; HttpOnly` 쿠키로 전달
- 재발급 시 쿠키의 `refreshToken` 값을 `@CookieValue`로 수신

```java
@PostMapping("/reissue")
public ResponseEntity<...> refresh(@CookieValue("refreshToken") String refreshToken) { ... }
```

## 로그인 사용자 정보 획득

`@LoginUser` 어노테이션으로 현재 인증된 사용자의 `UserCredential`을 컨트롤러 파라미터로 받는다.

```java
@PostMapping("/logout")
public ResponseEntity<...> logout(@LoginUser UserCredential authenticatedUser) {
    authFacade.logout(authenticatedUser.userGuid());
    ...
}
```

## URL 설계 규칙

- 도메인별로 컨트롤러를 분리하고 `@RequestMapping`으로 기본 경로를 지정한다.
- URL은 소문자 + 하이픈(`-`)으로 구성한다. 카멜케이스를 쓰지 않는다.
- 리소스 중심으로 설계한다: `/user/profile`, `/auth/login`, `/projects/{projectGuid}`

## Spring Security 설정 원칙

- `@EnableWebSecurity` 설정은 `shared/config/WebSecurityConfig`에 한 곳만 존재한다.
- JWT 필터는 `UsernamePasswordAuthenticationFilter` 앞에 등록한다.
- 인증이 필요 없는 경로는 `permitAll()`로 명시한다.
- `SessionCreationPolicy.STATELESS`를 유지한다. 세션을 사용하지 않는다.
- **현재 미완성**: `/admin/**` 경로가 `permitAll()` 상태다. ADMIN 권한이 필요한 경로에는 `.hasRole("ADMIN")`을 적용해야 한다.
