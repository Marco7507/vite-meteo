package supdevinci.vitemeteo.services

import retrofit2.http.GET
import retrofit2.http.Query
import supdevinci.vitemeteo.model.WeatherModel

interface WeatherApiService {
    //https://api.open-meteo.com/v1/forecast?latitude=44.84&longitude=-0.58&hourly=temperature_2m,relativehumidity_2m,precipitation_probability,precipitation&daily=weathercode,temperature_2m_max,temperature_2m_min,precipitation_sum&timezone=auto
    @GET("/v1/forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: Float,
        @Query("longitude") longitude: Float,
        @Query("hourly") hourly: String = "temperature_2m,relativehumidity_2m,precipitation_probability,precipitation",
        @Query("daily") daily: String = "weathercode,temperature_2m_max,temperature_2m_min,precipitation_sum",
        @Query("timezone") timezone: String = "auto"
    ): WeatherModel
}