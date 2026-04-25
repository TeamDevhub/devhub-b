# 영속성 계층 규칙

## JPA 엔티티 설계

### 기본 구조

```java
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)   // JPA 기본 생성자: protected
@Table(name = "user_email_credentials")
public class EmailCredentialEntity {

    @Id
    @Column(length = 32, nullable = false, unique = true)
    private String userGuid;             // GUID 기반 PK: String 타입

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)         // Enum: 반드시 STRING 방식
    @Column(nullable = false)
    private UserRole userRole;
}
```

### 규칙

- 기본 생성자는 `PROTECTED`로 선언한다. 외부에서 `new XxxEntity()`를 호출하지 못하게 막는다.
- PK가 GUID(`String`)인 경우: `@Column(length = 32, nullable = false, unique = true)` + `@Id`
- PK가 자동 증가(`Long`)인 경우: `@Id @GeneratedValue(strategy = GenerationType.IDENTITY)`
- Enum 컬럼은 반드시 `@Enumerated(EnumType.STRING)`을 사용한다.
- `boolean` 컬럼은 컬럼명에 `is` 접두사를 붙이지 않고 `deleted`, `blocked` 처럼 명명한다.
- 연관관계 매핑(`@ManyToOne`, `@OneToMany`)은 최소화한다. 다른 도메인의 데이터는 GUID 값으로 참조한다.
- Audit 정보(등록자, 등록일시 등)는 `BaseEntity`나 `@MappedSuperclass`를 활용한다.

## JPA Repository 설계

```java
public interface JpaEmailCredentialRepository extends JpaRepository<EmailCredentialEntity, String> {

    Optional<EmailCredentialEntity> findByEmail(String email);
    Optional<EmailCredentialEntity> findByUserGuid(String userGuid);
}
```

- 인터페이스 이름은 `Jpa{도메인}Repository` 패턴을 따른다.
- `JpaRepository<Entity, PK타입>`을 확장한다. PK 타입에 주의한다.
- 단순 조회는 Spring Data JPA 쿼리 메서드를 사용한다.
- 복잡한 조회(동적 조건, 집계, 페이징)는 QueryDSL 구현체(`{도메인}QueryDaoImpl`)를 별도로 작성한다.

## 어댑터 설계

### 기본 구조

```java
@Component
@RequiredArgsConstructor
public class EmailUserCredentialAdapter implements EmailUserCredentialRepository {

    private final JpaEmailCredentialRepository jpaEmailCredentialRepository;

    @Override
    public Optional<EmailUserCredential> findByEmail(String email) {
        return jpaEmailCredentialRepository.findByEmail(email)
                .map(this::toDomain);
    }

    private EmailUserCredential toDomain(EmailCredentialEntity entity) {
        return new EmailUserCredential(
                entity.getUserGuid(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getUserRole()
        );
    }
}
```

- `@Component`로 선언한다. `@Repository`를 사용하지 않는다.
- 단순 변환은 어댑터 내부의 private 메서드로 처리한다.
- 변환 로직이 복잡하거나 양방향으로 사용된다면 별도 `Mapper` 클래스로 분리한다.

## Mapper 설계

```java
public class UserMapper {

    // 인스턴스화 금지 (정적 유틸리티 클래스)
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

- Mapper는 정적 메서드만 가진 유틸리티 클래스다. 인스턴스를 만들지 않는다.
- `toEntity(Domain)`, `toDomain(Entity)` 두 방향 모두 제공한다.
- Mapper 안에 비즈니스 로직을 넣지 않는다. 단순 필드 매핑만 수행한다.
- `AuditInfo`처럼 공통 매핑이 있으면 private static 헬퍼 메서드로 분리한다.

## QueryDSL 사용

- QueryDSL 구현체 클래스는 `{도메인}QueryDaoImpl`로 명명한다.
- Q클래스는 `build/generated/querydsl/`에 자동 생성된다. 직접 수정하지 않는다.
- QueryDSL은 동적 조건 필터링, 페이지네이션, 복잡한 집계에만 사용한다.

## 소프트 삭제 패턴

삭제는 실제 DB 레코드를 지우지 않고 `deleted = true`로 처리한다.

```java
// 도메인에서 소프트 삭제 처리
public void withdraw() {
    this.deleted = true;
}

// 어댑터에서 save로 상태 반영
public void delete(User user) {
    jpaUserRepository.save(UserMapper.toEntity(user));  // deleted=true 상태로 저장
}
```
