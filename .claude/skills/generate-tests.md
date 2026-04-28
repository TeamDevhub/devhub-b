# Skill: 테스트 코드 생성

## 목적

이 프로젝트의 규칙에 맞는 테스트 코드를 생성하는 절차를 정의한다.

---

## Step 1. 대상 분석

대상 클래스를 읽고 다음을 파악한다:

- public 메서드 목록
- 각 메서드의 성공 시나리오
- 각 메서드의 실패 시나리오 (예외 조건)
- 외부 의존성 목록 (포트 인터페이스들)

```bash
# 클래스 파일 읽기
Read src/main/java/teamdevhub/devhub/core/{domain}/application/service/{Class}.java
```

---

## Step 2. 테스트 종류 결정

| 대상 클래스 | 테스트 종류 | 패키지 |
|---|---|---|
| 도메인 엔티티 (`User`, `Verification`) | small | `small/core/{domain}/domain/` |
| 서비스 (`XxxService`) | small | `small/core/{domain}/application/service/` |
| Facade (`XxxFacade`) | small | `small/core/{domain}/port/facade/` |
| 어댑터 (`XxxAdapter`) | medium | `medium/outbound/{domain}/adapter/` |
| 컨트롤러 (`XxxController`) | medium | `medium/api/{domain}/controller/` |
| DTO 검증 (`XxxRequestDto`) | medium | `medium/api/{domain}/model/request/` |

---

## Step 3. 필요한 Fake 확인 및 생성

단위 테스트에 필요한 Fake가 `fake/` 패키지에 있는지 확인한다.

```bash
# 기존 Fake 확인
Glob src/test/java/teamdevhub/devhub/fake/**/*.java
```

없는 Fake는 아래 템플릿으로 먼저 작성한다:

```java
package teamdevhub.devhub.fake.pure.application.port.out.{domain};

public class Fake{PortName} implements {PortInterface} {

    private final Map<String, T> store = new HashMap<>();

    // given*() 메서드: 테스트 데이터 사전 세팅
    public void given{Data}({Type} value) {
        store.put(key, value);
    }

    @Override
    public Optional<T> findBy{X}(String x) {
        return Optional.ofNullable(store.get(x));
    }
}
```

---

## Step 4. 테스트 클래스 작성

### 단위 테스트 템플릿

```java
package teamdevhub.devhub.small.core.{domain}.application.service;

import static org.assertj.core.api.Assertions.*;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class {ClassName}Test {

    private {TargetClass} target;

    // Fake 의존성들
    private Fake{Port1} fake{Port1};

    @BeforeEach
    void init() {
        fake{Port1} = new Fake{Port1}();
        target = new {TargetClass}(fake{Port1});
    }

    @Test
    @DisplayName("{시나리오_한국어_설명}")
    void {methodName}_{scenario}() {
        // given
        {설정 코드}

        // when
        {실행 코드}

        // then
        assertThat({결과}).{검증};
    }

    @Test
    @DisplayName("{실패_시나리오_한국어_설명}")
    void {methodName}_{scenario}_throwsException() {
        // given
        {설정 코드}

        // when, then
        assertThatThrownBy(() -> {실행 코드})
                .isInstanceOf({ExceptionClass}.class)
                .hasMessageContaining({ErrorCode}.getMessage());
    }
}
```

### 통합 테스트 템플릿 (어댑터)

```java
@SpringBootTest
@Transactional
class {AdapterClass}Test {

    @Autowired
    private {AdapterClass} adapter;

    @Autowired
    private Jpa{Domain}Repository jpaRepository;

    @BeforeEach
    void init() {
        jpaRepository.deleteAll();
    }

    @Test
    @DisplayName("{시나리오}")
    void {method}_{scenario}() {
        // given: JPA로 직접 저장하거나 어댑터 save 호출

        // when: 어댑터 조회/수정 메서드 호출

        // then: JPA로 직접 조회하여 DB 상태 검증
    }
}
```

---

## Step 5. 시나리오 체크리스트

모든 public 메서드에 대해 다음 시나리오를 커버했는지 확인한다:

- [ ] 정상 동작 (happy path)
- [ ] 필수 조건 누락 (null, empty)
- [ ] 중복 데이터 처리
- [ ] 데이터 없음 (not found)
- [ ] 도메인 규칙 위반

---

## Step 6. 컴파일 검증

```bash
./gradlew compileTestJava
```

오류가 없으면 완료다.
