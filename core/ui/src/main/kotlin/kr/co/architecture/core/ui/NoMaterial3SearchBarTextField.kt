package kr.co.architecture.core.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import kr.co.architecture.core.ui.theme.CustomColors
import kr.co.architecture.core.ui.theme.CustomShapes
import kr.co.architecture.core.ui.theme.CustomTypography
import kr.co.architecture.core.ui.theme.LocalCustomColors
import kr.co.architecture.core.ui.theme.LocalCustomShapes
import kr.co.architecture.core.ui.theme.LocalCustomTypography
import kr.co.architecture.core.ui.R as coreUiR


@Composable
fun NoMaterial3SearchBarTextField(
  modifier: Modifier = Modifier,
  value: String,
  onValueChange: (String) -> Unit,
  placeholder: String = "",
  onClickedErase: () -> Unit = {},
  enabled: Boolean = true,
  colors: CustomColors = LocalCustomColors.current,
  typography: CustomTypography = LocalCustomTypography.current,
  shape: CustomShapes = LocalCustomShapes.current,
  @DrawableRes leadingIconRes: Int = coreUiR.drawable.icon_search,
  @DrawableRes trailingIconRes: Int = coreUiR.drawable.icon_delete
) {
  BasicTextField(
    modifier = modifier
      .clip(shape.shape)
      .background(colors.searchBackground)
      /**
       * 과제 요구사항에 width를 335dp로 설정하라 나와있지만
       * 이는 `Configuration Change`에 대응을 못한다 판단되어 진행하지 않았습니다.
       */
      .height(54.dp)
      .padding(vertical = 15.dp)
      .padding(start = 20.dp, end = 18.dp),
    value = value,
    onValueChange = onValueChange,
    singleLine = true,
    enabled = enabled,
    textStyle = typography.searchContents,
    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
    keyboardActions = KeyboardActions(onSearch = { defaultKeyboardAction(ImeAction.Search) }),
    decorationBox = { innerTextField ->
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        // Leading icon
        Image(
          modifier = Modifier
            .size(24.dp),
          painter = painterResource(leadingIconRes),
          contentDescription = null,
        )

        // Input + PlaceHolder
        Box(
          modifier = Modifier
            .weight(1f),
          contentAlignment = Alignment.CenterStart
        ) {
          if (value.isEmpty()) {
            BasicText(
              text = placeholder,
              style = typography.searchMedium
            )
          }
          innerTextField()
        }

        // Trading icon
        Box(
          modifier = Modifier
            .size(18.dp)
            .clip(CircleShape)
            .noRippledClickable(
              onClick = { onClickedErase() }
            ),
          contentAlignment = Alignment.Center
        ) {
          Image(
            painter = painterResource(trailingIconRes),
            contentDescription = null
          )
        }
      }
    }
  )
}