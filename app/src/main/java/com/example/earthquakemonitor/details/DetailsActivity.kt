package com.example.earthquakemonitor.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.earthquakemonitor.databinding.ActivityDetailsBinding
import java.text.SimpleDateFormat
import java.util.*

class DetailsActivity : AppCompatActivity() {
    companion object {
        const val EARTHQUAKE_MAG = "mag"
        const val EARTHQUAKE_LON = "longitude"
        const val EARTHQUAKE_LAT = "latitude"
        const val EARTHQUAKE_LOC = "location"
        const val EARTHQUAKE_TIME = "time"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras

        var magnitude: Double = 0.0
        var latitude: Double = 0.0
        var longitude: Double = 0.0
        var place: String = ""
        var time: Long = 0

        if (bundle != null) {
            magnitude = bundle.getDouble(EARTHQUAKE_MAG)
            latitude = bundle.getDouble(EARTHQUAKE_LAT)
            longitude = bundle.getDouble(EARTHQUAKE_LON)
            place = bundle.getString(EARTHQUAKE_LOC).toString()
            time = bundle.getLong(EARTHQUAKE_TIME)
        } else {
            Toast.makeText(this, "Error while fetching data", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        val simpleDateFormat = SimpleDateFormat("dd/MMM/yyyy HH:mm:ss", Locale.getDefault())
        val date = Date(time)
        val formattedString = simpleDateFormat.format(date)

        binding.eqDetailLatitude.text = latitude.toString()
        binding.eqDetailLongitude.text = longitude.toString()
        binding.eqDetailMagnitude.text = magnitude.toString()
        binding.eqDetailPlace.text = place
        binding.eqDetailTime.text = formattedString
    }
}