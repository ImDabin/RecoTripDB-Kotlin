package com.f4.recotrip.ui.plan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.f4.recotrip.R

class RouteDayAdapter : RecyclerView.Adapter<RouteDayAdapter.DayViewHolder>() {

    private var dayList: List<RouteDay> = emptyList()

    fun setItems(newList: List<RouteDay>) {
        dayList = newList
        notifyDataSetChanged()
    }

    inner class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textDayTitle: TextView = itemView.findViewById(R.id.textDayTitle)
        val recyclerViewPlaces: RecyclerView = itemView.findViewById(R.id.recyclerViewPlaces)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.plan_fragment_day, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val day = dayList[position]
        holder.textDayTitle.text = day.dayTitle

        holder.recyclerViewPlaces.apply {
            layoutManager = LinearLayoutManager(holder.itemView.context)
            adapter = RouteItemAdapter(day.places)
        }
    }

    override fun getItemCount(): Int = dayList.size
}