package com.namnam.lab05

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat


class ForecastAdapter(private val forecastList: List<ForecastResponse.ListItem>) :
    RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    class ForecastViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvForecastItem: TextView = view.findViewById(R.id.tv_forecast_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.forecast_item, parent, false)
        return ForecastViewHolder(view)
    }

    override fun getItemCount() = forecastList.size

    private fun kelvinToCelsius(kelvin: Double): Double {
        return kelvin - 273.15
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val item = forecastList[position]
        val df = DecimalFormat("#.##")
        val temp = kelvinToCelsius(item.main.temp)
        val feelsLike = kelvinToCelsius(item.main.feelsLike)
        val humidity = item.main.humidity
        val description = item.weather[0].description
        val wind = item.wind.speed
        val clouds = item.clouds.all
        val pressure = item.main.pressure
        val dtTxt = item.dtTxt

        val forecastOutput = """
            Date: $dtTxt
            Temp: ${df.format(temp)} °C
            Feels Like: ${df.format(feelsLike)} °C
            Humidity: $humidity%
            Description: $description
            Wind Speed: $wind m/s
            Cloudiness: $clouds%
            Pressure: $pressure hPa
        """.trimIndent()

        holder.tvForecastItem.text = forecastOutput
    }

}