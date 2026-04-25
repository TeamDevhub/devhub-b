# DevHub 백엔드 — Claude Code 운영 가이드

## 프로젝트 개요

DevHub는 개발자 프로젝트 매칭 및 팀 빌딩 플랫폼의 백엔드 서비스다.

| 항목 | 내용 |
|---|---|
| 언어 / 프레임워크 | Java 17 / Spring Boot 3.5.7 |
| 빌드 | Gradle |
| 아키텍처 | 헥사고날 아키텍처 (Ports & Adapters) |
| 인증 | JWT Stateless — 액세스 토큰(Bearer) + 리프레시 토큰(HTTP-only 쿠키) |
| OAuth | Google · GitHub · Kakao · Naver |
| DB | H2 (개발/테스트 환경) |
| 주요 라이브러리 | Spring Security · Spring Data JPA · QueryDSL 5.1.0 · Lombok |

---

## 최상위 패키지 구조

```
teamdevhub.devhub
├── api/          — REST: Controller, Request/Response DTO
├── core/         — 핵심: Domain · Service · Port(인터페이스)
├── outbound/     — 어댑터: JPA · Security · OAuth · Infrastructure
└── shared/       — 공통: Config · ErrorCode · SuccessCode · Util
```

---

## 워크스페이스 구조 및 역할

```
.claude/
├── CLAUDE.md             ← 지금 이 파일 — 전체 진입점
│
├── rules/                ← 코드 작성 시 반드시 참조
│   ├── architecture.md
│   ├── code-conventions.md
│   ├── naming.md
│   ├── error-handling.md
│   ├── testing.md
│   ├── persistence.md
│   ├── api-design.md
│   ├── refactoring.md
│   ├── feature-development.md
│   └── things-to-avoid.md
│
├── agents/               ← 반복 작업용 역할 정의
│   ├── readme-agent.md
│   ├── test-agent.md
│   ├── refactor-agent.md
│   ├── notion-agent.md
│   └── build-agent.md
│
├── skills/               ← 작업 단위 실행 절차
│   ├── write-readme.md
│   ├── generate-tests.md
│   ├── refactor-safely.md
│   ├── notion-sync.md
│   └── build-verify.md
│
├── memory/               ← 세션 간 영구 컨텍스트
│   ├── project-memory.md
│   ├── style-memory.md
│   └── workflow-memory.md
│
├── mcp/                  ← 외부 도구 연결 설정
│   ├── github.md
│   ├── notion.md
│   ├── filesystem.md
│   ├── terminal.md
│   └── ci.md
│
└── prompts/              ← 에이전트 실행 트리거
    ├── run-readme-agent.md
    ├── run-test-agent.md
    ├── run-refactor-agent.md
    ├── run-notion-agent.md
    └── run-build-agent.md
```

---

## 규칙 파일 빠른 참조

| 파일 | 언제 읽어야 하는가 |
|---|---|
| `rules/architecture.md` | 새 클래스 추가 · 계층 간 의존성 결정 |
| `rules/naming.md` | 클래스 · 메서드 · 패키지 이름 결정 |
| `rules/code-conventions.md` | 코드 작성 전반 |
| `rules/error-handling.md` | 예외 처리 · API 응답 |
| `rules/testing.md` | 테스트 코드 작성 |
| `rules/persistence.md` | JPA 엔티티 · 어댑터 · 레포지토리 |
| `rules/api-design.md` | 컨트롤러 · DTO · Security |
| `rules/refactoring.md` | 기존 코드 수정 |
| `rules/feature-development.md` | 신규 기능 추가 |
| `rules/things-to-avoid.md` | 항상 — 안티패턴 방지 |

---

## 에이전트 실행 방법

| 작업 | 실행 명령 |
|---|---|
| README 작성 | `prompts/run-readme-agent.md` 참조 |
| 테스트 생성 | `prompts/run-test-agent.md` 참조 |
| 안전한 리팩토링 | `prompts/run-refactor-agent.md` 참조 |
| Notion 동기화 | `prompts/run-notion-agent.md` 참조 |
| 빌드 검증 | `prompts/run-build-agent.md` 참조 |

---

## 현재 알려진 미완성 영역

- `User.changePassword()` — 주석처리 상태, 비밀번호 변경 기능 미완성
- `UserProfileUseCase.updatePassword()` — 인터페이스에서 주석 처리됨
- `WebSecurityConfig` 106번 라인 — `/admin/**`가 `permitAll()` 상태, ADMIN 권한 검증 필요
- `ProjectServiceTest` — 의존성 불일치로 전체 주석 상태

---

## Claude가 이 프로젝트에서 준수해야 하는 핵심 원칙

1. **포트 인터페이스를 통해서만 의존한다** — 서비스는 JPA를 모른다.
2. **도메인 클래스에 Spring 어노테이션을 붙이지 않는다.**
3. **컨트롤러는 Facade만 주입받는다.**
4. **단위 테스트에서 Mockito를 쓰지 않는다** — Fake 구현체를 사용한다.
5. **에러 메시지를 하드코딩하지 않는다** — `ErrorCode` enum을 사용한다.
6. **setter를 추가하지 않는다** — 도메인 메서드로 상태를 변경한다.
7. **주석으로 코드를 비활성화하지 않는다** — 삭제하거나 완성한다.
8. **모든 API 응답은 `DataApiResponseDto<T>`로 감싼다.**
