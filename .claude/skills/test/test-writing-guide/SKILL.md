---
name: test-writing-guide
description: ViewModel, UseCase, repository mapping 단위 테스트를 추가하거나 수정할 때 사용하는 스킬.
---

# test-writing-guide

## 목적

과제 요구사항과 회귀 조건이 테스트 이름과 assertion에 직접 드러나게 합니다.

## 사용 시점

- ViewModel state/effect 테스트를 작성할 때
- UseCase 또는 mapper 테스트를 작성할 때
- 버그 수정 후 회귀 테스트를 추가할 때

## 핵심 원칙

- 테스트 함수명은 가능하면 한국어 백틱 문장으로 작성합니다.
- ViewModel 테스트는 state와 side effect를 검증합니다.
- 단순 의존성은 mock보다 fake object를 우선합니다.
- coroutine 테스트는 `MainDispatcherRule`을 사용합니다.
- 테스트는 요구사항을 구분 가능한 case로 나눕니다.

## 절차

1. 사용자 시나리오를 성공, 빈 데이터, 실패, edge case로 나눕니다.
2. 필요한 fake repository/use case를 준비합니다.
3. event를 전달하고 state/effect 변화를 검증합니다.
4. mapper는 입력과 출력만 검증합니다.
5. 관련 module의 `testDebugUnitTest`를 실행합니다.

## 출력

- 단위 테스트
- 필요한 fake/helper
- 실행한 테스트 명령 결과

## 점검

- 테스트 이름만 봐도 요구사항이 드러나는가
- assertion이 테스트 이름의 약속만 검증하는가
- 외부 시간, dispatcher, network에 의존하지 않는가
