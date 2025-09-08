## 목차
1. 빌드 환경
2. 아키텍처 구조도
3. 그 외, API 오류
4. 앱 시연 영상

## 1. 빌드 환경
해다 과제가 실행된 빌드 환경을 의미합니다. 만약 과제 빌드에 문제가 있을 시, 아래 빌드 환경을 참고하시면 좋습니다.
![BuildConfig.png](readme-img/BuildConfig.png)

## 2. 아키텍처 구조도
해당 과제가 실행된 아키텍처 구조입니다. 

- App앱 : **Android공식 아키텍처**를 사용했으며, `UI Layer`, `Domain Layer`, `Data Layer`에 따라 앱을 구조화 하였으며, 각 Layer에는 해당 `Model`과 `Mapper`들이 존재합니다. (eg., `*.DomainResponse`, `toMapperToDto()`/`toMapperToUiModel()`)
- custom-image-loader : picsum API 이미지를 로딩하기 위한 이미지 로딩 라이브러리로, 302응답을 받으면 CDN서버의 이미지를 로딩합니다.
- custom-http-client : http1.1 스펙의 네트워크 통신 라이브러리입니다. 302응답 뿐만 아니라, 일반적인 200응답도 함께 처리합니다.

![Architecture.png](readme-img/Architecture.png)

## 3. custom-image-loader
![Architecture.png](readme-img/custom-image-loader.png)

## 4. custom-http-client
ㄴ

## 3. 그 외, API 오류
검색어를 `f`로 설정 후, 필터를 `최신순`으로 설정할 경우, `L'OFFICIER...(A)`, `L'OFFICIER...(B)`이름의 도서 2권이 보입니다.
해당 도서는 `isbn`이 동일하여 북마크 클릭 시, 동시에 체크/해제되는 점, 과제 검토시 참고 부탁드립니다.

[//]: # (![BenchmarkResult.png]&#40;readme-img/apiError.png&#41;)

## 4. 앱 시연 영상
### 1). 주요 기능 시연 ([영상 보기](readme-img/main.mp4))
- 첫 로딩 : 이미지를 네트워크 로딩 및 메모리/디스크 캐싱
  - 앱을 나가지 않고, 위로 스크롤 : 메모리 캐싱된 이미지 로딩
  - 앱을 나간 후 (백스택 삭제) 재접속 : 디스크 캐싱된 이미지 로딩

### 2). 부가 기능 시연 - 다양한 상황(NetworkConnection, SocketTimeout, API Error)에서의 에러 대응 ([영상 보기](readme-img/sub2.mp4))
- `hello world`에러의 경우, API 통신 흐름 중간에 임의로 발생시킨 에러입니다.
- `API Key`에러의 경우, API키를 잘못된키로 하드코딩하여 임의로 발생시킨 에러입니다.
- 그 외, 에러 상황을 분기처리했으며, 예상치 못한 에러는 `UNKNOWN`에러로 발생시켰습니다.
