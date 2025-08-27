package kr.co.architecture.core.ui

import android.widget.TextView
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat

@Composable
fun HtmlText(
  modifier: Modifier = Modifier,
  inputText: String,
  maxLine: Int = 1,
  style: TextStyle = LocalTextStyle.current.copy(color = Color.Black)
) {
  AndroidView(modifier = modifier, factory = { context ->
    TextView(context).apply {
      this.text = HtmlCompat.fromHtml(inputText, HtmlCompat.FROM_HTML_MODE_COMPACT)
      this.setTextColor(Color.Black.toArgb())
      this.textSize = style.fontSize.value
    }
  }, update = { textView ->
    textView.run {
      text = HtmlCompat.fromHtml(inputText, HtmlCompat.FROM_HTML_MODE_COMPACT)
      maxLines = maxLine
      ellipsize = android.text.TextUtils.TruncateAt.END
    }
  })
}
