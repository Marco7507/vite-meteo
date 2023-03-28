package supdevinci.vitemeteo.view

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import supdevinci.vitemeteo.R
import supdevinci.vitemeteo.viewmodel.LocationViewModel
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import supdevinci.vitemeteo.databinding.ActivityMainBinding
import supdevinci.vitemeteo.viewmodel.WeatherApiViewModel

class MainActivity : AppCompatActivity() {
    lateinit var locationViewModel: LocationViewModel
    lateinit var weatherApiViewModel: WeatherApiViewModel
    lateinit var binding: ActivityMainBinding
    lateinit var positionButton: Button
    lateinit var weatherText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        weatherApiViewModel = ViewModelProvider(this).get(WeatherApiViewModel::class.java)

        positionButton = findViewById(R.id.position_button)
        weatherText = findViewById(R.id.weather_text)

        positionButton.setOnClickListener {
            displayToast(locationViewModel.getPositionToString())
            locationViewModel.position?.let { position ->
                weatherApiViewModel.getWeather(position.latitude, position.longitude)
                if (weatherApiViewModel.weather != null) {
                    weatherText.text = weatherApiViewModel.weather!!.hourly_units.temperature_2m
                }
            }
        }

        locationViewModel.getPosition(this)
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