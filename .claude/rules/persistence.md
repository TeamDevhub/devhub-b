# Persistence Layer Rules

## JPA Entity Design

### Basic Structure

```java id="4m8xkr"
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)   // JPA default constructor: protected
@Table(name = "user_email_credentials")
public class EmailCredentialEntity {

    @Id
    @Column(length = 32, nullable = false, unique = true)
    private String userGuid;             // GUID-based PK: String type

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)         // Enum: must use STRING strategy
    @Column(nullable = false)
    private UserRole userRole;
}
```

---

### Rules

* Default constructor must be `PROTECTED`
* Prevent external `new XxxEntity()` usage
* If PK is GUID (`String`):

```java id="7q2vtn"
@Id
@Column(length = 32, nullable = false, unique = true)
```

* If PK is auto-increment (`Long`):

```java id="1p6xds"
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
```

* Enum columns must always use:

```java id="3n9kqa"
@Enumerated(EnumType.STRING)
```

* Boolean columns should not use `is` prefix

Use:

```text id="8r4mcf"
deleted
blocked
```

Not:

```text id="6t1vzw"
isDeleted
isBlocked
```

* Minimize relationship mappings:

    * `@ManyToOne`
    * `@OneToMany`

Use GUID references for cross-domain relations instead.

* Audit fields (creator, created time, etc.) should use:

    * `BaseEntity`
    * `@MappedSuperclass`

---

## JPA Repository Design

```java id="5k3xpt"
public interface JpaEmailCredentialRepository
        extends JpaRepository<EmailCredentialEntity, String> {

    Optional<EmailCredentialEntity> findByEmail(String email);
    Optional<EmailCredentialEntity> findByUserGuid(String userGuid);
}
```

### Rules

* Interface naming:

```text id="9m7qld"
Jpa{Domain}Repository
```

* Extend:

```text id="2v8xra"
JpaRepository<Entity, PKType>
```

* Be careful with PK type
* Use Spring Data JPA query methods for simple lookups
* For complex queries (dynamic filters, aggregates, pagination), create separate QueryDSL implementation:

```text id="6n1kcs"
{Domain}QueryDaoImpl
```

---

## Adapter Design

### Basic Structure

```java id="3t5vpm"
@Component
@RequiredArgsConstructor
public class EmailUserCredentialAdapter
        implements EmailUserCredentialRepository {

    private final JpaEmailCredentialRepository jpaEmailCredentialRepository;

    @Override
    public Optional<EmailUserCredential> findByEmail(String email) {
        return jpaEmailCredentialRepository.findByEmail(email)
                .map(this::toDomain);
    }

    private EmailUserCredential toDomain(
            EmailCredentialEntity entity
    ) {
        return new EmailUserCredential(
                entity.getUserGuid(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getUserRole()
        );
    }
}
```

### Rules

* Use `@Component`
* Do not use `@Repository`
* Simple conversion logic may remain as private methods inside adapter
* If mapping becomes complex or bidirectional, extract to separate `Mapper`

---

## Mapper Design

```java id="8x2ntr"
public class UserMapper {

    // Prevent instantiation
    private UserMapper() {}

    public static UserEntity toEntity(User user) {
        return UserEntity.builder()
                .userGuid(user.getUserGuid())
                ...
                .build();
    }

    public static User toDomain(UserEntity entity) {
        return User.of(
                entity.getUserGuid(),
                ...
        );
    }
}
```

### Rules

* Mapper is a static utility class
* No instances allowed
* Provide both directions:

    * `toEntity(Domain)`
    * `toDomain(Entity)`
* No business logic inside mapper
* Only field mapping
* Shared mappings (e.g. `AuditInfo`) may use private static helper methods

---

## QueryDSL Usage

* Implementation class naming:

```text id="1r7kzd"
{Domain}QueryDaoImpl
```

* Q classes are auto-generated under:

```text id="4v9xmb"
build/generated/querydsl/
```

* Do not modify generated Q classes directly
* Use QueryDSL only for:

    * dynamic condition filtering
    * pagination
    * complex aggregation

---

## Soft Delete Pattern

Do not physically delete DB records.
Use `deleted = true`.

```java id="7m3qcf"
// Soft delete in domain
public void withdraw() {
    this.deleted = true;
}

// Persist changed state in adapter
public void delete(User user) {
    jpaUserRepository.save(
            UserMapper.toEntity(user)
    ); // saved with deleted=true
}
```
