package com.f4.recotrip.ui.plan.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.f4.recotrip.R
import com.f4.recotrip.databinding.PlanFragmentRouteBinding
import com.f4.recotrip.ui.plan.*

class RouteFragment : Fragment() {

    private var _binding: PlanFragmentRouteBinding? = null
    private val binding get() = _binding!!
    private lateinit var routeDayAdapter: RouteDayAdapter

    private val viewModel: PlanViewModel by activityViewModels()
    private var selectedPlan: String = "A"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlanFragmentRouteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        routeDayAdapter = RouteDayAdapter()
        binding.recyclerViewRoutePlan.apply {
            adapter = routeDayAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        val planA = listOf(
            RouteItem(1, "마나가하 섬", "10:00 - 12:00", "차량 15분", R.drawable.sample_image),
            RouteItem(2, "새섬 전망대", "12:30 - 14:00", "도보 10분", R.drawable.sample_image),
            RouteItem(3, "씨월드", "14:30 - 16:00", "버스 20분", R.drawable.sample_image),
        )
        val planB = listOf(
            RouteItem(1, "사이판 스카이워크", "10:00 - 11:00", "차량 7분", R.drawable.sample_image),
            RouteItem(2, "가라판 거리", "11:10 - 13:00", "도보 3분", R.drawable.sample_image),
        )
        val planC = listOf(
            RouteItem(1, "오비안 비치", "11:00 - 12:00", "도보 5분", R.drawable.sample_image),
            RouteItem(2, "마운트 카멜 대성당", "12:30 - 14:00", "차량 10분", R.drawable.sample_image),
        )

        routeDayAdapter.setItems(listOf(
            RouteDay("1일차", planA),
            RouteDay("2일차", planB),
            RouteDay("3일차", planC),
        ))
        binding.imageMapPreview.setImageResource(R.drawable.route_map_dummy_a)
        binding.btnNext.text = "플랜 A 선택"
        viewModel.selectedRoutePlan.value = "A" // 기본값도 저장

        binding.btnPlanA.setOnClickListener {
            routeDayAdapter.setItems(listOf(
                RouteDay("1일차", planA),
                RouteDay("2일차", planB),
                RouteDay("3일차", planC),
            ))
            binding.imageMapPreview.setImageResource(R.drawable.route_map_dummy_a)
            binding.btnNext.text = "플랜 A 선택"
            selectedPlan = "A"
            viewModel.selectedRoutePlan.value = "A"
        }

        binding.btnPlanB.setOnClickListener {
            routeDayAdapter.setItems(listOf(
                RouteDay("1일차", planB),
                RouteDay("2일차", planC),
                RouteDay("3일차", planA),
            ))
            binding.imageMapPreview.setImageResource(R.drawable.route_map_dummy_b)
            binding.btnNext.text = "플랜 B 선택"
            selectedPlan = "B"
            viewModel.selectedRoutePlan.value = "B"
        }

        binding.btnPlanC.setOnClickListener {
            routeDayAdapter.setItems(listOf(
                RouteDay("1일차", planC),
                RouteDay("2일차", planA),
                RouteDay("3일차", planB),
            ))
            binding.imageMapPreview.setImageResource(R.drawable.route_map_dummy_c)
            binding.btnNext.text = "플랜 C 선택"
            selectedPlan = "C"
            viewModel.selectedRoutePlan.value = "C"
        }

        binding.btnNext.setOnClickListener {

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LodgingFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}