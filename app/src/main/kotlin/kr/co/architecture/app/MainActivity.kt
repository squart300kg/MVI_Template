package kr.co.architecture.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kr.co.architecture.app.ui.theme.NoMaterial3Theme
import kr.co.architecture.app.ui.tab.MainTabEnum
import kr.co.architecture.app.ui.tab.NoMaterial3TabRow
import kr.co.architecture.core.ui.BaseCenterDialog
import kr.co.architecture.core.ui.BaseProgressBar
import kr.co.architecture.feature.bookmark.BookmarkScreen
import kr.co.architecture.feature.search.SearchScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      val viewModel = hiltViewModel<MainViewModel>()
      val loadingState by viewModel.globalUiBus.loadingState.collectAsStateWithLifecycle()
      val errorMessageState by viewModel.globalUiBus.failureDialog.collectAsStateWithLifecycle()
      var tabIndex by rememberSaveable { mutableIntStateOf(MainTabEnum.SEARCH.tabIndex) }

      NoMaterial3Theme {
        Column {
          NoMaterial3TabRow(
            selectedTab = MainTabEnum.from(tabIndex),
            onSelectedTabChanced = { tabIndex = it.tabIndex }
          )

          when (MainTabEnum.from(tabIndex)) {
            MainTabEnum.SEARCH -> {
              SearchScreen()
            }
            MainTabEnum.BOOKMARK -> {
              BookmarkScreen()
            }
          }
        }
      }

      BaseProgressBar(loadingState)

      errorMessageState?.let { state ->
        BaseCenterDialog(
          baseCenterDialogUiModel = state,
          onClickedConfirm = {
            viewModel.setEvent(MainUiEvent.OnClickedErrorDialogConfirm)
          }
        )
      }
    }
  }
}
