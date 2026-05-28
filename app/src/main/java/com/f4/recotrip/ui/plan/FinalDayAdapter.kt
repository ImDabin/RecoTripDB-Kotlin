package com.f4.recotrip.ui.plan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.f4.recotrip.databinding.PlanFragmentFinalDayBinding

class FinalDayAdapter : RecyclerView.Adapter<FinalDayAdapter.FinalDayViewHolder>() {

    private var dayList: List<FinalDay> = emptyList()

    fun setItems(newList: List<FinalDay>) {
        dayList = newList
        notifyDataSetChanged()
    }

    inner class FinalDayViewHolder(private val binding: PlanFragmentFinalDayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(day: FinalDay) {
            binding.textDayTitle.text = day.dayTitle
            val itemAdapter = FinalItemAdapter(day.items)
            binding.recyclerViewFinalItems.apply {
                layoutManager = LinearLayoutManager(binding.root.context)
                adapter = itemAdapter
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinalDayViewHolder {
        val binding = PlanFragmentFinalDayBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FinalDayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FinalDayViewHolder, position: Int) {
        holder.bind(dayList[position])
    }

    override fun getItemCount(): Int = dayList.size
}