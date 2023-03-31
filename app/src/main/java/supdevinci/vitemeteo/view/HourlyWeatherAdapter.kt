package supdevinci.vitemeteo.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import supdevinci.vitemeteo.R
import supdevinci.vitemeteo.model.HourlyWeather

class HourlyWeatherAdapter(private val items: List<HourlyWeather>): BaseAdapter() {
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): HourlyWeather {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(parent?.context).inflate(R.layout.hourly_list_item, parent, false)

        val hourlyWeather = getItem(position)

        val tvHourlyTime = view.findViewById<TextView>(R.id.tvHourlyTime)
        val ivHourlyIcon = view.findViewById<ImageView>(R.id.ivHourlyIcon)
        val tvHourlyTemp = view.findViewById<TextView>(R.id.tvHourlyTemp)

        tvHourlyTime.text = hourlyWeather.hour.toString() + "h"
        tvHourlyTemp.text = hourlyWeather.temperature.toString() + "°C"

        when (hourlyWeather.weatherCode) {
            0 -> {
                //sunny
                ivHourlyIcon.setBackgroundResource(R.drawable.sunny)
            }
            in 1..3 -> {
                //partly cloudy
                ivHourlyIcon.setBackgroundResource(R.drawable.partly_cloudy)
            }
            45, 48 -> {
                //foggy
                ivHourlyIcon.setBackgroundResource(R.drawable.cloudy)
            }
            in 51..57 -> {
                //drizzle
                ivHourlyIcon.setBackgroundResource(R.drawable.rainy)
            }
            in 60..67, in 80..83 -> {
                //rainy
                ivHourlyIcon.setBackgroundResource(R.drawable.rainy)
            }
            in 96..99 -> {
                //thunderstorm
                ivHourlyIcon.setBackgroundResource(R.drawable.thunderstrom)
            }
        }

        view.layoutParams = AbsListView.LayoutParams(
            AbsListView.LayoutParams.MATCH_PARENT,
            AbsListView.LayoutParams.WRAP_CONTENT
        )

        return view
    }

    override fun isEnabled(position: Int): Boolean {
        return false
    }
}