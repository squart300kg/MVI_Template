# 📱 MVI Template

# 🚀 개요
이 Repository는 크게 2가지 Branch로 구성돼있습니다.
- **Master Branch** : 언제든 새로운 앱을 구축할 수 있는 MVI 템플릿
- **Class101 Branch** : class101 강의 설명을 위한 샘플코드로, 실제 서비스 중인, `BuddyStock` App의 '알림센터' 부분화면을 추출 후, MVI 패턴에 맞게 추가 수정 함.

## BuddyStock App
![alt text](<./readme_img/buddystock.png>)

- [버디스탁 공식 홈페이지](https://buddystock.pickstudio.io/)
- [버디스탁 구글 스토어](https://play.google.com/store/apps/details?id=com.pickstudio.buddystock)

# 🏛️ Architecture?
`Main Branch`, `Class101 Branch`모두, `MVI` 패턴과 `Google의 App Architecture Guide` 결합하여 만들어졌습니다.

## 👉 1. Now In Android Architecture?
Google에서 Android App 구축 시, 권장하는 아키텍처 구조로 일명, `Google App Architecture`라고 불립니다. 기존, `Clean Architecture`와 다른 점은, `Dmoain Layer`를 선택적으로 구현할 수 있고, 의존성 관점에서 독립적으로 존재하지 않는다는 점입니다.

 ![alt text](<./readme_img/apparc.png>) 

 App Architecture구조는 `UI Layer`, `Domain Layer`, `Data Layer`3가지로 구성돼 있습니다. ([Google App Architecture Guide](https://developer.android.com/topic/architecture?hl=ko))
  
  - UI Layer ([UI Layer Guide](https://developer.android.com/topic/architecture/ui-layer?hl=ko)): 
    - UI Element(Activity, Fragment, Compose)
    - Class State Holder(UiState in ViewModel)
  - Domain Layer ([Domain Layer Guide](https://developer.android.com/topic/architecture/domain-layer?hl=ko)) : 
    - 순수 kotlin로직으로 구성돼야 함
    - Computational한 로직 정의
    - 여럿 ViewModel의 공통 로직 정의
  - Data Layer ([Data Layer Guide](https://developer.android.com/topic/architecture/data-layer?hl=ko)): 
    - Single Source Of Truth원칙
    - UI Layer, Domain Layer는 데이터 출처를 신경쓰지 않은 채, Data Layer의 Repository(interface)만 바라본다.
    - Repository는 여러 데이터 출처(RestAPI, WebSocket, Room, DataStore...)로부터 데이터를 구성할 수 있으며, 이는 캡슐화된다.

**ps. 추가로, 아래 블로그 글을 통해, Android App Architecture의 구현 예시를 공부할 수 있습니다.**
- [안드로이드 Google App Architecture에 대하여1](https://velog.io/@squart300kg/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-Clean-Architecture%EC%97%90-%EB%8C%80%ED%95%98%EC%97%AC)
- [안드로이드 Google App Architecture에 대하여2](https://velog.io/@squart300kg/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-Clean-Architecture%EC%97%90-%EB%8C%80%ED%95%98%EC%97%AC2)
- [안드로이드 Google App Architecture에 대하여3](https://velog.io/@squart300kg/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-Clean-Architecture%EB%9E%80-%EB%AC%B4%EC%97%87%EC%9D%B8%EA%B0%803)

## 👉 2. MVI Architecture?
  ![alt text](<./readme_img/mvi.png>)
  - Model : ViewModel에서 구축 된 3가지 모델체계(`UiState`, `UiEvent`, `UiSideEffect`)를 통해, 'UI 상태', 'UI 이벤트', 'UI 부수효과'를 체계적으로 관리한다.
  - View : 말 그대로 View를 의미한다.(Activity, Fragment, Compose UI)
  - Intent : Android Framework의 'Intent'객체가 아닌, 사용자의 '의도'를 의미한다. 

**ps. 추가로, 아래 블로그 글을 통해, 아키텍처 패턴의 변천사, MVI 패턴을 공부할 수 있습니다.**
- [Android MVI패턴이란](https://velog.io/@squart300kg/Android-MVI%ED%8C%A8%ED%84%B4%EC%9D%B4%EB%9E%80)
- [[발표자료] 안드로이드 아키텍처 변천사](https://velog.io/@squart300kg/%EB%B0%9C%ED%91%9C%EC%9E%90%EB%A3%8C-%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98-%EB%B3%80%EC%B2%9C%EC%82%AC)

## 🏗️ Project Structure
해당 프로젝트는 Google App Architecture + MVI 패턴을 기반으로,
app / core / feature 세 가지 축으로 구성됩니다.
```bash
project-root
 ├─ app
 │   └─ src/main/kotlin/kr/co/architecture
 │        ├─ app/              # 앱 전역 View & Composable UI
 │        ├─ benchmarks/       # 성능 측정 및 Benchmark 관련 코드
 │        ├─ build-logic/      # Gradle 빌드 설정 및 커스텀 플러그인 관리
 │        ├─ testing/          # 공용 테스트 유틸 및 테스트 관련 코드
 │        ├─ core/             # 앱의 공통 핵심 모듈
 │        │    ├─ common/      # 상수, 확장 함수, 공통 유틸
 │        │    ├─ domain/      # UseCase, 비즈니스 규칙 정의
 │        │    ├─ model/       # Data/Domain 계층의 데이터 모델
 │        │    ├─ network/     # 네트워크 통신 (API, Retrofit, DTO 등)
 │        │    ├─ repository/  # 데이터 소스 추상화 및 구현
 │        │    ├─ ui/          # Core 레벨에서 재사용 가능한 UI 컴포넌트
 │        │    └─ router/      # 네비게이션 라우팅 관리
 │        ├─ feature/          # 개별 기능 모듈 (화면 단위)
 │        │    ├─ first/       # 첫 번째 기능 (예: 홈, 리스트 등)
 │        │    ├─ second/      # 두 번째 기능 (예: 검색, 설정 등)
 │        │    ├─ detail/      # 상세 화면 관련 기능
 │        │    ├─ network/     # 해당 feature 전용 네트워크 통신
 │        │    ├─ repository/  # 해당 feature 전용 Repository
 │        │    ├─ ui/          # 해당 feature 전용 UI (Composable 등)
 │        │    └─ router/      # 해당 feature 전용 라우팅
 └
 ```

###  🔎 각 디렉토리 역할 요약
- app/ : 앱의 EntryPoint. MainActivity, 전역 Theme, Root NavHost 등 앱 실행에 필요한 최상위 요소들을 포함합니다.

- core/ : 앱 전반에서 재사용 가능한 로직과 컴포넌트를 모아둔 공통 모듈입니다.
  - domain/은 UseCase 중심의 비즈니스 규칙 정의
  - repository/는 데이터 소스 추상화
  - ui/는 여러 feature에서 공통으로 쓰이는 UI 요소 제공
  - router/는 화면 이동 책임을 담당 (NavController 의존성 역전 효과)

- feature/ : 화면 단위로 나뉜 독립적인 기능 모듈입니다. 각 모듈은 자체적으로 ViewModel, UiState, UiEvent, UiSideEffect를 포함하여 MVI 구조를 유지합니다. 따라서 Feature 단위로 추가/삭제/수정이 용이합니다.

- benchmarks/ : Jetpack Benchmark 라이브러리를 통한 성능 측정 코드.

- build-logic/ : Convention Plugin을 통한 빌드 스크립트 모듈화.

- testing/ : Fake Repository, Stub API, CoroutineTestRule 등 테스트 편의를 위한 코드 모음.

 ## 🏗️ (추가) /core/router
 ![alt text](<./readme_img/nav.png>)

 >
 출처 : [DroidKnights APP Repository의 readme.md](https://github.com/droidknights/DroidKnightsApp?tab=readme-ov-file#q-view%EC%97%90%EB%8F%84-api-%EB%AA%A8%EB%93%88%EC%9D%B4-%EC%9E%88%EB%8D%98%EB%8D%B0%EC%9A%94)

 Droid Knights앱에 있는 Navigation 모듈로, 각 `*.Screen.kt`에 대응하는 `*.ViewModel.kt`에서 화면 이동이 가능합니다. 이로써 `NavHostController`가 존재하는 root까지 람다함수를 전파하지 않아도 화면이동이 가능하며, Composable 함수들의 불필요 람다가 제거되고, 코드가 깔끔해진다는 장점이 있습니다.

 >> 
 **[주의사항]** : `Navigator`, `InternalNavigator` 모듈은 `@ActivityRetainedScope`로 지정되어 있고, 이는 Single Activity기반, Navigation을 사용한 프로젝트에 적합합니다. 만약, Multi-Activity기반 프로젝트일 경우, `NavigatorImpl`객체가 여럿 생성되어 화면 이동에 문제가 생길 수 있습니다. 따라서 scope를 `@SingletonComponent`로 변경 등의 방법으로 모듈 수정이 필요합니다.
