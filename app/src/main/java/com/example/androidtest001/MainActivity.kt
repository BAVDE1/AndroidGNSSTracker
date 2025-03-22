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
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
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
          val bottomPv = PaddingValues(bottom = innerPadding.calculateBottomPadding())
          LogPointerEvents()
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

@Composable
private fun LogPointerEvents(filter: PointerEventType? = null) {
  var log by remember { mutableStateOf("") }
  Column {
    Text(log)
    Box(
      Modifier
        .size(100.dp)
        .background(Color.Red)
        .pointerInput(filter) {
          awaitPointerEventScope {
            while (true) {
              val event = awaitPointerEvent()
              // handle pointer event
              if (filter == null || event.type == filter) {
                log = "${event.type}, ${event.changes.first().position}"
              }
            }
          }
        }
    )
  }
}