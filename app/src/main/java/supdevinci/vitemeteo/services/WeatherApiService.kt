package supdevinci.vitemeteo.services

import retrofit2.http.GET
import retrofit2.http.Query
import supdevinci.vitemeteo.model.WeatherModel

interface WeatherApiService {
    @GET("/v1/forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: Float,
        @Query("longitude") longitude: Float
    ): WeatherModel
}