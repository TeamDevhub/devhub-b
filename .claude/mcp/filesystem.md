# MCP: Filesystem

## 역할

프로젝트 파일시스템에 대한 MCP 기반 접근을 설정한다.
Claude Code CLI 환경에서는 내장 파일 도구(Read, Write, Edit, Glob, Grep)를 우선 사용한다.

## 설정

```json
{
  "mcpServers": {
    "filesystem": {
      "command": "npx",
      "args": [
        "-y",
        "@modelcontextprotocol/server-filesystem",
        "D:/devhub-b"
      ]
    }
  }
}
```

허용 경로: `D:/devhub-b` (프로젝트 루트)

## 이 프로젝트에서의 활용

주로 다음 작업에 사용한다:

| 상황 | 사용 도구 |
|---|---|
| 단일 파일 읽기 | Claude Code 내장 `Read` 도구 |
| 파일 검색 | Claude Code 내장 `Glob`, `Grep` 도구 |
| 파일 수정 | Claude Code 내장 `Edit`, `Write` 도구 |
| 디렉토리 트리 생성 | `list_directory` (MCP) |

## 주요 디렉토리 경로

```
D:/devhub-b/
├── src/main/java/teamdevhub/devhub/    ← 소스 코드
├── src/test/java/teamdevhub/devhub/    ← 테스트 코드
├── src/main/resources/                  ← 설정 파일
├── build/                               ← 빌드 결과물
└── .claude/                             ← AI 워크스페이스
```
