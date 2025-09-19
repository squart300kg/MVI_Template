package kr.co.architecture.feature.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import coil.load
import kr.co.architecture.core.model.MediaContents
import kr.co.architecture.core.model.uniqueId
import kr.co.architecture.feature.detail.databinding.ItemDetailPageBinding

// -------- Image --------

@BindingAdapter("imageUrl")
fun ImageView.bindImageUrl(url: String?) {
  if (url.isNullOrBlank()) {
    setImageDrawable(null)
  } else {
    load(url) { crossfade(true) }
  }
}

// -------- ViewPager2 --------

private class MediaPagerAdapter :
  ListAdapter<MediaContents, MediaPagerAdapter.VH>(DIFF) {

  class VH(val binding: ItemDetailPageBinding) : RecyclerView.ViewHolder(binding.root)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
    val inflater = LayoutInflater.from(parent.context)
    val binding = ItemDetailPageBinding.inflate(inflater, parent, false)
    return VH(binding)
  }

  override fun onBindViewHolder(holder: VH, position: Int) {
    holder.binding.item = getItem(position)
  }

  companion object {
    private val DIFF = object : DiffUtil.ItemCallback<MediaContents>() {
      override fun areItemsTheSame(oldItem: MediaContents, newItem: MediaContents) =
        oldItem.uniqueId() == newItem.uniqueId()

      override fun areContentsTheSame(oldItem: MediaContents, newItem: MediaContents) =
        oldItem == newItem
    }
  }
}

@BindingAdapter(value = ["pagerItems", "pagerStartIndex", "pagerEnabled"], requireAll = false)
fun ViewPager2.bindPager(
  items: List<MediaContents>?,
  startIndex: Int?,
  enabled: Boolean?
) {
  val adapter = (adapter as? MediaPagerAdapter) ?: MediaPagerAdapter().also { this.adapter = it }
  val list = items ?: emptyList()

  // 1) 리스트 갱신 (DiffUtil)
  adapter.submitList(list)

  // 2) 스와이프 가능 여부
  isUserInputEnabled = (enabled == true) && list.size > 1

  // 3) 초기 인덱스 적용 (현재와 다를 때만)
  val target = (startIndex ?: 0).coerceIn(0, (list.size - 1).coerceAtLeast(0))
  if (currentItem != target) {
    // 레이아웃/데이터 적용 이후에만 이동
    post { setCurrentItem(target, false) }
  }
}