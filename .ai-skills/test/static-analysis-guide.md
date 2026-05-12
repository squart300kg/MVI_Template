---
name: static-analysis-guide
description: qualityGateFast, Android lint, 하네스 정합성 검증을 추가하거나 수정할 때 사용하는 스킬.
---

# static-analysis-guide

## 목적

과제 중 반복 실행 가능한 빠른 품질 최저선을 유지합니다.

## 사용 시점

- `qualityGateFast` task를 수정할 때
- `verifyHarnessConsistency` script를 수정할 때
- Android lint나 test gate 범위를 조정할 때

## 핵심 원칙

- 빠른 기본 게이트는 compile, unit test, lint, harness consistency입니다.
- detekt/ktlint 커스텀 룰은 필요해질 때만 추가합니다.
- 게이트가 느려지면 과제 중 사용 빈도가 떨어지므로 기본 경로는 빠르게 유지합니다.
- 문서 규칙과 실제 검증 스크립트가 어긋나지 않아야 합니다.

## 절차

1. 새 규칙이 script, Gradle task, lint 중 어디에 맞는지 정합니다.
2. 하네스 문서 정합성은 shell script에 둡니다.
3. Android 코드 품질은 Gradle compile/test/lint task에 둡니다.
4. `qualityGateFast`에 포함한 뒤 로컬에서 실행합니다.
5. skill 문서에 명령과 기준을 갱신합니다.

## 출력

- 갱신된 quality task 또는 verification script
- 갱신된 skill/runbook
- 실행 결과

## 점검

- `./gradlew qualityGateFast`가 한 번에 실행되는가
- 실패 메시지가 다음 행동을 알려주는가
- sync 결과물이 원본과 일치하는가
