package kr.co.architecture.core.ui

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun BaseButton(
  modifier: Modifier = Modifier,
  @ColorRes buttonColorRes: Int,
  @StringRes buttonTextRes: Int,
  buttonTextStyle: TextStyle,
  border: BorderStroke? = null,
  onClick: () -> Unit = { }
) {
    val delayMillis by rememberSaveable { mutableLongStateOf(1000L) }
    var isClickable by rememberSaveable { mutableStateOf(true) }
    LaunchedEffect(isClickable) {
        if (!isClickable) {
          delay(delayMillis)
            isClickable = true
        }
    }

    Button(
        modifier = modifier,
        colors = ButtonColors(
            containerColor = colorResource(id = buttonColorRes),
            contentColor = colorResource(id = buttonColorRes),
            disabledContentColor = colorResource(id = buttonColorRes),
            disabledContainerColor = colorResource(id = buttonColorRes)
        ),
        shape = RoundedCornerShape(4.dp),
        border = border,
        onClick = {
            if (isClickable) {
                isClickable = false
                onClick()
            }
        }
    ) {
        Text(
            text = stringResource(id = buttonTextRes),
            style = buttonTextStyle
        )
    }
}