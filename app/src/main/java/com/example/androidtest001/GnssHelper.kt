package com.example.androidtest001

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.result.ActivityResultCaller
import androidx.core.app.ActivityCompat

/*
 Global Navigation Satellite System
 */
class GnssHelper constructor(private val context: Context, private val mainActivity: MainActivity) {
  private var lm: LocationManager? = null;
  private var listener: GnssListener = GnssListener()

  init {
    if (checkForLocationPermission()) {
      lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
      lm!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000.toLong(), 1.toFloat(), listener)
    }
  }

  private fun checkForLocationPermission(requestAccess: Boolean = true) : Boolean {
    val coarsePerms = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
    val finePerms = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
    val bgPerms = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION)

    if (
      finePerms == PackageManager.PERMISSION_GRANTED &&
      coarsePerms == PackageManager.PERMISSION_GRANTED &&
      bgPerms == PackageManager.PERMISSION_GRANTED) return true

    // no access!!!
    if (requestAccess) {
      mainActivity.requestPermissions(arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
      ))
    }
    return false
  }
}

class GnssListener : LocationListener {
  override fun onLocationChanged(p0: Location) {
    println("Not yet implemented")
  }
}
