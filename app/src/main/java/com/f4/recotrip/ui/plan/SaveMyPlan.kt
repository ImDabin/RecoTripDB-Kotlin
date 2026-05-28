package com.f4.recotrip.ui.plan

import com.f4.recotrip.R

data class SaveMyPlan(
    val title: String,
    val dateRange: String,
    val routePreviewResId: Int,
    val dailyPlans: List<List<String>> // [ ["용두암", "한라수목원"], ["성산일출봉", "섭지코지"], ... ]
) {
    companion object {
        fun getDummyPlans(): List<SaveMyPlan> {
            return listOf(
                SaveMyPlan(
                    title = "제주도 3박 4일 여행",
                    dateRange = "2025.05.10 ~ 2025.05.13",
                    routePreviewResId = R.drawable.route_map_dummy_a,
                    dailyPlans = listOf(
                        listOf("용두암", "한라수목원"),
                        listOf("성산일출봉", "섭지코지"),
                        listOf("이중섭거리", "천지연폭포")
                    )
                ),
                SaveMyPlan(
                    title = "부산 2박 3일 여행",
                    dateRange = "2025.06.01 ~ 2025.06.03",
                    routePreviewResId = R.drawable.route_map_dummy_b,
                    dailyPlans = listOf(
                        listOf("해운대", "광안리"),
                        listOf("감천문화마을", "자갈치시장")
                    )
                )
            )
        }
    }
}