---
name: build
description: MVI Template의 Gradle 빌드, 테스트, lint 실패를 분석하고 수정할 때 사용하는 스킬.
---

# build

## 목적

Gradle 실패를 빠르게 재현하고 원인을 코드, 리소스, 의존성, 환경 문제로 분류합니다.

## 사용 시점

- `compileDebugKotlin`, `assembleDebug`, `testDebugUnitTest`, `lintDebug`, `qualityGateFast`가 실패할 때
- Java/Android SDK 환경 문제와 코드 컴파일 문제를 구분해야 할 때

## 핵심 원칙

- 먼저 가장 작은 실패 명령을 실행합니다.
- 환경 문제와 코드 문제를 분리해서 수정합니다.
- Android 과제 작업의 기본 성공 기준은 `./gradlew qualityGateFast`입니다.

## 절차

1. 빠른 컴파일 확인은 `./gradlew :app:compileDebugKotlin`을 실행합니다.
2. 전체 빠른 품질 확인은 `./gradlew qualityGateFast`를 실행합니다.
3. APK 생성 확인이 필요하면 `./gradlew :app:assembleDebug`를 실행합니다.
4. 실패 로그에서 첫 번째 실제 원인 파일과 task를 찾습니다.
5. 수정 후 같은 명령을 다시 실행합니다.

## 출력

- 실행한 명령
- 실패 task와 원인
- 수정한 파일
- 재실행 결과

## 점검

- 첫 번째 실패 원인을 고쳤는가
- 같은 명령을 재실행했는가
- 환경 변수나 로컬 파일에 의존하는 문제를 문서화했는가
