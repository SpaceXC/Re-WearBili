package cn.spacexc.wearbili.remake.common.ui

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import cn.spacexc.wearbili.common.copyToClipboard
import cn.spacexc.wearbili.remake.app.Application
import cn.spacexc.wearbili.remake.app.settings.LocalConfiguration
import com.google.accompanist.placeholder.PlaceholderDefaults
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmerHighlightColor
import com.google.accompanist.placeholder.shimmer
import kotlinx.coroutines.launch

/**
 * Created by Xiaochang on 2022/9/17.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */
fun Modifier.lazyRotateInput(
    focusRequester: FocusRequester,
    state: LazyListState
): Modifier = composed {
    val scope = rememberCoroutineScope()
    onRotaryScrollEvent {
        scope.launch {
            state.scrollBy(it.verticalScrollPixels)
        }
        true
    }
        .focusRequester(focusRequester)
        .focusable()
}

fun Modifier.rotateInput(
    focusRequester: FocusRequester,
    state: ScrollState
): Modifier = composed {
    val scope = rememberCoroutineScope()
    onRotaryScrollEvent {
        scope.launch {
            state.scrollBy(it.verticalScrollPixels)
        }
        true
    }
        .focusRequester(focusRequester)
        .focusable()
}

@Composable
fun Modifier.clickVfx(
    interactionSource: MutableInteractionSource = rememberMutableInteractionSource(),
    isEnabled: Boolean = true,
    onClick: () -> Unit,
): Modifier = composed {
    val isLowPerformance = !LocalConfiguration.current.hasAnimation
    if (isEnabled) {
        if (isLowPerformance) {
            clickable(
                indication = null, interactionSource = interactionSource, onClick = onClick
            )
        } else {
            val isPressed by interactionSource.collectIsPressedAsState()
            val sizePercent by wearBiliAnimateFloatAsState(
                targetValue = if (isPressed) 0.9f else 1f,
                animationSpec = tween(durationMillis = 150)
            )
            scale(sizePercent).clickable(
                indication = null, interactionSource = interactionSource, onClick = onClick
            )
        }
    } else {
        Modifier
    }
}

@Composable
fun Modifier.clickVfx(
    interactionSource: MutableInteractionSource = rememberMutableInteractionSource(),
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {}
): Modifier = composed {
    val isLowPerformance = !LocalConfiguration.current.hasAnimation
    if (enabled) {
        if (isLowPerformance) {
            pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onClick() },
                    onLongPress = {
                        onLongClick()
                    }
                )
            }
        } else {
            val isPressed by interactionSource.collectIsPressedAsState()
            val sizePercent by wearBiliAnimateFloatAsState(
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
        }
    } else {
        Modifier
    }
}

@Composable
fun Modifier.clickAlpha(
    interactionSource: MutableInteractionSource = rememberMutableInteractionSource(),
    isEnabled: Boolean = true,
    onClick: () -> Unit,
): Modifier = composed {
    if (isEnabled) {
        val isLowPerformance = !LocalConfiguration.current.hasAnimation
        if (isLowPerformance) {
            clickable(
                indication = null, interactionSource = interactionSource, onClick = onClick
            )
        } else {
            val isPressed by interactionSource.collectIsPressedAsState()
            val sizePercent by wearBiliAnimateFloatAsState(
                targetValue = if (isPressed) 0.8f else 1f,
                animationSpec = tween(durationMillis = 150)
            )
            alpha(sizePercent).clickable(
                indication = null, interactionSource = interactionSource, onClick = onClick
            )
        }
    } else {
        Modifier
    }
}

@Composable
fun Modifier.clickAlpha(
    interactionSource: MutableInteractionSource = rememberMutableInteractionSource(),
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {}
): Modifier = composed {
    if (enabled) {
        val isLowPerformance = !LocalConfiguration.current.hasAnimation
        if (isLowPerformance) {
            pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onClick() },
                    onLongPress = {
                        onLongClick()
                    }
                )
            }
        } else {
            val isPressed by interactionSource.collectIsPressedAsState()
            val sizePercent by wearBiliAnimateFloatAsState(
                targetValue = if (isPressed) 0.8f else 1f,
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
        }
    } else {
        Modifier
    }
}

@Composable
fun Modifier.copyable(content: String, label: String = ""): Modifier {
    return clickVfx(onLongClick = {
        content.copyToClipboard(Application.getApplication(), "")
    })
}

fun Modifier.shimmerPlaceHolder(isLoading: Boolean, shape: Shape? = null) = composed {
    placeholder(
        visible = isLoading,
        shape = shape,
        highlight = PlaceholderHighlight.shimmer(
            highlightColor = PlaceholderDefaults.shimmerHighlightColor(
                backgroundColor = Color.DarkGray
            )
        ),
        color = Color(34, 34, 34, 179)
    )
}
