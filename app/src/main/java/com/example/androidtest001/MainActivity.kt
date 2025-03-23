package com.example.androidtest001

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.androidtest001.ui.theme.AndroidTest001Theme

class MainActivity : ComponentActivity() {
  private var requestPermsCallback: ((Map<String, Boolean>) -> Unit)? = null
  private val requestPermsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()
  ) { grantedResults: Map<String, Boolean> ->
    requestPermsCallback?.let { it(grantedResults) }
    requestPermsCallback = null
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      Logger.setupLogger(this)
      var gns: GnssHelper = GnssHelper(this)

      AndroidTest001Theme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          val topPv = PaddingValues(top = innerPadding.calculateTopPadding())
          val bottomPv = PaddingValues(bottom = innerPadding.calculateBottomPadding())
          Logger.LoggingUnit(bottomPv)
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
