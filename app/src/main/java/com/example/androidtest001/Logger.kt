package com.example.androidtest001

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.androidtest001.ui.theme.*

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
    val cornerShape: Shape = RoundedCornerShape(5.dp)
    val scrollState: ScrollState = rememberScrollState()
    val toggle: ToggleElement = ToggleElement({ toggled: Boolean -> println("toggled: $toggled") })

    val closeBtnUnit: @Composable () -> Unit = {
      PressElement { println("pressed") }.Unit {
        Box(Modifier.size(25.dp).clip(cornerShape).background(DARK_GREY_003).border(3.dp, DARK_GREY_001, cornerShape)) {
          Text(text = "X", modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center)
        }
      }
    }

    Box(Modifier.fillMaxSize().padding(pv)) {
      Box(
        modifier = Modifier.align(Alignment.BottomStart).background(LIGHT_GREY_008)
          .fillMaxWidth().height(height).padding(top = 5.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)
      ) {
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
          Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Box(Modifier.clip(cornerShape).size(300.dp, 10.dp).background(DARK_GREY_001))
          }
          Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "[some toggles]", color = Color.Black)
            toggle.Unit {
              Box(Modifier.size(40.dp).background(Color.Red))
            }
            closeBtnUnit()
          }
          Row(Modifier.verticalScroll(scrollState).horizontalScroll(scrollState).border(3.dp, DARK_GREY_001, cornerShape)
            .clip(cornerShape).fillMaxSize().background(DARK_GREY_003).weight(1f, true).padding(10.dp),
            verticalAlignment = Alignment.Bottom) {
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
              Text(text = "2222")
            }
          }
        }
      }
    }
  }
}