package com.f4.recotrip.ui.plan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.f4.recotrip.R

class RouteItemAdapter(private val placeList: List<RouteItem>) :
    RecyclerView.Adapter<RouteItemAdapter.PlaceViewHolder>() {

    inner class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textOrder: TextView = itemView.findViewById(R.id.textOrder)
        val imagePlace: ImageView = itemView.findViewById(R.id.imagePlace)
        val textPlaceName: TextView = itemView.findViewById(R.id.textPlaceName)
        val textTimeRange: TextView = itemView.findViewById(R.id.textTimeRange)
        val textTravelTime: TextView = itemView.findViewById(R.id.textTravelTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.plan_fragment_route_item, parent, false)
        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = placeList[position]
        holder.textOrder.text = place.order.toString()
        holder.textPlaceName.text = place.name
        holder.textTimeRange.text = place.timeRange
        holder.textTravelTime.text = place.moveTime
        holder.imagePlace.setImageResource(place.imageResId)
    }

    override fun getItemCount(): Int = placeList.size
}