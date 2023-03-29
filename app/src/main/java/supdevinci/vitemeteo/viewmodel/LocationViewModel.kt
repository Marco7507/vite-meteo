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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import supdevinci.vitemeteo.model.Position
import supdevinci.vitemeteo.view.MainActivity

class LocationViewModel: ViewModel(), LocationListener {
    val text: String = "Vos coordonnées GPS sont : "
    var position: Position? = null
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    private lateinit var onChangeFunction: (Position) -> Unit

    public fun getPositionToString(): String {
        if (position == null) {
            return "Veuillez activer votre GPS"
        }
        return text + position?.longitude + " " + position?.latitude
    }

    public fun getPosition(context: Context) {
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as MainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
        }
    }

    public fun onChange(onChangeFunction: (Position) -> Unit) {
        this.onChangeFunction = onChangeFunction
    }

    override fun onLocationChanged(location: Location) {
        println("location changed: $location")
        position = Position(location.longitude.toFloat(), location.latitude.toFloat())
        this.onChangeFunction(position!!)
    }
}