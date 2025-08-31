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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kr.co.architecture.core.ui.enums.BaseSortUiEnum
import kr.co.architecture.core.ui.enums.asString
import kr.co.architecture.test.testing.ui.SearchTags
import kr.co.architecture.core.ui.R as coreUiR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchHeader(
  modifier: Modifier = Modifier,
  onQueryChange: (String) -> Unit,
  onSearch: (String) -> Unit = {},
  trailingActions: @Composable RowScope.() -> Unit
) {
  val focusManager = LocalFocusManager.current
  val keyboard = LocalSoftwareKeyboardController.current

  Column(modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
    var query by rememberSaveable { mutableStateOf("") }
    OutlinedTextField(
      modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)
        .testTag(SearchTags.HeaderTextField),
      value = query,
      onValueChange = { query = it; onQueryChange(it) },
      placeholder = { Text(text = stringResource(coreUiR.string.placeHint)) },
      leadingIcon = { Icon(Icons.Default.Search, null) },
      trailingIcon = {
        if (query.isNotEmpty()) {
          IconButton(
            modifier = Modifier.testTag(SearchTags.HeaderClear),
            onClick = { query = ""; onQueryChange("") }
          ) { Icon(Icons.Outlined.Close, stringResource(coreUiR.string.erase)) }
        }
      },
      singleLine = true,
      shape = RoundedCornerShape(24.dp),
      keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
      keyboardActions = KeyboardActions(
        onSearch = {
          onSearch(query)
          focusManager.clearFocus(force = true)
          keyboard?.hide()
        }
      )
    )

    Spacer(Modifier.height(12.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Spacer(Modifier.weight(1f))
      trailingActions()
    }
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
      modifier = Modifier.testTag(SearchTags.SortChip),
      onClick = { expanded = true },
      label = { Text(selected.asString()) },
      leadingIcon = { Icon(Icons.Outlined.KeyboardArrowDown, contentDescription = null) }
    )
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
      options.forEach { option ->
        DropdownMenuItem(
          modifier = Modifier
            .testTag(SearchTags.sortItem(option.toString())),
          text = { Text(option.asString()) },
          onClick = { onChange(option); expanded = false }
        )
      }
    }
  }
}