# MCP: GitHub

## 역할

GitHub MCP를 통해 이슈, PR, 코드 리뷰를 관리한다.

## 설정

```json
{
  "mcpServers": {
    "github": {
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-github"],
      "env": {
        "GITHUB_PERSONAL_ACCESS_TOKEN": "${GITHUB_TOKEN}"
      }
    }
  }
}
```

`GITHUB_TOKEN` 환경변수에 Personal Access Token을 설정한다.
필요한 스코프: `repo`, `read:org`, `read:user`

## 이 프로젝트에서의 사용

| 작업 | MCP 도구 |
|---|---|
| 이슈 목록 조회 | `list_issues` |
| PR 생성 | `create_pull_request` |
| PR 리뷰 | `create_review` |
| 이슈 코멘트 | `create_issue_comment` |
| 파일 내용 조회 | `get_file_contents` |

## 저장소 정보

- **Owner**: `teamdevhub`
- **Repo**: `devhub` (실제 저장소명 확인 필요)
- **Base branch**: `dev`
- **Head branch**: 현재 `feature/user`

## PR 작성 가이드

PR 제목 형식: `[feat/fix/refactor] 변경 내용 요약`

PR 본문에 포함할 내용:
1. 변경 이유
2. 변경된 파일 목록
3. 테스트 방법
4. 관련 이슈 번호 (`Closes #xxx`)
