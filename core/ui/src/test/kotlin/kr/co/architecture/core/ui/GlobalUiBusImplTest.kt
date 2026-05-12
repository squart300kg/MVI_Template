package kr.co.architecture.core.ui

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kr.co.architecture.core.model.ArchitectureSampleHttpFailure
import kr.co.architecture.core.ui.util.UiText
import kr.co.architecture.test.testing.util.MainDispatcherRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GlobalUiBusImplTest {

  @get:Rule val mainRule = MainDispatcherRule()

  private lateinit var bus: GlobalUiBusImpl

  @Before
  fun setup() {
    bus = GlobalUiBusImpl()
  }

  @Test
  fun `초기 로딩 상태는 false`() = runTest {
    assertFalse(GlobalUiBusImpl().loadingState.value)
  }

  @Test
  fun `로딩 증가 후 감소하면 loadingState가 true에서 false로 바뀐다`() = runTest {
    // 준비
    val job = bus.loadingState.launchIn(this)

    // 실행
    bus.setLoadingState(true)
    advanceUntilIdle()
    assertEquals(
      true,
      bus.loadingState.value
    )

    // 검증
    bus.setLoadingState(false)
    advanceUntilIdle()
    assertEquals(
      false,
      bus.loadingState.value
    )
    job.cancel()
  }

  @Test
  fun `로딩 증가 횟수가 감소 횟수보다 많은 상태에서 로딩을 감소시켰을 때 loadingState는 true이다`() = runTest {
    // 준비
    val job = bus.loadingState.launchIn(this)
    bus.setLoadingState(true)
    bus.setLoadingState(true)
    advanceUntilIdle()
    assertEquals(
      true,
      bus.loadingState.value
    )

    // 실행
    bus.setLoadingState(false)
    advanceUntilIdle()

    // 검증
    assertEquals(
      true,
      bus.loadingState.value
    )

    job.cancel()
  }

  @Test
  fun `로딩 감소 횟수가 증가 횟수보다 많은 상태에서 로딩을 증가시켰을 때 loadingState는 true이다`() = runTest {
    // 준비
    val job = bus.loadingState.launchIn(this)
    bus.setLoadingState(false)
    bus.setLoadingState(false)
    advanceUntilIdle()
    assertEquals(
      false,
      bus.loadingState.value
    )

    // 실행
    bus.setLoadingState(true)
    advanceUntilIdle()

    // 검증
    assertEquals(
      true,
      bus.loadingState.value
    )

    job.cancel()
  }

  @Test
  fun `showFailureDialog를 호출하면 failureDialog에서 다운스트림받는다`() = runTest {
    // 준비
    val job = bus.failureDialog.launchIn(this)

    // 실행
    bus.showFailureDialog(
      kotlin.Exception("HelloWorld")
    )
    advanceUntilIdle()

    // 검증
    assertNotNull(bus.failureDialog.value)

    job.cancel()
  }

  @Test
  fun `서버 에러 문자열은 표시 슬롯에서 PlainText로 전달된다`() = runTest {
    // 준비
    val job = bus.failureDialog.launchIn(this)

    // 실행
    bus.showFailureDialog(
      ArchitectureSampleHttpFailure.Error(
        code = "400",
        message = "Bad Request"
      )
    )
    advanceUntilIdle()

    // 검증
    val dialog = bus.failureDialog.value
    assertEquals(
      UiText.PlainText("400"),
      dialog?.titleMessage
    )
    assertEquals(
      UiText.PlainText("Bad Request"),
      dialog?.contentMessage
    )

    job.cancel()
  }

  @Test
  fun `dismissDialog를 호출하면 failureDialog에서 null을 받는다`() = runTest {
    // 준비
    val job = bus.failureDialog.launchIn(this)
    bus.showFailureDialog(
      kotlin.Exception("HelloWorld")
    )
    advanceUntilIdle()
    assertNotNull(bus.failureDialog.value)

    // 실행
    bus.dismissDialog()
    advanceUntilIdle()

    // 검증
    assertNull(bus.failureDialog.value)

    job.cancel()
  }
}
