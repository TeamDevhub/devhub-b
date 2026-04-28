# Skill: 안전한 리팩토링

## 목적

기존 동작을 보존하면서 헥사고날 아키텍처 원칙에 맞게 코드를 개선하는 절차를 정의한다.

---

## Step 1. 대상 파악 및 문제 목록화

대상 파일을 읽고 다음을 확인한다:

- 도메인 로직이 서비스 계층에 누출되어 있는가?
- 파라미터가 3개 이상인 메서드가 있는가?
- 여러 도메인 UseCase를 조합하는 서비스가 있는가?
- 잘못된 계층 의존 관계가 있는가?

```bash
Read src/main/java/teamdevhub/devhub/core/{domain}/application/service/{Class}.java
```

---

## Step 2. 영향 범위 확인

변경 대상을 참조하는 모든 파일을 검색한다.

```bash
Grep "{ClassName}" src/main/java --include="*.java"
Grep "{ClassName}" src/test/java --include="*.java"
```

---

## Step 3. 테스트 존재 확인

리팩토링 전 커버하는 테스트가 있는지 확인한다.

```bash
Glob src/test/java/teamdevhub/devhub/small/core/{domain}/**/*.java
```

테스트가 없으면 리팩토링 전에 먼저 `/generate-tests` 스킬로 작성한다.

---

## Step 4. 리팩토링 패턴 선택

| 문제 | 패턴 |
|---|---|
| 서비스에 도메인 로직 | 도메인 메서드 추출 |
| 파라미터 3개 이상 | 커맨드 record 도입 |
| 여러 도메인 조합 | Facade 분리 |
| 복잡한 상태 변경 결과 | ChangeResult record 도입 |
| 잘못된 계층 의존 | 포트 인터페이스로 역전 |

---

## Step 5. 단계적 변경

한 번에 하나의 리팩토링 목표만 처리한다.

### 도메인 메서드 추출 예시

**Before (서비스 계층에 도메인 로직 누출):**
```java
// UserService.java
public void withdraw(String userGuid) {
    User user = userRepository.findByGuid(userGuid).orElseThrow(...);
    if (user.isDeleted()) throw new BusinessRuleException(ErrorCode.ALREADY_WITHDRAWN);
    user.setDeleted(true);  // ← 도메인 로직이 서비스에 있음
    userRepository.save(user);
}
```

**After (도메인 메서드로 이동):**
```java
// User.java
public void withdraw() {
    if (this.deleted) throw new DomainRuleException(ErrorCode.ALREADY_WITHDRAWN);
    this.deleted = true;
}

// UserService.java
public void withdraw(String userGuid) {
    User user = userRepository.findByGuid(userGuid).orElseThrow(...);
    user.withdraw();
    userRepository.save(user);
}
```

### 커맨드 record 도입 예시

**Before:**
```java
public void signupEmailUser(String email, String password, String name, String userGuid) { ... }
```

**After:**
```java
public record SignupEmailUserCommand(String email, String password, String name, String userGuid) {}
public void signupEmailUser(SignupEmailUserCommand command) { ... }
```

### ChangeResult 도입 예시

```java
// User.java
public record PositionChangeResult(List<String> added, List<String> removed) {}

public PositionChangeResult changePositions(List<String> newPositions) {
    List<String> added = newPositions.stream().filter(p -> !this.positions.contains(p)).toList();
    List<String> removed = this.positions.stream().filter(p -> !newPositions.contains(p)).toList();
    this.positions = new ArrayList<>(newPositions);
    return new PositionChangeResult(added, removed);
}
```

---

## Step 6. 절대 변경하지 않는 것

- 포트 인터페이스 메서드 시그니처
- 테스트 코드의 검증 내용 (assertThat 부분)
- `ErrorCode` enum 값과 메시지
- `DataApiResponseDto` 구조

---

## Step 7. 리팩토링 금지 패턴

코드 작성 시 다음을 도입하지 않는다:

- `setXxx()` 메서드 추가
- 도메인 클래스에 `@Service`, `@Component` 등 Spring 어노테이션
- `Mockito.mock()` 또는 `@MockBean`
- `// TODO` 주석으로 코드 비활성화 (주석처리 금지)

---

## Step 8. 컴파일 및 테스트 검증

```bash
./gradlew compileJava
./gradlew test --tests "teamdevhub.devhub.small.core.{domain}.*"
```

---

## Step 9. 리팩토링 보고서 작성

```
## 리팩토링 요약

### 변경된 파일
- path/to/File.java — 변경 이유

### 적용된 패턴
- 도메인 메서드 추출: XxxService.someLogic() → Xxx.someMethod()

### 보존된 동작
- 기존 테스트 X개 모두 통과

### 주의사항
- (추가 작업이 필요한 항목)
```
