package kr.co.architecture.app

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule

import org.junit.Test

import org.junit.Rule

class ExampleInstrumentedTest {

  @get:Rule
  val composeTestRule = createAndroidComposeRule<ComponentActivity>()

  @Test
  fun `compose rule을 생성할 수 있다`() {
  }
}
