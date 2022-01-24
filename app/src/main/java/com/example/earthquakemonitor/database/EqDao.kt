package com.example.earthquakemonitor.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.earthquakemonitor.Earthquake

@Dao
interface EqDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(eqList: MutableList<Earthquake>)

    @Query("SELECT * FROM earthquakes")
    fun getEarthquakes(): MutableList<Earthquake>

    @Query("SELECT * FROM earthquakes ORDER BY magnitude ASC")
    fun getEarthquakesByMagnitude(): MutableList<Earthquake>

    @Query("SELECT * FROM earthquakes ORDER BY time ASC")
    fun getEarthquakesByTime(): MutableList<Earthquake>

    @Query("SELECT * FROM earthquakes WHERE magnitude > :mag")
    fun getEarthquakesWithMagnitude(mag: Double): MutableList<Earthquake>

    @Update
    fun updateEq(vararg eq: Earthquake)

    @Delete
    fun deleteEq(vararg eq: Earthquake)
}