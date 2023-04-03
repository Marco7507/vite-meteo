package supdevinci.vitemeteo.services

import retrofit2.http.GET
import retrofit2.http.Query
import supdevinci.vitemeteo.model.Weather

interface WeatherApiService {
    @GET("/v1/forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("hourly") hourly: String = "temperature_2m,precipitation_probability,precipitation,weathercode",
        @Query("daily") daily: String = "weathercode,temperature_2m_max,temperature_2m_min,precipitation_probability_max",
        @Query("timezone") timezone: String = "auto"
    ): Weather
}