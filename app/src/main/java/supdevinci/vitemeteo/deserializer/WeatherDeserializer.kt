package supdevinci.vitemeteo.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import supdevinci.vitemeteo.model.WeatherModel
import java.lang.reflect.Type

class WeatherDeserializer: JsonDeserializer<WeatherModel> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): WeatherModel {
        val jsonObject = json!!.asJsonObject

        val weather = WeatherModel()

        weather.latitude = jsonObject.get("latitude").asFloat
        weather.longitude = jsonObject.get("longitude").asFloat
        weather.generationtime_ms = jsonObject.get("generationtime_ms").asFloat
        weather.utc_offset_seconds = jsonObject.get("utc_offset_seconds").asInt
        weather.timezone = jsonObject.get("timezone").asString
        weather.timezone_abbreviation = jsonObject.get("timezone_abbreviation").asString
        weather.elevation = jsonObject.get("elevation").asFloat
        weather.hourly_units.time = jsonObject.get("hourly_units").asJsonObject.get("time").asString
        weather.hourly_units.temperature_2m = jsonObject.get("hourly_units").asJsonObject.get("temperature_2m").asString
        weather.hourly_units.relativehumidity_2m = jsonObject.get("hourly_units").asJsonObject.get("relativehumidity_2m").asString
        weather.hourly_units.precipitation_probability = jsonObject.get("hourly_units").asJsonObject.get("precipitation_probability").asString
        weather.hourly_units.precipitation = jsonObject.get("hourly_units").asJsonObject.get("precipitation").asString
        weather.hourly.time = jsonObject.get("hourly").asJsonObject.get("time").asJsonArray.map { it.asString }
        weather.hourly.temperature_2m = jsonObject.get("hourly").asJsonObject.get("temperature_2m").asJsonArray.map { it.asFloat }
        weather.hourly.relativehumidity_2m = jsonObject.get("hourly").asJsonObject.get("relativehumidity_2m").asJsonArray.map { it.asFloat }
        weather.hourly.precipitation_probability = jsonObject.get("hourly").asJsonObject.get("precipitation_probability").asJsonArray.map { it.asFloat }
        weather.hourly.precipitation = jsonObject.get("hourly").asJsonObject.get("precipitation").asJsonArray.map { it.asFloat }
        weather.daily_units.time = jsonObject.get("daily_units").asJsonObject.get("time").asString
        weather.daily_units.weathercode = jsonObject.get("daily_units").asJsonObject.get("weathercode").asString
        weather.daily_units.temperature_2m_max = jsonObject.get("daily_units").asJsonObject.get("temperature_2m_max").asString
        weather.daily_units.temperature_2m_min = jsonObject.get("daily_units").asJsonObject.get("temperature_2m_min").asString
        weather.daily_units.precipitation_sum = jsonObject.get("daily_units").asJsonObject.get("precipitation_sum").asString
        weather.daily.time = jsonObject.get("daily").asJsonObject.get("time").asJsonArray.map { it.asString }
        weather.daily.weathercode = jsonObject.get("daily").asJsonObject.get("weathercode").asJsonArray.map { it.asInt }
        weather.daily.temperature_2m_max = jsonObject.get("daily").asJsonObject.get("temperature_2m_max").asJsonArray.map { it.asFloat }
        weather.daily.temperature_2m_min = jsonObject.get("daily").asJsonObject.get("temperature_2m_min").asJsonArray.map { it.asFloat }
        weather.daily.precipitation_sum = jsonObject.get("daily").asJsonObject.get("precipitation_sum").asJsonArray.map { it.asFloat }

        return weather
    }
}