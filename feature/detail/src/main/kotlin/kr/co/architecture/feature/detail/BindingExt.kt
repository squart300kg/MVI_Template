package kr.co.architecture.feature.detail

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2
import coil.load
import kr.co.architecture.core.model.MediaContents

@BindingAdapter("imageUrl")
fun ImageView.bindImageUrl(url: String?) {
  if (url.isNullOrBlank()) {
    setImageDrawable(null)
  } else {
    load(url) { crossfade(true) }
  }
}

@BindingAdapter(
  value = ["bookmarkVm", "bookmarkIndex"],
  requireAll = true
)
fun ImageView.bindBookmarkClick(
  viewModel: DetailViewModel?,
  index: Int?
) {
  setOnClickListener {
    if (viewModel == null || index == null) return@setOnClickListener
    val item = viewModel.uiState.value.uiModel.getOrNull(index) ?: return@setOnClickListener
    viewModel.setEvent(DetailUiEvent.OnClickedBookmark(item))
  }
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
