---
name: complete-task-and-final-check
description: Android 기능 추가/수정/정리 작업을 끝낼 때 빌드, 테스트, 하네스 정합성을 확인하는 스킬.
---

# complete-task-and-final-check

## 목적

완료 보고 전에 빌드와 검증이 통과하고, 변경 범위가 정리됐는지 확인합니다.

## 사용 시점

- 기능 구현, 리팩토링, 문서/skill 변경을 마무리할 때
- 커밋 또는 제출 직전 최종 상태를 확인할 때

## 핵심 원칙

### 검증 기준

- debug build compile 실패 상태로 작업을 끝내지 않습니다.
- 변경한 동작에는 단위 테스트를 추가하거나 기존 테스트를 갱신합니다.
- 문서/skill을 바꿨으면 sync와 consistency 검증을 함께 실행합니다.
- feature navigation/global UI 경계는 `verifyArchitectureRules`로 확인합니다.

### 완료 보고

- 완료 기록에는 현재 작업 내용, 수정 파일, 실행한 검증만 적고 다른 로컬 프로젝트나 참고한 외부 작업공간의 내용은 적지 않습니다.
- 실행하지 못한 검증은 이유와 남은 위험을 최종 보고에 적습니다.

## 절차

1. `git status --short`로 의도한 변경만 있는지 확인합니다.
2. 미사용 import, 빈 skeleton 테스트, 불필요한 sample 주석을 정리합니다.
3. [.ai-skills](../../.ai-skills) 또는 [AGENTS.md](../../AGENTS.md)를 바꿨다면 `./scripts/sync-harness-docs.sh copy`를 실행합니다.
4. `./gradlew qualityGateFast`를 실행합니다.
5. 필요하면 `./gradlew :app:assembleDebug`로 APK assemble까지 확인합니다.

## 출력

- 변경 요약
- 실행한 검증 명령과 성공/실패 결과
- 실패 또는 생략한 항목의 이유

## 점검

- debug compile이 성공했는가
- 관련 unit test가 성공했는가
- Android lint 또는 생략 사유가 확인됐는가
- architecture rule 검증이 통과했는가
- 하네스 mirror가 원본과 일치하는가
