package supdevinci.vitemeteo.view

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import supdevinci.vitemeteo.R
import supdevinci.vitemeteo.viewmodel.LocationViewModel
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.*
import supdevinci.vitemeteo.database.CityRoomDatabase
import supdevinci.vitemeteo.databinding.ActivityMainBinding
import supdevinci.vitemeteo.model.DailyWeather
import supdevinci.vitemeteo.model.HourlyWeather
import supdevinci.vitemeteo.viewmodel.WeatherApiViewModel
import java.time.LocalDate
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var weatherApiViewModel: WeatherApiViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var positionButton: Button
    private lateinit var weatherText: TextView
    private lateinit var weekWeatherListView: ListView
    private lateinit var hourlyWeatherListView: ListView
    private var loading = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(binding.root)
        setContentView(R.layout.loader)

        // ViewModels
        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        locationViewModel.onChange { callWeatherApi()  }
        locationViewModel.getPosition(this)

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
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        weekWeatherListView = findViewById(R.id.week_weather_list_view)
        hourlyWeatherListView = findViewById(R.id.hourly_weather_list_view)
    }

    private fun callWeatherApi() {
        locationViewModel.position?.let { position ->
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
                        val day = localDate.dayOfWeek.toString()
                        val dayCap = day.substring(0, 1).uppercase(Locale.ROOT) + day.substring(1).lowercase(Locale.ROOT)
                        val dailyWeather = DailyWeather(
                            daily.weathercode[i],
                            daily.temperatureMin[i],
                            daily.temperatureMax[i],
                            dayCap,
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

                        val hourlyWeather = HourlyWeather(
                            hourly.weathercode[i],
                            hourly.temperature[i],
                            hour
                        )
                        hourlyWeatherList.add(hourlyWeather)
                    }
                }

                val hourlyWeatherAdapter = HourlyWeatherAdapter(hourlyWeatherList)
                hourlyWeatherListView.adapter = hourlyWeatherAdapter
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
                locationViewModel.getPosition(this)
            } else {
                displayToast("Permission refusée")
            }
        }
    }

}