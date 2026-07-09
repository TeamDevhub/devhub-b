# Naming Conventions

## Class Naming

| Type                     | Pattern                                    | Example                                                     |
| ------------------------ | ------------------------------------------ | ----------------------------------------------------------- |
| Domain Entity            | `{Domain}`                                 | `User`, `Verification`                                      |
| UseCase Interface        | `{Role}UseCase`                            | `UserSignupUseCase`, `AuthenticationUseCase`                |
| Service Implementation   | `{Role}Service`                            | `UserSignupService`, `UserCredentialService`                |
| Facade                   | `{Domain}{Role}Facade`                     | `UserSignupFacade`, `AuthFacade`, `OAuthAuthFacade`         |
| Output Port (Repository) | `{Domain}Repository`                       | `UserCredentialRepository`, `VerificationRepository`        |
| Output Port (Provider)   | `{Feature}Provider`                        | `TokenIssueProvider`, `EncodedPasswordProvider`             |
| JPA Repository           | `Jpa{Domain}Repository`                    | `JpaEmailCredentialRepository`, `JpaUserRepository`         |
| Adapter                  | `{Domain}Adapter`                          | `UserCredentialAdapter`, `EmailUserCredentialAdapter`       |
| Mapper                   | `{Domain}Mapper`                           | `UserMapper`, `VerificationMapper`                          |
| JPA Entity               | `{Domain}Entity`                           | `UserEntity`, `EmailCredentialEntity`                       |
| Controller               | `{Domain}{Role}Controller`                 | `UserSignupController`, `AuthController`                    |
| Request DTO              | `{Action}{Target}RequestDto`               | `SignupRequestDto`, `LoginRequestDto`                       |
| Response DTO             | `{Target}ResponseDto`                      | `TokenResponseDto`, `UserDetailResponseDto`                 |
| Command Object           | `{Verb}{Target}Command`                    | `SignupUserCommand`, `UpdateProfileCommand`, `LoginCommand` |
| Result Object            | `{Verb}{Target}Result` or `{Target}Result` | `AuthResult`, `OAuthAuthResult`, `OAuthUserResult`          |
| Change Result Object     | `{Target}ChangeResult`                     | `UserPositionChangeResult`, `UserSkillChangeResult`         |

---

## Method Naming

## Domain Entity Methods

* **Creation**: `create{Role}()`
  Examples:

    * `createAdminUser()`
    * `createGeneralUser()`
    * `issue()`

* **Reconstruction**: `of(...)`
  Used when restoring an existing domain object from persisted data

* **State Change**: meaningful verbs
  Examples:

    * `withdraw()`
    * `confirm()`
    * `updateBasicProfile()`
    * `changePositions()`

* **Validation**: `assertXxx()`
  Example:

    * `assertValid()`

---

## Service / Facade Methods

* Create:

    * `save{Target}()`
    * `signup()`
    * `create{Target}()`

* Read:

    * `get{Target}()`
    * `find{Target}()`

* Update:

    * `update{Target}()`

* Delete:

    * `delete{Target}()`
    * `revoke()`
    * `consume()`

* Authentication:

    * `authenticate()`
    * `login()`
    * `logout()`

---

## Adapter / Repository Methods

* Follow standard JPA query method naming conventions
* Retrieval guaranteeing existence:

    * `findBy{Condition}()` (throws if not found)
* Optional retrieval:

    * `findBy{Condition}()` (distinguished by return type)
* Save:

    * `save{Target}()`
    * `saveAll()`

---

## Fake Test Double Naming

* `Fake{ClassName}`
  Examples:

    * `FakeUserRepository`
    * `FakeTokenParseProvider`

* `Stub{ClassName}`
  Always-throwing doubles

Example:

* `StubOAuthClient`

---

## Package Naming

* Domain package names use singular nouns:

```text id="7m2qvx"
user
auth
project
board
```

* Sub-packages should clearly indicate responsibility:

```text id="3p8ltk"
domain
application
port
adapter
persistence
```

---

## Constant Naming

* Test constants should be grouped in:

```text id="5r1xnd"
{Domain}TestConstant
```

* Constant names follow:

```text id="9v4kqs"
TEST_{TARGET}_{TYPE}
```

Examples:

```text id="2n7wcf"
TEST_USER_GUID_1
TEST_EMAIL_1
TEMP_TOKEN
```
