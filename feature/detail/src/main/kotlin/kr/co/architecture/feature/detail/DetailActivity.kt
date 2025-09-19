package kr.co.architecture.feature.detail

import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kr.co.architecture.core.ui.BaseDataBindingActivity
import kr.co.architecture.feature.detail.databinding.ActivityDetailBinding


@AndroidEntryPoint
class DetailActivity :
  BaseDataBindingActivity<
    ActivityDetailBinding,
    DetailUiState,
    DetailUiEvent,
    DetailUiSideEffect,
    DetailViewModel
    >() {

  override val layoutResId: Int = R.layout.activity_detail
  override val variableId: Int = BR.vm
  override val viewModel: DetailViewModel by viewModels()

  override fun onBindCreated(binding: ActivityDetailBinding) {
    // 1) id 추출 (딥링크 or 명시적 인텐트)
    val id = intent?.data?.getQueryParameter("id")
      ?: intent?.getStringExtra("id")

    if (id.isNullOrBlank()) {
      finish(); return
    }

  }

  override fun renderState(state: DetailUiState) {
    binding.state = state
  }

  override fun handleSideEffect(effect: DetailUiSideEffect) {
    when (effect) {
      DetailUiSideEffect.Load -> viewModel.fetchData()
    }
  }
}
