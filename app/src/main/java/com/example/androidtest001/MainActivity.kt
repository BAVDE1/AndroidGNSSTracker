package com.example.androidtest001

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.MutableLiveData
import com.example.androidtest001.classes.GnssHelper
import com.example.androidtest001.classes.Logger
import com.example.androidtest001.classes.PermissionHelper
import com.example.androidtest001.classes.PermissionPage
import com.example.androidtest001.ui.theme.AndroidTest001Theme
import com.example.androidtest001.units.OptionsMenuUnit
import java.util.logging.Logger

const val OPTIONS_OPEN_DEFAULT = false

enum class Menu {
  LOGGING, OPTIONS, PERMISSIONS
}

class MainActivity : ComponentActivity() {
  private val isOptionsOpen: MutableLiveData<Boolean> = MutableLiveData(OPTIONS_OPEN_DEFAULT)
  private val isPermissionsOpen: MutableLiveData<Boolean> = MutableLiveData(false)

//  private val menuLookups: HashMap<Menu, MutableLiveData<Boolean>> = hashMapOf(pairs = Pair<Menu, Logger.>)

  private val permHelper: PermissionHelper = PermissionHelper()
  private val gns: GnssHelper = GnssHelper()

  private fun initialise() {
    Logger.initialise(this)

    permHelper.initialise(this)
    gns.initialise(this, permHelper)

    Logger.onVisibilityChanged.registerCallback { isOptionsOpen.value = false }

    enableEdgeToEdge()
  }

  fun openMenu(menu: Menu) {

  }

  fun closeMenu(menu: Menu) {

  }

  fun openPermissionsMenu() {
    isPermissionsOpen.value = true
  }

  fun closePermissionsMenu() {
    isPermissionsOpen.value = false
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initialise()

    setContent {
      var isPermissionsOpenObserved: Boolean by remember { mutableStateOf(false) }
      isPermissionsOpen.observeForever { v: Boolean -> isPermissionsOpenObserved = v }

      AndroidTest001Theme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          OptionsMenuUnit(this, innerPadding, isOptionsOpen, OPTIONS_OPEN_DEFAULT)
          Logger.Unit(innerPadding)

          if (isPermissionsOpenObserved) {
            PermissionPage(this, innerPadding)
          }
        }
      }
    }
  }
}
