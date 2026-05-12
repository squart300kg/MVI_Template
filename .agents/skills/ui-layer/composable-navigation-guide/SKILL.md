---
name: composable-navigation-guide
description: Typed route, NavGraphBuilder 확장, bottom tab navigation을 추가하거나 수정할 때 사용하는 스킬.
---

# composable-navigation-guide

## 목적

현재 `core:router`와 typed route 기반 navigation 패턴을 유지합니다.

## 사용 시점

- 새 화면 route를 추가할 때
- detail argument를 전달할 때
- bottom tab 또는 root NavHost를 수정할 때

## 핵심 원칙

- route model은 `core:ui/NavigationRoute.kt`에 둡니다.
- 각 feature는 자체 `NavGraphBuilder.<feature>Screen()` 확장 함수를 제공합니다.
- root 등록은 `app/MainActivity`의 `NavHost`에서 수행합니다.
- ViewModel navigation은 `navigateTo(Route)`를 사용합니다.
- Composable에서 argument를 직접 오래 보관하지 않습니다.

## 절차

1. `@Serializable` route를 추가합니다.
2. feature `Screen.kt`에 `composable<Route>` 등록 함수를 추가합니다.
3. root `NavHost`에 feature registration을 추가합니다.
4. ViewModel 또는 callback에서 typed route로 이동합니다.
5. bottom tab이면 `MainBottomTab`과 icon/string resource를 함께 수정합니다.

## 출력

- typed route
- feature NavGraphBuilder extension
- root NavHost registration
- 필요한 tab/resource 변경

## 점검

- route argument 타입이 직렬화 가능한가
- startDestination과 bottom tab current state가 맞는가
- back stack 동작이 기존 화면과 충돌하지 않는가
