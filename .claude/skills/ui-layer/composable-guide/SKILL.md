---
name: composable-guide
description: Jetpack Compose 화면과 컴포넌트를 추가하거나 수정할 때 MVI, resource, preview 기준을 맞추는 스킬.
---

# composable-guide

## 목적

Compose UI를 현재 feature module 구조와 MVI 경계에 맞게 작성합니다.

## 사용 시점

- `*Screen.kt`를 새로 만들거나 수정할 때
- state hoisting, callback, Preview 구조를 정리할 때
- 공통 UI를 `core:ui`로 올릴지 판단할 때

## 핵심 원칙

- Route entry Composable과 stateless Content Composable을 분리합니다.
- Wrapper는 ViewModel state 수집과 effect collect만 담당합니다.
- Content는 `uiState`와 callback만 받아 렌더링합니다.
- `modifier: Modifier = Modifier`는 가능한 마지막 파라미터에 둡니다.
- 문자열, 색상, 치수는 resource 또는 theme token을 우선합니다.
- 화면 작업 후 Preview를 추가합니다.

## 절차

1. 기존 feature의 `firstScreen`, `secondScreen`, `detailScreen` 패턴을 확인합니다.
2. `NavGraphBuilder` 확장 함수에서 route entry를 등록합니다.
3. ViewModel wrapper에서 `collectAsStateWithLifecycle()`을 사용합니다.
4. Content Composable을 만들고 state/callback을 hoist합니다.
5. PreviewParameterProvider 또는 간단 Preview로 주요 상태를 확인합니다.

## 출력

- Route entry Composable
- Stateless Content Composable
- Preview
- 필요한 string/color/dimen resource

## 점검

- Composable이 repository/use case를 직접 호출하지 않는가
- ViewModel이 UI rendering detail을 알지 않는가
- 텍스트가 하드코딩으로 과하게 남지 않았는가
- Preview가 빌드 가능한가
