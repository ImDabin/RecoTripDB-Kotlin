package com.f4.recotrip.ui.plan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.f4.recotrip.R

class SimpleAdapter :
    RecyclerView.Adapter<SimpleAdapter.ViewHolder>() {

    private val dayPlans = mutableListOf<List<String>>() // 각 일자별 일정 텍스트 리스트

    fun setItems(items: List<List<String>>) {
        dayPlans.clear()
        dayPlans.addAll(items)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textDayTitle: TextView = itemView.findViewById(R.id.textDayTitle)
        private val textSchedule: TextView = itemView.findViewById(R.id.textSchedule)

        fun bind(dayIndex: Int, schedules: List<String>) {
            textDayTitle.text = "${dayIndex + 1}일차"

            // 더미 시간 포함해서 일정 출력
            val displayText = buildString {
                append("00:00 숙소 출발\n")
                schedules.forEachIndexed { i, place ->
                    append(String.format("%02d:00 %s\n", i + 9, place)) // 09:00, 10:00 등
                }
                append("00:00 숙소 도착")
            }

            textSchedule.text = displayText
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.plan_simple, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, dayPlans[position])
    }

    override fun getItemCount(): Int = dayPlans.size
}