package supdevinci.vitemeteo.view

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import supdevinci.vitemeteo.R
import supdevinci.vitemeteo.viewmodel.LocationViewModel
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import supdevinci.vitemeteo.database.CityRoomDatabase
import supdevinci.vitemeteo.databinding.ActivityMainBinding
import supdevinci.vitemeteo.viewmodel.WeatherApiViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var weatherApiViewModel: WeatherApiViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var positionButton: Button
    private lateinit var weatherText: TextView
    private lateinit var weekWeatherListView: ListView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModels
        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        locationViewModel.onChange { callWeatherApi()  }
        locationViewModel.getPosition(this)

        weatherApiViewModel = ViewModelProvider(this).get(WeatherApiViewModel::class.java)

        // View
        weekWeatherListView = findViewById(R.id.week_weather_list_view)
        //positionButton = findViewById(R.id.position_button)
        //weatherText = findViewById(R.id.weather_text)
    /*
        positionButton.setOnClickListener {
            displayToast(locationViewModel.getPositionToString())
            callWeatherApi()
        }

     */
        var tempWeek = listOf("Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche")

        var weekWeatherArrayAdapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, tempWeek)
        weekWeatherListView.adapter = weekWeatherArrayAdapter

        //weekWeatherListView.isScrollContainer = false
        // weekWeatherListView.isEnabled = false
        // Database Room
        val applicationScope = CoroutineScope(SupervisorJob())
        val database = CityRoomDatabase.getDatabase(this, applicationScope)
    }

    private fun callWeatherApi() {
        locationViewModel.position?.let { position ->
            GlobalScope.launch {
                val weather = weatherApiViewModel.getWeather(position.latitude, position.longitude)
                weather?.let {
                    runOnUiThread {
                        updateWeatherDisplay();
                    }
                }
            }
        }
    }

    private fun updateWeatherDisplay() {
        //weatherText.text = weatherApiViewModel.weather.toString()
        displayToast("weather api refresh")
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