package com.example.androidtest001

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableInferredTarget
import androidx.compose.runtime.ComposableTarget
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput


/**
 * onRelease: function called on release
 * modifierOptions: optional modifiers
 * inner: function returning a unit to be used as the contents of the button
 */
@Composable
fun InteractionRelease(
  onRelease: (PointerInputChange) -> Unit,
  modifier: Modifier = Modifier,
  inner: (@Composable () -> Unit)? = null
) {
  Box(Modifier.then(Modifier.pointerInput(onRelease) {
    awaitEachGesture {
      awaitFirstDown().also { it.consume() }
      val up = waitForUpOrCancellation()
      if (up != null) {
        up.consume()
        onRelease(up)
      }
    }
  }).then(modifier)) {
    if (inner != null) inner()
  }
}


/**
 * OUTDATED lol probably avoid using this anyway
 */
//@Composable
//fun InteractionPress(onPress: (PointerInputChange) -> Unit, modifierOptions: Modifier? = null) {
//  BuildInteractionElem(Modifier.pointerInput(onPress) {
//    awaitEachGesture {
//      val down = awaitFirstDown()
//      down.consume()
//      onPress(down)
//    }
//  }, modifierOptions)
//}