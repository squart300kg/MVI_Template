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
- `NavHostController`는 root/router 계층에서만 소유합니다.
- Screen/Content Composable은 `NavHostController`, `NavController`, `LocalUriHandler`를 직접 받거나 호출하지 않습니다.
- 화면 이동은 ViewModel의 `navigateTo(Route)`, `navigateBack()`, `navigateWeb(url)` 메서드로만 요청합니다.
- Composable은 navigation 이벤트를 ViewModel event/callback으로 전달하고 직접 `navigate(...)`, `popBackStack()`을 호출하지 않습니다.
- Composable에서 argument를 직접 오래 보관하지 않고 ViewModel 또는 typed route 입력으로 넘깁니다.

## 절차

1. `@Serializable` route를 추가합니다.
2. feature `Screen.kt`에 `composable<Route>` 등록 함수를 추가합니다.
3. root `NavHost`에 feature registration을 추가합니다.
4. 사용자 액션은 `setEvent(...)` 또는 ViewModel callback으로 전달합니다.
5. ViewModel에서 `navigateTo(Route)`, `navigateBack()`, `navigateWeb(url)` 중 하나를 호출합니다.
6. bottom tab이면 `MainBottomTab`과 icon/string resource를 함께 수정합니다.

## 출력

- typed route
- feature NavGraphBuilder extension
- root NavHost registration
- ViewModel navigation event 처리
- 필요한 tab/resource 변경

## 점검

- route argument 타입이 직렬화 가능한가
- feature Composable이 `NavHostController`나 `NavController`를 import하지 않는가
- feature Composable에서 `navigate(...)`, `popBackStack()`, `LocalUriHandler`를 직접 쓰지 않았는가
- 화면 이동이 ViewModel의 `navigateTo`, `navigateBack`, `navigateWeb`로 모였는가
- startDestination과 bottom tab current state가 맞는가
- back stack 동작이 기존 화면과 충돌하지 않는가
