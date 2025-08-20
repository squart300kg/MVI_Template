package kr.co.architecture.core.ui

import android.widget.TextView
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat

@Composable
fun HtmlText(
  modifier: Modifier = Modifier, 
  text: String, 
  style: TextStyle = LocalTextStyle.current
) {
  AndroidView(modifier = modifier, factory = { context ->
    TextView(context).apply {
      this.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_COMPACT)
      this.setTextColor(style.color.toArgb())
      this.textSize = style.fontSize.value
    }
  }, update = { textView ->
    textView.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_COMPACT)
  })
}
