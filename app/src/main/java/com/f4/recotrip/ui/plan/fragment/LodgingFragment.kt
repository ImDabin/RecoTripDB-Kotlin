package com.f4.recotrip.ui.plan.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.f4.recotrip.R
import com.f4.recotrip.databinding.PlanFragmentLodgingBinding
import com.f4.recotrip.ui.plan.LodgingAdapter
import com.f4.recotrip.ui.plan.LodgingItem
import com.f4.recotrip.ui.plan.PlanViewModel

class LodgingFragment : Fragment() {

    private var _binding: PlanFragmentLodgingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlanViewModel by activityViewModels()
    private lateinit var lodgingAdapter: LodgingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlanFragmentLodgingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ✅ 숙소 선택 초기화
        viewModel.initializeSelectedLodgings(viewModel.totalTripDays)

        // 더미 숙소 데이터 생성
        val dummyLodgingList = List(viewModel.totalTripDays) { dayIndex ->
            List(3) { i ->
                LodgingItem(
                    id = "${dayIndex}_${i}",
                    name = "🛏️ ${dayIndex + 1}일차 - 숙소 ${i + 1}",
                    address = "📍 주소 ${i + 1}",
                    description = "편안한 숙소 설명 ${i + 1}",
                    price = (10000 * (i + 1)).toString(),
                    imageResId = R.drawable.lodging_dummy_hotel
                )
            }
        }

        // 어댑터 설정
        lodgingAdapter = LodgingAdapter(dummyLodgingList) { selectedItem ->
            Log.d("LodgingFragment", "선택한 숙소: ${selectedItem.name}")
            val dayIndex = selectedItem.id.split("_")[0].toIntOrNull() ?: return@LodgingAdapter
            viewModel.selectLodgingForDay(dayIndex, selectedItem)
        }

        binding.recyclerViewLodging.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = lodgingAdapter
        }

        // ✅ 다음 버튼 클릭 시 FinalFragment로 이동
        binding.buttonNext.setOnClickListener {
            Log.d("LodgingFragment", "다음 버튼 클릭됨")
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FinalFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}