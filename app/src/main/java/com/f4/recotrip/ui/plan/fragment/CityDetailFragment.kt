package com.f4.recotrip.ui.plan.fragment

import androidx.fragment.app.activityViewModels
import com.f4.recotrip.ui.plan.PlanViewModel
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import com.f4.recotrip.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * CityDetailFragment displays detailed information about a city, including its name, description,
 * weather details, visa info, flight time, price info, landmarks, user reviews, and similar cities.
 * Dummy data is used for demonstration purposes until real data is available.
 */
class CityDetailFragment : Fragment() {

    private val viewModel: PlanViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment from fragment_city_detail.xml
        return inflater.inflate(R.layout.fragment_city_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cityId = arguments?.getString("city_id") ?: return
        val db = Firebase.firestore

        val progressBar = view.findViewById<ProgressBar>(R.id.progressBarDetailLoading)
        val textLoadingMessage = view.findViewById<TextView>(R.id.textLoadingMessage)
        val layoutContent = view.findViewById<LinearLayout>(R.id.layoutCityDetailContent)
        layoutContent.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        textLoadingMessage.visibility = View.VISIBLE

        val textCityName = view.findViewById<TextView>(R.id.textCityName)
        val textDescription = view.findViewById<TextView>(R.id.textDescription)
        val chipGroup = view.findViewById<ChipGroup>(R.id.chipGroupKeywords)
        val textWeather = view.findViewById<TextView>(R.id.textWeather)
        val switchWeather = view.findViewById<Switch>(R.id.switchMonthlyWeather)
        val textVisa = view.findViewById<TextView>(R.id.textVisa)
        val textFlight = view.findViewById<TextView>(R.id.textFlight)
        val textPrice = view.findViewById<TextView>(R.id.textPriceInfo)
        val layoutLandmarks = view.findViewById<LinearLayout>(R.id.layoutLandmarks)
        val textReview = view.findViewById<TextView>(R.id.textUserReview)
        val btnFavorite = view.findViewById<ImageButton>(R.id.btnFavorite)
        val btnShare = view.findViewById<ImageButton>(R.id.btnShare)
        val btnCreatePlan = view.findViewById<Button>(R.id.btnCreatePlan)
        val layoutSimilar = view.findViewById<LinearLayout>(R.id.layoutSimilarCities)

        db.collection("cities").document(cityId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val nameKo = document.getString("name_ko") ?: ""
                    val country = document.getString("country") ?: ""
                    val description = document.getString("description") ?: ""
                    val keywords = document.get("keywords") as? List<*> ?: emptyList<Any>()
                    val visa = document.getString("visa_info") ?: ""
                    val flight = document.getString("flight_time") ?: ""
                    val price = document.getString("price_info") ?: ""
                    val review = document.getString("user_review") ?: ""
                    val landmarks = document.get("landmarks") as? List<*> ?: emptyList<Any>()
                    val similar = document.get("similar") as? List<*> ?: emptyList<Any>()
                    val weatherMap = document.get("monthly_weather") as? Map<*, *>

                    textCityName.text = "$nameKo, $country"
                    textDescription.text = description
                    textVisa.text = visa
                    textFlight.text = flight
                    textPrice.text = price
                    textReview.text = review

                    chipGroup.removeAllViews()
                    keywords.forEach {
                        val chip = Chip(requireContext()).apply {
                            text = it.toString()
                            isClickable = false
                            isCheckable = false
                        }
                        chipGroup.addView(chip)
                    }

                    layoutLandmarks.removeAllViews()
                    landmarks.forEach {
                        val text = TextView(requireContext()).apply {
                            text = "• ${it.toString()}"
                            textSize = 14f
                            setPadding(0, 4, 0, 4)
                        }
                        layoutLandmarks.addView(text)
                    }

                    layoutSimilar.removeAllViews()
                    similar.forEach {
                        val text = TextView(requireContext()).apply {
                            text = "• ${it.toString()}"
                            textSize = 14f
                            setPadding(0, 4, 0, 4)
                        }
                        layoutSimilar.addView(text)
                    }

                    val janTemp = weatherMap?.get("1")?.toString() ?: "-"
                    textWeather.text = "1월 평균 기온: ${janTemp}도"
                    switchWeather.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked && weatherMap != null) {
                            val months = weatherMap.entries.sortedBy { it.key.toString().toIntOrNull() ?: 0 }
                            textWeather.text = months.joinToString(" / ") {
                                "${it.key}월: ${it.value}도"
                            }
                        } else {
                            textWeather.text = "1월 평균 기온: ${janTemp}도"
                        }
                    }
                }
                progressBar.visibility = View.GONE
                textLoadingMessage.visibility = View.GONE
                layoutContent.visibility = View.VISIBLE
            }
            .addOnFailureListener {
                progressBar.visibility = View.GONE
                textLoadingMessage.visibility = View.GONE
                layoutContent.visibility = View.VISIBLE
                Toast.makeText(requireContext(), "도시 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }

        var isFavorite = false
        btnFavorite.setOnClickListener {
            isFavorite = !isFavorite
            btnFavorite.setImageResource(
                if (isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
            )
        }

        btnShare.setOnClickListener {
            Toast.makeText(requireContext(), "공유 기능은 추후 구현 예정", Toast.LENGTH_SHORT).show()
        }

        btnCreatePlan.setOnClickListener {
            viewModel.selectedCity.value = cityId
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LandmarkFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}