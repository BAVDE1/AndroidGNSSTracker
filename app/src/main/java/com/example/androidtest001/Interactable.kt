package com.example.androidtest001

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer


class PressElement(
  private val onRelease: (PointerInputChange) -> Unit,
) {
  @Composable
  fun Unit(
    modifier: Modifier = Modifier,
    inner: (@Composable () -> Unit)
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
    }).then(modifier)) { inner() }
  }
}


class ToggleElement(
  private val onToggle: (Boolean) -> Unit,
  defaultVal: Boolean = false
) {
  private val pressElem: PressElement = PressElement(onRelease = { toggle() })
  private var toggled: MutableLiveData<Boolean> = MutableLiveData(defaultVal)

  /** silent: should this fire `onToggle` function */
  fun toggle(silent: Boolean = false) {
    toggled.value = !toggled.value!!
    if (!silent) onToggle(toggled.value!!)
  }

  @Composable
  fun Unit(
    modifier: Modifier = Modifier,
    inner: (@Composable (Boolean) -> Unit),
  ) {
    pressElem.Unit(modifier = modifier) {
      var isToggled by remember { mutableStateOf(false) }
      toggled.observeForever { v: Boolean -> isToggled = v }
      inner(isToggled)
    }
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