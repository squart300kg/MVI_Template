---
name: ui-model-guide
description: UiState, UiModel, DTO 매퍼를 추가하거나 정리할 때 모델 위치와 네이밍을 맞추는 스킬.
---

# ui-model-guide

## 목적

화면 렌더링 모델과 layer model의 경계를 분리하고, mapper를 일관된 형태로 유지합니다.

## 사용 시점

- `UiState`나 `UiModel`을 새로 만들 때
- DTO/domain model을 화면 표시용 모델로 변환할 때
- list wrapper, default factory, legacy naming을 정리할 때

## 핵심 원칙

- concrete `UiState`, `UiEvent`, `UiSideEffect`는 feature의 `*Contract.kt`에서 함께 관리합니다.
- 여러 화면에서 재사용하는 UI 전용 model은 `core:ui` 또는 가장 가까운 공통 UI 모듈에 둡니다.
- 한 feature 안에서만 쓰는 `UiModel`은 해당 feature Contract 또는 feature package에 둡니다.
- DTO는 `UiModel`로 직접 노출하지 않고 mapper를 거칩니다.
- 단순 collection wrapper인 `*ListUiModel`은 만들지 않고 `UiState`에 list를 직접 둡니다.
- 기본값은 생성자 default parameter로 표현하고 `getDefault()` factory를 만들지 않습니다.

## 절차

1. model이 domain model인지 화면 표시용 model인지 먼저 구분합니다.
2. 화면 표시용이면 재사용 범위에 따라 feature-local 또는 `core:ui` 위치를 정합니다.
3. DTO/domain model에서 화면 모델로 변환하는 top-level extension mapper를 추가합니다.
4. mapper 이름은 방향이 드러나게 `toUiModel`, `toDomainModel`, `toRequest`처럼 작성합니다.
5. `UiState`에는 실제 렌더링과 이벤트 처리에 쓰는 필드만 남깁니다.
6. ViewModel 테스트에서 mapper 결과나 state 갱신 결과를 검증합니다.

## 출력

- `UiState` 또는 `UiModel`
- DTO/domain mapper
- 필요한 테스트 fixture

## 점검

- DTO/Entity가 Composable 또는 `UiState`로 직접 새지 않았는가
- `UiModel`이 실제 화면 렌더링에 필요한 필드만 가지는가
- 단순 list wrapper나 `getDefault()`가 추가되지 않았는가
- 같은 model이 feature와 공통 모듈에 중복 선언되지 않았는가
