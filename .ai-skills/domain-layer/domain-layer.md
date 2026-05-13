---
name: domain-layer
description: core:domain, core:model, core:repository, core:network, core:database 경계를 추가하거나 수정할 때 사용하는 스킬.
---

# domain-layer

## 목적

Clean Architecture 의존 방향을 지키면서 기능의 domain/data 경계를 빠르게 추가합니다.

## 사용 시점

- UseCase, repository interface, domain model을 추가할 때
- API/DB DTO를 화면 모델과 분리해야 할 때
- feature ViewModel에서 사용할 domain 계약을 정리할 때

## 핵심 원칙

### 의존 경계

- `core:domain`은 Android, Retrofit, Room, Compose에 의존하지 않습니다.
- repository interface와 UseCase는 `core:domain`에 둡니다.

### 모델 위치

- `core:domain` 내부에서만 쓰는 model은 `core:domain`에 둡니다.
- feature, repository, mapper 등 여러 layer가 함께 쓰는 domain model은 `core:model`에 둡니다.
- DTO/API response와 Room Entity는 domain model로 직접 쓰지 않고 각 data module에서 mapper로 변환합니다.

### 구현 경계

- repository 구현체와 mapper는 `core:repository`에 둡니다.
- DTO/API는 `core:network`, Entity/DAO는 `core:database`에 둡니다.
- feature는 repository 구현체가 아니라 UseCase 또는 domain repository interface에 의존합니다.

## 절차

### 계약 정의

1. 필요한 model이 domain 내부 전용인지 여러 layer 공유 domain model인지 먼저 구분합니다.
2. domain 내부 전용 model은 `core:domain`, 여러 layer 공유 domain model은 `core:model`에 정의합니다.
3. `core:domain`에 repository interface를 만들고 필요한 suspend API를 선언합니다.
4. 한 책임만 가진 UseCase를 `core:domain`에 추가합니다.

### 데이터 구현

1. 외부 API가 필요하면 `core:network`에 DTO와 Retrofit API를 추가합니다.
2. 로컬 저장이 필요하면 `core:database`에 Entity/DAO를 추가합니다.
3. `core:repository`에서 DTO/Entity를 model로 변환하고 interface 구현체를 만듭니다.
4. Hilt binding을 추가해 feature ViewModel에서 UseCase를 주입합니다.

## 출력

- model
- repository interface
- UseCase
- repository implementation
- 필요한 DTO/Entity/mapper/Hilt binding

## 점검

- domain이 Android/Retrofit/Room/Compose 타입을 import하지 않는가
- DTO/Entity가 feature나 domain API로 직접 새지 않았는가
- UseCase가 여러 화면 책임을 한 파일에 섞지 않았는가
- ViewModel 테스트에서 fake repository나 fake UseCase로 검증할 수 있는가
