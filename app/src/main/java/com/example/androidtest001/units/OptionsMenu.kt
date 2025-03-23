package com.example.androidtest001.units

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import com.example.androidtest001.classes.PressElement
import com.example.androidtest001.ui.theme.BLACK
import com.example.androidtest001.ui.theme.DARK_GREY_003
import com.example.androidtest001.ui.theme.LIGHT_GREY_007

@Composable
fun OptionsMenuUnit(
  innerPadding: PaddingValues,
  isOptionsOpen: MutableLiveData<Boolean>,
  optionsOpenDefault: Boolean
) {
  val cornerShape: Shape = RoundedCornerShape(5.dp)

  var isOptionsOpenObserved: Boolean by remember { mutableStateOf(optionsOpenDefault) }
  isOptionsOpen.observeForever { v: Boolean -> isOptionsOpenObserved = v }

  Box(Modifier.fillMaxSize().padding(innerPadding)) {
    Box(
      modifier = Modifier.align(Alignment.BottomStart).padding(10.dp)
    ) {
      PressElement { _: PointerInputChange -> isOptionsOpen.value = !isOptionsOpen.value!! }.Unit {
        Row(
          Modifier.clip(cornerShape).background(LIGHT_GREY_007).border(3.dp, DARK_GREY_003, cornerShape)
            .width(40.dp), horizontalArrangement = Arrangement.Center
        ) {
          Text(
            if (isOptionsOpenObserved) "X" else "...",
            color = BLACK,
            modifier = Modifier.padding(10.dp),
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}
