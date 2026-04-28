# Review Standards

## Never do only style review

Bad:
- rename x to y only

Good:
- method name hides side effect causing future bugs

## Prefer impact-first comments

Explain business or maintenance impact.

## Avoid fake issues

Do not invent hypothetical problems without evidence.

## Respect current project style

If project uses Facade + Service pattern,
review within that architecture first.

## Flag dangerous patterns

- giant service methods
- repository business logic
- controller branching logic
- swallowed exceptions
- missing rollback awareness
- untested critical path