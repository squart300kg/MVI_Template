package kr.co.architecture.core.router.internal.navigator

/**
 * 화면 간 내비게이션을 정의하는 인터페이스.
 *
 * 구현체는 앱 내 라우팅(Compose Navigation 등) 또는 웹 브라우저 호출을 처리한다.
 */
interface Navigator {

  /**
   * 지정된 [route]로 화면 전환을 수행한다.
   *
   * @param route 이동할 대상 라우트
   * @param saveState 현재 화면의 상태를 저장할지 여부 (기본값: false)
   * @param launchSingleTop 동일한 화면이 스택 상단에 있을 경우 새로 생성하지 않고 재사용할지 여부 (기본값: false)
   */
  suspend fun navigate(
    route: Route,
    saveState: Boolean = false,
    launchSingleTop: Boolean = false
  )

  /**
   * 외부 또는 내부 웹뷰를 통해 지정된 [url]로 이동한다.
   *
   * @param url 이동할 웹 페이지 주소
   */
  suspend fun navigateWeb(url: String)

  /**
   * 현재 화면에서 뒤로 이동한다.
   *
   * 주로 NavController의 `popBackStack()` 또는 Activity의 `finish()`에 해당한다.
   */
  suspend fun navigateBack()
}
