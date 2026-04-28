# Refactoring Guide

## When to Extract Domain Methods

If the service layer reads domain state and performs conditional branching, consider moving that logic into a domain method.

```java id="4p8xkt"
// Before refactoring: domain logic leaked into service
if (!user.isDeleted() && user.getMannerDegree() >= 0) {
    user.setDeleted(true);  // ❌ setter usage
}

// After refactoring: encapsulated in domain method
user.withdraw();  // ✅ validates state + changes state internally
```

---

## Change Result Object Pattern

When follow-up persistence work is needed after state changes, return a separate `record` describing the change.

This prevents the service layer from directly comparing internal domain state.

```java id="7m2qvd"
// Domain method returns change result
public UserPositionChangeResult changePositions(
        Set<UserPosition> newPositions
) {
    if (!this.positions.equals(newPositions)) {
        Set<UserPosition> oldPositions = Set.copyOf(positions);
        this.positions = new HashSet<>(newPositions);

        return UserPositionChangeResult.changed(
                oldPositions,
                this.positions
        );
    }

    return UserPositionChangeResult.unchanged(this.positions);
}

// Service persists based on result
private void replacePositions(
        User user,
        Set<UserPosition> positions
) {
    UserPositionChangeResult result =
            user.changePositions(positions);

    if (result.changed()) {
        userPositionRepository.replace(
                result.previousPositions(),
                result.changedPositions()
        );
    }
}
```

---

## Handling Complex Conditions

Simple null checks or empty collection checks should be handled with early return inside domain methods.

```java id="2v9xrm"
public UserPositionChangeResult changePositions(
        Set<UserPosition> newPositions
) {
    if (newPositions == null || newPositions.isEmpty()) {
        return UserPositionChangeResult.unchanged(
                this.positions
        );
    }

    // remaining logic...
}
```

---

## When to Split Service → Facade

If a service method directly calls UseCases from other domains, move that orchestration logic into a Facade.

```java id="8n1qts"
// Before refactoring: service knows other domain UseCases ❌
@Service
public class UserSignupService {

    private final TermsUseCase termsUseCase;
    private final AuthenticationUseCase authUseCase;
}

// After refactoring: Facade orchestrates multiple UseCases ✅
@Service
public class UserSignupFacade {

    private final UserSignupUseCase userSignupUseCase;
    private final TermsUseCase termsUseCase;
    private final AuthenticationUseCase authenticationUseCase;
    private final VerificationUseCase verificationUseCase;
}
```

---

## When to Introduce Command Objects

Use a command object when:

* A service method has **3 or more parameters**
* Two or more parameters share the same type

```java id="5r3xkp"
// Before refactoring
void saveEmailUserInfo(
        String email,
        String username,
        String introduction,
        List<String> positions,
        ...
) {}

// After refactoring
void saveEmailUserInfo(
        SignupUserCommand command,
        String userGuid
) {}
```

---

## When to Extract Mapping Logic from Adapter

If conversion logic is repeated in 3 or more methods, or bidirectional mapping is required, extract a separate Mapper class.

```java id="1m7vqc"
// Simple one-way conversion: keep private inside adapter
private EmailUserCredential toDomain(
        EmailCredentialEntity entity
) { ... }

// Complex or bidirectional conversion: separate Mapper
UserMapper.toEntity(user);
UserMapper.toDomain(entity);
```

---

## Handling Commented-Out Code

When encountering disabled code via comments:

1. **If truly unnecessary**
   Delete it completely.

2. **If unfinished functionality**
   Check the active branch.
   If completion is planned, finish the feature and restore code.
   Otherwise, delete it.

3. **If unclear**
   Ask the user before proceeding.

---

## Currently Commented-Out Major Code

* `User.changePassword()`
  Password change feature incomplete

* `UserProfileUseCase.updatePassword()`
  Also commented out in interface

* `WebSecurityConfig` line 105
  `/admin/**` authorization configuration commented out
