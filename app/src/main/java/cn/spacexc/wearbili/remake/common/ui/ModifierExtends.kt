package cn.spacexc.wearbili.remake.common.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput

/**
 * Created by Xiaochang on 2022/9/17.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */
fun Modifier.clickVfx(
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
    isEnabled: Boolean = true,
    onClick: () -> Unit,
): Modifier = composed {
    if (isEnabled) {
        val isPressed by interactionSource.collectIsPressedAsState()
        val sizePercent by animateFloatAsState(
            targetValue = if (isPressed) 0.9f else 1f,
            animationSpec = tween(durationMillis = 150)
        )
        scale(sizePercent).clickable(
            indication = null, interactionSource = interactionSource, onClick = onClick
        )
    } else {
        Modifier
    }
}

fun Modifier.clickVfx(
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
    enabled: Boolean = true,
    onClick: () -> Unit,
    onLongClick: () -> Unit
): Modifier = composed {
    if (enabled) {
        val isPressed by interactionSource.collectIsPressedAsState()
        val sizePercent by animateFloatAsState(
            targetValue = if (isPressed) 0.9f else 1f,
            animationSpec = tween(durationMillis = 150)
        )
        scale(sizePercent).pointerInput(Unit) {
            detectTapGestures(
                onTap = { onClick() },
                onLongPress = {
                    onLongClick()
                },
                onPress = {
                    val press = PressInteraction.Press(it)
                    interactionSource.emit(press)
                    tryAwaitRelease()
                    interactionSource.emit(PressInteraction.Release(press))
                })
        }
    } else {
        Modifier
    }
}
