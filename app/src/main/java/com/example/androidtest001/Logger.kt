package com.example.androidtest001

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import com.example.androidtest001.ui.theme.*
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

val Logger: LoggerSingleton = LoggerSingleton()

enum class LogLevel {
  DEBUG, INFO, WARN, DANGER
}

class LoggerSingleton {
  private val SHOW_MORE_DEFAULT = false
  private val SHOW_DEBUG_DEFAULT = true
  private val SNAP_TO_BTM_DEFAULT = true

  private var initialized: Boolean = false
  private var activity: MainActivity? = null
  private var height: Dp = 400.dp

  private val logs: MutableLiveData<List<Log>> = MutableLiveData(listOf())

  private val showMore: MutableLiveData<Boolean> = MutableLiveData(SHOW_MORE_DEFAULT)
  private val showMoreToggle = ToggleElement({ toggled: Boolean -> showMore.value = toggled}, default = SHOW_MORE_DEFAULT)

  private val showDebug: MutableLiveData<Boolean> = MutableLiveData(SHOW_DEBUG_DEFAULT)
  private val showDebugToggle =
    ToggleElement({ toggled: Boolean -> showDebug.value = toggled }, default = SHOW_DEBUG_DEFAULT)

  // lock (from toggling itself off again) while waiting for auto scroll to finish
  private var snapToBtmLocked = SNAP_TO_BTM_DEFAULT
  private val snapToBtm: MutableLiveData<Boolean> = MutableLiveData(SNAP_TO_BTM_DEFAULT)
  private val snapToBtmToggle = ToggleElement({ toggled: Boolean ->
    if (toggled) snapToBtmLocked = true
    snapToBtm.value = toggled
  }, default = SNAP_TO_BTM_DEFAULT)

  private var randomLogBtn = PressElement({ _: PointerInputChange -> info(listOf("a log", "something", "another thing", "omba", "eeeeee").random())})

  fun setupLogger(activity: MainActivity) {
    if (initialized) return
    this.activity = activity
    initialized = true
  }

  fun isInitialized(): Boolean {
    return initialized
  }

  fun pushLog(level: LogLevel, msg: String) {
    val newLog = Log(logLevel = level, msg)
    logs.value = listOf(*logs.value!!.toTypedArray(), newLog)
  }

  fun debug(msg: String) {
    pushLog(LogLevel.DEBUG, msg)
  }

  fun info(msg: String) {
    pushLog(LogLevel.INFO, msg)
  }

  fun warn(msg: String) {
    pushLog(LogLevel.WARN, msg)
  }

  fun danger(msg: String) {
    pushLog(LogLevel.DANGER, msg)
  }

  @Composable
  fun LoggingUnit(pv: PaddingValues) {
    val cornerShape: Shape = RoundedCornerShape(5.dp)
    val scrollState: ScrollState = rememberScrollState()

    var logsObserved: List<Log> by remember { mutableStateOf(listOf()) }
    logs.observeForever { v: List<Log> -> logsObserved = v }

    var showMoreObserved: Boolean by remember { mutableStateOf(SHOW_MORE_DEFAULT) }
    showMore.observeForever { v: Boolean -> showMoreObserved = v }

    var showDebugObserved: Boolean by remember { mutableStateOf(SHOW_DEBUG_DEFAULT) }
    showDebug.observeForever { v: Boolean -> showDebugObserved = v }

    var snapToBtmObserved: Boolean by remember { mutableStateOf(SNAP_TO_BTM_DEFAULT) }
    snapToBtm.observeForever { v: Boolean -> snapToBtmObserved = v }

    // auto scroll (asynchronous)
    if (snapToBtm.value!!) LaunchedEffect(logsObserved.size, showMoreObserved, showDebugObserved) {
      scrollState.scrollTo(scrollState.maxValue)
      snapToBtmLocked = false  // unlock!
    }

    // manual scroll listener (conditional order is important)
    if (snapToBtmObserved && scrollState.value < scrollState.maxValue && !snapToBtmLocked) {
      snapToBtmToggle.toggle(value = false)
    }

    val showMoreUnit: @Composable () -> Unit = {
      showMoreToggle.Unit { toggled: Boolean ->
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
          Column(Modifier.size(25.dp).clip(cornerShape).background(LIGHT_GREY_007).border(3.dp, DARK_GREY_003, cornerShape), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(if (toggled) "v" else ">", color = BLACK)
          }
        }
      }
    }

    val snapToBtnUnit: @Composable () -> Unit = {
      snapToBtmToggle.Unit { toggled: Boolean ->
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
          toggleElementDefaultInner(toggled = toggled)
          Text("snap to btm", color = BLACK)
        }
      }
    }

    val showDebugUnit: @Composable () -> Unit = {
      showDebugToggle.Unit { toggled: Boolean ->
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
          toggleElementDefaultInner(toggled = toggled)
          Text("show debug", color = BLACK)
        }
      }
    }

    val randomLogUnit: @Composable () -> Unit = {
      randomLogBtn.Unit {
        Row(Modifier.clip(cornerShape).background(LIGHT_GREY_007).border(3.dp, DARK_GREY_003, cornerShape)) {
          Text("rand log", color = BLACK, modifier = Modifier.padding(horizontal = 5.dp, vertical = 0.dp))
        }
      }
    }

    val closeBtnUnit: @Composable () -> Unit = {
      PressElement { debug("really long message really long message really long message really long message") }.Unit {
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
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(5.dp)) {
          Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Box(Modifier.clip(cornerShape).fillMaxWidth(.8f).height(10.dp).background(DARK_GREY_001))
          }
          Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            showMoreUnit()
            snapToBtnUnit()
            closeBtnUnit()
          }
          if (showMoreObserved) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
              Box(Modifier.fillMaxWidth().height(5.dp).background(LIGHT_GREY_006))
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              showDebugUnit()
              randomLogUnit()
            }
          }
          Row(
            Modifier.fillMaxWidth().verticalScroll(scrollState).border(3.dp, DARK_GREY_003, cornerShape)
              .clip(cornerShape).background(DARK_GREEN).weight(1f, true).padding(10.dp),
            verticalAlignment = Alignment.Bottom
          ) {
            Column(Modifier.width(IntrinsicSize.Max), verticalArrangement = Arrangement.Bottom) {
              for (log: Log in logsObserved) {
                if (showDebugObserved || log.logLevel != LogLevel.DEBUG) {
                  Text(
                    "[${log.getFormattedTime()}] ${log.msg}",
                    fontFamily = FontFamily.Monospace,
                    color = log.getColor(),
                    style = TextStyle(textIndent = TextIndent(0.sp, 20.sp))
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}

class Log(val logLevel: LogLevel, val msg: String) {
  private var epochTime: Long = Instant.now().epochSecond

  fun getFormattedTime(): String {
    val sdf = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
    val netDate = Date(epochTime * 1000)
    return sdf.format(netDate)
  }

  fun getColor(): Color {
    return when (logLevel) {
      LogLevel.DEBUG -> GREY
      LogLevel.INFO -> WHITE
      LogLevel.WARN -> YELLOW
      LogLevel.DANGER -> RED
    }
  }
}
