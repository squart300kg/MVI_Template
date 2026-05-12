---
name: viewmodel-guide
description: BaseViewModel 기반 MVI Contract와 ViewModel을 새로 만들거나 수정할 때 사용하는 스킬.
---

# viewmodel-guide

## 목적

화면 상태, 사용자 이벤트, 단발성 effect를 일관된 MVI 구조로 관리합니다.

## 사용 시점

- 새 feature 화면을 만들 때
- 기존 ViewModel event/state/effect를 정리할 때
- navigation, loading, error 처리 위치를 판단할 때

## 핵심 원칙

- Contract에는 `UiState`, `UiEvent`, `UiSideEffect`를 함께 둡니다.
- ViewModel은 `BaseViewModel<State, Event, Effect>`를 상속합니다.
- user action은 `setEvent(...)`로 전달하고 `handleEvent`에서 처리합니다.
- 상태 변경은 `setState { copy(...) }`로만 수행합니다.
- suspend 작업은 `launchWithCatching`을 우선 사용합니다.
- 로딩과 에러 메시지 다이얼로그는 `launchWithCatching`이 연결하는 `globalUiBus`를 기본 경로로 사용합니다.
- feature `UiState`에는 전역 progress/error dialog용 `isLoading`, `errorDialog` 상태를 두지 않습니다.
- 화면 이동은 `navigateTo(Route)`, `navigateBack()`, `navigateWeb(url)` 메서드로 요청합니다.
- 화면 타입 분기는 `UiType` 또는 명확한 state field로 ViewModel에서 결정합니다.

## UiText 기준

- `UiText`는 화면 표시용 텍스트 토큰이며, ViewModel 로직에서 다시 raw `String`으로 복구하지 않습니다.
- 서버/도메인에서 온 문자열을 그대로 표시만 하면 `UiState`/`UiModel`에 `String`으로 둡니다.
- 리소스 문구, 리소스 문구와 서버 문자열의 조합, 서버 에러 메시지처럼 하나의 표시 슬롯에 여러 출처가 들어올 수 있으면 `UiText`를 사용합니다.
- 표시 슬롯에 서버 문자열이나 예외 메시지를 그대로 넣어야 할 때만 `UiText.PlainText`를 사용합니다.
- navigation, 저장, API 요청, 조건 분기 등에 다시 필요한 값은 `UiText`와 별도로 raw field나 domain model로 유지합니다.
- 클릭 이벤트는 표시 문자열 대신 `id` 같은 식별자를 전달하고, 목적지 화면은 가능하면 식별자로 데이터를 다시 조회합니다.

## 절차

1. `[Feature]Contract.kt`에 state/event/effect를 정의합니다.
2. `[Feature]ViewModel.kt`에서 `createInitialState()`와 `handleEvent()`를 구현합니다.
3. UseCase가 필요하면 constructor injection으로 받습니다.
4. suspend 작업은 `launchWithCatching`으로 감싸 전역 loading/error 처리를 위임합니다.
5. navigation은 typed route를 사용해 `navigateTo(...)`, `navigateBack()`, `navigateWeb(...)`로 호출합니다.
6. 테스트에서 fake repository/use case로 state와 effect를 검증합니다.

## 출력

- Contract
- ViewModel
- 필요한 UseCase 주입
- ViewModel 단위 테스트

## 점검

- ViewModel이 Compose API에 의존하지 않는가
- state mutation이 `setState`로 모였는가
- event가 누락 없이 `when`에서 처리되는가
- `UiText`를 raw 데이터 복구나 navigation payload 용도로 사용하지 않는가
- loading/error 처리가 feature `UiState`가 아니라 `globalUiBus` 경로로 처리되는가
- navigation이 Composable 직접 호출이 아니라 ViewModel 메서드 호출로 모였는가
- side effect collector가 Screen에 연결됐는가
