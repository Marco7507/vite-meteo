package supdevinci.vitemeteo.view

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import supdevinci.vitemeteo.R
import supdevinci.vitemeteo.viewmodel.LocationViewModel
import android.location.LocationListener
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import supdevinci.vitemeteo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var locationViewModel: LocationViewModel
    lateinit var binding: ActivityMainBinding
    lateinit var positionButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)

        positionButton = findViewById(R.id.position_button)

        positionButton.setOnClickListener {
            displayToast(locationViewModel.getPositionToString())
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