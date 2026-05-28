package com.f4.recotrip.ui.plan.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.f4.recotrip.databinding.PlanFragmentFinalBinding
import com.f4.recotrip.ui.home.HomeActivity
import com.f4.recotrip.ui.plan.*

class FinalFragment : Fragment() {

    private var _binding: PlanFragmentFinalBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlanViewModel by activityViewModels()

    private lateinit var finalDayAdapter: FinalDayAdapter
    private var finalDays: List<FinalDay> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlanFragmentFinalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadFinalSchedule()

        binding.buttonSavePlan.setOnClickListener {
            viewModel.setFinalDays(finalDays)
            Toast.makeText(requireContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun setupRecyclerView() {
        finalDayAdapter = FinalDayAdapter()
        binding.recyclerViewFinalSchedule.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = finalDayAdapter
        }
    }

    private fun loadFinalSchedule() {
        val days = mutableListOf<FinalDay>()

        val dayCount = viewModel.totalTripDays
        val lodgings = viewModel.selectedLodgings.value ?: List(dayCount) { null }

        for (i in 0 until dayCount) {
            val lodging = lodgings.getOrNull(i)
            val items = mutableListOf<FinalItem>()

            // 출발 숙소
            lodging?.let {
                items.add(
                    FinalItem(
                        time = viewModel.departureTimeGoing.value ?: "오전 9시",
                        title = "${i + 1}일차 출발 숙소",
                        place = it.name,
                        type = "숙소"
                    )
                )
            }

            // 관광지 더미
            items.add(
                FinalItem(
                    time = "오전 11시",
                    title = "관광지 1",
                    place = "관광지 A",
                    type = "관광지"
                )
            )
            items.add(
                FinalItem(
                    time = "오후 2시",
                    title = "관광지 2",
                    place = "관광지 B",
                    type = "관광지"
                )
            )

            // 도착 숙소
            lodging?.let {
                items.add(
                    FinalItem(
                        time = viewModel.arrivalTimeGoing.value ?: "오후 6시",
                        title = "${i + 1}일차 도착 숙소",
                        place = it.name,
                        type = "숙소"
                    )
                )
            }

            days.add(FinalDay("${i + 1}일차", items))
        }

        finalDays = days
        finalDayAdapter.setItems(days)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}