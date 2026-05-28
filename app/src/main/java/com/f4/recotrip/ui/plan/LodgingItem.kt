package com.f4.recotrip.ui.plan

data class LodgingItem(
    val id: String,
    val name: String,
    val address: String,
    val description: String,
    val price: String,
    val imageResId: Int,
    var isSelected: Boolean = false
)