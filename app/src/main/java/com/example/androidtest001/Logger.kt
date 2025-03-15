package com.example.androidtest001

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val Logger: LoggerSingleton = LoggerSingleton()

class LoggerSingleton {
  private var initialized: Boolean = false
  private var activity: MainActivity? = null

  private var height: Dp = 400.dp

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
    Box(Modifier.fillMaxSize().padding(pv)) {
      Box(
        modifier = Modifier.align(Alignment.BottomStart).background(Color.White).fillMaxWidth().height(height)
          .padding(10.dp)
      ) {
        Column {
          InteractionRelease(
            onRelease = { println("pressed") },
            inner = { Text(text = "button") })
          Text(text = "help", color = Color.Black)
        }
      }
    }
  }
}