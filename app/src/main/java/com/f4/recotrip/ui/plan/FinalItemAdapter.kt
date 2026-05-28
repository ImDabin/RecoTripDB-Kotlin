package com.f4.recotrip.ui.plan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.f4.recotrip.databinding.PlanFragmentFinalItemBinding

class FinalItemAdapter(private val items: List<FinalItem>) :
    RecyclerView.Adapter<FinalItemAdapter.FinalItemViewHolder>() {

    inner class FinalItemViewHolder(private val binding: PlanFragmentFinalItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FinalItem) {
            binding.textTime.text = item.time
            binding.textTitle.text = item.title
            binding.textPlace.text = item.place
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinalItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PlanFragmentFinalItemBinding.inflate(inflater, parent, false)
        return FinalItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FinalItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}