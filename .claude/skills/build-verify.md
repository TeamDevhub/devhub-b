# Skill: 빌드 검증

## 목적

코드 변경 후 빌드와 테스트가 통과하는지 확인하는 절차를 정의한다.

---

## Step 1. 컴파일 오류 먼저 확인

```bash
./gradlew compileJava
```

오류가 있으면 스택 트레이스에서 파일명과 라인 번호를 특정하여 즉시 수정한다.

---

## Step 2. 테스트 컴파일 확인

```bash
./gradlew compileTestJava
```

테스트 코드 컴파일 오류는 주로 다음 원인이다:

| 증상 | 원인 | 해결 |
|---|---|---|
| `cannot find symbol: class QXxx` | QueryDSL Q클래스 미생성 | `./gradlew clean compileJava` |
| `is not a @FunctionalInterface` | 포트 인터페이스에 새 메서드 추가됨 | Fake 구현체에도 메서드 추가 |
| `cannot find symbol: method xxx` | 메서드 시그니처 변경 | 테스트 코드 수정 |

---

## Step 3. 단위 테스트 실행

```bash
./gradlew test --tests "teamdevhub.devhub.small.*"
```

실패 시 리포트 확인:
```
build/reports/tests/test/index.html
```

---

## Step 4. 통합 테스트 실행

```bash
./gradlew test --tests "teamdevhub.devhub.medium.*"
```

자주 발생하는 통합 테스트 오류:

| 증상 | 원인 | 해결 |
|---|---|---|
| `Table "XXX" not found` | H2 스키마 불일치 | `spring.jpa.hibernate.ddl-auto=create-drop` 확인 |
| `required a single bean` | Bean 중복 정의 | `@Primary` 또는 `@Qualifier` 추가 |
| `JdbcSQLIntegrityConstraintViolation` | 테스트 간 데이터 오염 | `@BeforeEach` 에서 `deleteAll()` 호출 확인 |

---

## Step 5. 전체 빌드 (선택)

전체 검증이 필요할 때:

```bash
./gradlew build
```

빠른 빌드 (테스트 제외):
```bash
./gradlew build -x test
```

---

## Step 6. 빌드 검증 보고서 작성

```
## 빌드 검증 결과

### 상태: ✅ 성공 / ❌ 실패

### 컴파일
- Main: ✅ / ❌
- Test: ✅ / ❌

### 테스트 결과
- 전체: X개
- 통과: X개
- 실패: X개

### 실패 테스트 (있는 경우)
- ClassName#methodName: 실패 원인 요약

### 수정이 필요한 사항
- (있는 경우)
```
