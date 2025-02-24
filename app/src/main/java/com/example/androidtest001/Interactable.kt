package com.example.androidtest001

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun BuildInteractionElem(modifier: Modifier, modifierOptions: Modifier? = null): Unit {
  if (modifierOptions == null) return Box(modifier)
  Box(Modifier.then(modifier).then(modifierOptions))
}

@Composable
fun InteractionRelease(onRelease: (PointerInputChange) -> Unit, modifierOptions: Modifier? = null) {
  BuildInteractionElem(Modifier.pointerInput(onRelease) {
    awaitEachGesture {
      awaitFirstDown().also { it.consume() }
      val up = waitForUpOrCancellation()
      if (up != null) {
        up.consume()
        onRelease(up)
      }
    }
  }, modifierOptions)
}

@Composable
fun InteractionPress(onPress: (PointerInputChange) -> Unit, modifierOptions: Modifier? = null) {
  BuildInteractionElem(Modifier.pointerInput(onPress) {
    awaitEachGesture {
      val down = awaitFirstDown()
      down.consume()
      onPress(down)
    }
  }, modifierOptions)
}