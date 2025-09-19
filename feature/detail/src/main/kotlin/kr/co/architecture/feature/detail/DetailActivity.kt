package kr.co.architecture.feature.detail

import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kr.co.architecture.core.router.AppDeepLinks.Detail.ArgsKey.ID
import kr.co.architecture.core.router.AppDeepLinks.Detail.ArgsKey.ORIGIN
import kr.co.architecture.core.ui.BaseDataBindingActivity
import kr.co.architecture.feature.detail.databinding.ActivityDetailBinding


@AndroidEntryPoint
class DetailActivity : BaseDataBindingActivity<ActivityDetailBinding, DetailUiState, DetailUiEvent, DetailUiSideEffect, DetailViewModel>() {

  override val layoutResId: Int = R.layout.activity_detail
  override val variableId: Int = BR.vm
  override val viewModel: DetailViewModel by viewModels()

  override fun onBindCreated(binding: ActivityDetailBinding) {
    val id = intent?.data?.getQueryParameter(ID)
    val origin = intent?.data?.getQueryParameter(ORIGIN)
    println("detailLog 1 ; $id, $origin")
//    if (id.isNullOrBlank() || origin.isNullOrBlank()) { finish(); return }

    // SavedStateHandle로 전달
//    viewModel.apply {
//      savedStateHandle[ID] = id
//      savedStateHandle[ORIGIN] = fromBookmark
//    }
  }

  override fun renderState(state: DetailUiState) {
    binding.state = state
  }

  override fun handleSideEffect(effect: DetailUiSideEffect) {
    when (effect) {
      DetailUiSideEffect.OnFinish -> finish()
    }
  }
}
