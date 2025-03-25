package com.example.androidtest001

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.MutableLiveData
import com.example.androidtest001.classes.GnssHelper
import com.example.androidtest001.classes.Logger
import com.example.androidtest001.classes.PermissionHelper
import com.example.androidtest001.ui.theme.AndroidTest001Theme
import com.example.androidtest001.units.OptionsMenuUnit

class MainActivity : ComponentActivity() {
  private val OPTIONS_OPEN_DEFAULT = false
  private val isOptionsOpen: MutableLiveData<Boolean> = MutableLiveData(OPTIONS_OPEN_DEFAULT)

  private val permHelper: PermissionHelper = PermissionHelper()
  private val gns: GnssHelper = GnssHelper()

  private fun initialise() {
    Logger.initialise(this)

    permHelper.initialise(this)
    gns.initialise(this, permHelper)

    Logger.onVisibilityChanged.registerCallback { isOptionsOpen.value = false }

    enableEdgeToEdge()
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
}
