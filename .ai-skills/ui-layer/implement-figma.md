---
name: implement-figma
description: Figma 디자인이나 스크린샷을 Jetpack Compose 화면으로 옮길 때 현재 feature/module/resource 구조에 맞추는 스킬.
---

# implement-figma

## 목적

디자인을 현재 Compose/MVI 구조에 맞는 실제 화면 코드로 변환합니다.

## 사용 시점

- 사용자가 Figma URL, 스크린샷, 디자인 요구사항을 줄 때
- 기존 화면을 디자인에 맞게 수정할 때

## 핵심 원칙

- 디자인 산출물은 참고값이며, 구현은 현재 project resource/theme/component를 우선합니다.
- 화면 wrapper와 content Composable을 분리합니다.
- 텍스트, 색상, 치수는 resource/theme으로 옮길지 판단합니다.
- 이미지 asset은 Android resource naming 규칙에 맞춰 추가합니다.

## 절차

1. 디자인의 화면 상태와 사용자 action을 Contract로 정리합니다.
2. 기존 theme, color, dimen, 공통 UI를 확인합니다.
3. Compose layout을 Content Composable에 구현합니다.
4. ViewModel이 필요한 데이터와 callback을 연결합니다.
5. Preview로 주요 상태를 확인합니다.

## 출력

- Compose 화면 코드
- 필요한 resource/asset
- Preview
- 연결된 state/event

## 점검

- 디자인 맞춤 때문에 아키텍처 경계를 깨지 않았는가
- 하드코딩된 문자열/색상/치수가 과하게 남지 않았는가
- 작은 화면에서 텍스트나 버튼이 겹치지 않는가
