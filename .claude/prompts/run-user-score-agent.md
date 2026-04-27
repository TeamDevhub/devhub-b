# Prompt: Run Feature Agent

## Prompt

Act as the Feature Agent and safely implement a new feature for [Target Feature].

Refer to the following files:

- .claude/agents/feature-agent.md
- .claude/rules/architecture.md
- .claude/rules/refactoring.md
- .claude/rules/things-to-avoid.md
- .claude/memory/style-memory.md

---

## Scope Restriction

This task is limited to the following directories only:

- src/main/java/.../project/**
- src/main/java/.../user/**
- src/main/java/.../common/**
- src/test/java/.../project/**
- src/test/java/.../user/**

Do not modify files outside this scope.

Files outside the scope may be read for reference only.  
If external changes are required, do not edit them.  
Instead, include the impact and recommended follow-up actions in the final report.

---

## Feature Goal

Implement the **Post-Project User Review Feature**.

After a project is completed, a project member can review another member using a 1~5 star score.

The review score updates the target user's `mannerDegree`.

### Business Rules

- 3.0 score = no change
- Every 0.5 score difference changes mannerDegree by ±0.5

Examples:

- 5.0 → +2.0
- 4.0 → +1.0
- 3.0 → 0
- 2.0 → -1.0
- 1.0 → -2.0

Formula:

delta = score - 3.0  
mannerDegree += delta

---

## API Specification

### Endpoint

POST /projects/{projectGuid}/members/{userGuid}

### Request Body

{
"userGuid": "target-user-guid",
"projectGuid": "project-guid",
"score": 5
}

---

## Required Validations

1. Project must be completed.
2. Reviewer must be a member of the project.
3. Target user must be a member of the project.
4. Reviewer cannot review themselves.
5. Duplicate review is not allowed.
6. Score must be between 1.0 and 5.0.
7. Score must use 0.5 increments only.

---

## Workflow

1. Analyze related project/user domain structure.
2. Identify best location for review logic.
3. Check existing tests.
4. Add tests first if missing.
5. Implement feature incrementally:

- Domain logic
- Command / DTO
- UseCase / Facade
- Repository Port
- Adapter
- Controller/API

6. Run `./gradlew compileJava`

---

## Strictly Do Not Change

- Existing API response formats
- Existing `ErrorCode` enum values
- Existing authentication flow
- Existing Port method signatures (new methods may be added)
- Any files outside allowed scope

---

## Expected Output

1. Production-ready source code
2. Unit tests
3. Service tests

---

## Target Feature

Post-project member review feature