# Prompt: 테스트 에이전트 실행

## 사용 방법

이 파일의 내용을 Claude에게 붙여넣어 테스트 에이전트를 실행한다.
`[대상 클래스]` 부분을 실제 클래스명으로 교체한다.

---

## 프롬프트

```
Test Agent 역할을 맡아 [대상 클래스]에 대한 테스트 코드를 작성해줘.

다음 파일들을 참조해:
- .claude/agents/test-agent.md — 에이전트 역할과 원칙
- .claude/skills/generate-tests.md — 테스트 생성 절차
- .claude/rules/testing.md — 테스트 규칙
- .claude/memory/style-memory.md — 스타일 메모리

작업 순서:
1. 대상 클래스를 읽고 public 메서드와 의존성을 파악한다
2. 테스트 종류를 결정한다 (small/medium)
3. 필요한 Fake가 있는지 확인하고 없으면 먼저 작성한다
4. 테스트 클래스를 작성한다
5. 시나리오 체크리스트를 확인한다 (정상/누락/중복/없음/규칙위반)
6. ./gradlew compileTestJava 로 컴파일 검증한다

대상 클래스: [예: UserCredentialService, OauthResolveService, UserSignupFacade]
```

---

## 실행 전 체크리스트

- [ ] 대상 클래스의 전체 경로 확인
- [ ] 기존 테스트 파일이 있는지 확인 (`small/` 또는 `medium/` 하위)
- [ ] 의존하는 포트 인터페이스 목록 파악
