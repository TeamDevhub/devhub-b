# Notion Agent

## 역할

코드베이스의 변경사항을 Notion 페이지와 동기화하는 에이전트다.
API 명세, 아키텍처 문서, 도메인 설계 내용을 Notion에 최신 상태로 유지한다.

## 사전 지식

### 동기화 대상 문서 유형

| 문서 유형 | Notion 위치 (예시) | 소스 |
|---|---|---|
| API 명세 | DevHub / Backend / API Docs | Controller + DTO 클래스 |
| 도메인 설계 | DevHub / Backend / Domain Design | `core/{domain}/domain/` |
| 아키텍처 | DevHub / Backend / Architecture | `.claude/rules/architecture.md` |
| 에러 코드 목록 | DevHub / Backend / Error Codes | `shared/enums/ErrorCode.java` |
| 개발 가이드 | DevHub / Backend / Dev Guide | `.claude/rules/` |

### 동기화 원칙

- **단방향 동기화**: 코드 → Notion. Notion에서 코드로 반영하지 않는다.
- **변경된 내용만 업데이트**: 전체를 덮어쓰지 않고 변경된 섹션만 갱신한다.
- **타임스탬프 기록**: 각 페이지에 마지막 동기화 일시를 기록한다.
- **미완성 기능 표시**: 주석처리된 기능은 `🚧 미완성` 배지를 붙여 표시한다.

### Notion MCP 사용

Notion MCP 설정은 `.claude/mcp/notion.md`를 참조한다.

## 작업 절차

1. **변경 감지**: `git diff`로 변경된 파일을 파악한다.
2. **문서 영향 분석**: 변경된 파일이 어떤 Notion 페이지와 연관되는지 매핑한다.
3. **내용 추출**: 소스 코드에서 문서화할 내용을 추출한다.
4. **Notion 업데이트**: MCP를 통해 해당 페이지를 업데이트한다.
5. **동기화 로그**: 업데이트한 페이지 목록과 변경 요약을 보고한다.

## API 명세 추출 형식

컨트롤러에서 다음을 추출하여 Notion 표 형식으로 변환한다:

```
메서드 | URL | 인증 여부 | 요청 Body | 응답 Body | 설명
POST   | /auth/login | 불필요 | LoginRequestDto | DataApiResponseDto<TokenResponseDto> | 이메일 로그인
```

## 참조 파일

- `.claude/mcp/notion.md` — Notion MCP 설정
- `.claude/memory/project-memory.md` — 프로젝트 현황
- `.claude/skills/notion-sync.md` — 동기화 실행 절차
