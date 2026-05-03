# DevHub Backend — Claude Code Operating Guide

## Project Overview

DevHub is the backend service for a developer project matching and team-building platform.

| Item                 | Description                                                              |
| -------------------- | ------------------------------------------------------------------------ |
| Language / Framework | Java 17 / Spring Boot 3.5.7                                              |
| Build Tool           | Gradle                                                                   |
| Architecture         | Hexagonal Architecture (Ports & Adapters)                                |
| Authentication       | JWT Stateless — Access Token (Bearer) + Refresh Token (HTTP-only Cookie) |
| OAuth                | Google · GitHub · Kakao · Naver                                          |
| Database             | H2 (Development / Test Environment)                                      |
| Main Libraries       | Spring Security · Spring Data JPA · QueryDSL 5.1.0 · Lombok              |

---

## Top-Level Package Structure

```text
teamdevhub.devhub
├── api/          — REST: Controller, Request/Response DTO
├── core/         — Core: Domain · Service · Port (Interfaces)
├── outbound/     — Adapters: JPA · Security · OAuth · Infrastructure
└── shared/       — Common: Config · ErrorCode · SuccessCode · Util
```

---

## Workspace Structure and Roles

```text
.claude/
├── CLAUDE.md             ← This file — primary entry point
│
├── rules/                ← Must be referenced when writing code
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
├── agents/               ← Role definitions for repetitive tasks
│   ├── super-agent.md    ← Orchestration agent (use first)
│   ├── code-review-agent.md
│   ├── feature-agent.md
│   ├── refactor-agent.md
│   ├── test-agent.md
│   ├── readme-agent.md
│   ├── notion-agent.md
│   └── build-agent.md
│
├── docs/                 ← Generated review and analysis documents
│   ├── full-project-review.md          ← Full codebase review (2026-05-03)
│   └── fix-user-review-score-double-subtraction.md
│
├── skills/               ← Step-by-step execution procedures
│   ├── write-readme.md
│   ├── generate-tests.md
│   ├── refactor-safely.md
│   ├── notion-sync.md
│   └── build-verify.md
│
├── memory/               ← Persistent context across sessions
│   ├── project-memory.md
│   ├── style-memory.md
│   └── workflow-memory.md
│
└── prompts/              ← Agent execution triggers
    ├── run-readme-agent.md
    ├── run-test-agent.md
    ├── run-refactor-agent.md
    ├── run-notion-agent.md
    └── run-build-agent.md
```

---

## Quick Reference for Rule Files

| File                           | When to Read                                       |
| ------------------------------ | -------------------------------------------------- |
| `rules/architecture.md`        | Adding new classes · deciding dependency direction |
| `rules/naming.md`              | Naming classes · methods · packages                |
| `rules/code-conventions.md`    | General coding standards                           |
| `rules/error-handling.md`      | Exception handling · API responses                 |
| `rules/testing.md`             | Writing test code                                  |
| `rules/persistence.md`         | JPA entities · adapters · repositories             |
| `rules/api-design.md`          | Controllers · DTOs · Security                      |
| `rules/refactoring.md`         | Modifying existing code                            |
| `rules/feature-development.md` | Adding new features                                |
| `rules/things-to-avoid.md`     | Always — anti-pattern prevention                   |

---

## How to Run Agents

Use `super-agent.md` as the orchestration entry point for all tasks.

| Task               | Command                                        |
| ------------------ | ---------------------------------------------- |
| Code Review        | `run-super-agent code-review-agent [scope]`    |
| Feature Work       | `run-super-agent feature-agent [scope]`        |
| Refactoring        | `run-super-agent refactor-agent [scope]`       |
| Generate Tests     | `run-super-agent test-agent [scope]`           |
| Write README       | Refer to `prompts/run-readme-agent.md`         |
| Sync to Notion     | Refer to `prompts/run-notion-agent.md`         |
| Build Verification | Refer to `prompts/run-build-agent.md`          |

Valid scopes: `auth` · `user` · `project` · `admin` · `board` · `changed-files` · `all`

---

## Currently Known Incomplete Areas

* `ProjectServiceTest` — fully commented out due to dependency mismatch (`FakeProjectLikeRepository` missing)

---

## Known Technical Debt (from full-project-review 2026-05-03)

Critical issues requiring fix before release — see `docs/full-project-review.md` for full detail.

**Security (Block Release)**
* `OauthController` — OAuth CSRF: no state parameter on callback validation
* `LoggingAspect` — serializes all service params including passwords to INFO log
* `TraceIdMDCFilter` — `X-Trace-Id` header value injected into MDC without sanitization (log injection)
* `FileResponseFactory.attachment()` — Content-Disposition header injection via unsanitized filename
* `CookieFactory` — Refresh Token cookie `secure=false`
* `OauthController` — hardcoded `http://localhost:5173` redirect URL

**Correctness**
* `ProjectAdapter.getProjectDetail()` — `.get()` without `orElseThrow` (NoSuchElementException risk)
* `ProjectService.deleteProject()` — `findAllGuidByProjectGuid` called after `deleteByProjectGuid` (always returns empty)
* `GlobalExceptionHandler` — all unhandled exceptions return HTTP 400 instead of 500
* `AuthController.login()` — `@Valid` missing on `LoginRequestDto`

**Architecture**
* `BoardService` / `BoardQueryService` — directly depend on `UserRepository` (cross-domain violation)
* `Project` domain — `@Builder` is public, bypassing factory method enforcement
* `Project.getRecruitStatus()` — NPE if `recruitmentStartDate` / `recruitmentEndDate` is null
* `ProjectService.updateProject()` — inline TODO comments in production code

---

## Core Principles Claude Must Follow in This Project

1. **Depend only through port interfaces** — services must not know JPA directly.
2. **Do not annotate domain classes with Spring annotations.**
3. **Controllers must inject Facades only.**
4. **Do not use Mockito in unit tests** — use Fake implementations instead.
5. **Do not hardcode error messages** — use the `ErrorCode` enum.
6. **Do not add setters** — change state through domain methods.
7. **Do not disable code with comments** — delete it or complete it.
8. **All API responses must be wrapped with `DataApiResponseDto<T>`.**
