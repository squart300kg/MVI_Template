---
name: composable-preview-guide
description: Compose Preview와 PreviewParameterProvider를 추가하거나 정리할 때 사용하는 스킬.
---

# composable-preview-guide

## 목적

주요 UI 상태를 빠르게 눈으로 확인할 수 있는 Preview를 유지합니다.

## 사용 시점

- 새 Content Composable을 만들 때
- list, empty, loading, error 상태를 Preview로 확인할 때
- Preview data가 화면 파일을 복잡하게 만들 때

## 핵심 원칙

- 가능하면 ViewModel wrapper가 아니라 Content Composable을 Preview합니다.
- 상태가 2개 이상이면 `PreviewParameterProvider`를 사용합니다.
- 공통 preview provider는 `core:ui` 또는 해당 feature의 preview package에 둡니다.
- Preview는 production logic을 실행하지 않습니다.

## 절차

1. Preview 대상 UiState 또는 UiModel을 정합니다.
2. 필요한 case를 `sequenceOf(...)`로 제공합니다.
3. `BaseTheme`으로 감싸 실제 앱 theme에 가깝게 확인합니다.
4. Preview 전용 fake data가 production 코드에 섞이지 않게 둡니다.

## 출력

- `@Preview` 함수
- 필요한 PreviewParameterProvider
- 주요 상태별 preview data

## 점검

- Preview가 ViewModel/Hilt/network에 의존하지 않는가
- 긴 텍스트, 빈 데이터, 선택 상태가 최소 한 번씩 보이는가
- Preview 코드가 feature logic을 오염시키지 않는가
