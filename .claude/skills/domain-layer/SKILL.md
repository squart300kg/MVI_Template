---
name: domain-layer
description: MVI Template의 core:domain, core:model, core:repository 경계를 추가하거나 수정할 때 사용하는 스킬.
---

# domain-layer

## 목적

Clean Architecture의 의존 방향을 지키면서 domain logic을 빠르게 추가합니다.

## 사용 시점

- UseCase를 추가하거나 수정할 때
- repository interface 또는 구현체를 추가할 때
- network/database model을 domain model과 분리할 때

## 핵심 원칙

- `core:domain`은 Android, Retrofit, Room에 의존하지 않습니다.
- repository interface는 `core:domain`에 둡니다.
- repository 구현체와 mapper는 `core:repository`에 둡니다.
- layer 간 공유 model은 `core:model`에 둡니다.
- UseCase는 한 파일에 한 책임만 둡니다.

## 절차

1. UI가 필요한 데이터를 `core:model` model로 정의합니다.
2. domain repository interface를 정의합니다.
3. 필요한 UseCase를 `core:domain`에 추가합니다.
4. 구현체를 `core:repository`에 만들고 Hilt binding을 추가합니다.
5. API가 필요하면 `core:network` DTO와 `RemoteApi`를 추가합니다.

## 출력

- domain repository interface
- UseCase
- repository implementation
- 필요한 model/DTO/mapper

## 점검

- domain이 implementation module을 참조하지 않는가
- DTO가 UI나 domain에 직접 새지 않았는가
- 테스트에서 fake repository로 UseCase/ViewModel을 검증할 수 있는가
