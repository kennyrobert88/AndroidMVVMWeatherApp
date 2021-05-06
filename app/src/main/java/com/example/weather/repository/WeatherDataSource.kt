package com.example.weather.repository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weather.api.WeatherAPIService
import com.example.weather.vo.CurrentWeather
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import java.lang.Exception

class WeatherDataSource(private val apiService: WeatherAPIService, private val compositeDisposable: CompositeDisposable) {

    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeather>()
    val downloadedCurrentResponse: LiveData<CurrentWeather>
        get()= _downloadedCurrentWeather

    private val _networkState  = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get()= _networkState

    fun fetchCurrentWeather(location: String) {
        try{
            _networkState.postValue(NetworkState.LOADING)

            compositeDisposable.add(
                apiService.getCurrentWeather(location)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                            {
                                _downloadedCurrentWeather.postValue(it)
                                _networkState.postValue(NetworkState.LOADED)
                            },
                            {
                                _networkState.postValue(NetworkState.ERROR)
                            }
                    )
            )
        }
        catch (e: Exception){

        }
    }
}