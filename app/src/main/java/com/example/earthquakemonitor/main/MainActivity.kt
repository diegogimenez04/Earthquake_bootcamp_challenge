package com.example.earthquakemonitor.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.earthquakemonitor.details.DetailsActivity
import com.example.earthquakemonitor.Earthquake
import com.example.earthquakemonitor.api.ApiResponseStatus
import com.example.earthquakemonitor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.eqRecycler.layoutManager= LinearLayoutManager(this)
        val viewModel = ViewModelProvider(this,
            MainViewModelFactory(application))[MainViewModel::class.java]

        val adapter = EqAdapter()
        binding.eqRecycler.adapter = adapter

        viewModel.eqList.observe(this, {
            list ->
            adapter.submitList(list)
            handleEmptyView(list, binding)
        })

        viewModel.status.observe(this, {
            apiResponseStatus ->
            if(apiResponseStatus == ApiResponseStatus.LOADING){
                binding.eqProgressBar.visibility = View.VISIBLE
            } else if (apiResponseStatus == ApiResponseStatus.DONE) {
                binding.eqProgressBar.visibility = View.GONE
            } else if (apiResponseStatus == ApiResponseStatus.NO_INTERNET_CONNECTION){
                binding.eqProgressBar.visibility = View.GONE
                Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show()
            }
        })

        adapter.onItemClickListener = {
            //Toast.makeText(this, it.place, Toast.LENGTH_SHORT).show()
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra(DetailsActivity.EARTHQUAKE_MAG, it.magnitude)
            intent.putExtra(DetailsActivity.EARTHQUAKE_LON, it.longitude)
            intent.putExtra(DetailsActivity.EARTHQUAKE_LAT, it.latitude)
            intent.putExtra(DetailsActivity.EARTHQUAKE_TIME, it.time)
            intent.putExtra(DetailsActivity.EARTHQUAKE_LOC, it.place)
            startActivity(intent)
        }
    }

    private fun handleEmptyView(it: MutableList<Earthquake>, binding: ActivityMainBinding) {
        if (it.isEmpty())
            binding.eqEmptyView.visibility = View.VISIBLE
        else
            binding.eqEmptyView.visibility = View.GONE
    }
}