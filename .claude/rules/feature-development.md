# 신규 기능 추가 방법

## 개발 순서

신규 기능을 추가할 때는 반드시 아래 순서를 따른다. 계층 간 의존 방향이 안쪽 → 바깥쪽이기 때문에, 안쪽(도메인)부터 작성한다.

```
1. 도메인 모델
2. 커맨드/결과 객체
3. 포트 인터페이스 (UseCase + Repository)
4. 서비스 구현
5. Facade (필요한 경우)
6. 어댑터 + JPA 엔티티 + Mapper
7. 컨트롤러 + DTO
8. 테스트
```

## 단계별 체크리스트

### 1단계: 도메인 모델 (`core/{domain}/domain/`)

- [ ] 엔티티 클래스 또는 VO `record` 작성
- [ ] `@Builder(access = PRIVATE)` + private 생성자
- [ ] 정적 팩토리 메서드 (`create*`, `of`, `issue`)
- [ ] 비즈니스 메서드 작성
- [ ] 도메인 규칙 위반 시 `DomainRuleException.of(ErrorCode.XXX)` 사용

### 2단계: 커맨드/결과 객체 (`core/{domain}/port/in/command/`)

- [ ] `@Builder` + `record`로 커맨드 작성
- [ ] 변환 메서드 추가 (`toXxxCommand()`) — 필요한 경우

### 3단계: 포트 인터페이스

입력 포트 (`core/{domain}/port/in/usecase/`):
- [ ] UseCase 인터페이스 작성

출력 포트 (`core/{domain}/port/out/`):
- [ ] Repository 인터페이스 작성
- [ ] Provider 인터페이스 작성 (외부 의존이 있는 경우)

### 4단계: 서비스 구현 (`core/{domain}/application/`)

- [ ] `@Service`, `@Transactional`, `@RequiredArgsConstructor` 선언
- [ ] UseCase 인터페이스 구현
- [ ] 포트 인터페이스만 의존성으로 주입 (JPA 클래스 직접 참조 금지)
- [ ] 비즈니스 정책 위반 시 `BusinessRuleException.of(ErrorCode.XXX)` 사용

### 5단계: Facade (`core/{domain}/port/in/facade/`)

두 개 이상의 UseCase 조합이 필요한 경우에만 작성한다.

- [ ] `@Service`, `@Transactional`, `@RequiredArgsConstructor` 선언
- [ ] UseCase 인터페이스만 의존성으로 주입
- [ ] 조합 로직만 작성, 비즈니스 로직 직접 구현 금지

### 6단계: 어댑터 + 영속성 (`outbound/{domain}/`)

JPA 엔티티 (`adapter/entity/`):
- [ ] `@Entity`, `@Getter`, `@Builder`, `@NoArgsConstructor(access = PROTECTED)` 선언
- [ ] Enum 필드에 `@Enumerated(EnumType.STRING)` 적용
- [ ] 적절한 `@Table`, `@UniqueConstraint` 설정

JPA Repository (`persistence/`):
- [ ] `Jpa{도메인}Repository extends JpaRepository<Entity, PkType>` 작성

Mapper (`adapter/mapper/`):
- [ ] `{도메인}Mapper` 클래스 작성 (정적 메서드만)
- [ ] `toEntity()`, `toDomain()` 양방향 제공

어댑터 (`adapter/`):
- [ ] `@Component`, `@RequiredArgsConstructor` 선언
- [ ] 포트 인터페이스 구현
- [ ] 데이터 없을 때 `AdapterDataException.of(ErrorCode.XXX)` 또는 `Optional.empty()` 반환

### 7단계: API 계층 (`api/{domain}/`)

DTO (`api/{domain}/model/`):
- [ ] Request: `record` + `@Builder` + Bean Validation 어노테이션
- [ ] Request: `to{Command}()` 변환 메서드
- [ ] Response: `record` + 정적 팩토리 메서드

컨트롤러 (`api/{domain}/controller/`):
- [ ] `@RestController`, `@RequestMapping`, `@RequiredArgsConstructor` 선언
- [ ] Facade만 주입
- [ ] `ResponseEntity<DataApiResponseDto<T>>` 반환 타입
- [ ] `@Valid` + `@RequestBody` 사용

### 8단계: 테스트

- [ ] 도메인 단위 테스트 (`small/core/{domain}/domain/`)
- [ ] 서비스 단위 테스트 (`small/core/{domain}/application/`) — Fake 사용
- [ ] Facade 단위 테스트 (`small/core/{domain}/port/facade/`) — Fake UseCase 사용
- [ ] 어댑터 통합 테스트 (`medium/outbound/{domain}/adapter/`) — `@SpringBootTest`
- [ ] 컨트롤러 통합 테스트 (`medium/api/{domain}/controller/`) — `@MockitoBean` Facade
- [ ] Fake 구현체 작성 (`fake/pure/application/port/`) — 새 Repository가 있는 경우

## 신규 도메인 추가 시 패키지 생성 예시

`notification` 도메인을 새로 추가한다고 가정:

```
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
