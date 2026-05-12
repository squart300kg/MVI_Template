---
name: common-coding-guide
description: MVI Template의 Kotlin/Compose 코드를 작성하거나 정리할 때 공통 코드 스타일과 단순화 기준을 맞추는 스킬.
---

# common-coding-guide

## 목적

구현 속도를 해치지 않으면서 읽기 쉬운 Kotlin 코드를 유지합니다.
모듈 경계와 아키텍처 판단은 [architecture-baseline-guide](architecture-baseline-guide.md)를 따릅니다.

## 사용 시점

- Kotlin 파일을 새로 만들거나 수정할 때
- 불필요한 helper, wrapper, abstraction을 만들지 판단할 때
- 작업 마감 전 미사용 코드와 import를 정리할 때
- 새 모듈/계층 위치 판단이 아니라 코드 표현 방식을 정리할 때

## 핵심 원칙

- 한 번만 호출되는 코드는 호출부에 두는 것을 우선합니다.
- 실제 중복이 생기기 전에는 공통 abstraction을 만들지 않습니다.
- public API는 의도가 드러나는 이름과 명시적인 타입을 사용합니다.
- 테스트 fake는 `testing` 모듈에 둘 수 있지만, 한 테스트에서만 쓰면 테스트 파일 내부에 둡니다.
- [.ai-skills](../../.ai-skills)를 바꾸면 `./scripts/sync-harness-docs.sh copy`를 실행합니다.

## 절차

1. 변경 범위의 기존 스타일을 먼저 확인합니다.
2. 새 코드는 주변 파일의 들여쓰기, naming, sealed interface 패턴을 따릅니다.
3. 미사용 import, 죽은 sample 주석, 빈 테스트 skeleton을 정리합니다.
4. 의미 있는 리팩토링은 기능 변경과 섞이지 않게 최소 단위로 유지합니다.

## 출력

- 현재 코드베이스 스타일과 맞는 Kotlin/Compose 코드
- 필요 시 작은 테스트 fake/helper
- 정리된 import와 불필요 코드 제거 결과

## 점검

- 새 abstraction이 실제 중복을 줄이는가
- 테스트 없는 동작 변경이 남지 않았는가
- `qualityGateFast` 대상 명령을 실행했는가
