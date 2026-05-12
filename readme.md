# MVI Template

Clean Architecture + MVI 기반 Android 템플릿입니다.

## 목표

- 기존 멀티모듈 구조를 유지합니다.
- `first`, `second`, `detail` 샘플 feature를 통해 화면, navigation, ViewModel, domain/data 흐름을 확인할 수 있게 합니다.
- CI가 없어도 로컬과 AI 작업 절차에서 `qualityGateFast`로 최소 품질선을 확인합니다.

## Requirements

- Android Studio 또는 Android SDK
- JDK 17 이상
- Gradle wrapper 사용

현재 build logic과 Android compile option은 Java 17 target에 맞춰져 있습니다.

## Project Structure

```text
project-root
├─ app                 # MainActivity, root NavHost, bottom navigation
├─ feature
│  ├─ first            # list 샘플 feature
│  ├─ second           # second tab 샘플 feature
│  └─ detail           # typed route argument 샘플 feature
├─ core
│  ├─ ui               # BaseViewModel, GlobalUiBus, theme, 공통 UI
│  ├─ router           # ViewModel navigation abstraction
│  ├─ model            # layer 간 공유 model
│  ├─ domain           # UseCase, repository interface
│  ├─ repository       # repository implementation
│  ├─ network          # Retrofit API, DTO, network module
│  └─ database         # Room/local source
├─ testing             # coroutine rule, test helper
├─ benchmarks          # baseline profile / benchmark sample
└─ build-logic         # Gradle convention plugin
```

## Architecture Rules

- Feature 화면은 `*Contract.kt`, `*ViewModel.kt`, `*Screen.kt`를 기본 단위로 둡니다.
- `core:domain`에는 UseCase와 repository interface만 둡니다.
- `core:repository`는 domain repository interface를 구현합니다.
- `core:network`의 DTO는 UI로 직접 노출하지 않고 repository에서 model로 변환합니다.
- Navigation은 `core:ui/NavigationRoute.kt`의 typed route와 각 feature의 `NavGraphBuilder` 확장 함수로 연결합니다.

## Common Commands

```bash
./gradlew :app:compileDebugKotlin
./gradlew :app:assembleDebug
./gradlew testDebugUnitTest
./gradlew lintDebug
./gradlew detekt
./gradlew verifyHarnessConsistency
./gradlew verifyArchitectureRules
./gradlew qualityGateFast
```

## Network Configuration

`core:network`는 아래 순서로 값을 읽습니다.

1. 환경변수
2. Gradle property
3. `local.properties`
4. 기본값

사용 가능한 key:

```properties
apiKey=...
apiUrl=https://newsapi.org/
```

`local.properties`는 로컬 전용 파일이므로 git에 올리지 않습니다.

## AI Harness

Skill 원본과 mirror는 다음 구조를 사용합니다.

```text
.ai-skills/       # source of truth
.agents/skills/   # Codex mirror
.claude/skills/   # Claude mirror
```

문서나 skill을 수정한 뒤에는 아래 명령을 실행합니다.

```bash
./scripts/sync-harness-docs.sh copy
./scripts/verify-harness-consistency.sh
./scripts/verify-architecture-rules.sh
```

Gradle에서도 같은 검증을 실행할 수 있습니다.

```bash
./gradlew verifyHarnessConsistency
./gradlew verifyArchitectureRules
```

## Work Flow

1. 구현할 동작을 `UiState`, `UiEvent`, `UiSideEffect`로 나눕니다.
2. 필요한 model, repository interface, use case를 domain부터 추가합니다.
3. repository/network/database 구현을 연결합니다.
4. Compose Content는 stateless하게 두고 ViewModel wrapper에서 state/effect를 연결합니다.
5. 화면 이동은 ViewModel의 `navigateTo`, `navigateBack`, `navigateWeb`으로 요청합니다.
6. 로딩/에러는 `launchWithCatching`과 `globalUiBus` 경로로 처리합니다.
7. ViewModel 또는 mapper 테스트를 추가합니다.
8. `./gradlew qualityGateFast`로 마감 검증을 실행합니다.
