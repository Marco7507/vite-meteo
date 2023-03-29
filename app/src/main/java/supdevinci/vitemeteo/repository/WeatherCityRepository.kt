package supdevinci.vitemeteo.repository

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import supdevinci.vitemeteo.dao.WeatherCityDao
import supdevinci.vitemeteo.entity.WeatherCityEntity

class WeatherCityRepository(private val wordDao: WeatherCityDao) {
    val allCities: Flow<List<WeatherCityEntity>> = wordDao.getWeatherCities()

    @WorkerThread
    suspend fun insert(city: WeatherCityEntity) {
        wordDao.insert(city)
    }
}