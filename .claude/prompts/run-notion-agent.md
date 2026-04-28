# Prompt: Notion 에이전트 실행

## 사용 방법

이 파일의 내용을 Claude에게 붙여넣어 Notion 동기화 에이전트를 실행한다.

---

## 프롬프트

```
Notion Agent 역할을 맡아 코드베이스의 변경사항을 Notion에 동기화해줘.

다음 파일들을 참조해:
- .claude/agents/notion-agent.md — 에이전트 역할과 원칙
- .claude/skills/notion-sync.md — 동기화 절차
- .claude/mcp/notion.md — Notion MCP 설정

작업 순서:
1. git diff HEAD~1 --name-only 로 변경된 파일 목록을 확인한다
2. 변경된 파일이 어떤 Notion 페이지와 연관되는지 매핑한다
3. 소스 코드에서 문서화할 내용을 추출한다
4. 변경된 섹션만 Notion에 업데이트한다 (전체 덮어쓰기 금지)
5. 각 페이지 하단에 동기화 타임스탬프를 기록한다
6. 업데이트된 페이지 목록과 변경 요약을 보고한다

동기화 범위: [전체 / API 명세만 / 에러 코드만 / 특정 도메인: ___]
```

---

## 사전 조건

- [ ] `NOTION_TOKEN` 환경변수 설정 완료
- [ ] Notion Integration이 대상 페이지에 권한 부여됨
- [ ] `.claude/mcp/notion.md`에 페이지 ID 기록됨

## 주의 사항

- 동기화는 **코드 → Notion** 단방향만 허용
- Notion에서 직접 수정한 내용은 다음 동기화 시 덮어씌워질 수 있음
