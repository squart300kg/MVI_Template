package kr.co.architecture.feature.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kr.co.architecture.feature.detail.databinding.ActivityDetailBinding

@AndroidEntryPoint
class DetailActivity : ComponentActivity() {

  private lateinit var binding: ActivityDetailBinding
  private val viewModel: DetailViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
    binding.lifecycleOwner = this
    binding.vm = viewModel

    // 1) id 추출 (intent data or extra 둘 다 허용)
    val idFromUri = intent?.data?.getQueryParameter("id")
    val idFromExtra = intent?.getStringExtra("id")
    val id = idFromUri ?: idFromExtra

    if (id.isNullOrBlank()) {
      finish(); return
    }


    lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        launch {
          // 2) 사이드이펙트 수집 → 최초 로드
          viewModel.uiSideEffect.collect { effect ->
            when (effect) {
              DetailUiSideEffect.Load -> viewModel.fetchData()
            }
          }
        }
        launch {
          // 3) 상태 수집 → DataBinding 변수에 주입
          viewModel.uiState.collect { state ->
            binding.state = state
          }
        }
      }
    }
  }
}
