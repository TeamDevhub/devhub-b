# MCP: Terminal

## 역할

Gradle 빌드, 테스트 실행, Git 명령 등 터미널 작업을 수행한다.

## 설정

Claude Code CLI 환경에서는 `Bash` 도구를 직접 사용한다.
별도 MCP 서버 설정 없이 다음 명령을 실행할 수 있다.

## 이 프로젝트의 표준 명령어

### Gradle

```bash
# 컴파일 (빠른 검증)
./gradlew compileJava

# 테스트 컴파일
./gradlew compileTestJava

# 전체 단위 테스트
./gradlew test --tests "teamdevhub.devhub.small.*"

# 전체 통합 테스트
./gradlew test --tests "teamdevhub.devhub.medium.*"

# 특정 테스트 클래스
./gradlew test --tests "teamdevhub.devhub.small.core.auth.application.service.AuthenticatedUserServiceTest"

# 전체 빌드
./gradlew build

# 테스트 제외 빌드
./gradlew build -x test

# 클린 빌드 (QueryDSL Q클래스 재생성 포함)
./gradlew clean build
```

### Git

```bash
# 현재 상태
git status
git diff

# 브랜치 전환
git checkout feature/{기능명}

# 커밋
git add {파일}
git commit -m "[feat] 변경 내용"

# PR 준비
git push origin feature/{기능명}
```

### 테스트 리포트 위치

```
build/reports/tests/test/index.html        ← 테스트 결과 HTML
build/reports/jacoco/test/html/index.html  ← 커버리지 리포트
```

## 실행 환경 참고

- OS: Windows 11
- Shell: Bash (Git Bash / WSL)
- JDK: 17
- Gradle Wrapper: `./gradlew`
