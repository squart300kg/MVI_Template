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
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onCompletion
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
  if (vm == null || index == null) {
    return
  }

  // ★ 같은 (vm, index)면 재설치/재구독 안 함 → 중복 수집 방지
  val argsKey = R.id.iv_like_args
  val prev = getTag(argsKey) as? Pair<DetailViewModel, Int>
  if (prev?.first === vm && prev.second == index) return
  setTag(argsKey, vm to index)

  (getTag(R.id.iv_like_state_job) as? Job)?.cancel()
  setTag(R.id.iv_like_state_job, null)

  setOnClickListener {
    val item = vm.uiState.value.uiModels.getOrNull(index) ?: return@setOnClickListener
    vm.setEvent(DetailUiEvent.OnClickedBookmark(item))
  }

  val owner = findViewTreeLifecycleOwner() ?: return
  val job = owner.lifecycleScope.launch {
    owner.repeatOnLifecycle(Lifecycle.State.STARTED) {
      vm.uiState
        .mapNotNull { it.uiModels.getOrNull(index)?.bindingUiModel?.isBookmarked }
        .collectLatest { isOn ->
          println("collectLatest, $isOn, hashCode : ${this.hashCode()}")

          setImageResource(
            if (isOn) coreUiR.drawable.icon_like_on
            else coreUiR.drawable.icon_like_off
          )
        }
    }
  }
  setTag(R.id.iv_like_state_job, job)
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
