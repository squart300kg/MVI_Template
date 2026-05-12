---
name: new-dialog-creation-guide
description: BaseCenterDialog 또는 새 Compose Dialog/BottomSheet를 추가하거나 상태와 callback을 정리할 때 사용하는 스킬.
---

# new-dialog-creation-guide

## 목적

Dialog와 BottomSheet를 MVI state와 재사용 가능한 content 구조로 관리합니다.

## 사용 시점

- 확인/에러/선택 Dialog를 새로 만들 때
- `BaseCenterDialog`로 표현하기 어려운 커스텀 Dialog가 필요할 때
- Dialog 표시 상태를 Contract에 추가할 때

## 핵심 원칙

- 공통 에러는 `GlobalUiBus`와 `BaseCenterDialog`를 우선 사용합니다.
- 화면 전용 Dialog 상태는 `UiState`에 명시적으로 둡니다.
- Dialog wrapper와 content를 분리합니다.
- confirm/dismiss callback은 화면에서 주입합니다.

## 절차

1. 공통 Dialog로 충분한지 먼저 확인합니다.
2. 상태 필드와 event를 Contract에 추가합니다.
3. ViewModel에서 표시/닫기 상태를 갱신합니다.
4. Composable은 상태에 따라 Dialog를 렌더링합니다.
5. 주요 상태 Preview를 추가합니다.

## 출력

- Dialog state/event
- Dialog Composable
- Preview
- 필요한 string resource

## 점검

- dismiss 경로가 빠지지 않았는가
- 화면 회전 후 상태가 의도대로 유지되는가
- 공통 에러 Dialog와 역할이 겹치지 않는가
