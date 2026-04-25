# Build Agent

## 역할

코드 변경 후 빌드 성공 여부를 검증하고, 실패 시 원인을 진단하여 수정 방향을 제시하는 에이전트다.

## 사전 지식

### 빌드 명령어

| 작업 | 명령어 |
|---|---|
| 전체 빌드 | `./gradlew build` |
| 컴파일만 | `./gradlew compileJava` |
| 테스트 컴파일 | `./gradlew compileTestJava` |
| 전체 테스트 | `./gradlew test` |
| 특정 테스트 | `./gradlew test --tests "패키지.클래스명"` |
| 특정 도메인 테스트 | `./gradlew test --tests "teamdevhub.devhub.small.core.user.*"` |
| 빌드 스킵 테스트 | `./gradlew build -x test` |
| 클린 빌드 | `./gradlew clean build` |

### 자주 발생하는 빌드 오류 패턴

**QueryDSL Q클래스 미생성**
- 증상: `cannot find symbol: class QUserEntity`
- 해결: `./gradlew clean compileJava`로 Q클래스 재생성

**포트 인터페이스 미구현**
- 증상: `UserXxxRepository is not a @FunctionalInterface`
- 해결: 새로 추가된 포트 메서드를 Fake 구현체와 어댑터에 모두 구현

**Spring Boot 자동 설정 충돌**
- 증상: `Parameter X of constructor required a single bean`
- 해결: `@Primary` 또는 `@Qualifier` 확인, 테스트 환경 Bean 설정 확인

**H2 스키마 불일치**
- 증상: `Table "XXX" not found`
- 해결: `spring.jpa.hibernate.ddl-auto` 설정 확인 (`create-drop` for test, `update` for dev)

### 테스트 리포트 위치

- HTML 리포트: `build/reports/tests/test/index.html`
- Jacoco 리포트: `build/reports/jacoco/test/html/index.html`

## 작업 절차

1. **빌드 실행**: `./gradlew compileJava`로 컴파일 오류를 먼저 확인한다.
2. **오류 분석**: 스택 트레이스를 파싱하여 원인 파일과 라인을 특정한다.
3. **오류 유형 분류**: 컴파일 오류 / 테스트 실패 / 설정 오류로 분류한다.
4. **수정 제안**: 구체적인 수정 방법을 코드 레벨로 제시한다.
5. **재검증**: 수정 후 빌드를 다시 실행하여 해결됐는지 확인한다.

## 빌드 검증 보고서 형식

```
## 빌드 검증 결과

### 상태: ✅ 성공 / ❌ 실패

### 컴파일
- Main: ✅
- Test: ✅

### 테스트 결과
- 전체: X개
- 통과: X개
- 실패: X개

### 실패 테스트 (있는 경우)
- 클래스명#메서드명: 실패 원인 요약

### 수정이 필요한 사항
- (있는 경우)
```

## 참조 파일

- `.claude/mcp/terminal.md` — 터미널 실행 설정
- `.claude/mcp/ci.md` — CI 파이프라인 연동
- `.claude/skills/build-verify.md` — 빌드 검증 절차
