package kr.co.architecture.feature.detail

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
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

@BindingAdapter(value = ["pagerItems", "pagerStartIndex", "pagerEnabled"], requireAll = false)
fun ViewPager2.bindPager(
  items: List<MediaContents>?,
  startIndex: Int?,
  enabled: Boolean?
) {
  val pagerAdapter = (adapter as? DetailPagerAdapter) ?: DetailPagerAdapter().also {
    // 리스트가 비어있을 땐 복원하지 않게 해서 0번으로 튀는 현상 방지
    it.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    this.adapter = it
  }

  val list = items ?: emptyList()
  val targetIndex = (startIndex ?: 0).coerceIn(0, (list.size - 1).coerceAtLeast(0))

  // 1) 스와이프 가능 여부
  isUserInputEnabled = (enabled == true) && list.size > 1

  // 2) 리스트 갱신은 항상 submitList로 (DiffUtil)
  //    ▶ 초기 포커스는 "commit 콜백"에서 반드시 설정
  if (pagerAdapter.currentList != list) {
    pagerAdapter.submitList(list) {
      // 데이터가 실제로 반영된 후에 포커스 이동
      if (currentItem != targetIndex && list.isNotEmpty()) {
        post { setCurrentItem(targetIndex, false) }
      }
    }
  } else {
    // 같은 리스트인데 시작 인덱스만 바뀐 경우
    if (currentItem != targetIndex && list.isNotEmpty()) {
      post { setCurrentItem(targetIndex, false) }
    }
  }
}