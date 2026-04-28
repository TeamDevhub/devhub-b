# Prompt: README 에이전트 실행

## 사용 방법

이 파일의 내용을 Claude에게 붙여넣어 README 에이전트를 실행한다.

---

## 프롬프트

```
README Agent 역할을 맡아 DevHub 백엔드 프로젝트의 README를 작성하거나 최신화해줘.

다음 파일들을 참조해:
- .claude/agents/readme-agent.md — 에이전트 역할과 원칙
- .claude/skills/write-readme.md — 작성 절차

작업 순서:
1. 현재 컨트롤러 파일들을 스캔하여 실제 API 엔드포인트를 파악한다
2. build.gradle에서 기술 스택을 확인한다
3. 기존 README.md가 있으면 비교하여 outdated 항목을 찾는다
4. 실제 코드 기반으로 README를 작성한다 (추측 금지)
5. 미완성 기능(.claude/memory/project-memory.md 참조)은 명시적으로 표시한다
6. 완성 후 변경 사항 요약과 함께 검토를 요청한다

대상: [README.md 전체 / 특정 섹션만: ___]
```

---

## 실행 전 체크리스트

- [ ] 소스 코드가 최신 상태인지 확인
- [ ] 추가하거나 삭제된 API가 있는지 파악
- [ ] 환경 변수 변경 사항 확인
