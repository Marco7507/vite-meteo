package supdevinci.vitemeteo.model

import com.google.gson.annotations.SerializedName

class Weather {
    val latitude: Float = 0.0f
    val longitude: Float = 0.0f
    val generationtime_ms: Float = 0.0f
    val utc_offset_seconds: Int = 0
    val timezone: String = ""
    val timezone_abbreviation: String = ""
    val elevation: Float = 0.0f
    val hourly_units: HourlyUnits = HourlyUnits()
    val hourly: Hourly = Hourly()
    val daily_units: DailyUnits = DailyUnits()
    val daily: Daily = Daily()
}

data class HourlyUnits (
    val time: String = "",
    val temperature_2m: String = "",
    val precipitation_probability: String = "",
    val precipitation: String = "",
    val weathercode: String = "",
)

data class Hourly (
    val time: List<String> = listOf(),
    @SerializedName("temperature_2m") val temperature: List<Float> = listOf(),
    @SerializedName("precipitation_probability") val precipitationProbability: List<Int> = listOf(),
    @SerializedName("weathercode") val weathercode: List<Int> = listOf(),
)

data class DailyUnits (
    val time: String = "",
    val temperature_min: String = "",
    val temperature_max: String = "",
    val precipitation_probability: String = "",
    val precipitation: String = "",
    val weathercode: String = "",
)

data class Daily (
    val time: List<String> = listOf(),
    @SerializedName("temperature_2m_min") val temperatureMin: List<Float> = listOf(),
    @SerializedName("temperature_2m_max") val temperatureMax: List<Float> = listOf(),
    @SerializedName("precipitation_probability_max") val precipitationProbability: List<Int> = listOf(),
    @SerializedName("weathercode") val weathercode: List<Int> = listOf(),
)
