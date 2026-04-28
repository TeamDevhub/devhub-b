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
│   ├── readme-agent.md
│   ├── test-agent.md
│   ├── refactor-agent.md
│   ├── notion-agent.md
│   └── build-agent.md
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

| Task               | Execution Command                        |
| ------------------ | ---------------------------------------- |
| Write README       | Refer to `prompts/run-readme-agent.md`   |
| Generate Tests     | Refer to `prompts/run-test-agent.md`     |
| Safe Refactoring   | Refer to `prompts/run-refactor-agent.md` |
| Sync to Notion     | Refer to `prompts/run-notion-agent.md`   |
| Build Verification | Refer to `prompts/run-build-agent.md`    |

---

## Currently Known Incomplete Areas

* `User.changePassword()` — commented out, password change feature incomplete
* `UserProfileUseCase.updatePassword()` — commented out in interface
* `WebSecurityConfig` line 106 — `/admin/**` is currently `permitAll()`, ADMIN authorization required
* `ProjectServiceTest` — fully commented out due to dependency mismatch

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
