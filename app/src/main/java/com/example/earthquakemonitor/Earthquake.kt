package com.example.earthquakemonitor

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "earthquakes") @Parcelize
data class Earthquake(@PrimaryKey val id: String, val place: String, val magnitude: Double, val time: Long,
                      val longitude: Double, val latitude: Double):
    Parcelable