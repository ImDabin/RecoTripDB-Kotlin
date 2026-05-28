package com.f4.recotrip.ui.plan.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.f4.recotrip.R
import com.f4.recotrip.databinding.PlanFragmentKeywordBinding
import com.f4.recotrip.ui.plan.PlanViewModel
import com.google.android.material.chip.Chip
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo

class KeywordFragment : Fragment() {

    private var _binding: PlanFragmentKeywordBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlanViewModel by activityViewModels()

    private val maxKeywords = 5

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlanFragmentKeywordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentKeywords = viewModel.keywords.value ?: mutableListOf()
        viewModel.keywords.value = currentKeywords.toMutableList()
        updateKeywordList(currentKeywords)

        DEFAULT_SUGGESTED_KEYWORDS.forEach { keyword ->
            val chip = createStyledChip(keyword).apply {
                setOnClickListener {
                    if (!viewModel.keywords.value!!.contains(keyword)) {
                        if (viewModel.keywords.value!!.size < maxKeywords) {
                            viewModel.keywords.value!!.add(keyword)
                            addKeywordChip(keyword)
                        } else {
                            showToast("키워드는 최대 5개까지 선택 가능합니다.")
                        }
                    }
                }
            }
            binding.chipGroupSuggested.addView(chip)
        }

        binding.btnAddKeyword.setOnClickListener {
            handleKeywordInput()
        }

        binding.editKeyword.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                handleKeywordInput()
                true
            } else {
                false
            }
        }

        binding.btnNext.setOnClickListener {
            if (viewModel.keywords.value.isNullOrEmpty()) {
                showToast("최소 1개의 키워드를 입력해주세요!")
            } else {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, CityFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    private fun updateKeywordList(savedKeywords: List<String>) {
        binding.chipGroupKeywords.removeAllViews()
        savedKeywords.forEach { keyword ->
            addKeywordChip(keyword)
        }
    }

    private fun addKeywordChip(keyword: String) {
        val chip = createStyledChip(keyword, R.color.gray_chip).apply {
            isCloseIconVisible = true
            setOnCloseIconClickListener {
                viewModel.keywords.value!!.remove(keyword)
                binding.chipGroupKeywords.removeView(this)
            }
        }
        binding.chipGroupKeywords.addView(chip)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun handleKeywordInput() {
        val input = binding.editKeyword.text.toString().trim()
        if (input.isNotEmpty() && !viewModel.keywords.value!!.contains(input)) {
            if (viewModel.keywords.value!!.size < maxKeywords) {
                viewModel.keywords.value!!.add(input)
                addKeywordChip(input)
                binding.editKeyword.text.clear()
            } else {
                showToast("키워드는 최대 5개까지 선택 가능합니다.")
            }
        }
    }

    private fun createStyledChip(keyword: String, backgroundColorResId: Int = R.color.mainblue): Chip {
        return Chip(requireContext()).apply {
            text = keyword
            isCheckable = false
            isClickable = true
            setTextColor(ContextCompat.getColor(context, android.R.color.white))
            chipBackgroundColor = ContextCompat.getColorStateList(context, backgroundColorResId)
            chipStrokeWidth = 0f
            chipStrokeColor = ColorStateList.valueOf(Color.TRANSPARENT)
            shapeAppearanceModel = shapeAppearanceModel
                .toBuilder()
                .setAllCornerSizes(48f)
                .build()
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
    companion object {
        val DEFAULT_SUGGESTED_KEYWORDS = listOf(
            "액티비티", "산", "호수", "바다", "휴양", "강", "캠핑", "관광", "맛집"
        )
    }
}