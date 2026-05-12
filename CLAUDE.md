# MVI Template Agent Runbook

## 목적

이 프로젝트는 6시간 안드로이드 사전과제에서 바로 기능 구현을 시작할 수 있도록 유지하는 Clean Architecture + MVI starter입니다.

## 기본 구조

- `app`: `MainActivity`, root `NavHost`, bottom navigation, app-level ViewModel
- `feature/*`: 화면 단위 Compose UI, Contract, ViewModel
- `core:ui`: 공통 Compose UI, `BaseViewModel`, `GlobalUiBus`, route model
- `core:router`: ViewModel에서 navigation을 요청할 수 있게 하는 router abstraction
- `core:domain`: UseCase와 repository interface
- `core:repository`: repository implementation
- `core:network`: Retrofit/API/DTO
- `core:database`: Room/local source
- `core:model`: layer 간 공유 model
- `testing`: coroutine rule, fake/stub helper
- `build-logic`: Android convention plugin

## 작업 규칙

- 현재 골격 샘플인 `first`, `second`, `detail` feature는 과제 전까지 유지합니다.
- 새 화면은 `feature/<name>` 모듈 또는 가장 가까운 기존 feature 안에 `*Contract.kt`, `*ViewModel.kt`, `*Screen.kt` 순서로 추가합니다.
- domain repository interface는 `core:domain`, 구현체는 `core:repository`에 둡니다.
- 화면 이동은 feature Composable에서 `NavHostController`를 직접 호출하지 않고 ViewModel의 `navigateTo`, `navigateBack`, `navigateWeb`으로 요청합니다.
- 전역 progress와 에러 메시지 다이얼로그는 ViewModel의 `launchWithCatching`/`globalUiBus` 경로를 사용합니다.
- 사용자 노출 문자열/색상/치수는 가능한 resource 또는 theme token을 우선합니다.
- `.ai-skills`를 수정하면 `./scripts/sync-harness-docs.sh copy`를 실행해 mirror를 갱신합니다.

## 기본 명령

```bash
./gradlew :app:compileDebugKotlin
./gradlew :app:assembleDebug
./gradlew testDebugUnitTest
./gradlew lintDebug
./gradlew verifyHarnessConsistency
./gradlew verifyArchitectureRules
./gradlew qualityGateFast
```

## Skill 정본

- 원본: `.ai-skills`
- Codex mirror: `.agents/skills`
- Claude mirror: `.claude/skills`
- sync: `./scripts/sync-harness-docs.sh copy`
- consistency: `./scripts/verify-harness-consistency.sh`
- architecture: `./scripts/verify-architecture-rules.sh`

## 마감 기준

- 앱이 debug 기준으로 컴파일됩니다.
- 변경한 ViewModel/domain/data logic에는 단위 테스트를 추가하거나 기존 테스트를 갱신합니다.
- 스킬/런북/스크립트를 바꾸면 sync와 harness consistency 검증을 통과합니다.
- 최종 보고에는 실행한 검증 명령과 실패/생략 사유를 포함합니다.
