package supdevinci.vitemeteo.model

data class HourlyWeather (
    val weatherCode: Int,
    val temperature: Int,
    val hour: String
    )