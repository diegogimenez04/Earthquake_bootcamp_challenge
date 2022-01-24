package com.example.earthquakemonitor.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.earthquakemonitor.details.DetailsActivity
import com.example.earthquakemonitor.Earthquake
import com.example.earthquakemonitor.R
import com.example.earthquakemonitor.api.ApiResponseStatus
import com.example.earthquakemonitor.api.WorkerUtil
import com.example.earthquakemonitor.databinding.ActivityMainBinding

private const val SORT_TYPE_KEY = "sortType"

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.eqRecycler.layoutManager= LinearLayoutManager(this)

        WorkerUtil.scheduleSync(this)
        val sortType = getSortType()

        viewModel = ViewModelProvider(this,
            MainViewModelFactory(application, sortType))[MainViewModel::class.java]

        val adapter = EqAdapter()
        binding.eqRecycler.adapter = adapter

        viewModel.eqList.observe(this, {
            list ->
            adapter.submitList(list)
            handleEmptyView(list, binding)
        })

        viewModel.status.observe(this, {
            apiResponseStatus ->
            when (apiResponseStatus) {
                ApiResponseStatus.LOADING -> {
                    binding.eqProgressBar.visibility = View.VISIBLE
                }
                ApiResponseStatus.DONE -> {
                    binding.eqProgressBar.visibility = View.GONE
                }
                ApiResponseStatus.NO_INTERNET_CONNECTION -> {
                    binding.eqProgressBar.visibility = View.GONE
                    Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show()
                }
            }
        })

        adapter.onItemClickListener = {
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra(DetailsActivity.EARTHQUAKE_MAG, it.magnitude)
            intent.putExtra(DetailsActivity.EARTHQUAKE_LON, it.longitude)
            intent.putExtra(DetailsActivity.EARTHQUAKE_LAT, it.latitude)
            intent.putExtra(DetailsActivity.EARTHQUAKE_TIME, it.time)
            intent.putExtra(DetailsActivity.EARTHQUAKE_LOC, it.place)
            startActivity(intent)
        }
    }

    private fun getSortType(): Boolean {
        val prefs = getPreferences(MODE_PRIVATE)
        return prefs.getBoolean(SORT_TYPE_KEY, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == R.id.main_menu_sort_magnitude) {
            viewModel.reloadEarthquakesFromDb(true)
            saveSortType(true)
        } else if (itemId == R.id.main_menu_sort_time) {
            viewModel.reloadEarthquakesFromDb(false)
            saveSortType(false)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveSortType(sortByMagnitude: Boolean) {
        val prefs = getPreferences(MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean(SORT_TYPE_KEY, sortByMagnitude)
        editor.apply()
    }

    private fun handleEmptyView(it: MutableList<Earthquake>, binding: ActivityMainBinding) {
        if (it.isEmpty())
            binding.eqEmptyView.visibility = View.VISIBLE
        else
            binding.eqEmptyView.visibility = View.GONE
    }
}