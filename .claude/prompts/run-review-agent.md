Act as the Code Review Agent.

Review the CURRENT backend codebase state.

Do not review only one class.
Do not focus on git diff.


Review the auth and user modules as complete domains.

Analyze:

- package structure
- controller/facade/service/domain boundaries
- duplicated responsibilities
- hidden bugs
- weak tests
- maintainability risks
- security concerns
- refactoring priorities

Use strict senior standards.

Output in Korean markdown file:

docs/reviews/codebase-auth-user-review.md