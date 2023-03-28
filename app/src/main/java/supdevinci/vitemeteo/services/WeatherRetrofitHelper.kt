package supdevinci.vitemeteo.services

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import supdevinci.vitemeteo.deserializer.WeatherDeserializer
import supdevinci.vitemeteo.model.WeatherModel

object WeatherRetrofitHelper {

    val baseUrl = "https://api.open-meteo.com/"

    fun getRetrofit(): Retrofit {
        val WeatherDeserializer = WeatherDeserializer()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
