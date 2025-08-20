package kr.co.architecture.core.ui

import androidx.compose.runtime.compositionLocalOf

val LocalOnErrorMessageChanged = compositionLocalOf<(BaseCenterDialogUiModel) -> Unit> { {} }
val LocalOnLoadingStateChanged = compositionLocalOf<(isLoading: Boolean) -> Unit> { {} }
val LocalOnRefreshStateChanged = compositionLocalOf<(isLoading: Boolean) -> Unit> { {} }