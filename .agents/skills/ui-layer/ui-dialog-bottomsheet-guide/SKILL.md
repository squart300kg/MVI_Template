---
name: ui-dialog-bottomsheet-guide
description: 전역 error/loading UI와 화면 전용 Dialog/BottomSheet 상태, callback을 구분해 구현할 때 사용하는 스킬.
---

# ui-dialog-bottomsheet-guide

## 목적

전역 error/loading UI와 화면 전용 Dialog/BottomSheet를 섞지 않고 MVI state와 callback 경계로 관리합니다.

## 사용 시점

- 확인/에러/선택 Dialog를 새로 만들 때
- `BaseCenterDialog`로 표현하기 어려운 커스텀 Dialog가 필요할 때
- Dialog 표시 상태를 Contract에 추가할 때
- BottomSheet를 화면 전용 상태로 제어할 때

## 핵심 원칙

### 전역 UI

- 공통 에러 메시지 다이얼로그는 feature-local dialog가 아니라 ViewModel의 `globalUiBus.showFailureDialog(...)` 또는 `launchWithCatching` 경로를 사용합니다.
- `BaseCenterDialog`는 [MainActivity.kt](../../app/src/main/kotlin/kr/co/architecture/app/MainActivity.kt)에서 `globalUiBus.failureDialog`를 구독해 표시하는 전역 경로를 우선합니다.
- loading은 feature 화면에 별도 progress state를 만들지 않고 [GlobalUiBus.kt](../../core/ui/src/main/kotlin/kr/co/architecture/core/ui/GlobalUiBus.kt) 경로를 사용합니다.

### 화면 전용 UI

- 화면 전용 Dialog 상태는 `UiState`에 명시적으로 둡니다.
- Dialog wrapper와 content를 분리합니다.
- confirm/dismiss callback은 화면에서 주입합니다.

## 절차

### 역할 구분

1. 에러 메시지 표시인지 사용자 선택/입력 플로우인지 먼저 구분합니다.
2. 에러 메시지나 전역 loading이라면 feature dialog/progress를 만들지 않고 ViewModel의 `launchWithCatching` 또는 `globalUiBus.showFailureDialog(...)`를 사용합니다.
3. 사용자 선택/입력 플로우라면 상태 필드와 event를 Contract에 추가합니다.

### 구현과 확인

1. ViewModel에서 표시/닫기 상태를 갱신합니다.
2. Composable은 상태에 따라 Dialog를 렌더링합니다.
3. 주요 상태 Preview를 추가합니다.

## 출력

- Dialog state/event
- Dialog Composable
- Preview
- 필요한 string resource

### 예시

```kotlin
// 에러/로딩은 feature UiState에 두지 않습니다.
private fun load() = launchWithCatching {
  setState { copy(items = getItems()) }
}

// 사용자 확인 플로우는 feature UiState로 둡니다.
data class SampleUiState(
  val confirmDialog: ConfirmDialogUiModel? = null,
)
```

## 점검

- dismiss 경로가 빠지지 않았는가
- 화면 회전 후 상태가 의도대로 유지되는가
- 공통 에러 Dialog와 역할이 겹치지 않는가
- 에러 메시지/로딩 처리가 feature Composable이 아니라 `GlobalUiBus` 경로로 연결됐는가
