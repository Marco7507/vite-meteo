package supdevinci.vitemeteo.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherApiClient {

    private val baseUrl = "https://api.open-meteo.com/"

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    public fun getApiService(): WeatherApiService {
        return getRetrofit().create(WeatherApiService::class.java)
    }
}
