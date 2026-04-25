# 리팩토링 가이드

## 도메인 메서드 추출 기준

서비스 계층에서 도메인 상태를 읽어서 조건 분기를 하고 있다면, 그 로직을 도메인 메서드로 옮기는 것을 검토한다.

```java
// 리팩토링 전: 서비스에 도메인 로직이 누출됨
if (!user.isDeleted() && user.getMannerDegree() >= 0) {
    user.setDeleted(true);  // ❌ setter 사용
}

// 리팩토링 후: 도메인 메서드로 캡슐화
user.withdraw();  // ✅ 내부에서 상태 검사 + 변경
```

## 변경 결과 객체 패턴

상태 변경 이후 후속 처리(저장소 반영)가 필요할 때, 변경 결과를 별도 record로 반환한다. 서비스가 도메인 내부 상태를 직접 비교하지 않도록 추상화한다.

```java
// 도메인 메서드가 변경 결과를 반환
public UserPositionChangeResult changePositions(Set<UserPosition> newPositions) {
    if (!this.positions.equals(newPositions)) {
        Set<UserPosition> oldPositions = Set.copyOf(positions);
        this.positions = new HashSet<>(newPositions);
        return UserPositionChangeResult.changed(oldPositions, this.positions);
    }
    return UserPositionChangeResult.unchanged(this.positions);
}

// 서비스에서 결과를 받아 저장소에 반영
private void replacePositions(User user, Set<UserPosition> positions) {
    UserPositionChangeResult result = user.changePositions(positions);
    if (result.changed()) {
        userPositionRepository.replace(result.previousPositions(), result.changedPositions());
    }
}
```

## 복잡한 조건 처리

단순 null 체크나 빈 컬렉션 검사는 도메인 메서드 내부에서 조기 반환(early return)으로 처리한다.

```java
public UserPositionChangeResult changePositions(Set<UserPosition> newPositions) {
    if (newPositions == null || newPositions.isEmpty()) {
        return UserPositionChangeResult.unchanged(this.positions);  // 조기 반환
    }
    // 이후 로직...
}
```

## Service → Facade 분리 기준

서비스 메서드가 다른 도메인의 UseCase를 직접 호출하고 있다면, 그 조합 로직을 Facade로 올린다.

```java
// 리팩토링 전: 서비스가 다른 도메인 UseCase를 알고 있음 ❌
@Service
public class UserSignupService {
    private final TermsUseCase termsUseCase;           // 다른 도메인
    private final AuthenticationUseCase authUseCase;   // 다른 도메인
}

// 리팩토링 후: Facade가 여러 UseCase를 조합 ✅
@Service
public class UserSignupFacade {
    private final UserSignupUseCase userSignupUseCase;
    private final TermsUseCase termsUseCase;
    private final AuthenticationUseCase authenticationUseCase;
    private final VerificationUseCase verificationUseCase;
}
```

## 커맨드 객체 도입 기준

서비스 메서드의 파라미터가 3개 이상이거나, 같은 타입의 파라미터가 2개 이상이면 커맨드 객체로 묶는다.

```java
// 리팩토링 전
void saveEmailUserInfo(String email, String username, String introduction, List<String> positions, ...) {}

// 리팩토링 후
void saveEmailUserInfo(SignupUserCommand command, String userGuid) {}
```

## 어댑터 내 변환 분리 기준

어댑터의 변환 코드가 3개 이상의 메서드에서 반복되거나, 도메인 ↔ 엔티티 양방향 변환이 필요하면 Mapper 클래스로 분리한다.

```java
// 변환이 단순하고 한 방향만 사용: 어댑터 내 private 메서드로 유지
private EmailUserCredential toDomain(EmailCredentialEntity entity) { ... }

// 변환이 복잡하거나 양방향: 별도 Mapper 클래스
UserMapper.toEntity(user);
UserMapper.toDomain(entity);
```

## 주석처리된 코드 처리

주석으로 비활성화된 코드를 발견했을 때 취할 행동:

1. **실제로 불필요하다면**: 완전히 삭제한다.
2. **미완성 기능이라면**: 작업 중인 브랜치를 확인하고, 완성할 계획이 있으면 해당 기능 완성 후 주석 해제. 없으면 삭제한다.
3. **판단이 어려우면**: 사용자에게 확인 후 처리한다.

현재 주석처리된 주요 코드:
- `User.changePassword()` — 비밀번호 변경 기능 미완성
- `UserProfileUseCase.updatePassword()` — 인터페이스에서도 주석
- `WebSecurityConfig` 105번 라인 — `/admin/**` 권한 설정 주석
