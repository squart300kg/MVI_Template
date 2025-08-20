package kr.co.kurly.benchmarks

import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until

fun UiDevice.waitAndFindObject(selector: BySelector, timeout: Long = 10_000): UiObject2 {
  if (!wait(Until.hasObject(selector), timeout)) {
    throw AssertionError("Element not found on screen in ${timeout}ms (selector=$selector)")
  }

  return findObject(selector)
}

fun UiDevice.fling(element: UiObject2, direction: Direction) {
  // Set some margin from the sides to prevent triggering system navigation
  element.setGestureMargin(displayWidth / 5)

  element.fling(direction)
  waitForIdle()
}