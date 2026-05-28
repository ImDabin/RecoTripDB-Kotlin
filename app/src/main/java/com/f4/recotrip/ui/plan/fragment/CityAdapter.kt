package com.f4.recotrip.ui.plan.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.f4.recotrip.R

class CityAdapter(
    private var cityList: List<String>,
    private val onCityClick: (String) -> Unit  // ✅ 도시 클릭 콜백 추가
) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    inner class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cityName: TextView = itemView.findViewById(R.id.tvCityName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.plan_fragment_city_item, parent, false)
        return CityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = cityList[position]
        holder.cityName.text = city

        holder.itemView.setOnClickListener {
            onCityClick(city)  // ✅ 도시 클릭 시 콜백 실행
        }
    }

    override fun getItemCount(): Int = cityList.size

    fun updateList(newList: List<String>) {
        cityList = newList
        notifyDataSetChanged()
    }
}