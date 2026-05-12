---
name: extract-shared-compose
description: 여러 feature에 중복된 Compose UI를 core:ui 또는 feature 내부 공통 컴포저블로 추출할 때 사용하는 스킬.
---

# extract-shared-compose

## 목적

반복되는 UI를 추출하되 과제 속도를 해치지 않는 수준으로만 공통화합니다.

## 사용 시점

- 같은 list item, empty state, top bar, card가 2곳 이상 반복될 때
- feature 내부 private Composable이 너무 커졌을 때

## 핵심 원칙

- 한 번만 쓰는 UI는 추출하지 않습니다.
- feature 전용 중복은 feature 내부에 먼저 둡니다.
- 앱 전역으로 재사용되는 UI만 `core:ui`로 올립니다.
- 추출은 동작 변경 없이 진행합니다.

## 절차

1. 중복 UI의 공통 입력과 화면별 차이를 구분합니다.
2. callback은 호출 화면에서 주입합니다.
3. 기존 resource와 theme을 그대로 사용합니다.
4. 기존 화면을 새 공통 Composable 호출로 교체합니다.
5. 공통 Composable Preview를 추가합니다.

## 출력

- 공통 Composable
- 교체된 기존 화면
- Preview

## 점검

- 공통 API가 화면별 세부 구현을 과하게 노출하지 않는가
- 기존 click/navigation 동작이 유지됐는가
- 불필요한 새 resource나 dependency를 추가하지 않았는가
