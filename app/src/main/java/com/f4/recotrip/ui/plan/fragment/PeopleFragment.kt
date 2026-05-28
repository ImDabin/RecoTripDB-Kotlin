package com.f4.recotrip.ui.plan.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.f4.recotrip.R
import com.f4.recotrip.databinding.PlanFragmentPeopleBinding
import com.f4.recotrip.ui.plan.PlanViewModel

class PeopleFragment : Fragment() {

    private var _binding: PlanFragmentPeopleBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlanViewModel by activityViewModels()

    private var adultCount = 2
    private var childCount = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlanFragmentPeopleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ✅ ViewModel에서 값 복원 (null 방지)
        val restored = viewModel.selectedPeople.value ?: Pair(2, 1)
        adultCount = restored.first
        childCount = restored.second
        updateCountViews()

        Log.d("PeopleFragment", "복원된 인원: 성인 $adultCount, 아동 $childCount")

        // 🔼 성인 +
        binding.btnAdultPlus.setOnClickListener {
            adultCount++
            updateCountViews()
            viewModel.selectedPeople.value = Pair(adultCount, childCount)
        }

        // 🔽 성인 -
        binding.btnAdultMinus.setOnClickListener {
            if (adultCount > 1) {
                adultCount--
                updateCountViews()
                viewModel.selectedPeople.value = Pair(adultCount, childCount)
            } else {
                Toast.makeText(context, "성인은 최소 1명 이상이어야 합니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 🔼 아동 +
        binding.btnChildPlus.setOnClickListener {
            childCount++
            updateCountViews()
            viewModel.selectedPeople.value = Pair(adultCount, childCount)
        }

        // 🔽 아동 -
        binding.btnChildMinus.setOnClickListener {
            if (childCount > 0) {
                childCount--
                updateCountViews()
                viewModel.selectedPeople.value = Pair(adultCount, childCount)
            }
        }


        // ▶ 다음 버튼
        binding.btnNext.setOnClickListener {
            // 여기선 저장 안 해도 됨 (이미 저장됨)
            Log.d("PeopleFragment", "다음 클릭됨 - 저장값: 성인 $adultCount / 아동 $childCount")

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, DateFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun updateCountViews() {
        binding.tvAdultCount.text = adultCount.toString()
        binding.tvChildCount.text = childCount.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}