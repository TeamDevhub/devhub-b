# 아키텍처 규칙

## 헥사고날 아키텍처 (Ports & Adapters)

이 프로젝트는 헥사고날 아키텍처를 실제로 지킨다. 계층 간 의존 방향은 항상 바깥 → 안쪽이다.

```
[api] → [core/port/in/facade] → [core/port/in/usecase]
                                        ↑
                              [core/application/service]
                                        ↓
                              [core/port/out (인터페이스)]
                                        ↑
                              [outbound/adapter (구현체)]
                                        ↓
                              [outbound/persistence/JpaRepository]
```

## 계층별 책임

### `api/` — REST 계층
- HTTP 요청 수신, DTO 변환, 응답 반환만 담당한다.
- `Facade`를 주입받아 호출한다. `UseCase`나 `Service`를 직접 주입받지 않는다.
- 비즈니스 판단을 하지 않는다.

### `core/{domain}/domain/` — 도메인 계층
- 도메인 엔티티, 값 객체(VO), 도메인 예외가 위치한다.
- **Spring 의존성이 없다.** `@Service`, `@Component` 등 어떤 Spring 어노테이션도 사용하지 않는다.
- 비즈니스 규칙을 메서드로 캡슐화한다.

### `core/{domain}/application/` — 애플리케이션 계층
- UseCase 인터페이스 구현체(`@Service`)가 위치한다.
- 포트 인터페이스(`port/out/`)에만 의존한다. JPA 클래스를 import하지 않는다.
- 도메인 객체를 가져와서 도메인 메서드를 호출하고 결과를 저장소에 위임한다.

### `core/{domain}/port/in/` — 입력 포트
- `usecase/`: 기능의 진입점이 되는 인터페이스
- `facade/`: 여러 UseCase를 조합하는 오케스트레이터 (`@Service`)
- `command/`: 입력 커맨드 객체 (`record`)

### `core/{domain}/port/out/` — 출력 포트
- 저장소, 외부 서비스 등의 인터페이스만 위치한다.
- 구현체는 여기에 없다. 구현체는 `outbound/`에 있다.

### `outbound/` — 어댑터 계층
- `adapter/`: 포트 인터페이스를 구현하는 `@Component`
- `persistence/`: `JpaRepository` 확장 인터페이스
- `infrastructure/`: JWT, OAuth, 비밀번호 인코딩 등 기술 구현체

## Facade 사용 기준

Facade를 작성해야 하는 경우:
- 두 개 이상의 UseCase가 하나의 트랜잭션 안에서 순서대로 실행되어야 할 때
- 예: 회원가입 = 인증 확인 → 자격증명 저장 → 사용자 정보 저장 → 약관 동의 → 인증 소비

Facade 없이 컨트롤러에서 직접 호출해도 되는 경우:
- UseCase가 단 하나만 관여하는 단순 조회

## 의존성 방향 위반 예시 (절대 금지)

```java
// 금지: 서비스에서 JPA 직접 사용
@Service
public class UserProfileService {
    private final JpaUserRepository jpaUserRepository; // ❌ outbound 직접 참조
}

// 금지: 컨트롤러에서 서비스 직접 주입
@RestController
public class UserController {
    private final UserProfileService userProfileService; // ❌ UseCase가 아닌 구현체 직접 주입
}

// 금지: 도메인 클래스에 Spring 어노테이션
@Entity // ❌ 도메인 객체에 JPA 어노테이션
public class User { ... }
```
