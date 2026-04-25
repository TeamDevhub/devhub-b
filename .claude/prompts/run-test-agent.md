# Prompt: Run Test Agent

## Prompt

Act as the Test Agent and improve test coverage for auth/user related source code.

Refer to the following files:

- .claude/agents/test-agent.md — agent role and principles
- .claude/skills/generate-tests.md — test generation workflow
- .claude/rules/testing.md — testing rules
- .claude/memory/style-memory.md — project style memory

## Scope Restriction

This task is limited to auth/user related source and test files only.

Source scope:

- src/main/java/.../auth/**
- src/main/java/.../user/**

Test scope:

- src/test/java/**/small/**/auth/**
- src/test/java/**/small/**/user/**
- src/test/java/**/medium/**/auth/**
- src/test/java/**/medium/**/user/**

Do not modify files outside this scope.

Files outside the scope may be read for reference only.

---

## Goal

Within the auth/user domains:

1. Detect production classes with missing tests
2. Add missing test classes
3. Add missing scenarios to existing tests
4. Fix broken, weak, duplicated, or low-quality tests
5. Improve consistency with existing small/medium test style
6. Create missing Fake classes when necessary

---

## Workflow

1. Scan auth/user production classes
2. Match each class with existing small/medium tests
3. Identify missing or insufficient coverage
4. Read public methods, dependencies, and behavior
5. Decide proper test type (`small` or `medium`)
6. Reuse existing Fake/TestFixture classes if available
7. If required Fake classes do not exist, create them
8. Write or improve tests
9. Validate key scenarios:

- success case
- null / missing input
- duplicate data
- not found case
- validation failure
- permission/auth failure
- business rule violation

10. Run:

```bash id="5ep6x0"
./gradlew compileTestJava