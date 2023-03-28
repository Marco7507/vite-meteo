package supdevinci.vitemeteo.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModel
import supdevinci.vitemeteo.model.Position
import supdevinci.vitemeteo.view.MainActivity

class LocationViewModel: ViewModel(), LocationListener {
    val text: String = "Vos coordonnées GPS sont : "
    var position: Position? = Position(3.0f, 5.0f)
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

    public fun getPositionToString(): String {
        return text + position?.longitude + " " + position?.latitude
    }

    /**
     * get the user's position
     */
    public fun getPosition(context: Context) {
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as MainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
        }
    }

    override fun onLocationChanged(location: Location) {
        position = Position(location.longitude.toFloat(), location.latitude.toFloat())
    }
}