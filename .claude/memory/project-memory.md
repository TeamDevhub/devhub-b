# 프로젝트 현황 메모리

## 프로젝트 기본 정보

- **이름**: DevHub Backend
- **목적**: 개발자 프로젝트 매칭 및 팀 빌딩 플랫폼
- **스택**: Java 17 / Spring Boot 3.5.7 / Gradle / H2 (개발)
- **아키텍처**: 헥사고날 (Ports & Adapters)
- **브랜치 전략**: `main` ← `dev` ← `feature/*`
- **현재 활성 브랜치**: `feature/user`

---

## 도메인 목록

| 도메인 | 상태 | 비고 |
|---|---|---|
| `auth` | 활성 | 이메일 + OAuth(4종) |
| `user` | 활성 | 회원가입, 프로필, 탈퇴 |
| `project` | 활성 (부분) | 서비스 테스트 미완성 |
| `board` | 존재 | 상세 현황 미파악 |
| `file` | 존재 | 상세 현황 미파악 |
| `application` | 존재 | 상세 현황 미파악 |
| `terms` | 활성 | 회원가입 시 약관 동의 |
| `admin` | 활성 (부분) | 권한 검증 미완성 |
| `notification` | 존재 | 상세 현황 미파악 |

---

## 인증 구조

```
이메일 로그인:  POST /auth/login → AuthFacade → UserCredentialService.authenticate()
OAuth 로그인:   GET /oauth/{provider}/callback → OauthAuthFacade
회원가입:       POST /user/signup → UserSignupFacade
토큰 재발급:    POST /auth/reissue → UserCredentialService.getUserForReissue()
```

---

## 미완성 기능 (코드 내 주석처리 상태)

| 기능 | 위치 | 상태 |
|---|---|---|
| 비밀번호 변경 | `User.changePassword()` | 도메인 메서드 주석처리 |
| 비밀번호 변경 UseCase | `UserProfileUseCase.updatePassword()` | 인터페이스 주석처리 |
| Admin 권한 검증 | `WebSecurityConfig:106` | `/admin/**` 가 `permitAll()` |
| ProjectServiceTest | `ProjectServiceTest.java` | 전체 주석 상태 |

---

## 테스트 현황

| 분류 | 위치 | 상태 |
|---|---|---|
| 단위 테스트 | `src/test/.../small/` | auth/user 도메인 완성 |
| 통합 테스트 | `src/test/.../medium/` | auth 어댑터 완성 |
| E2E 테스트 | `src/test/.../large/` | 미작성 |
| Fake 구현체 | `src/test/.../fake/` | auth/user 도메인 완성 |

---

## 테스트 상수 위치

```
src/test/java/teamdevhub/devhub/constant/UserTestConstant.java
```

주요 상수: `TEST_USER_GUID_1`, `TEST_EMAIL_1`, `TEMP_TOKEN`

---

## 마지막 업데이트

2024년 — feature/user 브랜치 기준. auth/user 테스트 코드 보완 완료.
