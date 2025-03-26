package com.example.androidtest001

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
const val PERMISSION_OPEN_DEFAULT = true

enum class Menu {
  LOGGING, OPTIONS, PERMISSIONS
}

class MainActivity : ComponentActivity() {
  private val isOptionsOpen: MutableLiveData<Boolean> = MutableLiveData(OPTIONS_OPEN_DEFAULT)
  private val isPermissionsOpen: MutableLiveData<Boolean> = MutableLiveData(PERMISSION_OPEN_DEFAULT)

//  private val menuLookups: HashMap<Menu, MutableLiveData<Boolean>> = hashMapOf(pairs = Pair<Menu, Logger.>)

  val permHelper: PermissionHelper = PermissionHelper()
  val gns: GnssHelper = GnssHelper()

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
    isOptionsOpen.value = false
    isPermissionsOpen.value = true
  }

  fun closePermissionsMenu() {
    isPermissionsOpen.value = false
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initialise()

    setContent {
      var isPermissionsOpenObserved: Boolean by remember { mutableStateOf(PERMISSION_OPEN_DEFAULT) }
      isPermissionsOpen.observeForever { v: Boolean -> isPermissionsOpenObserved = v }

      AndroidTest001Theme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          OptionsMenuUnit(this, innerPadding, isOptionsOpen, OPTIONS_OPEN_DEFAULT)

          if (isPermissionsOpenObserved) {
            PermissionPage(this, innerPadding)
          }

          Logger.Unit(innerPadding)
        }
      }
    }
  }
}
