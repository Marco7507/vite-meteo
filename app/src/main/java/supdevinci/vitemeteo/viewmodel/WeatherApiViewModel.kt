package supdevinci.vitemeteo.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import supdevinci.vitemeteo.model.WeatherModel
import supdevinci.vitemeteo.services.WeatherApiService
import supdevinci.vitemeteo.services.WeatherRetrofitHelper

class WeatherApiViewModel(): ViewModel() {
    var weather: WeatherModel? = null
    private val weatherApiService = WeatherRetrofitHelper.getRetrofit().create(WeatherApiService::class.java)

    fun getWeather(latitude: Float, longitude: Float) {
        val scope = CoroutineScope(Job() + Dispatchers.IO)
        scope.launch {
            try {
                weather = weatherApiService.getWeather(latitude, longitude)
            } catch (e: Exception) {
                weather = null
                println(e)
            }
        }
    }
}