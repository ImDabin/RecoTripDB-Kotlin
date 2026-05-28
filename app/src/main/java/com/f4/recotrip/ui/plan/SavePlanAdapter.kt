package com.f4.recotrip.ui.plan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.f4.recotrip.databinding.PlanActivityMyplanItemBinding

class SavePlanAdapter(
    private val planList: List<SaveMyPlan>,
    private val onClick: (SaveMyPlan) -> Unit
) : RecyclerView.Adapter<SavePlanAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: PlanActivityMyplanItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(plan: SaveMyPlan) {
            binding.textTitle.text = plan.title
            binding.textDate.text = plan.dateRange
            binding.imageMap.setImageResource(plan.routePreviewResId)

            binding.root.setOnClickListener {
                onClick(plan)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PlanActivityMyplanItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(planList[position])
    }

    override fun getItemCount(): Int = planList.size
}