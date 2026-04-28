# Skill: README 작성

## 목적

DevHub 백엔드의 실제 소스 코드를 기반으로 정확한 README를 작성하는 절차를 정의한다.
추측으로 작성하지 않으며, 코드를 읽은 후 사실만 기술한다.

---

## Step 1. 현재 코드 스캔

```bash
# 컨트롤러 목록 파악
Glob src/main/java/teamdevhub/devhub/api/**/*Controller.java

# 실행 설정 파악
Read src/main/resources/application.yml
Read src/main/resources/application-dev.yml (if exists)

# 의존성 파악
Read build.gradle
```

---

## Step 2. 기존 README 확인

```bash
Read README.md
```

있으면 outdated 항목을 파악한다. 없으면 새로 작성한다.

---

## Step 3. API 엔드포인트 추출

각 컨트롤러에서 다음을 추출한다:

| 항목 | 추출 위치 |
|---|---|
| HTTP 메서드 | `@GetMapping`, `@PostMapping` 등 |
| URL 경로 | `@RequestMapping` + 메서드 매핑 값 |
| 인증 여부 | `@LoginUser` 파라미터 존재 여부 |
| 요청 Body | `@RequestBody` 타입 |
| 응답 Body | 반환 타입 |
| 설명 | 메서드명 또는 `@Operation` |

---

## Step 4. 환경 변수 목록 추출

```bash
Grep "System.getenv\|@Value\|getenv" src/main/java --include="*.java"
```

설정 파일에서 `${...}` 패턴을 추출하여 `.env.example` 형태로 정리한다.

---

## Step 5. README 구조 작성

```markdown
# DevHub Backend

## 프로젝트 소개

## 기술 스택

| 분류 | 기술 |
|---|---|
| Framework | Spring Boot 3.x |
| Language | Java 17 |
| Build | Gradle |
| Database | H2 (로컬), MySQL (운영) |
| 인증 | JWT (Bearer + HttpOnly Cookie) |

## 아키텍처

헥사고날 아키텍처 (Ports & Adapters)
- api/ → facade → usecase ← service → port.out ← adapter ← JPA

## 로컬 실행 방법

1. H2 TCP 서버 실행 (별도 필요)
2. 환경 변수 설정
3. `./gradlew bootRun --args='--spring.profiles.active=dev'`

## 환경 변수 설정

| 변수명 | 설명 | 예시 |
|---|---|---|
| JWT_SECRET | JWT 서명 키 | (임의 문자열) |
| ...

## API 문서

### 인증 (Auth)

| 메서드 | URL | 인증 | 설명 |
|---|---|---|---|
| POST | /auth/login | 불필요 | 이메일 로그인 |
| ...

## 테스트 실행

```bash
./gradlew test
./gradlew test --tests "teamdevhub.devhub.small.*"
```

## 현재 개발 상태

### 완성된 기능
- ...

### 미완성 기능 (주석처리됨)
- 비밀번호 변경 (`changePassword`) — 도메인 메서드 주석처리 상태
- ...
```

---

## Step 6. 미완성 기능 표시 기준

코드에서 다음 패턴을 찾으면 미완성으로 표시한다:

```bash
Grep "TODO\|FIXME\|주석\|미구현" src/main/java --include="*.java" -i
```

주석처리된 public 메서드도 미완성으로 간주한다.

---

## Step 7. 검토 요청

작성 완료 후 다음 형식으로 보고한다:

```
## README 작성 완료

### 추가된 섹션
- ...

### Outdated로 제거된 내용
- ...

### 미완성으로 표시한 기능
- ...

### 확인이 필요한 항목
- (환경 변수 중 실제 값을 모르는 항목)
```
