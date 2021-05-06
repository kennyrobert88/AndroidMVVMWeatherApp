package com.example.weather.ui

import androidx.lifecycle.LiveData
import com.example.weather.api.WeatherAPIService
import com.example.weather.repository.NetworkState
import com.example.weather.repository.WeatherDataSource
import com.example.weather.vo.CurrentWeather
import io.reactivex.disposables.CompositeDisposable

class WeatherRepository (private val apiService : WeatherAPIService) {
    lateinit var weatherDataSource: WeatherDataSource

    fun fetchWeather(compositeDisposable: CompositeDisposable, location: String) : LiveData<CurrentWeather>{
        weatherDataSource = WeatherDataSource(apiService, compositeDisposable)
        weatherDataSource.fetchCurrentWeather(location)

        return weatherDataSource.downloadedCurrentResponse
    }

    fun getWeatherNetworkState(): LiveData<NetworkState>{
        return weatherDataSource.networkState
    }
}