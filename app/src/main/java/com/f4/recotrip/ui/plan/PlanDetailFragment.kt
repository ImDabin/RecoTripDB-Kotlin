package com.f4.recotrip.ui.plan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.f4.recotrip.databinding.PlanFragmentPlanDetailBinding

class PlanDetailFragment : Fragment() {

    private var _binding: PlanFragmentPlanDetailBinding? = null
    private val binding get() = _binding!!

    // 외부에서 직접 주입받는 방식으로 변경
    lateinit var selectedPlan: SaveMyPlan

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlanFragmentPlanDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindDataToUI()
    }

    private fun bindDataToUI() {
        binding.textTitle.text = selectedPlan.title
        binding.textDateRange.text = selectedPlan.dateRange
        binding.imageMap.setImageResource(selectedPlan.routePreviewResId)

        val adapter = SimpleAdapter() // → 아래에 설명
        binding.recyclerViewSchedule.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewSchedule.adapter = adapter
        adapter.setItems(selectedPlan.dailyPlans)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}