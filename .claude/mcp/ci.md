# MCP: CI 파이프라인

## 역할

GitHub Actions 기반 CI 파이프라인의 설정과 결과를 확인한다.

## 현재 CI 설정

CI 파이프라인이 있다면 다음 경로에서 확인한다:

```bash
Glob .github/workflows/*.yml
```

## 권장 CI 파이프라인 구성

이 프로젝트에 CI가 없다면 다음 구성을 권장한다:

```yaml
# .github/workflows/ci.yml
name: CI

on:
  push:
    branches: [ dev, feature/** ]
  pull_request:
    branches: [ dev ]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v3

      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Cache Gradle
        uses: actions/cache@v3
        with:
          path: ~/.gradle/caches
          key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle*') }}

      - name: Grant execute permission
        run: chmod +x gradlew

      - name: Compile
        run: ./gradlew compileJava compileTestJava

      - name: Run unit tests
        run: ./gradlew test --tests "teamdevhub.devhub.small.*"

      - name: Run integration tests
        run: ./gradlew test --tests "teamdevhub.devhub.medium.*"
```

## GitHub MCP로 CI 결과 확인

```
list_workflow_runs — 최근 실행 목록
get_workflow_run — 특정 실행 결과
list_jobs_for_workflow_run — 잡별 결과
```

## 환경 변수 (GitHub Secrets)

CI 실행 시 필요한 Secret:

| Secret 이름 | 설명 |
|---|---|
| `JWT_SECRET` | JWT 서명 키 |
| `OAUTH_GOOGLE_CLIENT_ID` | Google OAuth ID |
| `OAUTH_GOOGLE_CLIENT_SECRET` | Google OAuth Secret |
| `OAUTH_GITHUB_CLIENT_ID` | GitHub OAuth ID |
| `OAUTH_GITHUB_CLIENT_SECRET` | GitHub OAuth Secret |
| `OAUTH_KAKAO_CLIENT_ID` | Kakao OAuth ID |
| `OAUTH_NAVER_CLIENT_ID` | Naver OAuth ID |
| `OAUTH_NAVER_CLIENT_SECRET` | Naver OAuth Secret |
