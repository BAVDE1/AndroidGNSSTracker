package com.example.androidtest001.classes
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.example.androidtest001.MainActivity

const val ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
const val ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
const val ACCESS_BACKGROUND_LOCATION = Manifest.permission.ACCESS_BACKGROUND_LOCATION

class PermissionHelper {
  private var initialized: Boolean = false
  private var activity: MainActivity? = null
  private var context: Context? = null

  private var permCache: HashMap<String, Boolean> = HashMap()
  private var permRequestLock: Boolean = false

  /** SINGLE */
  private var requestPermChecking: String = ""
  private var requestPermCallback: ((Boolean) -> Unit)? = null
  private val requestPermLauncher = activity!!.registerForActivityResult(
    ActivityResultContracts.RequestPermission()
  ) { grantedResult: Boolean ->
    permCache[requestPermChecking] = grantedResult
    requestPermCallback?.let { it(grantedResult) }
    requestPermCallback = null
    permRequestLock = false
  }

  /** MULTIPLE */
  private var requestPermsCallback: ((Map<String, Boolean>) -> Unit)? = null
  private val requestPermsLauncher = activity!!.registerForActivityResult(
    ActivityResultContracts.RequestMultiplePermissions()
  ) { grantedResults: Map<String, Boolean> ->
    for (result in grantedResults) {
      permCache[result.key] = result.value
    }
    requestPermsCallback?.let { it(grantedResults) }
    requestPermsCallback = null
    permRequestLock = false
  }

  fun initialise(activity: MainActivity) {
    if (initialized) return
    this.activity = activity
    this.context = activity.applicationContext
    initialized = true
  }

  fun isPermissionGranted(perm: String): Boolean {
    if (permCache.containsKey(perm)) return permCache[perm] == true
    val granted = ActivityCompat.checkSelfPermission(context!!, perm) == PackageManager.PERMISSION_GRANTED
    permCache[perm] = granted
    return granted
  }

  fun arePermissionsGranted(perms: Array<String>): Boolean {
    for (perm in perms) {
      if (!isPermissionGranted(perm)) return false
    }
    return true
  }

  fun requestPermission(perm: String, callback: ((Boolean) -> Unit)?) {
    if (permRequestLock) return
    permRequestLock = true
    requestPermChecking = perm
    requestPermCallback = callback
    requestPermLauncher.launch(perm)
  }

  fun requestPermissions(perms: Array<String>, callback: ((Map<String, Boolean>) -> Unit)?) {
    if (permRequestLock) return
    permRequestLock = true
    requestPermsCallback = callback
    requestPermsLauncher.launch(perms)
  }
}