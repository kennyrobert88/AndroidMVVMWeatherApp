package com.example.weather.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.weather.repository.NetworkState
import com.example.weather.vo.CurrentWeather
import io.reactivex.disposables.CompositeDisposable

class MainViewViewModel(private val weatherRepository: WeatherRepository, location: String): ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val weather : LiveData<CurrentWeather> by lazy{
        weatherRepository.fetchWeather(compositeDisposable, location)
    }

    val networkState: LiveData<NetworkState> by lazy {
        weatherRepository.getWeatherNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}