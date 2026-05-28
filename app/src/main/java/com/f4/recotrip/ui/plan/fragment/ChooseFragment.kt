package com.f4.recotrip.ui.plan.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.f4.recotrip.R
import com.f4.recotrip.databinding.PlanFragmentChooseBinding

class ChooseFragment : Fragment() {

    private var _binding: PlanFragmentChooseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PlanFragmentChooseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRecommend.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, KeywordFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnSelect.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SelectFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}