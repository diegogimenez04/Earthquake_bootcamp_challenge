package com.example.earthquakemonitor.main

import androidx.lifecycle.LiveData
import com.example.earthquakemonitor.Earthquake
import com.example.earthquakemonitor.api.EqJsonResponse
import com.example.earthquakemonitor.api.service
import com.example.earthquakemonitor.database.EqDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository(private val database: EqDatabase) {

    val eqList: LiveData<MutableList<Earthquake>> = database.eqDao.getEarthquakes()

    suspend fun fetchEarthquakes() {
        return withContext(Dispatchers.IO) {
            val eqJsonResponse = service.getLastHourEarthquakes()
            val eqList = parseEarthquakeResult(eqJsonResponse)

            database.eqDao.insertAll(eqList)
        }
    }

    private fun parseEarthquakeResult(eqJsonResponse: EqJsonResponse): MutableList<Earthquake> {
        /* Without Moshi
        val eqJSONObject = JSONObject(eqListString)
        val featuresJsonArray = eqJSONObject.getJSONArray("features")

        val eqList = mutableListOf<Earthquake>()

        for (i in 0 until featuresJsonArray.length()){
            val featuresJsonObject = featuresJsonArray[i] as JSONObject
            val id = featuresJsonObject.getString("id")

            val propertiesJsonObject = featuresJsonObject.getJSONObject("properties")
            val magnitude = propertiesJsonObject.getDouble("mag")
            val place = propertiesJsonObject.getString("place")
            val time = propertiesJsonObject.getLong("time")

            val geometryJsonObject = featuresJsonObject.getJSONObject("geometry")
            val coordinatesJsonArray = geometryJsonObject.getJSONArray("coordinates")
            val latitude = coordinatesJsonArray.getDouble(0)
            val longitude = coordinatesJsonArray.getDouble(0)

            val earthquake = Earthquake(id, place, magnitude, time, longitude, latitude)
            eqList.add(earthquake)
        }

        return  eqList
        */

        val eqList = mutableListOf<Earthquake>()
        val featureList = eqJsonResponse.features

        for (feature in featureList){
            val id = feature.id

            val properties = feature.properties
            val magnitude = properties.mag
            val time = properties.time
            val place = properties.place

            val geometry = feature.geometry
            val latitude = geometry.latitude
            val longitude = geometry.longitude

            eqList.add(Earthquake(id, place, magnitude, time, longitude, latitude))
        }


        return  eqList
    }
}