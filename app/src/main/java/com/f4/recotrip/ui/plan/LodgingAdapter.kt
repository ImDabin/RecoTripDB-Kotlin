package com.f4.recotrip.ui.plan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.f4.recotrip.databinding.PlanLodgingDayBinding

class LodgingAdapter(
    private var lodgingList: List<List<LodgingItem>>,
    private val onItemClick: (LodgingItem) -> Unit
) : RecyclerView.Adapter<LodgingAdapter.LodgingDayViewHolder>() {

    fun updateData(newLodgingList: List<List<LodgingItem>>) {
        lodgingList = newLodgingList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LodgingDayViewHolder {
        val binding = PlanLodgingDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LodgingDayViewHolder(binding)
    }

    override fun getItemCount(): Int = lodgingList.size

    override fun onBindViewHolder(holder: LodgingDayViewHolder, position: Int) {
        holder.bind(position + 1, lodgingList[position])
    }

    inner class LodgingDayViewHolder(private val binding: PlanLodgingDayBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(day: Int, items: List<LodgingItem>) {
            binding.textDayTitle.text = "${day}일차 숙소 선택"

            // ✅ layoutManager 설정 (필수)
            binding.recyclerViewLodgingItems.layoutManager = LinearLayoutManager(binding.root.context)

            // ✅ adapter 설정
            val adapter = LodgingItemAdapter(items, onItemClick)
            binding.recyclerViewLodgingItems.adapter = adapter
        }
    }
}