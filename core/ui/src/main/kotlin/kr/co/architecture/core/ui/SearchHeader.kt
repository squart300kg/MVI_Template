package kr.co.architecture.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.AssistChip
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import kr.co.architecture.core.ui.enums.SortUiEnum
import kr.co.architecture.core.ui.R as coreUiR

data class SearchHeaderUiModel(
  val query: () -> String = {""},
  val sort: SortUiEnum = SortUiEnum.ACCURACY
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchHeader(
  modifier: Modifier = Modifier,
  uiModel: SearchHeaderUiModel,
  leftLabelText: String? = null,
  onQueryChange: (String) -> Unit,
  onSearch: () -> Unit,
  trailingActions: @Composable RowScope.() -> Unit
) {
  val focusManager = LocalFocusManager.current
  val keyboardController = LocalSoftwareKeyboardController.current

  Column(modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {

    OutlinedTextField(
      modifier = Modifier.fillMaxWidth().height(60.dp),
      value = uiModel.query(),
      onValueChange = onQueryChange,
      placeholder = { Text(text = stringResource(coreUiR.string.placeHint)) },
      leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
      trailingIcon = {
        if (uiModel.query().isNotEmpty()) {
          IconButton(onClick = { onQueryChange("") }) {
            Icon(Icons.Outlined.Close, contentDescription = stringResource(coreUiR.string.erase))
          }
        }
      },
      singleLine = true,
      shape = RoundedCornerShape(24.dp),
      keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
      keyboardActions = KeyboardActions(
        onSearch = {
          onSearch()
          focusManager.clearFocus(force = true)
          keyboardController?.hide()
        }
      )
    )

    Spacer(Modifier.height(12.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // 좌측 라벨
      Text(
        text = leftLabelText ?: stringResource(uiModel.sort.resId),
        modifier = Modifier.weight(1f)
      )

      // 우측 액션칩들 (필터/정렬 등 원하는 만큼)
      Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = trailingActions
      )
    }
  }
}

@Composable
fun SortMenuChip(
  selected: SortUiEnum,
  onChange: (SortUiEnum) -> Unit,
  label: String = stringResource(coreUiR.string.sort)
) {
  var expanded by rememberSaveable { mutableStateOf(false) }
  Box {
    AssistChip(
      onClick = { expanded = true },
      label = { Text(label) },
      leadingIcon = { Icon(Icons.Outlined.KeyboardArrowDown, contentDescription = null) }
    )
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
      SortUiEnum.entries.forEach { option ->
        DropdownMenuItem(
          text = { Text(stringResource(option.resId)) },
          onClick = { onChange(option); expanded = false }
        )
      }
    }
  }
}

@Composable
fun FilterChip(
  onClick: () -> Unit,
  label: String = stringResource(coreUiR.string.filter)
) {
  AssistChip(
    onClick = onClick,
    label = { Text(label) },
    leadingIcon = { Icon(Icons.Outlined.Menu, contentDescription = null) }
  )
}
