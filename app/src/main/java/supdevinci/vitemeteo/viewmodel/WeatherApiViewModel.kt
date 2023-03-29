package supdevinci.vitemeteo.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import supdevinci.vitemeteo.model.WeatherModel
import supdevinci.vitemeteo.services.WeatherApiService
import supdevinci.vitemeteo.services.WeatherRetrofitHelper
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class WeatherApiViewModel(): ViewModel() {
    var weather: WeatherModel? = null
    private val weatherApiService = WeatherRetrofitHelper.getRetrofit().create(WeatherApiService::class.java)

    suspend fun getWeather(latitude: Float, longitude: Float): WeatherModel? {
        val scope = CoroutineScope(Job() + Dispatchers.IO)
        scope.launch {
            try {
                weather = weatherApiService.getWeather(latitude, longitude)
                println("Weather: $weather")
            } catch (e: Exception) {
                weather = null
            }
        }.join()
        return weather
    }
}

