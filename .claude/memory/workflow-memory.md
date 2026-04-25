# 워크플로우 메모리

## 신규 기능 개발 순서

새로운 도메인 기능을 추가할 때 반드시 이 순서를 따른다:

```
1. 도메인 엔티티/VO 작성      (core/{domain}/domain/)
2. 포트 인터페이스 작성        (core/{domain}/port/in/usecase/, port/out/)
3. 서비스 작성                (core/{domain}/application/service/)
4. Facade 작성 (필요 시)      (core/{domain}/port/in/facade/)
5. JPA 엔티티 + Repository   (outbound/persistence/)
6. 어댑터 작성                (outbound/{domain}/adapter/)
7. 컨트롤러 + DTO 작성        (api/web/controller/, model/)
8. 테스트 작성                (small/ + medium/)
```

---

## Fake 구현체 관리

Fake 구현체는 포트 인터페이스를 인메모리 Map으로 구현한다.

위치: `src/test/java/teamdevhub/devhub/fake/pure/application/port/out/{domain}/`

명명: `Fake{PortInterfaceName}` (예: `FakeUserRepository`, `FakeRefreshTokenRepository`)

작성 규칙:
- `given{Data}()` 메서드로 테스트 데이터 사전 세팅
- `HashMap`을 기반 저장소로 사용
- 예외 시뮬레이션은 `given*()` 메서드로 제어

---

## 브랜치 및 커밋 전략

```
feature/{기능명} → dev → main
```

커밋 메시지 형식:
```
[feat] 기능 설명
[fix] 버그 수정 설명
[test] 테스트 코드 변경
[refactor] 리팩토링 설명
[docs] 문서 변경
```

---

## 빌드 및 검증

| 단계 | 명령어 | 시점 |
|---|---|---|
| 빠른 검증 | `./gradlew compileJava` | 코드 변경 후 즉시 |
| 테스트 컴파일 | `./gradlew compileTestJava` | Fake/테스트 변경 후 |
| 단위 테스트 | `./gradlew test --tests "*.small.*"` | 서비스/도메인 변경 후 |
| 통합 테스트 | `./gradlew test --tests "*.medium.*"` | 어댑터 변경 후 |
| 전체 빌드 | `./gradlew build` | PR 전 최종 검증 |

QueryDSL Q클래스 재생성 필요 시: `./gradlew clean compileJava`

---

## 자주 발생하는 문제 및 해결법

### 포트 메서드 추가 후 빌드 실패
새 메서드를 포트 인터페이스에 추가하면 Fake 구현체와 어댑터 모두에 구현해야 한다.

### 통합 테스트 데이터 오염
`@BeforeEach` 에서 `jpaRepository.deleteAll()` 호출을 빠뜨리면 테스트 간 간섭 발생.

### H2 스키마 불일치
테스트 환경 `application.yml`에서 `spring.jpa.hibernate.ddl-auto=create-drop` 설정 확인.

---

## 에이전트 활용 가이드

| 상황 | 사용할 에이전트/스킬 |
|---|---|
| 새 기능의 테스트 작성 | `agents/test-agent.md` + `skills/generate-tests.md` |
| 코드 구조 개선 | `agents/refactor-agent.md` + `skills/refactor-safely.md` |
| 문서 업데이트 | `agents/readme-agent.md` + `skills/write-readme.md` |
| Notion 동기화 | `agents/notion-agent.md` + `skills/notion-sync.md` |
| 빌드 실패 진단 | `agents/build-agent.md` + `skills/build-verify.md` |
