package kr.co.architecture.feature.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.architecture.feature.detail.databinding.ItemDetailPageBinding

class DetailPagerAdapter :
  ListAdapter<UiModel, DetailPagerAdapter.ViewHolder>(DIFF) {

  init { setHasStableIds(true) }

  override fun getItemId(position: Int) = getItem(position).uniqueId().hashCode().toLong()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    val binding = ItemDetailPageBinding.inflate(inflater, parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.binding.item = getItem(position).bindingUiModel
  }

  class ViewHolder(val binding: ItemDetailPageBinding) : RecyclerView.ViewHolder(binding.root)

  companion object {
    private val DIFF = object : DiffUtil.ItemCallback<UiModel>() {
      override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel) =
        oldItem.uniqueId() == newItem.uniqueId()

      override fun areContentsTheSame(oldItem: UiModel, newItem: UiModel) =
        oldItem == newItem
    }
  }
}