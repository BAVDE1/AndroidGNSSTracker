package com.example.androidtest001

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

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
  fun LoggingUnit() {
    Column {
      Text("we logging :D")
    }
  }
}