package com.f4.recotrip.ui.plan.fragment

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.f4.recotrip.R
import com.f4.recotrip.databinding.PlanFragmentFlightBinding
import com.f4.recotrip.ui.plan.PlanViewModel
import java.util.*

class FlightFragment : Fragment() {

    private var _binding: PlanFragmentFlightBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlanViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlanFragmentFlightBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.arrivalTimeGoing.observe(viewLifecycleOwner) { arrivalTimeGoing ->
            binding.btnArrivalTimeGoing.setText(arrivalTimeGoing)
        }
        viewModel.departureTimeReturning.observe(viewLifecycleOwner) { departureTimeReturning ->
            binding.btnDepartureTimeReturning.setText(departureTimeReturning)
        }
        viewModel.arrivalTimeReturning.observe(viewLifecycleOwner) { arrivalTimeReturning ->
            binding.btnArrivalTimeReturning.setText(arrivalTimeReturning)
        }
        // 여행 가는 날 출발 시간 선택
        binding.btnDepartureTimeGoing.setOnClickListener {
            showTimePickerDialog { selectedTime ->
                binding.btnDepartureTimeGoing.setText(selectedTime)
                viewModel.departureTimeGoing.value = selectedTime
            }
        }
        // 여행 가는 날 도착 시간 선택
        binding.btnArrivalTimeGoing.setOnClickListener {
            showTimePickerDialog { selectedTime ->
                binding.btnArrivalTimeGoing.setText(selectedTime)
                viewModel.arrivalTimeGoing.value = selectedTime
            }
        }
        // 여행 돌아오는 날 출발 시간 선택
        binding.btnDepartureTimeReturning.setOnClickListener {
            showTimePickerDialog { selectedTime ->
                binding.btnDepartureTimeReturning.setText(selectedTime)
                viewModel.departureTimeReturning.value = selectedTime
            }
        }
        // 여행 돌아오는 날 도착 시간 선택
        binding.btnArrivalTimeReturning.setOnClickListener {
            showTimePickerDialog { selectedTime ->
                binding.btnArrivalTimeReturning.setText(selectedTime)
                viewModel.arrivalTimeReturning.value = selectedTime
            }
        }



        binding.btnNext.setOnClickListener {
            // 비행기 시간 정보가 모두 입력된 경우
            if (binding.btnDepartureTimeGoing.text.isNullOrBlank() || binding.btnArrivalTimeGoing.text.isNullOrBlank() ||
                binding.btnDepartureTimeReturning.text.isNullOrBlank() || binding.btnArrivalTimeReturning.text.isNullOrBlank()) {
                Toast.makeText(requireContext(), "모든 시간을 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                // ViewModel에 시간 정보 저장
                viewModel.departureTimeGoing.value = binding.btnDepartureTimeGoing.text.toString()
                viewModel.arrivalTimeGoing.value = binding.btnArrivalTimeGoing.text.toString()
                viewModel.departureTimeReturning.value = binding.btnDepartureTimeReturning.text.toString()
                viewModel.arrivalTimeReturning.value = binding.btnArrivalTimeReturning.text.toString()

                // 다음 화면으로 이동
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, RouteFragment())  // NextFragment는 실제 화면으로 교체
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    // 시간 선택 다이얼로그
    private fun showTimePickerDialog(onTimeSet: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                onTimeSet(selectedTime)
            },
            hour, minute, true
        )
        timePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}