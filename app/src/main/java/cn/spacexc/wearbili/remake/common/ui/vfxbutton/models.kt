package like

import androidx.compose.animation.core.Easing
import androidx.compose.ui.graphics.Color

data class CircleColor(
    val start: Color,
    val end: Color,
)

data class BubbleColor(
    val dotPrimaryColor: Color,
    val dotSecondaryColor: Color,
    val dotThirdColor: Color = dotPrimaryColor,
    val dotLastColor: Color = dotSecondaryColor,
) {
    private var index = 0
    fun next(): Color {
        //return BilibiliPink
        if (index > 3) {
            index = 0
        }
        return when (index) {
            0 -> dotPrimaryColor
            1 -> dotSecondaryColor
            2 -> dotThirdColor
            3 -> dotLastColor
            else -> {
                dotLastColor
            }
        }.apply {
            index++
        }
    }
}

val EaseOutBounce: Easing = Easing { fraction ->
    val n1 = 7.5625f
    val d1 = 2.75f
    var newFraction = fraction
    return@Easing if (newFraction < 1f / d1) {
        n1 * newFraction * newFraction
    } else if (newFraction < 2f / d1) {
        newFraction -= 1.5f / d1
        n1 * newFraction * newFraction + 0.75f
    } else if (newFraction < 2.5f / d1) {
        newFraction -= 2.25f / d1
        n1 * newFraction * newFraction + 0.9375f
    } else {
        newFraction -= 2.625f / d1
        n1 * newFraction * newFraction + 0.984375f
    }
}

val Decelerate: Easing = Easing { fraction ->
    return@Easing (1.0 - (1.0 - fraction) * (1.0 - fraction)).toFloat()
}
