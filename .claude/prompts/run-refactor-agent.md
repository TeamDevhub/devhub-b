# Prompt: Run Refactor Agent

## Prompt

Act as the Refactor Agent and safely refactor [Target].

Refer to the following files:

- .claude/agents/refactor-agent.md
- .claude/skills/refactor-safely.md
- .claude/rules/refactoring.md
- .claude/rules/things-to-avoid.md

## Scope Restriction

This task is limited to the following directories only:

- src/main/java/.../auth/**
- src/main/java/.../user/**
- src/test/java/.../auth/**
- src/test/java/.../user/**

Do not modify files outside this scope.

Files outside the scope may be read for reference only.  
If external changes are required, do not edit them.  
Instead, include the impact and recommended follow-up actions in the final report.

## Refactoring Goal

[Example: Remove duplicated logic in the auth service layer]

## Workflow

1. Analyze target files within the auth/user directories
2. List current problems and refactoring opportunities
3. Check impact scope using grep/search
4. Verify whether tests exist (if not, create tests only within auth/user scope first)
5. Perform refactoring incrementally, one goal at a time
6. Run `./gradlew compileJava`
7. Run related tests to verify behavior is preserved
8. Write a final refactoring report

## Strictly Do Not Change

- Port interface method signatures
- `ErrorCode` enum values
- Any files outside auth/user directories

Target: [File path or class name inside auth/user]