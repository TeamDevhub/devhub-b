# Skill: Notion 동기화

## 목적

코드베이스의 변경사항을 Notion 페이지와 단방향 동기화하는 절차를 정의한다.
코드 → Notion 방향만 허용하며, Notion에서 코드로 반영하지 않는다.

---

## Step 1. 변경 감지

```bash
git diff HEAD~1 --name-only
```

변경된 파일이 어떤 Notion 문서와 연관되는지 매핑한다:

| 변경된 파일 경로 | 연관 Notion 페이지 |
|---|---|
| `api/**/*Controller.java` | API 명세 |
| `core/{domain}/domain/*.java` | 도메인 설계 |
| `shared/enums/ErrorCode.java` | 에러 코드 목록 |
| `.claude/rules/*.md` | 개발 가이드 |
| `build.gradle` | 기술 스택 |

---

## Step 2. 컨트롤러에서 API 명세 추출

각 컨트롤러에서 다음을 추출하여 표 형식으로 변환한다:

```
메서드 | URL | 인증 여부 | 요청 Body | 응답 Body | 설명
POST   | /auth/login | 불필요 | LoginRequestDto | DataApiResponseDto<TokenResponseDto> | 이메일 로그인
GET    | /user/me | 필요 | — | DataApiResponseDto<UserProfileDto> | 내 프로필 조회
```

인증 여부 판단:
- `@LoginUser` 파라미터 있으면 → **필요**
- 없으면 → **불필요**

---

## Step 3. 에러 코드 목록 추출

```bash
Read src/main/java/teamdevhub/devhub/shared/enums/ErrorCode.java
```

다음 형식으로 변환한다:

```
코드 | 메시지 | HTTP 상태 | 발생 상황
ERR.DVH.0001 | 이미 가입된 이메일입니다 | 409 | 이메일 중복 회원가입 시도
```

---

## Step 4. 미완성 기능 표시

주석처리된 기능은 `🚧 미완성` 배지를 붙여 표시한다:

```bash
Grep "//.*public\|/\*.*\*/" src/main/java --include="*.java" -n
```

---

## Step 5. Notion MCP로 업데이트

Notion MCP 설정은 `.claude/mcp/notion.md`를 참조한다.

업데이트 순서:
1. 연관 Notion 페이지 ID 확인
2. 변경된 섹션만 업데이트 (전체 덮어쓰기 금지)
3. 페이지 하단에 동기화 타임스탬프 기록:
   ```
   _마지막 동기화: YYYY-MM-DD HH:mm (KST)_
   ```

---

## Step 6. 동기화 로그 출력

```
## Notion 동기화 완료

### 업데이트된 페이지
- API 명세: /auth 섹션 2개 엔드포인트 추가
- 에러 코드: ERR.DVH.0042 추가

### 스킵된 페이지 (변경 없음)
- 도메인 설계
- 개발 가이드

### 동기화 일시
- 2024-XX-XX HH:mm KST
```
