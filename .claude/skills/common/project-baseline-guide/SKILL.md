---
name: project-baseline-guide
description: MVI Template에서 Android 기능을 추가하거나 수정할 때 모듈 구조, 명령, 아키텍처 기준을 확인하는 스킬.
---

# project-baseline-guide

## 목적

과제 중 빠르게 구현하더라도 현재 Clean Architecture + MVI 골격을 유지합니다.

## 사용 시점

- 새 feature, use case, repository, API를 추가할 때
- 기존 feature의 화면, ViewModel, domain/data 흐름을 수정할 때
- 작업 마감 전 기본 검증 명령을 고를 때

## 핵심 원칙

- feature는 `*Contract.kt`, `*ViewModel.kt`, `*Screen.kt`를 함께 유지합니다.
- repository interface는 `core:domain`, 구현체는 `core:repository`에 둡니다.
- DTO는 `core:network`, 공유 model은 `core:model`에 둡니다.
- 공통 UI와 MVI base type은 `core:ui`를 우선 사용합니다.
- navigation은 typed route, `NavGraphBuilder` 확장, ViewModel `navigateTo`/`navigateBack`/`navigateWeb` 패턴을 유지합니다.
- 전역 loading/error는 feature state가 아니라 `BaseViewModel.launchWithCatching`과 `GlobalUiBus` 경로를 사용합니다.

## 절차

1. 변경할 화면과 데이터 흐름이 어느 모듈에 속하는지 먼저 정합니다.
2. UI state/event/effect를 Contract에 정의합니다.
3. ViewModel은 event 처리, state 갱신, side effect 발생만 담당하게 둡니다.
4. 데이터가 필요하면 domain use case와 repository interface부터 추가합니다.
5. 화면 이동과 loading/error가 starter 공통 경로를 쓰는지 확인합니다.
6. 작업 중 빠른 확인은 `./gradlew :app:compileDebugKotlin`을 실행합니다.
7. 마감 전에는 `./gradlew qualityGateFast`를 실행합니다.

## 출력

- 변경된 feature/domain/data 코드
- 필요한 테스트
- 실행한 Gradle 검증 결과

## 점검

- feature가 현재 module dependency 방향을 깨지 않았는가
- domain이 Android, Retrofit, Room 구현 타입에 의존하지 않는가
- feature Composable이 `NavHostController`, `BaseProgressBar`, 전역 `BaseCenterDialog`를 직접 쓰지 않는가
- debug build가 컴파일되는가
- skill이나 runbook을 바꿨다면 sync를 실행했는가
