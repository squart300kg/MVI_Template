---
name: integrate-server-api-guide
description: Retrofit API와 repository를 통해 서버 데이터를 feature 화면에 연결할 때 사용하는 스킬.
---

# integrate-server-api-guide

## 목적

서버 응답을 DTO, repository, use case, UI model로 분리해 연결합니다.

## 사용 시점

- 새 REST API를 추가할 때
- 기존 API 응답을 화면에 바인딩할 때
- mock/local 데이터에서 실제 network data로 전환할 때

## 핵심 원칙

- Retrofit endpoint와 response DTO는 `core:network`에 둡니다.
- 화면이 쓰는 model은 DTO가 아니라 `core:model` 또는 feature UiModel입니다.
- error 변환은 `core:network`/`core:repository` 경계에서 처리합니다.
- API key나 base URL은 local file 필수값으로 만들지 않고 env/Gradle property/default를 지원합니다.

## 절차

1. API schema를 확인하고 DTO를 추가합니다.
2. `RemoteApi`에 endpoint를 추가합니다.
3. repository implementation에서 DTO를 model로 변환합니다.
4. domain repository interface와 UseCase를 통해 ViewModel에 주입합니다.
5. 성공, 빈 응답, 실패 케이스를 테스트합니다.

## 출력

- DTO와 endpoint
- repository mapping
- UseCase
- ViewModel/UI binding
- 관련 테스트

## 점검

- DTO가 feature UI에 직접 노출되지 않았는가
- API 실패가 전역 에러 처리 또는 화면 상태로 명확히 전달되는가
- local secret 없이 debug build가 가능한가
