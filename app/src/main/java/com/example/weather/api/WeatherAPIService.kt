package com.example.weather.api

import com.example.weather.vo.CurrentWeather
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "f74e06d3c96ba9d2203ed84f35d7dd77"
const val UNITS = "metric"
const val URL = "https://api.openweathermap.org/data/2.5/"
// Using request interceptor
// http://api.weatherstack.com/current?access_key=e68cd860dc2132184d038a38a05ec5cb&query=New%20York

// Comparing to the MovieDB, we are combining the kt and Interface File
interface WeatherAPIService {
    @GET("weather")
    fun getCurrentWeather(
            @Query("q") location: String
    ): Single<CurrentWeather>
}

