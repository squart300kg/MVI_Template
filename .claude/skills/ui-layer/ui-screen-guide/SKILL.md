---
name: ui-screen-guide
description: Jetpack Compose 화면, Content Composable, Preview, 공통 Compose 추출을 추가하거나 정리할 때 사용하는 스킬.
---

# ui-screen-guide

## 목적

Compose UI를 feature module 구조와 MVI 경계에 맞게 작성하고, 눈으로 확인 가능한 Preview를 유지합니다.

## 사용 시점

- `*Screen.kt`를 새로 만들거나 수정할 때
- state hoisting, callback, Preview 구조를 정리할 때
- 반복되는 Compose UI를 feature 내부 또는 `core:ui`로 추출할지 판단할 때

## 핵심 원칙

- Route entry Composable과 stateless Content Composable을 분리합니다.
- Wrapper는 ViewModel state 수집과 effect collect만 담당합니다.
- Content는 `uiState`와 callback만 받아 렌더링합니다.
- `modifier: Modifier = Modifier`는 가능한 마지막 파라미터에 둡니다.
- 문자열, 색상, 치수는 resource 또는 theme token을 우선합니다.
- 전역 progress와 에러 메시지 다이얼로그는 feature Composable이 직접 그리지 않고 [ui-dialog-bottomsheet-guide](ui-dialog-bottomsheet-guide.md)를 따릅니다.
- Screen/Content Composable은 `NavHostController`를 직접 받지 않고 [ui-navigation-guide](ui-navigation-guide.md)의 ViewModel callback 경로를 사용합니다.
- 가능하면 ViewModel wrapper가 아니라 Content Composable을 Preview합니다.
- 한 번만 쓰는 UI는 공통화하지 않고, 2곳 이상 반복되면 feature 내부 공통 Composable을 먼저 고려합니다.

## 텍스트 렌더링 기준

- `String` 상태는 `Text(value)`로 직접 표시합니다.
- [UiText.kt](../../core/ui/src/main/kotlin/kr/co/architecture/core/ui/util/UiText.kt) 상태는 Composable에서만 `asString()`으로 resolve합니다.
- Composable에서 resolve한 `UiText` 결과를 ViewModel event payload로 다시 넘기지 않습니다.
- `UiText.PlainText`는 표시 슬롯의 일반 문자열을 그리기 위한 값이며 raw 데이터 복구 용도로 사용하지 않습니다.
- 리소스 문구와 동적 문자열을 조합해야 하면 ViewModel에서 `UiText.StringResource(resId, args)`를 만들고 Composable은 표시만 담당합니다.

## 절차

1. 기존 feature의 `firstScreen`, `secondScreen`, `detailScreen` 패턴을 확인합니다.
2. `NavGraphBuilder` 확장 함수에서 route entry를 등록합니다.
3. ViewModel wrapper에서 `collectAsStateWithLifecycle()`을 사용합니다.
4. Content Composable을 만들고 state/callback을 hoist합니다.
5. loading/error는 ViewModel의 `launchWithCatching`과 `globalUiBus` 경로로 처리되게 둡니다.
6. 상태가 2개 이상이면 `PreviewParameterProvider`를 사용하고, 단순 상태는 간단 Preview로 확인합니다.
7. 공통화가 필요하면 기존 화면 동작을 바꾸지 않는 범위에서 Composable만 추출합니다.

## 출력

- Route entry Composable
- Stateless Content Composable
- Preview
- 필요한 string/color/dimen resource
- 필요 시 feature-local 또는 `core:ui` 공통 Composable

## 점검

- Composable이 repository/use case를 직접 호출하지 않는가
- ViewModel이 UI rendering detail을 알지 않는가
- feature Composable이 `BaseProgressBar`, 전역 `BaseCenterDialog`, `NavHostController`를 직접 사용하지 않는가
- `UiText.asString()`이 Composable 표시 경계 밖으로 새지 않았는가
- 텍스트가 하드코딩으로 과하게 남지 않았는가
- Preview가 빌드 가능한가
- 공통 Composable API가 화면별 세부 구현을 과하게 노출하지 않는가

## 예시

```kotlin
@Composable
internal fun SampleRoute(
  viewModel: SampleViewModel = hiltViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  SampleContent(
    uiState = uiState,
    onItemClick = { id -> viewModel.setEvent(SampleEvent.ItemClick(id)) },
  )
}

@Composable
private fun SampleContent(
  uiState: SampleUiState,
  onItemClick: (Long) -> Unit,
  modifier: Modifier = Modifier,
) {
  // uiState와 callback만 사용해 렌더링합니다.
}
```
