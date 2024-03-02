package cn.spacexc.wearbili.remake.common.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import cn.spacexc.wearbili.remake.app.settings.LocalConfiguration

@Composable
fun wearBiliAnimateFloatAsState(
    targetValue: Float,
    animationSpec: AnimationSpec<Float> = tween()
): State<Float> {
    return if (LocalConfiguration.current.hasAnimation) animateFloatAsState(
        targetValue = targetValue,
        label = "",
        animationSpec = animationSpec
    ) else remember(targetValue) {
        mutableFloatStateOf(targetValue)
    }
}

@Composable
fun wearBiliAnimateDpAsState(
    targetValue: Dp,
    animationSpec: AnimationSpec<Dp> = tween()
): State<Dp> {
    return if (LocalConfiguration.current.hasAnimation) animateDpAsState(
        targetValue = targetValue,
        label = "",
        animationSpec = animationSpec
    ) else remember(targetValue) {
        mutableStateOf(targetValue)
    }
}

@Composable
fun wearBiliAnimateColorAsState(
    targetValue: Color,
    animationSpec: AnimationSpec<Color> = tween()
): State<Color> {
    return if (LocalConfiguration.current.hasAnimation) animateColorAsState(
        targetValue = targetValue,
        label = "",
        animationSpec = animationSpec
    ) else remember(targetValue) {
        mutableStateOf(targetValue)
    }
}

@Composable
fun <S> WearBiliAnimatedContent(
    targetState: S,
    modifier: Modifier = Modifier,
    transitionSpec: AnimatedContentTransitionScope<S>.() -> ContentTransform = {
        (fadeIn(animationSpec = tween(220, delayMillis = 90)) +
                scaleIn(initialScale = 0.92f, animationSpec = tween(220, delayMillis = 90)))
            .togetherWith(fadeOut(animationSpec = tween(90)))
    },
    contentAlignment: Alignment = Alignment.TopStart,
    label: String = "AnimatedContent",
    contentKey: (targetState: S) -> Any? = { it },
    content: @Composable (targetState: S) -> Unit
) {
    if (LocalConfiguration.current.hasAnimation) {
        AnimatedContent(
            targetState,
            modifier,
            transitionSpec,
            contentAlignment,
            label,
            contentKey
        ) {
            content(it)
        }
    } else {
        content(targetState)
    }
}