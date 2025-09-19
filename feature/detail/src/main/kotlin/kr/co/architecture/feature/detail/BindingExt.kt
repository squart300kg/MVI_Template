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

@BindingAdapter(value = ["pagerItems", "pagerStartIndex"], requireAll = true)
fun ViewPager2.bindPager(
  items: List<MediaContents>?,
  startIndex: Int?
) {
  val pagerAdapter = (adapter as? DetailPagerAdapter)
    ?: DetailPagerAdapter().also { adapter = it }
  pagerAdapter.submitList(items) {
    post { setCurrentItem(startIndex ?: 0, false) }
  }
}