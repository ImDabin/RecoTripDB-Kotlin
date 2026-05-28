package com.f4.recotrip.ui.plan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlanViewModel : ViewModel() {
    val selectedPlaces = MutableLiveData<MutableList<LandmarkData>>(mutableListOf())
    val keywords = MutableLiveData<MutableList<String>>(mutableListOf())
    var cachedCityList: List<CityData>? = null

    val selectedCity = MutableLiveData<String>()
    val selectedPeople = MutableLiveData<Pair<Int, Int>>(Pair(0, 0))
    val travelDates = MutableLiveData<Pair<String, String>>()

    val departureTimeGoing = MutableLiveData<String>()
    val arrivalTimeGoing = MutableLiveData<String>()
    val departureTimeReturning = MutableLiveData<String>()
    val arrivalTimeReturning = MutableLiveData<String>()

    private val _lodgingData = MutableLiveData<List<LodgingDay>>()
    val lodgingData: LiveData<List<LodgingDay>> = _lodgingData

    fun setLodgingData(data: List<LodgingDay>) {
        _lodgingData.value = data
    }

    val totalTripDays: Int
        get() = travelDates.value?.let { (start, end) ->
            3 // 임시
        } ?: 3

    private val _selectedLodgings = MutableLiveData<MutableList<LodgingItem?>>()
    val selectedLodgings: LiveData<MutableList<LodgingItem?>> = _selectedLodgings

    fun initializeSelectedLodgings(dayCount: Int) {
        _selectedLodgings.value = MutableList(dayCount) { null }
    }

    fun selectLodgingForDay(dayIndex: Int, lodgingItem: LodgingItem) {
        _selectedLodgings.value?.let {
            it[dayIndex] = lodgingItem
            _selectedLodgings.value = it.toMutableList()
        }
    }

    // ✅ 추가: 선택된 플랜 저장
    val selectedRoutePlan = MutableLiveData<String>()

    private val _finalDays = MutableLiveData<List<FinalDay>>()
    val finalDays: LiveData<List<FinalDay>> = _finalDays

    fun setFinalDays(days: List<FinalDay>) {
        _finalDays.value = days
    }
}