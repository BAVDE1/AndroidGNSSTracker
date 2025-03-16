package com.example.androidtest001

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    val scrollState: ScrollState = rememberScrollState()

    val closeBtn: @Composable () -> Unit = {
      InteractionRelease(
      onRelease = { println("pressed") },
      inner = { Box(Modifier.size(25.dp).clip(RoundedCornerShape(5.dp)).background(Color.DarkGray)) { Text(text = "X", modifier = Modifier.align(Alignment.Center))} })
    }

    Box(Modifier.fillMaxSize().padding(pv)) {
      Box(
        modifier = Modifier.align(Alignment.BottomStart).background(Color.White)
          .fillMaxWidth().height(height).padding(10.dp)
      ) {
        Column {
          Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "[some toggles]", color = Color.Black)
            closeBtn()
          }
          Row(Modifier.verticalScroll(scrollState).horizontalScroll(scrollState)
            .fillMaxSize().background(Color.Red).weight(1f, true), verticalAlignment = Alignment.Bottom) {
            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
              Text(text = "1")
              Text(text = "2")
              Text(text = "2")
              Text(text = "2")
              Text(text = "2")
              Text(text = "2")
              Text(text = "2")
              Text(text = "2")
              Text(text = "2")
              Text(text = "2")
              Text(text = "2")
              Text(text = "2")
              Text(text = "2")
              Text(text = "2")
              Text(text = "2")
              Text(text = "2")
              Text(text = "2")
              Text(text = "2")
              Text(text = "2")
            }
          }
        }
      }
    }
  }
}