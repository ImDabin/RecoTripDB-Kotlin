package com.f4.recotrip.ui.plan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.f4.recotrip.R

class LandmarkAdapter(
    private val landmarks: List<LandmarkData>,
    private val selectedItems: MutableList<LandmarkData>,
    private val onItemChecked: (LandmarkData, Boolean) -> Unit
) : RecyclerView.Adapter<LandmarkAdapter.LandmarkViewHolder>() {

    inner class LandmarkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgLandmark: ImageView = itemView.findViewById(R.id.imgLandmark)
        val tvName: TextView = itemView.findViewById(R.id.tvLandmarkName)
        val tvDesc: TextView = itemView.findViewById(R.id.tvLandmarkDescription)
        val cbSelect: CheckBox = itemView.findViewById(R.id.cbSelect)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LandmarkViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.plan_fragment_landmark_item, parent, false)
        return LandmarkViewHolder(view)
    }

    override fun onBindViewHolder(holder: LandmarkViewHolder, position: Int) {
        val item = landmarks[position]

        holder.tvName.text = item.name
        holder.tvDesc.text = item.description
        holder.imgLandmark.setImageResource(item.imageRes)

        // ✅ View 재활용 시 중복 리스너 제거
        holder.cbSelect.setOnCheckedChangeListener(null)
        holder.cbSelect.isChecked = selectedItems.contains(item)

        // ✅ 체크 변경 시 selectedItems에 직접 반영
        holder.cbSelect.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked && !selectedItems.contains(item)) {
                selectedItems.add(item)
            } else if (!isChecked && selectedItems.contains(item)) {
                selectedItems.remove(item)
            }
            onItemChecked(item, isChecked)
        }
    }

    override fun getItemCount(): Int = landmarks.size
}