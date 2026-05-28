package com.f4.recotrip.ui.plan

import android.os.Parcel
import android.os.Parcelable

data class PlanDetail(
    val title: String,
    val dateRange: String,
    val imageResId: Int,
    val finalDays: List<FinalDay>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        mutableListOf<FinalDay>().apply {
            parcel.readList(this, FinalDay::class.java.classLoader)
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(dateRange)
        parcel.writeInt(imageResId)
        parcel.writeList(finalDays)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<PlanDetail> {
        override fun createFromParcel(parcel: Parcel): PlanDetail {
            return PlanDetail(parcel)
        }

        override fun newArray(size: Int): Array<PlanDetail?> {
            return arrayOfNulls(size)
        }
    }
}