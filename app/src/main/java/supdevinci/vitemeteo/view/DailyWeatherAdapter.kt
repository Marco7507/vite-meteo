package supdevinci.vitemeteo.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import supdevinci.vitemeteo.R
import supdevinci.vitemeteo.model.DailyWeather

class DailyWeatherAdapter(private val items: List<DailyWeather>): BaseAdapter() {

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): DailyWeather {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView ?: LayoutInflater.from(parent.context).inflate(R.layout.daily_weather_item, parent, false)
        val dailyWeather: DailyWeather = getItem(position)

        if (dailyWeather != null) {
            val tvDay = view.findViewById<TextView>(R.id.tvDay)
            tvDay.text = dailyWeather.day
            val tvPrecipitation = view.findViewById<TextView>(R.id.tvPrecipitation)
            tvPrecipitation.text = dailyWeather.precipitationProbability.toString() + "%"

            val ivWeather = view.findViewById<ImageView>(R.id.ivWeather)
            when (dailyWeather.weatherCode) {
                0 -> {
                    //sunny
                    ivWeather.setBackgroundResource(R.drawable.sunny)
                }
                in 1..3 -> {
                    //partly cloudy
                    ivWeather.setBackgroundResource(R.drawable.partly_cloudy)
                }
                45, 48 -> {
                    //foggy
                    ivWeather.setBackgroundResource(R.drawable.cloudy)
                }
                in 51..57 -> {
                    //drizzle
                    ivWeather.setBackgroundResource(R.drawable.rainy)
                }
                in 60..67, in 80..83 -> {
                    //rainy
                    ivWeather.setBackgroundResource(R.drawable.rainy)
                }
                in 96..99 -> {
                    //thunderstorm
                    ivWeather.setBackgroundResource(R.drawable.thunderstrom)
                }
            }


            val tvTemperatureMin = view.findViewById<TextView>(R.id.tvTemperatureMin)
            tvTemperatureMin.text = dailyWeather.temperatureMin.toInt().toString() + "°"
            val tvTemperatureMax = view.findViewById<TextView>(R.id.tvTemperatureMax)
            tvTemperatureMax.text = dailyWeather.temperatureMax.toInt().toString() + "°"
        }
        return view
    }

    override fun isEnabled(position: Int): Boolean {
        return false
    }
}