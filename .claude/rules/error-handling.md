# 예외 처리 규칙

## 예외 계층 구조

세 가지 예외 타입이 있다. 발생 위치에 따라 구분하여 사용한다.

| 예외 클래스 | 발생 위치 | 의미 |
|---|---|---|
| `DomainRuleException` | `core/{domain}/domain/` | 도메인 규칙 위반 |
| `BusinessRuleException` | `core/{domain}/application/` | 비즈니스 정책 위반, 전제 조건 미충족 |
| `AdapterDataException` | `outbound/{domain}/adapter/` | 데이터 없음, 인프라 접근 실패 |

모두 동일한 생성 패턴을 따른다:

```java
// 도메인 계층: 도메인 규칙 위반
throw DomainRuleException.of(ErrorCode.ALREADY_DELETED);

// 애플리케이션 계층: 비즈니스 정책 위반
throw BusinessRuleException.of(ErrorCode.REFRESH_TOKEN_INVALID);

// 어댑터 계층: 데이터 없음
throw AdapterDataException.of(ErrorCode.USER_NOT_FOUND);
```

## ErrorCode 사용 원칙

- 에러 메시지를 하드코딩하지 않는다. 반드시 `ErrorCode` enum 값을 사용한다.
- 새로운 에러 상황이 생기면 `ErrorCode`에 먼저 추가한 뒤, 예외를 던진다.
- `ErrorCode.UNKNOWN_FAIL`은 임시 처리에만 사용한다. 가능한 빨리 구체적인 코드로 교체한다.

```java
// ErrorCode 구조
public enum ErrorCode {
    USER_NOT_FOUND("ERR.DVH.0014", "로그인된 사용자가 존재하지 않습니다", UNAUTHORIZED),
    REFRESH_TOKEN_INVALID("ERR.DVH.0015", "유효하지 않은 토큰입니다", UNAUTHORIZED),
    ALREADY_DELETED("ERR.DVH.0032", "이미 탈퇴한 회원입니다", BAD_REQUEST),
    ...
}
```

## API 응답 규칙

모든 API 응답은 `DataApiResponseDto<T>`로 감싼다.

```java
// 성공 응답: 데이터 있음
return ResponseEntity.ok(
    DataApiResponseDto.successWithData(SuccessCode.LOGIN_SUCCESS, data)
);

// 성공 응답: 데이터 없음
return ResponseEntity.ok(
    DataApiResponseDto.successWithoutData(SuccessCode.LOGOUT_SUCCESS)
);
```

컨트롤러에서 직접 에러 응답을 만들지 않는다. 예외를 던지면 `GlobalExceptionHandler`가 처리한다.

## 계층별 예외 패턴

### 도메인 계층

```java
// 도메인 메서드 내부에서 상태를 검사하고 예외를 던진다
public void withdraw() {
    if (this.deleted) {
        throw DomainRuleException.of(ErrorCode.ALREADY_DELETED);
    }
    this.deleted = true;
}

public void confirm(String code, LocalDateTime now) {
    boolean success = verify(code, now);
    if (!success) {
        throw DomainRuleException.of(ErrorCode.VERIFICATION_FAIL);
    }
}
```

### 애플리케이션 계층

```java
// 중복 검사: 존재하면 예외
userCredentialRepository.findEmailUserCredentialByEmail(email)
        .ifPresent(c -> {
            throw BusinessRuleException.of(ErrorCode.UNKNOWN_FAIL);
        });

// 전제 조건: 토큰 불일치
if (savedRefreshToken == null || !savedRefreshToken.token().equals(refreshToken)) {
    throw BusinessRuleException.of(ErrorCode.REFRESH_TOKEN_INVALID);
}
```

### 어댑터 계층

```java
// 데이터 없음: Optional이 아닌, 존재를 보장하는 조회
public User findByUserGuid(String userGuid) {
    return jpaUserRepository.findByUserGuid(userGuid)
            .orElseThrow(() -> AdapterDataException.of(ErrorCode.USER_NOT_FOUND));
}
```

## Optional 반환 vs 예외 던지기

어댑터 메서드에서 두 가지 패턴을 명확히 구분한다.

```java
// 데이터가 없을 수도 있는 경우 → Optional 반환
Optional<UserCredential> findEmailUserCredentialByEmail(String email);
Optional<UserCredential> findOAuthUserCredentialByOAuth(VerificationProvider provider, String oauthId);

// 반드시 존재해야 하는 경우 → 없으면 예외
User findByUserGuid(String userGuid);
Verification findByVerificationTarget(VerificationTarget verificationTarget);
```
