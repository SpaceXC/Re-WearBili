package like

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

@Stable
class LikeButtonState(
    initIsLiked: Boolean,
    private val duration: Int,
) {

    val isLiked = mutableStateOf(initIsLiked)

    val outerCircleAnimation = Animatable(0.0f)
    val innerCircleAnimation = Animatable(0.2f)
    val scaleAnimation = Animatable(1.0f)
    val bubblesAnimation = Animatable(0.0f)

    private fun isRunning() =
        outerCircleAnimation.isRunning ||
                innerCircleAnimation.isRunning ||
                scaleAnimation.isRunning ||
                bubblesAnimation.isRunning

    suspend fun unlike() {
        if (isRunning()) return
        outerCircleAnimation.snapTo(0.0f)
        innerCircleAnimation.snapTo(0.0f)
        bubblesAnimation.snapTo(0.0f)
        scaleAnimation.snapTo(1.0f)
        isLiked.value = false
    }

    fun likeWithoutAnimation() {
        isLiked.value = true
    }

    suspend fun like(scope: CoroutineScope) {
        if (isRunning()) return
        isLiked.value = true
        val job1 = scope.async {
            outerCircleAnimation.animateTo(
                targetValue = 1.0f,
                animationSpec = tween(
                    (duration * 0.3).toInt(),
                    easing = LinearOutSlowInEasing,
                    delayMillis = 0,
                )
            )
        }

        val job2 = scope.async {
            innerCircleAnimation.animateTo(
                targetValue = 1.0f,
                animationSpec = tween(
                    (duration * 0.3).toInt(),
                    easing = LinearOutSlowInEasing,
                    delayMillis = (duration * 0.2).toInt(),
                )
            )
        }

        val job3 = scope.async {
            scaleAnimation.snapTo(0.0f)
            scaleAnimation.animateTo(
                targetValue = 1.0f,
                animationSpec = tween(
                    (duration * 0.35).toInt(),
                    easing = EaseOutBounce,
                    delayMillis = (duration * 0.35).toInt(),
                )
            )
        }

        val job4 = scope.async {
            bubblesAnimation.animateTo(
                targetValue = 1.0f,
                animationSpec = tween(
                    duration,
                    easing = Decelerate,
                    delayMillis = (duration * 0.1).toInt(),
                )
            )
        }
        awaitAll(job1, job2, job3, job4)
    }
}

@Composable
fun rememberLikeButtonState(
    initIsLiked: Boolean = false,
    duration: Int = 1500,
): LikeButtonState {
    return remember {
        LikeButtonState(
            initIsLiked,
            duration,
        )
    }
}
