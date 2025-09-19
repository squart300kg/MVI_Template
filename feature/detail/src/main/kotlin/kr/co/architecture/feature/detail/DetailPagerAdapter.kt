package kr.co.architecture.feature.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.architecture.core.model.MediaContents
import kr.co.architecture.core.model.uniqueId
import kr.co.architecture.feature.detail.databinding.ItemDetailPageBinding

class DetailPagerAdapter :
  ListAdapter<MediaContents, DetailPagerAdapter.VH>(DIFF) {

  class VH(val binding: ItemDetailPageBinding) : RecyclerView.ViewHolder(binding.root)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
    val inflater = LayoutInflater.from(parent.context)
    val binding = ItemDetailPageBinding.inflate(inflater, parent, false)
    return VH(binding)
  }

  override fun onBindViewHolder(holder: VH, position: Int) {
    holder.binding.item = getItem(position)
  }

  companion object Companion {
    private val DIFF = object : DiffUtil.ItemCallback<MediaContents>() {
      override fun areItemsTheSame(oldItem: MediaContents, newItem: MediaContents) =
        oldItem.uniqueId() == newItem.uniqueId()

      override fun areContentsTheSame(oldItem: MediaContents, newItem: MediaContents) =
        oldItem == newItem
    }
  }
}