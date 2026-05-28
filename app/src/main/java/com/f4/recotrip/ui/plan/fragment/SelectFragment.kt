package com.f4.recotrip.ui.plan.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.f4.recotrip.databinding.PlanFragmentSelectBinding
import com.f4.recotrip.ui.plan.PlanViewModel

class SelectFragment : Fragment() {

    private var _binding: PlanFragmentSelectBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlanViewModel by activityViewModels()

    private lateinit var cityAdapter: CityAdapter
    private val allCityList = listOf("홍콩", "밴쿠버", "카트만두", "이스탄불", "로마") // 필요 시 확장
    private var filteredCityList = allCityList.toMutableList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlanFragmentSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 🔎 어댑터 초기화: 도시 클릭 시 ViewModel에 저장하고 다음 프래그먼트로 이동
        cityAdapter = CityAdapter(filteredCityList) { selectedCity ->
            viewModel.selectedCity.value = selectedCity

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(com.f4.recotrip.R.id.fragment_container, CityDetailFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.recyclerView.adapter = cityAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 🔁 검색어 필터링 기능
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                filteredCityList = if (query.isEmpty()) {
                    allCityList.toMutableList()
                } else {
                    allCityList.filter { it.contains(query) }.toMutableList()
                }
                cityAdapter.updateList(filteredCityList)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}