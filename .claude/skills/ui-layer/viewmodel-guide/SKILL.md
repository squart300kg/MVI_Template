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
- 화면 타입 분기는 `UiType` 또는 명확한 state field로 ViewModel에서 결정합니다.

## 절차

1. `[Feature]Contract.kt`에 state/event/effect를 정의합니다.
2. `[Feature]ViewModel.kt`에서 `createInitialState()`와 `handleEvent()`를 구현합니다.
3. UseCase가 필요하면 constructor injection으로 받습니다.
4. navigation은 typed route를 사용해 `navigateTo(...)`로 호출합니다.
5. 테스트에서 fake repository/use case로 state와 effect를 검증합니다.

## 출력

- Contract
- ViewModel
- 필요한 UseCase 주입
- ViewModel 단위 테스트

## 점검

- ViewModel이 Compose API에 의존하지 않는가
- state mutation이 `setState`로 모였는가
- event가 누락 없이 `when`에서 처리되는가
- side effect collector가 Screen에 연결됐는가
