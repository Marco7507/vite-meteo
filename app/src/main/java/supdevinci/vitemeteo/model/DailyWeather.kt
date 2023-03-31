package supdevinci.vitemeteo.model

data class DailyWeather (
    val weatherCode: Int,
    val temperatureMin: Float,
    val temperatureMax: Float,
    val day: String,
    val precipitationProbability: Int,
)