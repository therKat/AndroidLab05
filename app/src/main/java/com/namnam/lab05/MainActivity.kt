package com.namnam.lab05

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.namnam.lab05.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val apiKey = "0035a7e6ea68d8b651e805560ec627cc"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGetWeather.setOnClickListener {
            val city = binding.etInputCity.text.toString()
            val country = binding.etInputCountry.text.toString()
            if (city.isNotEmpty() && country.isNotEmpty()) {
                fetchWeatherData("$city,$country")
                fetchForecastData("$city,$country")
            }
        }

        binding.rvForecast.layoutManager = LinearLayoutManager(this)
    }

    private fun fetchWeatherData(location: String) {
        val weatherService = RetrofitClient.instance.create(WeatherService::class.java)
        val call = weatherService.getWeather(location, apiKey)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    weatherResponse?.let {
                        val df = DecimalFormat("#.##")
                        val cityName = it.name
                        val countryName = it.sys.country
                        val timezone = it.timezone / 3600
                        val temp = kelvinToCelsius(it.main.temp)
                        val feelsLike = kelvinToCelsius(it.main.feels_like)
                        val humidity = it.main.humidity
                        val description = it.weather[0].description
                        val wind = it.wind.speed
                        val clouds = it.clouds.all
                        val pressure = it.main.pressure

                        val output = "Current weather of $cityName ($countryName)\n" +
                                "Timezone GMT ${if (timezone >= 0) "+" else "-"}$timezone\n" +
                                "Temp: ${df.format(temp)} °C\n" +
                                "Feels Like: ${df.format(feelsLike)} °C\n" +
                                "Humidity: $humidity%\n" +
                                "Description: $description\n" +
                                "Wind Speed: $wind m/s (meters per second)\n" +
                                "Cloudiness: $clouds%\n" +
                                "Pressure: $pressure hPa"

                        binding.tvWeatherResult.text = output
                    }
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.e("Weather", "Error: ${t.message}")
            }
        })
    }

    private fun fetchForecastData(location: String) {
        val weatherService = RetrofitClient.instance.create(WeatherService::class.java)
        val call = weatherService.getForecast(location, apiKey)

        call.enqueue(object : Callback<ForecastResponse> {
            override fun onResponse(call: Call<ForecastResponse>, response: Response<ForecastResponse>) {
                if (response.isSuccessful) {
                    val forecastResponse = response.body()
                    forecastResponse?.let {
                        val forecastList = it.list
                        val forecastAdapter = ForecastAdapter(forecastList)
                        binding.rvForecast.adapter = forecastAdapter
                    }
                }
            }

            override fun onFailure(call: Call<ForecastResponse>, t: Throwable) {
                Log.e("MainActivity", "Failed to get forecast data", t)
            }
        })
    }

    private fun kelvinToCelsius(kelvin: Double): Double {
        return kelvin - 273.15
    }
}