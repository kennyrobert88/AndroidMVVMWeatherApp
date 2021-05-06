package com.example.weather.api


import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object WeatherAppAPIClient {
    operator fun invoke(): WeatherAPIService {
        val requestInterceptor = Interceptor{ chain ->
            val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("units", UNITS)
                    .addQueryParameter("appid",API_KEY)
                    .build()

            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()
            return@Interceptor chain.proceed(request)
        }
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .build()
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherAPIService::class.java)
    }
}