# MCP: Notion

## 역할

Notion MCP를 통해 프로젝트 문서를 코드베이스와 동기화한다.

## 설정

```json
{
  "mcpServers": {
    "notion": {
      "command": "npx",
      "args": ["-y", "@notionhq/notion-mcp-server"],
      "env": {
        "OPENAPI_MCP_HEADERS": "{\"Authorization\": \"Bearer ${NOTION_TOKEN}\", \"Notion-Version\": \"2022-06-28\"}"
      }
    }
  }
}
```

`NOTION_TOKEN` 환경변수에 Notion Integration Token을 설정한다.

## Integration 설정

1. https://www.notion.so/my-integrations 에서 새 Integration 생성
2. 연결할 Notion 페이지에서 Integration 권한 부여
3. Token을 환경변수에 설정

## 이 프로젝트의 Notion 문서 구조

```
DevHub (workspace)
└── Backend
    ├── API Docs          ← 엔드포인트 명세
    ├── Domain Design     ← 도메인 설계
    ├── Architecture      ← 아키텍처 다이어그램
    ├── Error Codes       ← ErrorCode enum 목록
    └── Dev Guide         ← 개발 가이드 (rules/ 파일 반영)
```

실제 페이지 ID는 Notion URL에서 확인한다:
`https://notion.so/{workspace}/{page-name}-{PAGE_ID}`

## 주요 사용 도구

| 작업 | MCP 도구 |
|---|---|
| 페이지 조회 | `retrieve_a_page` |
| 블록 조회 | `retrieve_block_children` |
| 페이지 업데이트 | `update_a_page` |
| 블록 추가 | `append_block_children` |
| 블록 수정 | `update_a_block` |
| 데이터베이스 쿼리 | `query_a_database` |

## 동기화 원칙

- **단방향**: 코드 → Notion (Notion 내용을 코드로 반영하지 않음)
- **변경분만 업데이트**: 전체 덮어쓰기 금지
- **타임스탬프 기록**: 각 페이지 하단에 마지막 동기화 시각 기록
