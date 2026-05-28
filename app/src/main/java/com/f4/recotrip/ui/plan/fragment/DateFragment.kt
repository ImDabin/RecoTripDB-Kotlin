package com.f4.recotrip.ui.plan.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.f4.recotrip.R
import com.f4.recotrip.databinding.PlanFragmentDateBinding
import com.f4.recotrip.ui.plan.PlanViewModel
import java.text.SimpleDateFormat
import java.util.*

class DateFragment : Fragment() {

    private var _binding: PlanFragmentDateBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlanViewModel by activityViewModels()

    private var startDate: String? = null
    private var endDate: String? = null
    private var isSelectingStart = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlanFragmentDateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sdf = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())

        // 기존에 선택한 날짜가 있으면 화면에 표시
        viewModel.travelDates.observe(viewLifecycleOwner) { dates ->
            startDate = dates.first
            endDate = dates.second
            binding.tvStartDate.text = startDate
            binding.tvEndDate.text = endDate
        }

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val dateStr = sdf.format(Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }.time)

            if (isSelectingStart) {
                startDate = dateStr
                binding.tvStartDate.text = dateStr
                isSelectingStart = false
                Toast.makeText(requireContext(), "도착 날짜를 선택해주세요", Toast.LENGTH_SHORT).show()
            } else {
                endDate = dateStr
                binding.tvEndDate.text = dateStr
                isSelectingStart = true
            }
        }


        binding.btnNext.setOnClickListener {
            if (startDate.isNullOrBlank() || endDate.isNullOrBlank()) {
                Toast.makeText(requireContext(), "출발/도착 날짜를 모두 선택해주세요", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.travelDates.value = Pair(startDate!!, endDate!!)

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FlightFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}