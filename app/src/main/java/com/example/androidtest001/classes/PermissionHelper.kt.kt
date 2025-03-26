package com.example.androidtest001.classes

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.androidtest001.MainActivity
import com.example.androidtest001.ui.theme.DARK_GREY_001
import com.example.androidtest001.ui.theme.DARK_GREY_003
import com.example.androidtest001.ui.theme.LIGHT_GREY_007
import com.example.androidtest001.ui.theme.RED

const val ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
const val ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
const val ACCESS_BACKGROUND_LOCATION = Manifest.permission.ACCESS_BACKGROUND_LOCATION

class PermissionHelper {
  private var initialized: Boolean = false
  private var activity: MainActivity? = null
  private var context: Context? = null

  private var permRequestLock: Boolean = false

  /** SINGLE */
  private var requestPermCallback: ((Boolean) -> Unit)? = null
  private var requestPermLauncher: ActivityResultLauncher<String>? = null

  /** MULTIPLE */
  private var requestPermsCallback: ((Map<String, Boolean>) -> Unit)? = null
  private var requestPermsLauncher: ActivityResultLauncher<Array<String>>? = null

  fun initialise(activity: MainActivity) {
    if (initialized) return
    this.activity = activity
    this.context = activity.applicationContext

    setupLaunchers()
    initialized = true
  }

  private fun setupLaunchers() {
    requestPermLauncher = activity!!.registerForActivityResult(
      ActivityResultContracts.RequestPermission()
    ) { grantedResult: Boolean ->
      requestPermCallback?.let { it(grantedResult) }
      requestPermCallback = null
      permRequestLock = false
    }

    requestPermsLauncher = activity!!.registerForActivityResult(
      ActivityResultContracts.RequestMultiplePermissions()
    ) { grantedResults: Map<String, Boolean> ->
      for (result in grantedResults) {
      }
      requestPermsCallback?.let { it(grantedResults) }
      requestPermsCallback = null
      permRequestLock = false
    }
  }

  fun isPermissionGranted(perm: String): Boolean {
    return ActivityCompat.checkSelfPermission(context!!, perm) == PackageManager.PERMISSION_GRANTED
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
    requestPermCallback = callback
    requestPermLauncher!!.launch(perm)
  }

  fun requestPermissions(perms: Array<String>, callback: ((Map<String, Boolean>) -> Unit)?) {
    if (permRequestLock) return
    permRequestLock = true
    requestPermsCallback = callback
    requestPermsLauncher!!.launch(perms)
  }
}

@Composable
fun PermissionPage(activity: MainActivity, innerPadding: PaddingValues) {
  val cornerShape: Shape = RoundedCornerShape(5.dp)
  val permsList: ArrayList<String> =
    arrayListOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION, ACCESS_BACKGROUND_LOCATION)

  Box(Modifier.fillMaxSize().padding(innerPadding).background(LIGHT_GREY_007)) {
    Column(Modifier.fillMaxWidth()) {
      Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
          Text("Permissions")
        }
        PressElement { activity.closePermissionsMenu() }.Unit {
          Column(
            Modifier.size(25.dp).clip(cornerShape).background(DARK_GREY_003).border(3.dp, DARK_GREY_001, cornerShape)
          ) {
            Text(text = "X", modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center)
          }
        }
      }
    }
  }
}