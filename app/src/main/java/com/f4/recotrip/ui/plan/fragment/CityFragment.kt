package com.f4.recotrip.ui.plan.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.marginStart
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.f4.recotrip.R
import com.f4.recotrip.databinding.PlanFragmentCityBinding
import com.f4.recotrip.ui.plan.PlanViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import com.f4.recotrip.ui.plan.CityData

class CityFragment : Fragment() {

    private var _binding: PlanFragmentCityBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlanViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PlanFragmentCityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.cachedCityList != null) {
            addCityViews(viewModel.cachedCityList!!)
        } else {
            binding.progressBarCityLoading.visibility = View.VISIBLE
            lifecycleScope.launch {
                try {
                    val cities = fetchRecommendedCities()
                    viewModel.cachedCityList = cities
                    addCityViews(cities)
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "도시 추천 실패: ${e.message}", Toast.LENGTH_SHORT).show()
                } finally {
                    binding.progressBarCityLoading.visibility = View.GONE
                }
            }
        }
    }

    private suspend fun fetchRecommendedCities(): List<CityData> {
        val client = OkHttpClient()
        val keywords = viewModel.keywords.value ?: emptyList()

        val json = JSONObject().apply {
            put("keywords", JSONArray(keywords))
        }

        val requestBody = json.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("https://recotrip-backend-production.up.railway.app/api/recommend/city")
            .post(requestBody)
            .build()

        val cityList = mutableListOf<CityData>()

        withContext(Dispatchers.IO) {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                val body = response.body?.string() ?: "{}"
                val cityJson = JSONObject(body).getJSONArray("cities")

                for (i in 0 until cityJson.length()) {
                    val obj = cityJson.getJSONObject(i)
                    val id = obj.getString("id")
                    val nameKo = obj.getString("name_ko")
                    val countryName = obj.getString("country")
                    val keywords = mutableListOf<String>()
                    val keywordsArray = obj.getJSONArray("keywords")
                    for (j in 0 until keywordsArray.length()) {
                        keywords.add(keywordsArray.getString(j))
                    }
                    cityList.add(CityData(id, nameKo, keywords, countryName))
                }
            }
        }
        return cityList
    }

    private fun addCityViews(cities: List<CityData>) {
        binding.layoutRecommendedCities.removeAllViews()
        val context = requireContext()

        cities.forEachIndexed { index, city ->
            if (index > 0) {
                val divider = View(context).apply {
                    setBackgroundColor(0xFFDDDDDD.toInt())
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1 // height of the divider
                    )
                }
                binding.layoutRecommendedCities.addView(divider)
            }
            val cityLayout = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 24, 0, 24)
                }
            }

            val icon = ImageView(context).apply {
                setImageResource(R.drawable.ic_location_pin)
                layoutParams = LinearLayout.LayoutParams(
                    60, // width
                    60  // height1
                ).apply {
                    setMargins(16, 0, 32, 0)
                    gravity = Gravity.CENTER_VERTICAL
                }
            }

            val cityTextLayout = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }

            val cityName = TextView(context).apply {
                text = city.nameKo
                textSize = 18f
                setTypeface(typeface, android.graphics.Typeface.BOLD)
                setTextColor(0xFF000000.toInt())
            }

            val countryName = TextView(context).apply {
                text = city.countryName
                textSize = 13f
                setTextColor(0xFFAAAAAA.toInt())
            }

            cityTextLayout.addView(cityName)
            cityTextLayout.addView(countryName)

            cityLayout.addView(icon)
            cityLayout.addView(cityTextLayout)

            cityLayout.setOnClickListener {
                binding.progressBarCityLoading.visibility = View.VISIBLE
                val fragment = CityDetailFragment()
                val args = Bundle().apply {
                    putString("city_id", city.id)
                }
                fragment.arguments = args

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }

            binding.layoutRecommendedCities.addView(cityLayout)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}