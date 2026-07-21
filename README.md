# DevHub Backend

> 개발자를 위한 프로젝트 매칭 & 팀빌딩 플랫폼 **DevHub**의 백엔드 서비스

DevHub는 사이드 프로젝트·스터디 팀원을 찾는 개발자들을 위한 매칭 플랫폼입니다. 이 저장소는 헥사고날 아키텍처(Ports & Adapters)를 기반으로 설계된 백엔드 API 서버로, 인증/OAuth, 프로젝트 매칭, 게시판, 관리자 기능 등 서비스 전 영역의 API를 제공합니다.

[![Java](https://img.shields.io/badge/Java-17-orange)](.)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen)](.)
[![Gradle](https://img.shields.io/badge/Build-Gradle-blue)](.)
[![QueryDSL](https://img.shields.io/badge/QueryDSL-5.1.0-lightgrey)](.)
[![Architecture](https://img.shields.io/badge/Architecture-Hexagonal-9cf)](.)

- 배포 도메인: `devhub-24.com`

---

## 목차

1. [핵심 설계 포인트](#핵심-설계-포인트)
2. [기술 스택](#기술-스택)
3. [아키텍처](#아키텍처)
4. [CI/CD 파이프라인](#cicd-파이프라인)
5. [로컬 실행 방법](#로컬-실행-방법)
6. [API 문서](#api-문서-swagger-ui)
7. [API 개요](#api-개요)
8. [인증 방식](#인증-방식)
9. [테스트 전략 & 커버리지](#테스트-전략--커버리지)
10. [주요 기능](#주요-기능)

---

## 핵심 설계 포인트

- **헥사고날 아키텍처** — 도메인 로직을 Spring/JPA 등 외부 기술로부터 분리. Service는 Port 인터페이스에만 의존하고, 실제 구현은 `outbound` 어댑터가 담당합니다. 13개 도메인 전체에 `domain / application / port(in·out) / adapter` 계층 구분을 일관되게 적용했습니다.
- **명확한 책임 분리** — Controller는 Facade만 주입받고, Facade는 여러 UseCase를 조합하는 오케스트레이션만 담당합니다. 전체적으로 Controller 24개 · Facade 23개 · UseCase 36개 · Service 36개 · Adapter 45개 · JPA Repository 28개 · QueryDSL Dao 8개로 구성되어, 계층별 역할이 파일 단위로 명확히 나뉩니다.
- **계층별 예외 체계** — 도메인/애플리케이션/어댑터 계층마다 `DomainRuleException` · `BusinessRuleException` · `AdapterDataException`을 구분하고, 63개로 세분화된 `ErrorCode`(성공 응답은 `SuccessCode` 15종)를 통해 일관된 에러 응답을 제공합니다. 컨트롤러에서 에러 응답을 직접 조립하지 않고 `GlobalExceptionHandler`가 전담합니다.
- **Fake 기반 단위 테스트** — Mockito 대신 Port 인터페이스를 직접 구현한 Fake 객체(67개)를 사용해, Mock 프레임워크에 의존하지 않는 순수 Java 단위 테스트를 작성합니다. 덕분에 `small` 테스트는 Spring Context 기동 없이 초 단위로 실행됩니다.
- **Stateless JWT 인증** — Access Token(Header) + Refresh Token(HttpOnly Cookie) 조합으로 세션을 사용하지 않는 무상태 인증을 구현하고, Google·GitHub·Kakao·Naver 4종 소셜 로그인을 지원합니다.
- **역할 기반 인가(RBAC) 적용 완료** — `/admin/**` 하위 전체 엔드포인트와 약관 등록(`POST /terms/**`)에 `hasRole("ADMIN")`을 적용해 관리자 전용 API를 실제로 보호합니다. 관리자 도메인은 회원·게시글·배너·공통코드·모집폼·프로젝트 6개 서브 컨트롤러로 세분화되어 있습니다.
- **OAuth 보안 강화** — OAuth 인증 시작 시 발급한 `state` 값을 `HttpOnly` 쿠키(`oauthState`)에 저장해두었다가 콜백 시 쿠키 값과 파라미터 값을 비교 검증해 CSRF를 방지합니다. Refresh Token·OAuth state 쿠키의 `Secure` 속성과 OAuth 성공 후 리다이렉트할 프론트엔드 주소(`app.frontend.base-url`)는 하드코딩 대신 환경변수로 주입받도록 분리되어 있습니다.
- **동적 쿼리 분리** — 단순 조회는 Spring Data JPA, 검색 필터·페이징·복합 조건이 필요한 조회는 QueryDSL 기반 `QueryDaoImpl`로 분리해 구현했습니다.
- **관측성** — Actuator·Micrometer Tracing·Prometheus를 연동하고, AOP 기반 `LoggingAspect`와 `TraceIdMDCFilter`로 요청 단위 트레이스 로깅을 남깁니다.
- **자동화된 CI/CD 파이프라인** — GitHub Actions가 빌드 → Docker 이미지 push → 인프라 저장소의 Kubernetes 매니페스트 갱신까지 자동으로 처리합니다. 배포 대상은 로컬 minikube 클러스터로, GitOps 구조 자체를 로컬 환경에서 직접 구축·검증했습니다.
- **검증된 안정성** — 532개 테스트가 100% 통과(0 failures)하는 상태를 유지하며, 매 빌드마다 Jacoco로 커버리지를 측정합니다. 자세한 도메인별 수치는 [테스트 전략 & 커버리지](#테스트-전략--커버리지) 참고.

---

## 기술 스택

| 분류 | 기술 |
|---|---|
| 언어 / 프레임워크 | Java 17 · Spring Boot 3.5.7 |
| 빌드 도구 | Gradle |
| 아키텍처 | 헥사고날 아키텍처 (Ports & Adapters) |
| 데이터베이스 | MySQL (운영) · H2 (로컬/테스트) |
| ORM / 쿼리 | Spring Data JPA · QueryDSL 5.1.0 |
| 인증 | Spring Security · JWT(jjwt 0.11.5) — Access Token(Bearer) + Refresh Token(HttpOnly Cookie) |
| OAuth2 | Google · GitHub · Kakao · Naver |
| 이메일 | Spring Mail (Gmail SMTP) |
| API 문서 | Springdoc OpenAPI 2.7.0 (Swagger UI) |
| 관측성(Observability) | Spring Actuator · Micrometer Tracing(Brave) · Prometheus |
| 파일 저장 | 로컬 파일시스템 / 볼륨 마운트 (`file.storage.root-path`) |
| 테스트 | JUnit 5 · AssertJ · Jacoco (커버리지 리포트) |
| CI/CD | GitHub Actions · Docker · Kubernetes (로컬 minikube, GitOps 방식 매니페스트 갱신) |

---

## 아키텍처

의존성은 항상 바깥 계층(`api`, `outbound`)에서 안쪽 계층(`core`)을 향해 흐르며, 도메인은 특정 기술에 의존하지 않습니다.

```
[Controller] → [Facade] → [UseCase 인터페이스]
                                  ↑
                        [Service 구현체 (core/application)]
                                  ↓
                        [Repository 포트 인터페이스 (core/port/out)]
                                  ↑
                        [JPA 어댑터 구현체 (outbound/adapter)]
                                  ↓
                        [JpaRepository / QueryDSL → RDB]
```

### 패키지 구조

```
teamdevhub.devhub
├── api/            REST 계층 — Controller, Request/Response DTO
│   └── {domain}/   auth · user · project · application · board · admin · file · notification · terms · report · skilltrend · home ...
├── core/           도메인 계층 — Entity, Service, Port(UseCase/Repository), Facade
│   └── {domain}/
│       ├── domain/         순수 자바 도메인 모델
│       ├── application/    UseCase 구현체 (Service)
│       └── port/
│           ├── in/         usecase · facade · command
│           └── out/        repository · provider 인터페이스
├── outbound/       어댑터 계층 — JPA, QueryDSL, OAuth 클라이언트, JWT, 파일 저장 구현체
│   └── {domain}/
│       ├── adapter/        Port 구현체, Entity ↔ Domain 매퍼
│       └── persistence/    JpaRepository, QueryDSL Dao
└── shared/         공통 — Config, ErrorCode/SuccessCode, 공통 유틸, 응답 래퍼
```

도메인은 `auth · user · project · application(프로젝트 지원) · board · admin · file · notification · terms · report · skilltrend · common(web) · home` 총 13개로 구성되어 있습니다.

---

## CI/CD 파이프라인

```
Push (dev 브랜치)
    ↓
GitHub Actions: Checkout → ./gradlew clean build
    ↓
Docker Build & Push → Docker Hub
    ↓
devhub-infra 저장소의 Kubernetes deployment.yml 이미지 태그 자동 갱신 → Git Push
    ↓
로컬 minikube 클러스터에 반영 (GitOps)
```

- CI/CD는 `.github/workflows/main.yml`의 GitHub Actions로 운영합니다. `Jenkinsfile-dev`는 초기에 사용했던 파이프라인 정의로 저장소에 남아있지만, 현재 실제로 동작하는 것은 GitHub Actions입니다.
- 매 빌드마다 `Dockerfile` 기반 이미지를 생성해 `{DOCKERHUB_ID}/devhub-b-dev:{BUILD_TAG}` 형태로 태깅하고 Docker Hub에 푸시합니다.
- 애플리케이션 코드 저장소와 배포 매니페스트 저장소(`devhub-infra`)를 분리해 GitOps 방식으로 운영하며, 실제 배포 대상은 클라우드 클러스터가 아닌 **로컬 minikube**입니다. 즉 실서비스 인프라가 아니라 GitOps 워크플로 자체를 로컬 환경에서 구축·연습하는 목적의 파이프라인입니다.
- 운영 환경은 `application-prd.yml`을 통해 DB 접속정보, OAuth 키, JWT 시크릿 등을 전부 환경변수로 주입받습니다.

---

## 로컬 실행 방법

### 사전 조건

- JDK 17
- H2 Database (TCP 서버 모드로 실행)

### 1. H2 TCP 서버 실행

```bash
java -cp h2-*.jar org.h2.tools.Server -tcp -tcpAllowOthers -start
```

기본 접속 URL: `jdbc:h2:tcp://localhost/~/devhub`

### 2. 환경 변수 설정

`application-local.yml` 또는 환경변수로 아래 값을 설정합니다.

```yaml
oauth:
  google:
    client-id: {GOOGLE_CLIENT_ID}
    client-secret: {GOOGLE_CLIENT_SECRET}
    redirect-uri: http://localhost:8080/api/auth/oauth/google/callback
  github:
    client-id: {GITHUB_CLIENT_ID}
    client-secret: {GITHUB_CLIENT_SECRET}
    redirect-uri: http://localhost:8080/api/auth/oauth/github/callback
  kakao:
    client-id: {KAKAO_CLIENT_ID}
    client-secret: {KAKAO_CLIENT_SECRET}
    redirect-uri: http://localhost:8080/api/auth/oauth/kakao/callback
  naver:
    client-id: {NAVER_CLIENT_ID}
    client-secret: {NAVER_CLIENT_SECRET}
    redirect-uri: http://localhost:8080/api/auth/oauth/naver/callback

spring:
  mail:
    username: {GMAIL_ADDRESS}
    password: {GMAIL_APP_PASSWORD}

jwt:
  secret:
    key: {JWT_SECRET_BASE64}

file:
  storage:
    root-path: ./local-files

app:
  frontend:
    base-url: http://localhost:5173   # OAuth 로그인/회원가입 성공 후 리다이렉트할 프론트엔드 주소
  cookie:
    secure: false                     # 운영 환경(HTTPS)에서는 true로 설정
```

### 3. 서버 실행

```bash
./gradlew bootRun
```

서버 포트: `8080` · Context Path: `/api`

---

## API 문서 (Swagger UI)

서버 실행 후 아래 URL에서 전체 API 명세를 확인할 수 있습니다.

```
http://localhost:8080/api/swagger-ui/index.html
```

---

## API 개요

전체 명세는 Swagger UI를 참고하시고, 아래는 도메인별 대표 엔드포인트 요약입니다.

### 인증 (`/auth`)

| 메서드 | URL | 설명 |
|---|---|---|
| POST | `/auth/login` | 이메일 로그인 — Bearer 토큰 + 리프레시 쿠키 발급 |
| POST | `/auth/reissue` | 리프레시 쿠키로 액세스 토큰 재발급 |
| POST | `/auth/logout` | 로그아웃 — 리프레시 토큰 무효화 |
| PUT | `/auth/password` | 비밀번호 변경 |
| GET | `/auth/oauth/{provider}` | OAuth 인증 시작 (google · github · kakao · naver) |
| GET | `/auth/oauth/{provider}/callback` | OAuth 콜백 — 기존 회원 로그인 / 신규 회원 가입 페이지로 분기 |
| POST | `/auth/oauth/signup` | OAuth 신규 회원가입 |
| POST | `/auth/verification/email` | 이메일 인증코드 발송 |
| POST | `/auth/verification/email/confirm` | 이메일 인증코드 확인 |

### 사용자 (`/user`)

| 메서드 | URL | 설명 |
|---|---|---|
| POST | `/user/signup` | 이메일 회원가입 (가입 후 자동 로그인) |
| GET | `/user` / `/user/profile` | 내 정보 / 프로필 상세 조회 |
| PUT | `/user/profile` | 프로필 수정 (이름·소개·포지션·스킬) |
| POST | `/user/profile/image` | 프로필 이미지 변경 |
| DELETE | `/user/profile` | 회원 탈퇴 (소프트 삭제) |
| GET | `/user/profile/boards` | 내가 작성한 게시글 목록 |
| GET | `/user/projects` `/user/projects/likes` `/user/projects/applications` | 내가 만든/좋아요한/지원한 프로젝트 목록 |

### 프로젝트 (`/projects`)

| 메서드 | URL | 설명 |
|---|---|---|
| GET | `/projects` | 프로젝트 목록 조회 (검색 필터 + 페이징) |
| POST | `/projects` | 프로젝트 생성 |
| GET | `/projects/{projectGuid}` | 프로젝트 상세 조회 |
| PUT / DELETE | `/projects/{projectGuid}` | 프로젝트 수정 / 삭제 |
| POST | `/projects/{projectGuid}/likes` | 좋아요 토글 |
| POST | `/projects/{projectGuid}/applications` | 프로젝트 지원 (지원서 양식 답변 포함) |
| GET | `/projects/{projectGuid}/applications` | 지원자 목록 조회 |
| GET | `/projects/applications/{applicationGuid}` | 지원 상세 조회 (답변 포함) |
| PUT | `/projects/applications/{applicationGuid}/approve` | 지원 승인/거절 |
| PUT | `/projects/applications/{applicationGuid}/cancel` | 본인 지원 취소 (미처리 건만 가능) |
| POST | `/projects/{projectGuid}/members/{userGuid}` | 프로젝트 멤버 평가 (매너 점수) |
| GET | `/applicationForms` | 프로젝트별 지원서 양식 조회 |

### 게시판 (`/boards`)

| 메서드 | URL | 설명 |
|---|---|---|
| GET | `/boards` | 게시글 목록 조회 (검색 + 페이징) |
| POST | `/boards` | 게시글 작성 |
| GET | `/boards/{boardGuid}` | 게시글 상세 (조회수 쿠키 처리) |
| PUT | `/boards/{boardGuid}` | 게시글 수정 |
| POST | `/boards/{boardGuid}/likes` | 게시글 좋아요 |
| POST | `/boards/delete` | 게시글 복수 삭제 |
| POST / PUT / DELETE | `/boards/{boardGuid}/comments/**` | 댓글 작성/수정/삭제 |

### 홈 (`/home`)

| 메서드 | URL | 설명 |
|---|---|---|
| GET | `/home` | 홈 화면에 필요한 배너·프로젝트·게시글 데이터를 한 번에 조회 |

### 스킬 트렌드 (`/skill-trends`)

| 메서드 | URL | 설명 |
|---|---|---|
| GET | `/skill-trends` | 카드 통계·수요 스킬·인기 포지션·월별 타임라인 등 스킬 트렌드 통계 조회 |

### 신고 (`/reports`)

| 메서드 | URL | 설명 |
|---|---|---|
| POST | `/reports` | 게시글 또는 댓글 신고 등록 |

### 기타 도메인

| 도메인 | 대표 엔드포인트 | 설명 |
|---|---|---|
| 공통 코드 (공개) | `GET /common/code`, `PUT /common/save`, `PUT /common/saveAll` | 그룹별 공통 코드 조회 및 저장 |
| 알림 | `GET /notification/list`, `PUT /notification/checked/{guid}` | 알림 조회 / 읽음 처리 |
| 파일 | `POST /files`, `GET /files/{guid}`, `GET /files/{guid}/download` | 파일 업로드 / 조회 / 다운로드 |
| 약관 | `GET /terms`, `POST /terms`(ADMIN) | 약관 조회 / 등록 |

### 관리자 (`/admin/**`, ADMIN 권한 필요)

| 도메인 | 대표 엔드포인트 | 설명 |
|---|---|---|
| 회원 관리 | `GET /admin/users`, `GET/PUT /admin/users/{userGuid}` | 회원 목록/상세 조회, 닉네임·소개 수정 |
| 회원 제재 | `POST /admin/users/{userGuid}/ban`, `POST /admin/users/{userGuid}/unban`, `POST /admin/users/{userGuid}/password` | 정지 / 정지 해제 / 비밀번호 강제 초기화 |
| 회원 활동 조회 | `GET /admin/users/{userGuid}/projects`, `GET /admin/users/{userGuid}/projects/applicant` | 특정 회원이 등록/지원한 프로젝트 조회 |
| 신고 처리 | `GET /admin/users/reports`, `GET /admin/users/{userGuid}/reports`, `GET /admin/users/{userGuid}/reports/reported`, `PUT /admin/users/reports/{reportGuid}/process` | 전체/수신/제출 신고 내역 조회 및 처리 완료 처리 |
| 게시글 관리 | `GET /admin/boards`, `POST /admin/boards/delete` | 게시글 목록 조회, 개별/일괄 삭제 |
| 배너 관리 | `GET /admin/banner/list`, `PUT /admin/banner`, `PUT /admin/banner/{bannerGuid}`, `DELETE /admin/banner/{bannerGuid}` | 배너 목록 조회 / 등록 / 수정 / 삭제 |
| 공통 코드 관리 | `GET /admin/code/list`, `PUT /admin/code` | 공통 코드 목록 조회, 등록/수정 |
| 지원서 양식 관리 | `GET /admin/form/list`, `PUT /admin/form`, `DELETE /admin/form/{applicationFormGuid}` | 프로젝트 지원서 양식 조회 / 등록·수정 / 삭제 |
| 프로젝트 관리 | `GET /admin/projects`, `GET/PUT/DELETE /admin/projects/{projectGuid}`, `GET /admin/projects/{projectGuid}/applicants`, `PUT /admin/projects/{projectGuid}/applicants/{applicationGuid}/status` | 프로젝트 목록/상세/수정/삭제, 지원자 목록 조회 및 승인/거절 |

---

## 인증 방식

### JWT Stateless

```
로그인 성공 시
  Response Header: Authorization: Bearer {accessToken}
  Response Cookie:  refreshToken={refreshToken}; HttpOnly; Path=/

인증이 필요한 요청
  Request Header: Authorization: Bearer {accessToken}

토큰 재발급
  POST /auth/reissue  →  refreshToken 쿠키 자동 전송
```

- 세션을 사용하지 않는 `STATELESS` 정책 (`SessionCreationPolicy.STATELESS`)
- 커스텀 JWT 인가 필터를 `UsernamePasswordAuthenticationFilter` 이전에 등록
- 컨트롤러에서는 `@LoginUser` 어노테이션으로 인증된 사용자(`UserCredential`)를 바로 주입받아 사용

### 인가(Authorization) 정책 (`WebSecurityConfig`)

| 대상 | 정책 |
|---|---|
| `/auth/**`, `/user/signup`, Swagger, Actuator, 정적 리소스 | 인증 없이 허용 (`permitAll`) |
| `GET /common/**`, `/files/**`, `/projects`, `/projects/**`, `/boards`, `/boards/**`, `/home`, `/skill-trends`, `/terms/**` | 조회(GET)는 비로그인 사용자도 허용 |
| `POST /terms/**` | ADMIN 권한 필요 |
| `/admin/**` | ADMIN 권한 필요 (`hasRole("ADMIN")`) |
| 그 외 모든 요청 | 인증 필요 (`anyRequest().authenticated()`) |

### CORS 허용 출처

- `http://localhost:3000`, `http://localhost:5173` (로컬 개발)
- `https://devhub-24.com`, `https://www.devhub-24.com` (운영)

---

## 테스트 전략 & 커버리지

```
src/test/java/teamdevhub/devhub/
├── small/      단위 테스트 — Spring Context 없이 순수 Java로 실행
├── medium/     통합 테스트 — @SpringBootTest + H2
├── fake/       Fake 구현체 — Port 인터페이스를 직접 구현한 테스트 더블
└── constant/   테스트 상수 (TestConstant)
```

- **단위 테스트(small)**: Mockito를 사용하지 않고, Port 인터페이스를 구현한 `Fake` 객체(`FakeUserRepository` 등)를 직접 조립해 Service/Facade/Domain 로직을 검증합니다.
- **통합 테스트(medium)**: `@SpringBootTest` + 실제 JPA 리포지토리로 어댑터·컨트롤러의 동작을 검증합니다.
- 모든 테스트는 `given / when / then` 구조와 AssertJ(`assertThat`, `assertThatThrownBy`)로 통일된 스타일을 따릅니다.

```bash
# 전체 빌드 + 테스트
./gradlew build

# 단위 테스트만
./gradlew test --tests "teamdevhub.devhub.small.*"

# 통합 테스트만
./gradlew test --tests "teamdevhub.devhub.medium.*"

# 커버리지 리포트 (Jacoco)
./gradlew test jacocoTestReport
# build/reports/jacoco/test/html/index.html
```

### 최근 측정 결과

`./gradlew test jacocoTestReport` 기준.

| 항목 | 결과 |
|---|---|
| 총 테스트 | 532건 |
| 실패 / 에러 | 0건 (100% 통과) |
| 실행 시간 | 약 2분 |
| 라인 커버리지 | 41.1% (2,288 / 5,573) |
| 브랜치 커버리지 | 40.4% (351 / 868) |
| 메서드 커버리지 | 45.2% (619 / 1,370) |
| 클래스 커버리지 | 50.2% (221 / 440) |

### 도메인/기능별 라인 커버리지

핵심 인증·회원 도메인은 90% 안팎까지 두텁게 검증되어 있는 반면, 프로젝트·게시판·관리자 도메인은 상대적으로 테스트가 얇습니다. 신규 기능 작업 시 우선적으로 보강이 필요한 영역을 파악하는 용도로 참고하세요.

| 영역 | 라인 커버리지 | 브랜치 커버리지 | 비고                                                                                                                                            |
|---|---:|---:|-----------------------------------------------------------------------------------------------------------------------------------------------|
| 약관 (`terms`) | 91.8% | 100.0% |                                                                                                                                               |
| 회원/프로필 (`user`) | 85.1% | 97.5% | 가장 두텁게 검증된 영역                                                                                                                                 |
| 공통 인프라 (`shared`/`common`/`web`/`security`) | 81.9% | 81.3% | `ErrorCode`/`SuccessCode` 등 열거형 상수 관련 소스 외 예외 변환·공통 응답 래퍼·인가 필터 같은 실제 로직은 이미 두텁게 검증됨                                                          |
| 파일 (`file`) | 71.6% | 56.2% |                                                                                                                                               |
| 인증/OAuth (`auth`) | 65.2% | 38.2% | Google·GitHub·Kakao·Naver와 직접 통신하는 외부 연동 어댑터(outbound.auth.infrastructure.oauth.*, 207라인)를 제외하면 88.2%. 이메일 인증·JWT 재발급 등 순수 서비스 로직은 이미 두텁게 검증됨 |
| 홈 (`home`) | 48.5% | 39.1% |                                                                                                                                               |
| 스킬 트렌드 (`skilltrend`) | 41.6% | 0.0% |                                                                                                                                               |
| 알림 (`notification`) | 37.8% | 100.0% |                                                                                                                                               |
| 관리자 - 지원서 양식 (`admin.form`) | 11.7% | 5.3% |                                                                                                                                               |
| 신고 (`report`) | 14.6% | 0.0% |                                                                                                                                               |
| 게시판 (`board`) | 2.4% | 0.0% |                                                                                                                                               |
| 프로젝트 (`project`) | 1.3% | 4.8% |                                                                                                                                               |
| 관리자 - 배너/게시글/공통코드 (`admin.banner`·`admin.board`·`admin.code`) | 0.0% | 0.0% |                                                                                                                                               |
| 프로젝트 지원(신청) (`application`) | 0.0% | 0.0% |                                                                                                                                               |

> 수치는 `build/reports/jacoco/test/jacocoTestReport.xml`을 기준으로 도메인 패키지 단위로 합산한 값이며, 빌드할 때마다 갱신됩니다.

---

## 주요 기능

- **인증** — 이메일/소셜(Google·GitHub·Kakao·Naver) 회원가입 및 로그인, OAuth state 기반 CSRF 방지, 이메일 인증, 비밀번호 변경, JWT 재발급
- **프로필** — 사용자 정보/프로필 조회·수정, 프로필 이미지 변경, 회원 탈퇴(소프트 삭제)
- **프로젝트 매칭** — 프로젝트 CRUD, 좋아요, 지원서 양식 기반 지원·취소·승인/거절 프로세스, 완료 프로젝트 팀원 매너 리뷰
- **커뮤니티** — 게시판 CRUD, 조회수 집계, 좋아요, 댓글 CRUD
- **모집폼** — 프로젝트별 맞춤 지원서 양식 조회 (관리자 등록/수정/삭제)
- **신고** — 게시글/댓글 신고 등록, 관리자 신고 처리(전체·수신·제출 내역 조회 및 처리 완료)
- **홈 / 스킬 트렌드** — 홈 화면 통합 데이터(배너·프로젝트·게시글) 조회, 수요 스킬·인기 포지션·월별 타임라인 등 스킬 트렌드 통계 제공
- **파일** — 파일 업로드/다운로드/인라인 보기, 메타데이터 조회
- **관리자** — 회원 관리(상세 조회·정보 수정·정지/해제·비밀번호 강제 초기화), 회원별 활동/신고 내역 조회, 게시글·배너·공통코드·지원서 양식·프로젝트 전 영역 관리, 신고 처리

---
