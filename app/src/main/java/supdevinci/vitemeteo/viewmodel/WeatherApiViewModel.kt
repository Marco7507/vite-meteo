package supdevinci.vitemeteo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import supdevinci.vitemeteo.model.Weather
import supdevinci.vitemeteo.services.WeatherApiClient

class WeatherApiViewModel(): ViewModel() {
    private val _weather = MutableLiveData<Weather>()
    val weather: LiveData<Weather> = _weather

    private val weatherApiService = WeatherApiClient.getApiService()

    fun getWeather(latitude: Float, longitude: Float) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            try {
                val weather = weatherApiService.getWeather(latitude, longitude)
                withContext(Dispatchers.Main) {
                    _weather.postValue(weather)
                    println(weather)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

