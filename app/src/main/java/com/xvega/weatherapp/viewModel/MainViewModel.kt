package com.xvega.weatherapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xvega.weatherapp.model.ApiState
import com.xvega.weatherapp.repository.DataRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MainViewModel() : ViewModel() {
    private var repository: DataRepository = DataRepository
    private var getDataJob: Job? = null
    private val daysN = 3
    private var location = "55.7569,37.6151"

    private val _weatherData = MutableStateFlow<ApiState>(ApiState.Empty)
    val weatherData = _weatherData.asStateFlow()

    fun getInitialData() {
        if (_weatherData.value is ApiState.Success) return
        else refreshData()
    }

    fun refreshData() {
        if (getDataJob?.isActive == true) {
            return
        }
        getDataJob = viewModelScope.launch {
            if (_weatherData.value !is ApiState.Success) {
                _weatherData.value = ApiState.Loading
            }
            repository.getWeatherData(location, daysN)
                .catch { e ->
                    _weatherData.value = ApiState.Failure(e)
                }.collect { data ->
                    _weatherData.value = ApiState.Success(data)
                }
        }
    }
}