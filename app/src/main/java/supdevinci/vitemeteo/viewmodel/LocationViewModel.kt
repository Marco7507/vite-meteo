package supdevinci.vitemeteo.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import supdevinci.vitemeteo.model.Position
import supdevinci.vitemeteo.view.MainActivity
import java.util.*

class LocationViewModel(var context: Context): ViewModel(), LocationListener {
    private val _position = MutableLiveData<Position>()
    var position: LiveData<Position> = _position
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    private var positionLocked: Boolean = false

    public fun getCurrentPosition() {
        positionLocked = false
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as MainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
        }
    }

    public fun setPosition(position: Position) {
        positionLocked = true
        _position.postValue(position)
    }

    public fun getCityName(latitude: Double, longitude: Double): String {
        var geocoder = android.location.Geocoder(context, Locale.getDefault())
        var addresses = geocoder.getFromLocation(latitude, longitude, 1)
        var cityName = "Position actuelle"
        if (addresses != null) {
            cityName = addresses[0].locality
        }
        return cityName
    }

    override fun onLocationChanged(location: Location) {
        if (positionLocked) {
            return
        }
        var cityName = getCityName(location.latitude, location.longitude)
        val nextPosition = Position(location.longitude, location.latitude, cityName)
        _position.postValue(nextPosition)
    }
}