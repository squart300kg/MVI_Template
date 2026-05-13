# MVI Template 하네스 인덱스

이 문서는 스킬 목차와 검증 진입점 역할만 합니다.
상세 규칙과 절차는 링크된 스킬 문서를 정본으로 봅니다.
[AGENTS.md](AGENTS.md)와 [CLAUDE.md](CLAUDE.md)는 동일 내용이며, `./scripts/sync-harness-docs.sh copy`로 동기화합니다.

## 1. 시작 순서

- 구현 전 [architecture-baseline-guide](.ai-skills/common/architecture-baseline-guide.md)로 모듈 경계와 아키텍처 기준을 확인합니다.
- 작업 유형에 맞는 스킬을 아래 목차에서 선택합니다.
- 문서나 스킬을 수정했다면 [ai-docs-guide](.ai-skills/workflow/ai-docs-guide.md)를 따릅니다.
- 완료 전 [complete-task-and-final-check](.ai-skills/common/complete-task-and-final-check.md) 기준으로 검증합니다.

## 2. 스킬 목차

### 2-1. 공통 기준

- 아키텍처 기준: [architecture-baseline-guide](.ai-skills/common/architecture-baseline-guide.md)
- 공통 Kotlin/Compose 코드 스타일: [common-coding-guide](.ai-skills/common/common-coding-guide.md)
- 빌드/Gradle 오류 분석: [build](.ai-skills/common/build.md)
- 작업 완료 점검: [complete-task-and-final-check](.ai-skills/common/complete-task-and-final-check.md)

### 2-2. UI 레이어

- Compose 화면/Preview/공통화: [ui-screen-guide](.ai-skills/ui-layer/ui-screen-guide.md)
- Navigation/route: [ui-navigation-guide](.ai-skills/ui-layer/ui-navigation-guide.md)
- ViewModel/Contract/UiModel: [ui-state-viewmodel-guide](.ai-skills/ui-layer/ui-state-viewmodel-guide.md)
- Dialog/BottomSheet: [ui-dialog-bottomsheet-guide](.ai-skills/ui-layer/ui-dialog-bottomsheet-guide.md)

### 2-3. Domain/Data

- Domain 계층 경계: [domain-layer](.ai-skills/domain-layer/domain-layer.md)
- 서버 API 연동: [integrate-server-api-guide](.ai-skills/data-layer/integrate-server-api-guide.md)

### 2-4. Test/Quality

- 테스트 작성: [test-writing-guide](.ai-skills/test/test-writing-guide.md)
- 정적 분석/품질 게이트: [static-analysis-guide](.ai-skills/test/static-analysis-guide.md)

### 2-5. Workflow

- 문서/스킬 관리: [ai-docs-guide](.ai-skills/workflow/ai-docs-guide.md)
- Shell script 관리: [shell-script-guide](.ai-skills/workflow/shell-script-guide.md)
- Worktree/PR/merge: [worktree-merge-guide](.ai-skills/workflow/worktree-merge-guide.md)

## 3. 프로젝트 경계

- `app`: root `NavHost`, app-level ViewModel, global UI host
- `feature/*`: 화면 단위 Compose UI, Contract, ViewModel
- `core:ui`: 공통 Compose UI, `BaseViewModel`, `GlobalUiBus`, route model
- `core:router`: ViewModel navigation 요청을 처리하는 router abstraction
- `core:domain`: UseCase와 repository interface
- `core:repository`: repository implementation과 mapper
- `core:network`: Retrofit/API/DTO
- `core:database`: Room/local source
- `core:model`: layer 간 공유 model
- `testing`: coroutine rule, fake/stub helper
- `quality:detekt-rules`: Detekt custom rule
- `build-logic`: Android convention plugin

## 4. 검증 명령

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

## 5. 유지 방식

- 스킬 원본: `.ai-skills`
- Codex mirror: `.agents/skills` (gitignored local sync result)
- Claude mirror: `.claude/skills` (gitignored local sync result)
- 문서/스킬 sync: `./scripts/sync-harness-docs.sh copy`
- 하네스 검증: `./scripts/verify-harness-consistency.sh`
- 구조 검증: `./scripts/verify-architecture-rules.sh`
