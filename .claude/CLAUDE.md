# DevHub 백엔드 — Claude Code 가이드

## 프로젝트 개요

DevHub는 개발자 프로젝트 매칭 및 팀 빌딩 플랫폼의 백엔드 서비스다.

- **기술 스택**: Java 17, Spring Boot 3.5.7, Gradle
- **아키텍처**: 헥사고날 아키텍처 (Ports & Adapters)
- **인증**: JWT Stateless (액세스 토큰 Bearer + 리프레시 토큰 HTTP-only 쿠키)
- **OAuth**: Google, GitHub, Kakao, Naver
- **데이터베이스**: H2 (개발/테스트 환경)
- **주요 의존성**: Spring Security, Spring Data JPA, QueryDSL 5.1.0, Lombok

## 규칙 파일 목록

코드를 작성하거나 수정하기 전에 관련 규칙 파일을 반드시 확인한다.

| 파일 | 적용 시점 |
|---|---|
| [architecture.md](rules/architecture.md) | 새 클래스 추가, 레이어 간 의존성 결정 시 |
| [naming.md](rules/naming.md) | 클래스·메서드·패키지 이름 결정 시 |
| [code-conventions.md](rules/code-conventions.md) | 코드 작성 전반 |
| [error-handling.md](rules/error-handling.md) | 예외 처리 및 API 응답 작성 시 |
| [testing.md](rules/testing.md) | 테스트 코드 작성 시 |
| [persistence.md](rules/persistence.md) | JPA 엔티티, 어댑터, 레포지토리 작성 시 |
| [api-design.md](rules/api-design.md) | 컨트롤러, DTO, Security 설정 시 |
| [refactoring.md](rules/refactoring.md) | 기존 코드 수정 및 리팩토링 시 |
| [feature-development.md](rules/feature-development.md) | 신규 기능 추가 시 |
| [things-to-avoid.md](rules/things-to-avoid.md) | 항상 |

## 패키지 최상위 구조

```
teamdevhub.devhub
├── api/          # REST 계층: Controller + Request/Response DTO
├── core/         # 비즈니스 핵심: Domain + Service + Port(인터페이스)
├── outbound/     # 어댑터: JPA + Security + OAuth + Infrastructure
└── shared/       # 공통: Config, ErrorCode, SuccessCode, Util
```

## 현재 미완성 영역 (주의)

- `User.changePassword()` — 주석처리됨, 비밀번호 변경 기능 미완성
- `WebSecurityConfig` 106번 라인 — `/admin/**`가 `permitAll()` 상태, ADMIN 권한 검증 미적용
- `UserCredentialServiceTest`, `OauthResolveServiceTest` — 최근에 복원됨, 구조 변경 이력 있음
