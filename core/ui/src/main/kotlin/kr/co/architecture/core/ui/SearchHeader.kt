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
import kotlinx.collections.immutable.ImmutableList
import kr.co.architecture.core.ui.enums.BaseSortUiEnum
import kr.co.architecture.core.ui.enums.asString
import kr.co.architecture.core.ui.R as coreUiR

data class SearchHeaderUiModel(
  val query: () -> String = {""}
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchHeader(
  modifier: Modifier = Modifier,
  uiModel: SearchHeaderUiModel,
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
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalAlignment = Alignment.CenterVertically,
      content = {
        Spacer(Modifier.weight(1f))
        trailingActions()
      }
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortMenuChip(
  selected: BaseSortUiEnum,
  options: ImmutableList<BaseSortUiEnum>,
  onChange: (BaseSortUiEnum) -> Unit
) {
  var expanded by rememberSaveable { mutableStateOf(false) }
  Box {
    AssistChip(
      onClick = { expanded = true },
      label = { Text(selected.asString()) },
      leadingIcon = { Icon(Icons.Outlined.KeyboardArrowDown, contentDescription = null) }
    )
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
      options.forEach { opt ->
        DropdownMenuItem(
          text = { Text(opt.asString()) },
          onClick = { onChange(opt); expanded = false }
        )
      }
    }
  }
}
