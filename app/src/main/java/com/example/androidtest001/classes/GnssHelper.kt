package com.example.androidtest001.classes

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.example.androidtest001.MainActivity

/*
 Global Navigation Satellite System
 */
class GnssHelper {
  private var mainActivity: MainActivity? = null
  private var context: Context? = null
  private var lm: LocationManager? = null
  private var listener: GnssListener = GnssListener()

  fun initialise(activity: MainActivity) {
    mainActivity = activity
    context = mainActivity!!.applicationContext
    setupLocationService()
  }

  private fun setupLocationService() {
    if (hasLocationPermission()) {
      lm = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
      setupLocationUpdates()
    } else requestLocationPermission()
  }

  private fun requestCurrentLocation() {
  }

  @SuppressLint("MissingPermission")  // should already be checked
  private fun setupLocationUpdates() {
    if (isLocationEnabled()) {
      lm!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000.toLong(), 1.toFloat(), listener)
    }
  }

  private fun isLocationEnabled(): Boolean {
    if (lm == null) return false
    return lm!!.isLocationEnabled
  }

  private fun hasLocationPermission(): Boolean {
    val coarsePerms = ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION)
    val finePerms = ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION)
    val bgPerms = ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    return (finePerms == PackageManager.PERMISSION_GRANTED &&
            coarsePerms == PackageManager.PERMISSION_GRANTED &&
            bgPerms == PackageManager.PERMISSION_GRANTED)
  }

  private fun requestLocationPermission() {
    mainActivity!!.requestPermissions(
      arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
      ), { permsGranted: Map<String, Boolean> ->
        requestLocationPermissionCallback(permsGranted)
      }
    )
  }

  private fun requestLocationPermissionCallback(permsGranted: Map<String, Boolean>) {
    println(permsGranted)
  }
}

class GnssListener : LocationListener {
  override fun onLocationChanged(p0: Location) {
    println("Not yet implemented")
  }
}
