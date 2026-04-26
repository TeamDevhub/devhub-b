# Senior Review Skill

When reviewing code, always inspect:

## Architecture

- Is controller too fat?
- Is service doing too many responsibilities?
- Is domain logic misplaced?
- Is infra leaking upward?

## Spring Backend

- Transaction boundary correct?
- N+1 risk?
- Optional misuse?
- Null safety?
- Validation location correct?
- Exception type meaningful?

## Testing

- Are tests missing?
- Are tests brittle?
- Happy path only?
- Failure path absent?

## Security

- auth bypass risk?
- trusting client input?
- token handling safe?
- sensitive logs exposed?

## Maintainability

- duplicate logic?
- magic strings?
- over-complex method?
- poor naming?
- hidden side effects?

## Always Prefer

Concrete fixes over abstract criticism.