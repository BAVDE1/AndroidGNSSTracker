package com.example.androidtest001

import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

val Logger: LoggerSingleton = LoggerSingleton()

class LoggerSingleton {
  private var initialized: Boolean = false
  private var activity: MainActivity? = null

  fun setupLogger(activity: MainActivity) {
    if (initialized) return
    this.activity = activity
    initialized = true
  }

  fun isInitialized(): Boolean {
    return initialized
  }

  @Composable
  fun LoggingUnit(pv: PaddingValues) {
    Box(Modifier.fillMaxSize()) {
      Text(modifier = Modifier.align(Alignment.BottomStart).padding(pv), text = "help")
    }
  }
}