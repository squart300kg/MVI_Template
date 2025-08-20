package kr.co.architecture.core.ui

import kr.co.architecture.core.ui.util.UiText

// 아래 요소는 BaseBottomDialog, BaseCenterDialog에서 사용됨
interface TwoButtonStyle {
    val leftMessage: UiText
    val leftTextColor: Int
    val leftBackgroundColor: Int
    val leftBorderColor: Int
    val rightMessage: UiText
    val rightTextColor: Int
    val rightBackgroundColor: Int
    val rightBorderColor: Int
  }
  interface OneButtonStyle {
    val message: UiText
    val textColor: Int
    val backgroundColor: Int
    val borderColor: Int
  }