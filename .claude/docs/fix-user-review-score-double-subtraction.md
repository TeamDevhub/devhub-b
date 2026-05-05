# 작업 변경 요약

> 작성일: 2026-05-03  
> 대상 브랜치: `feature/user`  
> 관련 커밋: `0d10f345` ([refactor] 유저리뷰 관련 테스트 코드 및 내부 메서드 호출 수정)

---

## 1. 작업 개요

`UserReview` 도메인에서 매너도(mannerDegree) 업데이트 시 점수 기준값(3.0)이 두 번 차감되는 버그를 수정하였다.  
`reviewScore()` 메서드 제거, 서비스 반환값 변경, 관련 테스트 정정이 포함된 리팩토링이다.

---

## 2. 변경 사항

### 수정된 파일

| 파일 | 변경 내용 |
|------|-----------|
| `core/user/domain/UserReview.java` | `SCORE_OFFSET` 상수 제거, `reviewScore()` 메서드 제거 |
| `core/user/application/service/UserReviewService.java` | `userReview.reviewScore()` → `userReview.getScore()` 반환값 변경 |
| `small/core/user/domain/UserReviewTest.java` | 삭제된 `reviewScore()` 메서드 검증 테스트 3개 제거 |
| `small/core/user/application/service/UserReviewServiceTest.java` | 반환값 단언 `1.0` → `4.0` 수정, 미사용 import 제거 |

### 제거된 요소

- `UserReview.SCORE_OFFSET` (= 3.0) 상수
- `UserReview.reviewScore()` 메서드
- `UserReviewTest` 내 `reviewScore_*` 테스트 3개
- `UserReviewServiceTest` 내 `FakeUserRepository` import (미사용)

---

## 3. 기존 문제점

### 핵심 버그: 중립값(3.0) 이중 차감

리뷰 점수가 매너도에 반영되기까지의 호출 체인은 다음과 같았다.

```
UserReview.create(4.0 점수로 생성)
  └─ UserReviewService.reviewMember()
       └─ return userReview.reviewScore()   // 4.0 - 3.0 = 1.0 (delta)
            └─ UserReviewFacade
                 └─ userProfileUseCase.updateUserMannerDegree(revieweeGuid, 1.0)
                      └─ user.applyReviewScore(1.0)
                           └─ mannerDegree += (1.0 - 3.0)  // = -2.0  ← 버그
```

4.0점(중립 이상)의 긍정적 리뷰를 받았음에도 매너도가 **-2.0** 감소했다.  
즉, 좋은 리뷰를 받을수록 매너도가 낮아지는 반대 방향으로 동작했다.

### 원인 분석

| 위치 | 설계 의도 | 실제 동작 | 불일치 |
|------|-----------|-----------|--------|
| `UserReview.reviewScore()` | 매너 델타 반환 (`score - 3.0`) | 의도대로 동작 | — |
| `User.applyReviewScore(input)` | raw 점수를 받아 `input - 3.0` 계산 | 의도대로 동작 | — |
| `UserReviewService` | raw 점수 반환 예상 | delta를 반환 | **불일치** |
| `UserReviewFacade` | raw 점수를 하위로 전달 | delta를 전달 | **불일치** |

두 계층 모두 각자의 계층에서는 올바르게 설계되었으나, **연결 지점에서 계약(contract)이 어긋났다.**

### 잘못 작성된 테스트

`UserReviewServiceTest`의 성공 케이스:
```java
// 4.0점 리뷰 후
assertThat(reviewScore).isEqualTo(1.0);  // delta(1.0)가 반환됨을 검증
```
이 단언은 버그 동작을 정상으로 검증하고 있었다.  
테스트가 통과했기 때문에 버그가 오래 잠복할 수 있었다.

`UserReviewTest`의 `reviewScore()` 테스트들은 도메인 메서드의 동작 자체는 올바르게 검증했으나, 해당 메서드가 호출 체인에서 잘못 사용되고 있다는 사실을 감지하지 못했다.

---

## 4. 개선 내용

### 핵심 설계 변경

**`UserReview.reviewScore()` 제거 → 서비스가 `getScore()`(raw 점수)를 반환**

```
UserReview.create(4.0 점수로 생성)
  └─ UserReviewService.reviewMember()
       └─ return userReview.getScore()     // 4.0 (raw score)
            └─ UserReviewFacade
                 └─ userProfileUseCase.updateUserMannerDegree(revieweeGuid, 4.0)
                      └─ user.applyReviewScore(4.0)
                           └─ mannerDegree += (4.0 - 3.0)  // = +1.0  ← 정상
```

### 설계 원칙 적용

- **단일 책임**: 중립값(3.0) 차감 계산은 `User.applyReviewScore()` 한 곳에서만 수행
- **명확한 계약**: `UserReviewUseCase.reviewMember()`의 반환값은 raw 점수(1.0~5.0)임이 명확해짐
- **dead code 제거**: 사용되지 않는 `reviewScore()`, `SCORE_OFFSET` 삭제
- **테스트 정합성**: 서비스 테스트의 단언이 실제 반환 계약과 일치하도록 수정

---

## 5. 기대 효과

### 안정성

매너도 증감 방향이 리뷰 점수와 일치하게 된다.  
긍정적 리뷰(4.0, 5.0) → 매너도 증가, 부정적 리뷰(1.0, 2.0) → 매너도 감소.

### 유지보수성

`reviewScore()` 메서드가 존재할 때는 "이 메서드가 반환하는 값이 delta인지 raw인지"를 메서드명만으로 구분할 수 없었다.  
제거 후 호출 체인의 데이터 흐름이 `getScore()` → raw 점수로 단순화되었다.

### 테스트 신뢰성

기존 서비스 테스트는 버그 동작을 정상으로 검증했다.  
수정 후 테스트가 실제 계약(raw 점수 반환)을 검증한다.

---

## 6. 남은 리스크

### 검증 순서 문제 (미수정)

`UserReviewService.reviewMember()` 내 유효성 검사 순서:

```java
validateReviewableProject(project);   // 1. 프로젝트 완료 여부
validateDuplicateReview(command);     // 2. 중복 리뷰 여부
validateProjectMembers(command);      // 3. 멤버 여부
```

멤버가 아닌 사용자가 접근할 경우, 중복 리뷰 체크(DB 조회)를 먼저 수행한 후 멤버 여부를 검사한다.  
멤버 검사가 중복 검사보다 먼저 실행되는 것이 의미상 올바르지만, 현재 테스트 범위에서 관찰 가능한 오동작은 없다.

### `UserReviewUseCase` 반환 계약 미문서화

`double reviewMember(ReviewUserCommand command)`의 반환값이 **raw 점수(1.0~5.0)**임을 인터페이스에서 명시하지 않는다.  
JavaDoc이나 계약 표현 없이 구현만으로 파악해야 한다.

### `Project.builder()` 직접 호출 (테스트)

`UserReviewServiceTest`의 `completedProject()`, `incompleteProject()` 헬퍼 메서드에서 `Project.builder()`를 직접 호출한다.  
프로젝트 규칙상 도메인 생성은 정적 팩토리 메서드를 통해야 한다. `Project`에 적합한 팩토리 메서드가 없어 임시로 builder를 직접 사용하고 있다.

---

## 7. 추후 개선 예상 포인트

### 검증 순서 정렬

```java
// 현재
validateReviewableProject → validateDuplicateReview → validateProjectMembers

// 권장
validateReviewableProject → validateProjectMembers → validateDuplicateReview
```
멤버가 아닌 자에 대한 중복 리뷰 DB 조회를 방지하고, 오류 우선순위가 의미상 올바르게 된다.

### `UserReviewUseCase` 반환 계약 명시

인터페이스에 raw 점수 반환임을 나타내는 방법을 고려할 것.  
JavaDoc 또는 반환 타입을 래핑하는 Result 객체 도입.

### `Project` 도메인 팩토리 메서드 보완

`UserReviewServiceTest`에서 `Project.builder()` 직접 호출을 제거하려면, `Project`에 테스트 목적에 맞는 정적 팩토리 메서드(`forTest()` 또는 `withEndDate()`)가 필요하다.

### `UserReviewFacadeTest` 검증 강화

현재 Facade 테스트는 UseCase 호출 순서 및 orchestration을 검증하나, 매너도 업데이트에 실제로 어떤 값이 전달되는지는 검증하지 않는다. `FakeUserProfileUseCase.updateUserMannerDegree()`의 호출 인자를 캡처하여 단언하는 테스트를 추가할 것.

---

## 8. 추천 후속 작업

1. **검증 순서 수정**: `validateProjectMembers`를 `validateDuplicateReview` 앞으로 이동하고, 해당 순서를 검증하는 테스트 케이스 추가
2. **Facade 테스트 보강**: `FakeUserProfileUseCase`가 받은 `reviewScore` 인자를 검증하여 체인 전체의 정합성을 테스트로 보장
3. **`Project` 팩토리 메서드 추가**: 테스트에서 `Project.builder()` 직접 사용을 해소
4. **`UserReviewUseCase` 계약 문서화**: 반환값의 의미(raw 점수, 1.0~5.0 범위)를 인터페이스 수준에서 명시

---

## 9. 한 줄 총평

계층 간 데이터 계약 불일치로 발생한 실 동작 버그였으며, 잘못 작성된 테스트가 오랫동안 이를 은폐하고 있었다. 핵심 수정은 단순하지만, 테스트 신뢰성에 대한 구조적 경계심이 필요한 사례다.
