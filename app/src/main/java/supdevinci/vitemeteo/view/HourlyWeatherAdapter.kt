package supdevinci.vitemeteo.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import supdevinci.vitemeteo.R
import supdevinci.vitemeteo.model.HourlyWeather

class HourlyWeatherAdapter(private val items: List<HourlyWeather>): RecyclerView.Adapter<HourlyWeatherAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvHourlyTime: TextView = view.findViewById(R.id.tvHourlyTime)
        val ivHourlyIcon: ImageView = view.findViewById(R.id.ivHourlyIcon)
        val tvHourlyTemp: TextView = view.findViewById(R.id.tvHourlyTemp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hourly_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hourlyWeather = items[position]
        holder.tvHourlyTime.text = hourlyWeather.hour.toString() + "h"
        holder.tvHourlyTemp.text = hourlyWeather.temperature.toString() + "°C"


        when (hourlyWeather.weatherCode) {
            0 -> {
                holder.ivHourlyIcon.setBackgroundResource(R.drawable.sunny)
            }
            in 1..3 -> {
                holder.ivHourlyIcon.setBackgroundResource(R.drawable.partly_cloudy)
            }
            45, 48 -> {
                holder.ivHourlyIcon.setBackgroundResource(R.drawable.cloudy)
            }
            in 51..57 -> {
                holder.ivHourlyIcon.setBackgroundResource(R.drawable.rainy)
            }
            in 60..67, in 80..83 -> {
                holder.ivHourlyIcon.setBackgroundResource(R.drawable.rainy)
            }
            in 96..99 -> {
                holder.ivHourlyIcon.setBackgroundResource(R.drawable.stormy)
            }
        }
    }

    override fun getItemCount() = items.size
}