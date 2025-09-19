package kr.co.architecture.core.ui

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import kr.co.architecture.core.ui.theme.LocalCustomColors
import kr.co.architecture.core.ui.theme.LocalCustomShapes
import kr.co.architecture.core.ui.theme.LocalCustomTypography
import kr.co.architecture.core.ui.util.noRippledClickable
import kr.co.architecture.core.ui.R as coreUiR

@Composable
fun NoMaterial3SearchBarTextField(
  modifier: Modifier = Modifier,
  onValueChange: (String) -> Unit,
  placeholder: String = "",
  onSearch: () -> Unit = {},
  onClickedErase: () -> Unit = {}
) {
  val focusManager = LocalFocusManager.current
  val keyboard = LocalSoftwareKeyboardController.current
  val typography = LocalCustomTypography.current
  var query by rememberSaveable { mutableStateOf("") }
  BasicTextField(
    modifier = modifier
      .clip(LocalCustomShapes.current.shape)
      .background(LocalCustomColors.current.searchBackground)
      /**
       * 과제 요구사항에 width를 335dp로 설정하라 나와있지만
       * 이는 `Configuration Change`에 대응을 못한다 판단되어 진행하지 않았습니다.
       */
      .height(54.dp)
      .padding(vertical = 15.dp)
      .padding(start = 20.dp, end = 18.dp),
    value = query,
    onValueChange = { query = it; onValueChange(query) },
    singleLine = true,
    textStyle = typography.searchContents,
    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
    keyboardActions = KeyboardActions(
      onSearch = {
        onSearch()
        focusManager.clearFocus(force = true)
        keyboard?.hide()
      }
    ),
    decorationBox = { innerTextField ->
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        // Leading icon
        Image(
          modifier = Modifier
            .size(24.dp),
          painter = painterResource(coreUiR.drawable.icon_search),
          contentDescription = null,
        )

        // Input + PlaceHolder
        Box(
          modifier = Modifier
            .weight(1f),
          contentAlignment = Alignment.CenterStart
        ) {
          if (query.isEmpty()) {
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
              onClick = { query = ""; onClickedErase() }
            ),
          contentAlignment = Alignment.Center
        ) {
          Image(
            painter = painterResource(coreUiR.drawable.icon_delete),
            contentDescription = null
          )
        }
      }
    }
  )
}