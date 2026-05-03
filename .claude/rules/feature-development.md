# How to Add a New Feature

## Development Order

When adding a new feature, always follow the sequence below.
Because dependency flow must go **inner → outer**, implementation starts from the innermost layer (domain).

```text id="5n2xkr"
1. Domain model
2. Command / Result objects
3. Port interfaces (UseCase + Repository)
4. Service implementation
5. Facade (if needed)
6. Adapter + JPA entity + Mapper
7. Controller + DTO
8. Tests
```

---

## Step-by-Step Checklist

## Step 1: Domain Model (`core/{domain}/domain/`)

* [ ] Create entity class or VO `record`
* [ ] Use `@Builder(access = PRIVATE)` + private constructor
* [ ] Add static factory methods (`create*`, `of`, `issue`)
* [ ] Implement business methods
* [ ] Use `DomainRuleException.of(ErrorCode.XXX)` for domain rule violations

---

## Step 2: Command / Result Objects (`core/{domain}/port/in/command/`)

* [ ] Create commands using `@Builder` + `record`
* [ ] Add conversion methods (`toXxxCommand()`) when needed

---

## Step 3: Port Interfaces

### Input Ports (`core/{domain}/port/in/usecase/`)

* [ ] Create UseCase interfaces

### Output Ports (`core/{domain}/port/out/`)

* [ ] Create Repository interfaces
* [ ] Create Provider interfaces (when external dependencies exist)

---

## Step 4: Service Implementation (`core/{domain}/application/`)

* [ ] Declare:

    * `@Service`
    * `@Transactional`
    * `@RequiredArgsConstructor`
* [ ] Implement UseCase interfaces
* [ ] Inject only port interfaces (no direct JPA dependency)
* [ ] Use `BusinessRuleException.of(ErrorCode.XXX)` for business policy violations

---

## Step 5: Facade (`core/{domain}/port/in/facade/`)

Create only when two or more UseCases must be combined.

* [ ] Declare:

    * `@Service`
    * `@Transactional`
    * `@RequiredArgsConstructor`
* [ ] Inject only UseCase interfaces
* [ ] Write orchestration logic only
* [ ] Do not implement business logic directly

---

## Step 6: Adapter + Persistence (`outbound/{domain}/`)

### JPA Entity (`adapter/entity/`)

* [ ] Declare:

    * `@Entity`
    * `@Getter`
    * `@Builder`
    * `@NoArgsConstructor(access = PROTECTED)`
* [ ] Apply `@Enumerated(EnumType.STRING)` to enum fields
* [ ] Configure proper `@Table`, `@UniqueConstraint`

### JPA Repository (`persistence/`)

* [ ] Create:

```text id="8v1mqt"
Jpa{Domain}Repository extends JpaRepository<Entity, PkType>
```

### Mapper (`adapter/mapper/`)

* [ ] Create `{Domain}Mapper` class (static methods only)
* [ ] Provide both:

    * `toEntity()`
    * `toDomain()`

### Adapter (`adapter/`)

* [ ] Declare:

    * `@Component`
    * `@RequiredArgsConstructor`
* [ ] Implement port interfaces
* [ ] If data is missing:

    * return `AdapterDataException.of(ErrorCode.XXX)`
    * or `Optional.empty()`

---

## Step 7: API Layer (`api/{domain}/`)

### DTO (`api/{domain}/model/`)

* [ ] Request:

    * `record`
    * `@Builder`
    * Bean Validation annotations
* [ ] Request includes `to{Command}()` conversion method
* [ ] Response:

    * `record`
    * static factory methods

### Controller (`api/{domain}/controller/`)

* [ ] Declare:

    * `@RestController`
    * `@RequestMapping`
    * `@RequiredArgsConstructor`
* [ ] Inject Facade only
* [ ] Return type:

```text id="2q7ldp"
ResponseEntity<DataApiResponseDto<T>>
```

* [ ] Use:

    * `@Valid`
    * `@RequestBody`

---

## Step 8: Tests

* [ ] Domain unit tests
  `small/core/{domain}/domain/`

* [ ] Service unit tests (using Fake implementations)
  `small/core/{domain}/application/`

* [ ] Facade unit tests (using Fake UseCases)
  `small/core/{domain}/port/facade/`

* [ ] Adapter integration tests (`@SpringBootTest`)
  `medium/outbound/{domain}/adapter/`

* [ ] Controller integration tests (`@MockitoBean` Facade)
  `medium/api/{domain}/controller/`

* [ ] Fake implementations for new repositories
  `fake/pure/application/port/`

---

## Example Package Structure for a New Domain

Assume a new `notification` domain is added:

```text id="6m3xra"
core/notification/
├── domain/
│   ├── Notification.java
│   └── vo/
│       └── NotificationType.java
├── application/
│   └── service/
│       └── NotificationService.java
└── port/
    ├── in/
    │   ├── command/
    │   │   └── SendNotificationCommand.java
    │   ├── facade/
    │   │   └── NotificationFacade.java
    │   └── usecase/
    │       └── NotificationUseCase.java
    └── out/
        └── NotificationRepository.java

outbound/notification/
├── adapter/
│   ├── entity/
│   │   └── NotificationEntity.java
│   ├── mapper/
│   │   └── NotificationMapper.java
│   └── NotificationAdapter.java
└── persistence/
    └── JpaNotificationRepository.java

api/notification/
├── controller/
│   └── NotificationController.java
└── model/
    ├── request/
    └── response/
```
