package com.example.androidtest001

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.MutableLiveData
import com.example.androidtest001.classes.GnssHelper
import com.example.androidtest001.classes.Logger
import com.example.androidtest001.ui.theme.AndroidTest001Theme
import com.example.androidtest001.units.OptionsMenuUnit

class MainActivity : ComponentActivity() {
  private var requestPermsCallback: ((Map<String, Boolean>) -> Unit)? = null
  private val requestPermsLauncher = registerForActivityResult(
    ActivityResultContracts.RequestMultiplePermissions()
  ) { grantedResults: Map<String, Boolean> ->
    requestPermsCallback?.let { it(grantedResults) }
    requestPermsCallback = null
  }

  private val OPTIONS_OPEN_DEFAULT = false
  private val isOptionsOpen: MutableLiveData<Boolean> = MutableLiveData(OPTIONS_OPEN_DEFAULT)

  private val gns: GnssHelper = GnssHelper()

  private fun initialise() {
    Logger.setupLogger(this)
    gns.initialise(this)
    enableEdgeToEdge()

    Logger.onVisibilityChanged = { isOptionsOpen.value = false }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initialise()

    setContent {
      AndroidTest001Theme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          OptionsMenuUnit(innerPadding, isOptionsOpen, OPTIONS_OPEN_DEFAULT)
          Logger.Unit(innerPadding)
        }
      }
    }
  }

  fun requestPermissions(perms: Array<String>, callback: (Map<String, Boolean>) -> Unit) {
    if (requestPermsCallback != null) return
    requestPermsCallback = callback
    requestPermsLauncher.launch(perms)
  }
}
