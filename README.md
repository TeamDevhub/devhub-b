# DevHub Backend

개발자 프로젝트 매칭 및 팀 빌딩 플랫폼의 백엔드 서비스.

---

## 기술 스택

| 분류 | 기술 |
|---|---|
| 언어 / 프레임워크 | Java 17 / Spring Boot 3.5.7 |
| 빌드 | Gradle |
| 아키텍처 | 헥사고날 아키텍처 (Ports & Adapters) |
| 데이터베이스 | H2 (TCP 모드, 로컬 실행 필요) |
| ORM | Spring Data JPA / QueryDSL 5.1.0 |
| 인증 | JWT (jjwt 0.11.5) — 액세스 토큰(Bearer) + 리프레시 토큰(HTTP-only 쿠키) |
| OAuth2 | Google · GitHub · Kakao · Naver |
| 이메일 | Spring Mail (Gmail SMTP) |
| 문서 | Springdoc OpenAPI 2.7.0 (Swagger UI) |
| 파일 저장 | 로컬 파일시스템 (`./local-files`) |

---

## 아키텍처

![img.png](img.png)

헥사고날 아키텍처(Ports & Adapters)를 채택하여 도메인 로직을 외부 기술 스택으로부터 격리한다.

```
[Controller] → [Facade] → [UseCase 인터페이스]
                                  ↑
                        [Service 구현체]
                                  ↓
                        [Repository 포트 인터페이스]
                                  ↑
                        [JPA 어댑터 구현체]
                                  ↓
                        [JpaRepository / H2]
```

---

## 로컬 실행 방법

### 사전 조건

- JDK 17
- H2 Database (TCP 서버 모드로 실행 필요)

### 1. H2 TCP 서버 실행

H2 콘솔 또는 아래 명령으로 TCP 서버를 먼저 실행한다:

```bash
java -cp h2-*.jar org.h2.tools.Server -tcp -tcpAllowOthers -start
```

기본 접속 URL: `jdbc:h2:tcp://localhost/~/devhub`

### 2. 환경 변수 설정

`application.yml`에서 아래 값들을 설정한다 (또는 환경변수로 주입):

```yaml
oauth:
  google:
    client-id: {GOOGLE_CLIENT_ID}
    client-secret: {GOOGLE_CLIENT_SECRET}
    redirect-uri: http://localhost:8080/auth/oauth/google/callback
  github:
    client-id: {GITHUB_CLIENT_ID}
    client-secret: {GITHUB_CLIENT_SECRET}
    redirect-uri: http://localhost:8080/auth/oauth/github/callback
  kakao:
    client-id: {KAKAO_CLIENT_ID}
    client-secret: {KAKAO_CLIENT_SECRET}
    redirect-uri: http://localhost:8080/auth/oauth/kakao/callback
  naver:
    client-id: {NAVER_CLIENT_ID}
    client-secret: {NAVER_CLIENT_SECRET}
    redirect-uri: http://localhost:8080/auth/oauth/naver/callback

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
```

### 3. 서버 실행

```bash
./gradlew bootRun
```

서버 기본 포트: `8080`

---

## API 문서 (Swagger UI)

서버 실행 후 아래 URL에서 API 명세를 확인할 수 있다:

```
http://localhost:8080/swagger-ui/index.html
```

---

## API 목록

### 인증 (`/auth`)

| 메서드 | URL | 인증 필요 | 설명 |
|---|---|---|---|
| POST | `/auth/login` | 불필요 | 이메일 로그인 — Bearer 토큰 + 리프레시 쿠키 발급 |
| POST | `/auth/reissue` | 쿠키 | 액세스 토큰 재발급 (refreshToken 쿠키 사용) |
| POST | `/auth/logout` | 필요 | 로그아웃 — 리프레시 토큰 무효화 |
| PUT | `/auth/password` | 필요 | 비밀번호 변경 |
| GET | `/auth/oauth/{provider}` | 불필요 | OAuth 인증 시작 — provider: `google`, `github`, `kakao`, `naver` |
| GET | `/auth/oauth/{provider}/callback` | 불필요 | OAuth 콜백 처리 — 기존 회원이면 홈으로 리다이렉트, 신규면 회원가입 페이지로 |
| POST | `/auth/oauth/signup` | 불필요 | OAuth 신규 회원가입 (tempToken 사용) |
| POST | `/auth/verification/email` | 불필요 | 이메일 인증코드 발송 |
| POST | `/auth/verification/email/confirm` | 불필요 | 이메일 인증코드 확인 |

### 사용자 (`/user`)

| 메서드 | URL | 인증 필요 | 설명 |
|---|---|---|---|
| POST | `/user/signup` | 불필요 | 이메일 회원가입 — 가입 후 자동 로그인 |
| GET | `/user` | 필요 | 내 기본 정보 조회 |
| GET | `/user/profile` | 필요 | 내 프로필 상세 조회 |
| POST | `/user/profile/image` | 필요 | 프로필 이미지 변경 |
| PUT | `/user/profile` | 필요 | 프로필 수정 (이름, 소개, 포지션, 스킬) |
| DELETE | `/user/profile` | 필요 | 회원 탈퇴 (소프트 삭제) |
| GET | `/user/profile/boards` | 필요 | 내가 작성한 게시글 목록 |
| GET | `/user/projects` | 필요 | 내가 만든 프로젝트 목록 |
| GET | `/user/projects/likes` | 필요 | 좋아요한 프로젝트 목록 |
| GET | `/user/projects/applications` | 필요 | 지원한 프로젝트 목록 |

### 프로젝트 (`/projects`)

| 메서드 | URL | 인증 필요 | 설명 |
|---|---|---|---|
| GET | `/projects` | 선택 | 프로젝트 목록 조회 (검색 필터 + 페이징) |
| POST | `/projects` | 필요 | 프로젝트 생성 |
| GET | `/projects/{projectGuid}` | 선택 | 프로젝트 상세 조회 |
| GET | `/projects/{projectGuid}/form` | 불필요 | 모집폼 포함 상세 조회 |
| PUT | `/projects/{projectGuid}` | 필요 | 프로젝트 수정 |
| DELETE | `/projects/{projectGuid}` | 불필요 | 프로젝트 삭제 |
| POST | `/projects/{projectGuid}/likes` | 필요 | 프로젝트 좋아요 토글 |
| POST | `/projects/{projectGuid}/applications` | 필요 | 프로젝트 지원 |
| GET | `/projects/{projectGuid}/applications` | 불필요 | 프로젝트 지원자 목록 (페이징) |
| GET | `/projects/applications/{applicationGuid}` | 불필요 | 지원서 상세 |
| PUT | `/projects/applications/{applicationGuid}/approve` | 필요 | 지원서 승인/거절 (`?approved=true\|false`) |
| POST | `/projects/{projectGuid}/members/{userGuid}` | 필요 | 프로젝트 멤버 평가 |

### 게시판 (`/boards`)

| 메서드 | URL | 인증 필요 | 설명 |
|---|---|---|---|
| GET | `/boards` | 불필요 | 게시글 목록 조회 (검색 + 페이징) |
| POST | `/boards` | 필요 | 게시글 작성 |
| GET | `/boards/{boardGuid}` | 선택 | 게시글 상세 (조회수 쿠키 처리) |
| PUT | `/boards/{boardGuid}` | 필요 | 게시글 수정 |
| POST | `/boards/{boardGuid}/likes` | 필요 | 게시글 좋아요 |
| POST | `/boards/delete` | 불필요 | 게시글 삭제 (배열로 복수 삭제 가능) |
| POST | `/boards/{boardGuid}/comments` | 필요 | 댓글 작성 |
| PUT | `/boards/{boardGuid}/comments/{commentGuid}` | 필요 | 댓글 수정 |
| DELETE | `/boards/{boardGuid}/comments/{commentGuid}` | 불필요 | 댓글 삭제 |

### 모집폼 (`/applicationForms`)

| 메서드 | URL | 인증 필요 | 설명 |
|---|---|---|---|
| GET | `/applicationForms` | 불필요 | 모집폼 목록 조회 (검색 필터) |

### 알림 (`/notification`)

| 메서드 | URL | 인증 필요 | 설명 |
|---|---|---|---|
| GET | `/notification/list` | 필요 | 알림 목록 조회 |
| PUT | `/notification/checked/{notificationGuid}` | 필요 | 알림 읽음 처리 |

### 파일 (`/files`)

| 메서드 | URL | 인증 필요 | 설명 |
|---|---|---|---|
| POST | `/files` | 불필요 | 파일 업로드 (multipart) |
| GET | `/files/{fileGuid}/meta` | 불필요 | 파일 메타데이터 조회 |
| GET | `/files/{fileGuid}` | 불필요 | 파일 인라인 보기 |
| GET | `/files/{fileGuid}/download` | 불필요 | 파일 다운로드 |
| DELETE | `/files/{fileGuid}` | 불필요 | 파일 삭제 |

### 약관 (`/terms`)

| 메서드 | URL | 인증 필요 | 설명 |
|---|---|---|---|
| GET | `/terms` | 불필요 | 약관 목록 조회 |
| POST | `/terms` | 불필요 | 약관 등록 |

### 관리자 - 사용자 관리 (`/admin/users`)

> **주의**: `/admin/**` 경로가 현재 `permitAll()` 상태입니다. 운영 전 ADMIN Role 검증 설정이 필요합니다.

| 메서드 | URL | 인증 필요 | 설명 |
|---|---|---|---|
| GET | `/admin/users` | 🚧 | 사용자 목록 조회 (검색 필터 + 페이징) |
| GET | `/admin/users/{userGuid}` | 🚧 | 사용자 상세 조회 (역할·정지 상태 포함) |
| PUT | `/admin/users/{userGuid}` | 🚧 | 사용자 정보 수정 (닉네임·소개) |
| POST | `/admin/users/{userGuid}/password` | 🚧 | 비밀번호 초기화 (이메일 계정 전용) |
| POST | `/admin/users/{userGuid}/ban` | 🚧 | 사용자 정지 |
| DELETE | `/admin/users/{userGuid}/ban` | 🚧 | 사용자 정지 해제 |
| GET | `/admin/users/{userGuid}/projects` | 🚧 | 사용자가 등록한 프로젝트 목록 |
| GET | `/admin/users/{userGuid}/projects/applicant` | 🚧 | 사용자가 지원한 프로젝트 목록 |
| GET | `/admin/users/{userGuid}/reports` | 🚧 | 사용자가 받은 신고 목록 |
| GET | `/admin/users/{userGuid}/reports/reported` | 🚧 | 사용자가 제출한 신고 목록 |
| GET | `/admin/users/reports` | 🚧 | 전체 신고 목록 조회 (페이징) |

### 관리자 - 게시판 관리 (`/admin/boards`)

| 메서드 | URL | 인증 필요 | 설명 |
|---|---|---|---|
| GET | `/admin/boards` | 🚧 | 게시글 목록 조회 (검색 필터 + 페이징) |

### 관리자 - 배너 관리 (`/admin/banner`)

| 메서드 | URL | 인증 필요 | 설명 |
|---|---|---|---|
| GET | `/admin/banner/list` | 🚧 | 배너 목록 조회 (검색 필터 + 페이징) |
| PUT | `/admin/banner` | 🚧 | 배너 등록 |
| PUT | `/admin/banner/{bannerGuid}` | 🚧 | 배너 수정 |
| DELETE | `/admin/banner/{bannerGuid}` | 🚧 | 배너 삭제 |

### 관리자 - 공통코드 관리 (`/admin/code`)

| 메서드 | URL | 인증 필요 | 설명 |
|---|---|---|---|
| GET | `/admin/code/list` | 🚧 | 공통코드 목록 조회 |
| PUT | `/admin/code` | 🚧 | 공통코드 등록/수정 |

---

## 인증 방식

### JWT Stateless

```
로그인 성공 시:
  - Response Header: Authorization: Bearer {accessToken}
  - Response Cookie: refreshToken={refreshToken}; HttpOnly; Path=/

인증이 필요한 요청:
  - Request Header: Authorization: Bearer {accessToken}

토큰 재발급:
  - POST /auth/reissue — refreshToken 쿠키 자동 전송
```

### CORS 허용 출처

- `http://localhost:3000`
- `http://localhost:5173`

---

## 테스트 실행

```bash
# 전체 빌드 + 테스트
./gradlew build

# 단위 테스트만
./gradlew test --tests "teamdevhub.devhub.small.*"

# 통합 테스트만
./gradlew test --tests "teamdevhub.devhub.medium.*"

# 커버리지 리포트 (테스트 후 자동 생성)
# build/reports/jacoco/test/html/index.html
```

### 테스트 구조

| 패키지 | 종류 | 설명 |
|---|---|---|
| `small/` | 단위 테스트 | Spring Context 없음, Fake 구현체 사용 |
| `medium/` | 통합 테스트 | `@SpringBootTest` + H2 |
| `fake/` | 테스트 더블 | Fake 포트 구현체 |

---

## 현재 개발 상태

### 완성된 기능

- 이메일 회원가입 / 로그인 / 로그아웃
- OAuth 로그인 (Google, GitHub, Kakao, Naver)
- 이메일 인증코드 발송 및 확인
- 비밀번호 변경
- 프로필 조회 / 수정 / 이미지 변경
- 회원 탈퇴 (소프트 삭제)
- 프로젝트 CRUD / 좋아요
- 프로젝트 지원 / 승인
- 프로젝트 멤버 평가
- 게시판 CRUD / 조회수 / 좋아요
- 게시판 댓글 CRUD
- 모집폼 목록 조회
- 파일 업로드 / 다운로드
- 알림 조회 / 읽음 처리
- 약관 등록 / 조회
- 관리자 사용자 관리 (목록/상세 조회, 정보 수정, 비밀번호 초기화, 정지/해제, 프로젝트·신고 조회)
- 관리자 게시판 관리 (목록 조회)
- 관리자 배너 관리 (CRUD)
- 관리자 공통코드 관리 (등록/수정/조회)

### 미완성 기능

- `/admin/**` 경로 ADMIN Role 인가 설정 (`WebSecurityConfig` — 현재 `permitAll()` 상태)
- `User.changePassword()` 도메인 메서드 (주석 처리됨)
- `UserProfileUseCase.updatePassword()` 인터페이스 메서드 (주석 처리됨)
