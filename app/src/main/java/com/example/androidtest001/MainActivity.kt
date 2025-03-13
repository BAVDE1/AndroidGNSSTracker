package com.example.androidtest001

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.unit.dp
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
          LogPointerEvents(topPv)
          Logger.LoggingUnit()
        }
      }
    }
  }

  //todo: add granted callback
  fun requestPermissions(perms: Array<String>, callback: (Map<String, Boolean>) -> Unit) {
    if (requestPermsCallback != null) return
    requestPermsCallback = callback
    requestPermsLauncher.launch(perms)
  }
}

@Composable
private fun LogPointerEvents(pv: PaddingValues) {
  var log by remember { mutableStateOf("---") }

  Column {
    Text(log, Modifier.padding(pv))
    Row {
      InteractionPress({ e: PointerInputChange -> log = "press ${e.position}" }, Modifier.size(100.dp).background(Color.Blue))
      InteractionRelease({ e: PointerInputChange -> log = "release ${e.position}" }, Modifier.size(100.dp).background(Color.Red))
    }
  }
}
