package com.namnam.lab05

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("weather")
    fun getWeather(
        @Query("q") location: String,
        @Query("appid") apiKey: String
    ): Call<WeatherResponse>

    @GET("forecast")
    fun getForecast(
        @Query("q") location: String,
        @Query("appid") apiKey: String
    ): Call<ForecastResponse>
}