package kr.co.architecture.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kr.co.architecture.core.ui.BaseCenterDialog
import kr.co.architecture.core.ui.BaseProgressBar
import kr.co.architecture.core.ui.theme.BaseTheme
import kr.co.architecture.feature.home.HomeScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  private val viewModel by viewModels<MainViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      val loadingState by viewModel.globalUiBus.loadingState.collectAsStateWithLifecycle()
      val errorMessageState by viewModel.globalUiBus.errorDialog.collectAsStateWithLifecycle()

      BaseTheme {
        HomeScreen()

        BaseProgressBar(loadingState)

        errorMessageState?.let { state ->
          BaseCenterDialog(
            baseCenterDialogUiModel = state,
            onClickedConfirm = {
              viewModel.globalUiBus.dismissDialog()
            }
          )
        }
      }
    }
  }
}
