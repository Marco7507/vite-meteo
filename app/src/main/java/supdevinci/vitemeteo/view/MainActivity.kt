package supdevinci.vitemeteo.view

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import supdevinci.vitemeteo.R
import supdevinci.vitemeteo.viewmodel.LocationViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import supdevinci.vitemeteo.database.CityRoomDatabase
import supdevinci.vitemeteo.model.DailyWeather
import supdevinci.vitemeteo.model.HourlyWeather
import supdevinci.vitemeteo.viewmodel.WeatherApiViewModel
import java.time.LocalDate
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var weatherApiViewModel: WeatherApiViewModel
    private lateinit var weekWeatherListView: ListView
    private lateinit var hourlyWeatherListView: RecyclerView
    private lateinit var tvCityName: TextView
    private lateinit var tvCurrentTemp: TextView
    private lateinit var ivWeatherIcon: ImageView
    private lateinit var tvTemperatureMinMax: TextView
    private var loading = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(binding.root)
        setContentView(R.layout.loader)

        // ViewModels
        locationViewModel = LocationViewModel(this)
        locationViewModel.position.observe(this) { position ->
            position?.let {
                callWeatherApi()
            }
        }
        locationViewModel.getCurrentPosition()

        weatherApiViewModel = ViewModelProvider(this).get(WeatherApiViewModel::class.java)
        weatherApiViewModel.weather.observe(this) { weather ->
            weather?.let {
                if (loading) {
                    loading = false
                    initViews()
                }
                displayToast("Weather updated")
                updateWeatherDisplay()
            }
        }

        // Database Room
        val applicationScope = CoroutineScope(SupervisorJob())
        val database = CityRoomDatabase.getDatabase(this, applicationScope)
    }

    private fun initViews() {
        setContentView(R.layout.activity_main)

        weekWeatherListView = findViewById(R.id.week_weather_list_view)
        hourlyWeatherListView = findViewById(R.id.hourly_weather_list_view)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        hourlyWeatherListView.layoutManager = layoutManager
        tvCityName = findViewById(R.id.city_name)
        tvCurrentTemp = findViewById(R.id.current_temp)
        ivWeatherIcon = findViewById(R.id.weather_icon)
        tvTemperatureMinMax = findViewById(R.id.temperature_min_max)
    }

    private fun callWeatherApi() {
        locationViewModel.position.value?.let { position ->
            val scope = CoroutineScope(Dispatchers.Main)
            scope.launch {
                weatherApiViewModel.getWeather(position.latitude, position.longitude)
            }
        }
    }

    private fun updateWeatherDisplay() {
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            weatherApiViewModel.weather.value?.let {
                //weatherText.text = it.toString()
                val weather = weatherApiViewModel.weather.value

                var dailyWeatherList: MutableList<DailyWeather> = mutableListOf()
                weather?.daily?.let { daily ->
                    for (i in 0 until daily.time.size) {

                        val localDate = LocalDate.parse(daily.time[i])

                        var day = localDate.dayOfWeek.name.lowercase()
                        // get french day name


                        if (i == 0) {
                            day = "Aujourd'hui"
                        }
                        val dailyWeather = DailyWeather(
                            daily.weathercode[i],
                            daily.temperatureMin[i],
                            daily.temperatureMax[i],
                            day,
                            daily.precipitationProbability[i],
                        )
                        dailyWeatherList.add(dailyWeather)
                    }
                }

                val dailyWeatherAdapter = DailyWeatherAdapter(dailyWeatherList)
                weekWeatherListView.adapter = dailyWeatherAdapter

                val hourlyWeatherList: MutableList<HourlyWeather> = mutableListOf()
                weather?.hourly?.let { hourly ->
                    for (i in 0 until hourly.time.size) {
                        //get hour from time
                        val hour = hourly.time[i].substring(11, 13)

                        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                        if (hour.toInt() < currentHour) {
                            continue
                        }

                        val hourlyWeather = HourlyWeather(
                            hourly.weathercode[i],
                            hourly.temperature[i].toInt(),
                            hour
                        )
                        hourlyWeatherList.add(hourlyWeather)
                    }
                }

                val hourlyWeatherAdapter = HourlyWeatherAdapter(hourlyWeatherList)
                hourlyWeatherListView.adapter = hourlyWeatherAdapter

                var currentTempIndex = 0;
                val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                for (i in 0 until hourlyWeatherList.size) {
                    if (hourlyWeatherList[i].hour.toInt() >= currentHour) {
                        currentTempIndex = i
                        break
                    }
                }

                val currentWeather = hourlyWeatherList[currentTempIndex]

                tvCityName.text = locationViewModel.position.value?.cityName

                tvCurrentTemp.text = currentWeather.temperature.toString() + "°"

                when (currentWeather.weatherCode) {
                    0 -> {
                        ivWeatherIcon.setBackgroundResource(R.drawable.sunny)
                    }
                    in 1..3 -> {
                        ivWeatherIcon.setBackgroundResource(R.drawable.partly_cloudy)
                    }
                    45, 48 -> {
                        ivWeatherIcon.setBackgroundResource(R.drawable.cloudy)
                    }
                    in 51..57 -> {
                        ivWeatherIcon.setBackgroundResource(R.drawable.rainy)
                    }
                    in 60..67, in 80..83 -> {
                        ivWeatherIcon.setBackgroundResource(R.drawable.rainy)
                    }
                    in 96..99 -> {
                        ivWeatherIcon.setBackgroundResource(R.drawable.stromy)
                    }
                }

                tvTemperatureMinMax.text = dailyWeatherList[0].temperatureMax.toInt().toString() + "°/ " + dailyWeatherList[0].temperatureMin.toInt().toString() + "°"
            }
        }
    }

    private fun displayToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * check if the app has the permission to access the location
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 2) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationViewModel.getCurrentPosition()
            } else {
                displayToast("Permission refusée")
            }
        }
    }

}