package com.example.earthquakemonitor.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.earthquakemonitor.Earthquake
import com.example.earthquakemonitor.api.ApiResponseStatus
import com.example.earthquakemonitor.database.getDatabase
import kotlinx.coroutines.*
import java.net.UnknownHostException

private val TAG = MainViewModel::class.java.simpleName
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val repository = MainRepository(database)

    private val _status = MutableLiveData<ApiResponseStatus>()
    val status: LiveData<ApiResponseStatus>
        get() = _status

    val eqList = repository.eqList

    init {
        viewModelScope.launch {
            try {
                _status.value = ApiResponseStatus.LOADING
                repository.fetchEarthquakes()
                _status.value = ApiResponseStatus.DONE
            } catch (e: UnknownHostException) {
                _status.value = ApiResponseStatus.NO_INTERNET_CONNECTION
                Log.d(TAG, "No internet connection.", e)
            }

        }
    }
}