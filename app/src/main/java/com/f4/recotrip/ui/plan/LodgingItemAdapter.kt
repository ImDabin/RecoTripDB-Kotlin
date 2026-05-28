package com.f4.recotrip.ui.plan

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.f4.recotrip.R
import com.f4.recotrip.databinding.PlanLodgingItemBinding

class LodgingItemAdapter(
    private val items: List<LodgingItem>,
    private val onItemClick: (LodgingItem) -> Unit
) : RecyclerView.Adapter<LodgingItemAdapter.LodgingItemViewHolder>() {

    private var selectedPosition = -1  // 하루당 하나만 선택

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LodgingItemViewHolder {
        val binding = PlanLodgingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LodgingItemViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: LodgingItemViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    inner class LodgingItemViewHolder(private val binding: PlanLodgingItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LodgingItem, position: Int) {
            binding.textName.text = item.name
            binding.textAddress.text = item.address
            binding.textPrice.text = "${item.price}원"
            binding.imageHotel.setImageResource(item.imageResId)

            // 강조: 선택된 항목만 배경색 적용
            val cardColor = if (position == selectedPosition) {
                ContextCompat.getColor(binding.root.context, R.color.mint_chip)
            } else {
                Color.WHITE
            }
            binding.root.setCardBackgroundColor(cardColor)

            binding.root.setOnClickListener {
                if (selectedPosition != position) {
                    val prevPosition = selectedPosition
                    selectedPosition = position
                    notifyItemChanged(prevPosition)
                    notifyItemChanged(selectedPosition)
                    onItemClick(item)
                }
            }
        }
    }
}