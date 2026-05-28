package com.f4.recotrip.ui.plan.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.f4.recotrip.R
import com.f4.recotrip.databinding.PlanFragmentLandmarkBinding
import com.f4.recotrip.ui.plan.LandmarkAdapter
import com.f4.recotrip.ui.plan.LandmarkData
import com.f4.recotrip.ui.plan.PlanViewModel

class LandmarkFragment : Fragment() {

    private var _binding: PlanFragmentLandmarkBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlanViewModel by activityViewModels()

    private lateinit var landmarkAdapter: LandmarkAdapter
    private val selectedLandmarks = mutableListOf<LandmarkData>()
    private val landmarkList = mutableListOf<LandmarkData>()
    private val filteredList = mutableListOf<LandmarkData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlanFragmentLandmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ✅ 기존 선택 복원
        selectedLandmarks.clear()
        selectedLandmarks.addAll(viewModel.selectedPlaces.value ?: emptyList())

        // ✅ 상단 텍스트
        val city = viewModel.selectedCity.value ?: "알 수 없는 도시"
        val keywords = viewModel.keywords.value ?: listOf()
        binding.tvCityTitle.text = "📍 $city 로 여행 떠나요!"
        binding.tvSelectedKeywords.text = if (keywords.isNotEmpty()) "키워드: ${keywords.joinToString(", ")}" else "키워드 없음"

        // ✅ 관광지 목록 초기화 (중복 방지)
        landmarkList.clear()
        landmarkList.addAll(
            listOf(
                LandmarkData("경복궁", "조선 시대 궁궐", R.drawable.ic_launcher_background),
                LandmarkData("남산타워", "서울의 상징", R.drawable.ic_launcher_background),
                LandmarkData("한강공원", "산책과 피크닉", R.drawable.ic_launcher_background),
                LandmarkData("롯데월드", "놀이공원", R.drawable.ic_launcher_background)
            )
        )

        // ✅ 필터 초기화
        filteredList.clear()
        filteredList.addAll(landmarkList)

        setupAdapter()

        // ✅ 검색 필터링
        binding.etLandmarkSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().lowercase()
                filteredList.clear()
                filteredList.addAll(
                    landmarkList.filter {
                        it.name.lowercase().contains(query) || it.description.lowercase().contains(query)
                    }
                )
                landmarkAdapter.notifyDataSetChanged()
            }
        })


        // ▶ 다음
        binding.btnNext.setOnClickListener {
            if (selectedLandmarks.isEmpty()) {
                Toast.makeText(requireContext(), "최소 1개의 장소를 선택해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.selectedPlaces.value = selectedLandmarks.toMutableList()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, PeopleFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    private fun setupAdapter() {
        landmarkAdapter = LandmarkAdapter(filteredList, selectedLandmarks) { landmark, isChecked ->
            // 상태 저장은 Adapter 내부에서 이미 처리됨
            viewModel.selectedPlaces.value = selectedLandmarks.toMutableList()
        }

        binding.recyclerView.apply {
            adapter = landmarkAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}