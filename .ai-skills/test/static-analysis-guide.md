---
name: static-analysis-guide
description: qualityGateFast, Android lint, 하네스 정합성 검증을 추가하거나 수정할 때 사용하는 스킬.
---

# static-analysis-guide

## 목적

반복 실행 가능한 빠른 품질 최저선을 유지합니다.

## 사용 시점

- `qualityGateFast` task를 수정할 때
- `verifyHarnessConsistency` script를 수정할 때
- Detekt custom rule과 [detekt.yml](../../detekt.yml)을 수정할 때
- Android lint나 test gate 범위를 조정할 때

## 핵심 원칙

### 게이트 경계

- 빠른 기본 게이트는 compile, unit test, lint, harness consistency, architecture script입니다.
- 가벼운 구조 검증은 [verify-architecture-rules.sh](../../scripts/verify-architecture-rules.sh)를 함께 사용합니다.
- 게이트가 느려지면 사용 빈도가 떨어지므로 기본 경로는 빠르게 유지합니다.

### Detekt 규칙

- Detekt custom rule은 [quality/detekt-rules](../../quality/detekt-rules) 하위에 구현과 테스트를 함께 둡니다.
- Detekt custom rule class 위에는 해당 규칙이 막는 실패 모드를 설명하는 1~2줄 KDoc을 둡니다.
- navigation과 전역 UI 규칙은 `verifyArchitectureRules`에서 최소한으로 강제합니다.

### 문서 정합성

- 문서 규칙과 실제 검증 스크립트가 어긋나지 않아야 합니다.

### 규칙 카탈로그

#### Code Hygiene

- `SA-CODE-001`: 앱에서 참조하지 않는 `private class`, `private member`, `private property`를 남기지 않습니다.
  구현: [detekt.yml](../../detekt.yml)
- `SA-CODE-002`: 사용하지 않는 import와 wildcard import를 남기지 않습니다.
  구현: [detekt.yml](../../detekt.yml)

#### Navigation

- `SA-NAV-001`: feature Screen/Content Composable은 `NavHostController`, `NavController`, `LocalUriHandler`를 직접 사용하지 않습니다.
  구현: [FeatureArchitectureRules.kt](../../quality/detekt-rules/src/main/kotlin/kr/co/architecture/quality/detekt/FeatureArchitectureRules.kt)
- `SA-NAV-002`: feature Composable은 `navigate(...)`, `popBackStack()`을 직접 호출하지 않습니다.
  구현: [FeatureArchitectureRules.kt](../../quality/detekt-rules/src/main/kotlin/kr/co/architecture/quality/detekt/FeatureArchitectureRules.kt)
- `SA-NAV-003`: 화면 이동은 ViewModel의 `navigateTo(Route)`, `navigateBack()`, `navigateWeb(url)`로 요청합니다.

#### Global UI

- `SA-GLOBAL-UI-001`: feature source는 전역 progress를 위해 `BaseProgressBar`를 직접 렌더링하지 않습니다.
  구현: [FeatureArchitectureRules.kt](../../quality/detekt-rules/src/main/kotlin/kr/co/architecture/quality/detekt/FeatureArchitectureRules.kt)
- `SA-GLOBAL-UI-002`: 공통 에러 메시지 다이얼로그는 feature-local `BaseCenterDialog`가 아니라 `GlobalUiBus` 경로를 사용합니다.
  구현: [FeatureArchitectureRules.kt](../../quality/detekt-rules/src/main/kotlin/kr/co/architecture/quality/detekt/FeatureArchitectureRules.kt)
- `SA-GLOBAL-UI-003`: suspend 작업의 기본 loading/error 처리는 `BaseViewModel.launchWithCatching`을 사용합니다.

#### ViewModel / Contract

- `SA-VIEWMODEL-001`: Contract는 `UiState`, `UiEvent`, `UiSideEffect`를 함께 유지합니다.
- `SA-VIEWMODEL-002`: state mutation은 `setState { copy(...) }`로 모읍니다.
- `SA-VIEWMODEL-003`: ViewModel은 Compose API, `NavHostController`, Android View 타입에 의존하지 않습니다.

#### UiModel / Mapper

- `SA-MODEL-001`: DTO/Entity를 `UiState`나 Composable에 직접 노출하지 않습니다.
- `SA-MODEL-002`: 단순 collection wrapper `*ListUiModel`은 만들지 않습니다.
- `SA-MODEL-003`: 기본값은 생성자 default parameter로 표현하고 `getDefault()` factory를 만들지 않습니다.

#### Script / Harness

- `SA-SCRIPT-001`: `scripts/*.sh`는 `shell-script-guide`의 필수 헤더를 가집니다.
- `SA-HARNESS-001`: `.ai-skills` 원본과 생성된 `.agents`/`.claude` local mirror는 sync 결과와 일치해야 합니다.

### Detekt Custom Rule

#### 파일 위치

- 설정 파일: [detekt.yml](../../detekt.yml)
- RuleSetProvider: [MviTemplateStyleRuleSetProvider.kt](../../quality/detekt-rules/src/main/kotlin/kr/co/architecture/quality/detekt/MviTemplateStyleRuleSetProvider.kt)
- Rule 구현: [FeatureArchitectureRules.kt](../../quality/detekt-rules/src/main/kotlin/kr/co/architecture/quality/detekt/FeatureArchitectureRules.kt)
- Rule 테스트: [FeatureArchitectureRulesTest.kt](../../quality/detekt-rules/src/test/kotlin/kr/co/architecture/quality/detekt/FeatureArchitectureRulesTest.kt)

#### 작성 기준

- concrete rule class 위에는 1-2줄 KDoc을 추가합니다.
- rule 테스트 함수명은 [test-writing-guide](test-writing-guide.md)의 한국어 백틱 문장 규칙을 따릅니다.

```kotlin
/**
 * Feature 화면에서 navigation 객체를 직접 소유하는 코드를 막습니다.
 * 화면 이동은 ViewModel의 navigation method로 모읍니다.
 */
internal class NoFeatureDirectNavigationRule(
  config: Config,
) : PackageScopedRule(config)
```

### 후속 후보

#### UI 규칙

- 사용자 노출 문자열 하드코딩 금지
- Compose 색상 하드코딩 금지
- `modifier: Modifier = Modifier` 마지막 파라미터 강제

#### 상태와 비동기 규칙

- `SavedStateHandle` lazy 보관 금지
- `launchWithCatching` 내부 `runCatching`/`try-catch` 중복 금지
- public top-level Composable에 Preview 요구

## 절차

### 규칙 위치 결정

1. 새 규칙이 script, Gradle task, lint 중 어디에 맞는지 정합니다.
2. 하네스 문서 정합성은 shell script에 둡니다.
3. Android 구조 규칙은 [verify-architecture-rules.sh](../../scripts/verify-architecture-rules.sh)와 `verifyArchitectureRules`에 둡니다.
4. Android 코드 품질은 Gradle compile/test/lint task에 둡니다.

### 반영과 검증

1. Detekt custom rule을 추가하거나 수정하면 rule class KDoc과 한국어 테스트명을 함께 갱신합니다.
2. `qualityGateFast`에 포함한 뒤 로컬에서 실행합니다.
3. skill 문서에 명령과 기준을 갱신합니다.

## 출력

- 갱신된 quality task 또는 verification script
- 갱신된 skill/runbook
- 실행 결과

## 점검

- `./gradlew qualityGateFast`가 한 번에 실행되는가
- 실패 메시지가 다음 행동을 알려주는가
- sync 결과물이 원본과 일치하는가
