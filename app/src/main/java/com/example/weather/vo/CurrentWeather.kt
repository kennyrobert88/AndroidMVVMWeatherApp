package com.example.weather.vo


data class CurrentWeather(
        val base: String,
        val cod: Int,
        val dt: Int,
        val id: Int,
        val main: Main,
        val name: String,
        val sys: Sys,
        val timezone: Int,
        val visibility: Int,
        val weather: List<Weather>,
        val wind: Wind
)