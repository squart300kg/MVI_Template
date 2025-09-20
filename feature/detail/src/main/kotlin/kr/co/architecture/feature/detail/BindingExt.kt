package kr.co.architecture.feature.detail

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import coil.load
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.co.architecture.core.ui.R as coreUiR

@BindingAdapter("imageUrl")
fun ImageView.bindImageUrl(url: String?) {
  if (url.isNullOrBlank()) {
    setImageDrawable(null)
  } else {
    load(url) { crossfade(true) }
  }
}

// TODO: 코드정리
@BindingAdapter(value = ["bookmarkVm", "bookmarkIndex"], requireAll = true)
fun ImageView.bindBookmark(vm: DetailViewModel?, index: Int?) {
  // 기존 수집 Job 정리
  val key = R.id.iv_like_state_job
  (getTag(key) as? Job)?.cancel()
  setTag(key, null)

  // 클릭 리스너 초기화
  setOnClickListener(null)

  if (vm == null || index == null) {
    setImageResource(coreUiR.drawable.icon_like_off)
    return
  }

  // 클릭 → 즉시 이벤트 전송
  setOnClickListener {
    val item = vm.uiState.value.uiModels.getOrNull(index) ?: return@setOnClickListener
    vm.setEvent(DetailUiEvent.OnClickedBookmark(item))
  }

  // 아이콘 상태 구독 (수명주기 안전)
  val owner = findViewTreeLifecycleOwner()
  if (owner == null) {
    val isOn = vm.uiState.value.uiModels.getOrNull(index)?.bindingUiModel?.isBookmarked == true
    setImageResource(if (isOn) coreUiR.drawable.icon_like_on else coreUiR.drawable.icon_like_off)
    return
  }

  val job = owner.lifecycleScope.launch {
    owner.repeatOnLifecycle(Lifecycle.State.STARTED) {
      vm.uiState.collectLatest { state ->
        val isOn = state.uiModels.getOrNull(index)?.bindingUiModel?.isBookmarked == true
        setImageResource(if (isOn) coreUiR.drawable.icon_like_on else coreUiR.drawable.icon_like_off)
      }
    }
  }
  setTag(key, job)
}

@BindingAdapter(value = ["pagerItems", "pagerStartIndex"], requireAll = true)
fun ViewPager2.bindPager(
  items: List<UiModel>?,
  startIndex: Int?
) {
  val pagerAdapter = (adapter as? DetailPagerAdapter)
    ?: DetailPagerAdapter().also { adapter = it }
  pagerAdapter.submitList(items) {
    post { setCurrentItem(startIndex ?: 0, false) }
  }
}

@BindingAdapter("pagerOnPageSelected")
fun ViewPager2.bindOnPageSelected(viewModel: DetailViewModel) {
  registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
    override fun onPageSelected(position: Int) {
      viewModel.setEvent(DetailUiEvent.OnSwipe(position))
    }
  })
}
