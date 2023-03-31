package supdevinci.vitemeteo.model

data class HourlyWeather (
    val weatherCode: Int,
    val temperature: Float,
    val hour: String
    )