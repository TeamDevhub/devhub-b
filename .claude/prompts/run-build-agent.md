# Prompt: 빌드 에이전트 실행

## 사용 방법

이 파일의 내용을 Claude에게 붙여넣어 빌드 검증 에이전트를 실행한다.

---

## 프롬프트

```
Build Agent 역할을 맡아 현재 코드의 빌드와 테스트 상태를 검증해줘.

다음 파일들을 참조해:
- .claude/agents/build-agent.md — 에이전트 역할과 원칙
- .claude/skills/build-verify.md — 빌드 검증 절차
- .claude/mcp/terminal.md — 명령어 참조

작업 순서:
1. ./gradlew compileJava 로 컴파일 오류 확인
2. ./gradlew compileTestJava 로 테스트 컴파일 오류 확인
3. 오류가 있으면 스택 트레이스에서 파일명과 라인을 특정한다
4. 오류 유형을 분류한다 (컴파일 오류 / 테스트 실패 / 설정 오류)
5. 구체적인 수정 방법을 제시한다
6. 수정 후 다시 빌드하여 성공을 확인한다
7. 빌드 검증 보고서를 작성한다

검증 범위: [전체 / 단위 테스트만 / 통합 테스트만 / 특정 도메인: ___]
```

---

## 자주 발생하는 오류 빠른 참조

| 증상 | 해결 |
|---|---|
| `cannot find symbol: class QXxx` | `./gradlew clean compileJava` |
| Fake에서 메서드 미구현 오류 | Fake 클래스에 메서드 추가 |
| `Table "XXX" not found` | test `application.yml` ddl-auto 확인 |
| `required a single bean` | `@Primary` 또는 `@Qualifier` 확인 |

## 실행 전 체크리스트

- [ ] 최근 변경된 파일 목록 파악 (`git status`)
- [ ] 포트 인터페이스에 새 메서드 추가 여부 확인
- [ ] 새 Fake 구현체 필요 여부 확인
