package kr.co.architecture.feature.detail

import android.widget.TextView
import androidx.databinding.BindingAdapter
import kr.co.architecture.core.ui.util.UiText
import kr.co.architecture.core.ui.util.asString

@BindingAdapter("textUi")
fun TextView.bindTextUi(text: UiText?) {
  if (text == null) {
    setText("")
  } else {
    setText(text.asString(context))
  }
}