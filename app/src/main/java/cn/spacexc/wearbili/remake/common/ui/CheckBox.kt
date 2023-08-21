package cn.spacexc.wearbili.remake.common.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Created by XC-Qan on 2023/8/15.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

/*@Composable
fun CheckBox(
    modifier: Modifier = Modifier,
    size: DpSize = DpSize(16.dp, 16.dp),
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val color by animateColorAsState(
        targetValue = if (isChecked) BilibiliPink else Color(
            87,
            87,
            87,
            77
        )
    )
    val alpha by animateFloatAsState(targetValue = if (isChecked) 1f else 0f)
    Box(
        modifier = modifier
            .size(size)
            .background(color = color, shape = RoundedCornerShape(30))
            .border(
                width = 0.3f.dp,
                shape = RoundedCornerShape(30),
                brush = Brush.linearGradient(
                    listOf(
                        Color(121, 121, 121, 255),
                        Color.Transparent
                    )
                )
            )
            .clickAlpha {
                onCheckedChange(!isChecked)
            }
            .drawWithContent {
                drawContent()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_checkmark_rounded),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(size.times(0.55f)).alpha(alpha)
        )
    }
}*/

/**
 * @author 花生🥜大佬！！！Furry最高！！
 * @author little modification by me
 *
 * 🥜大佬写的时候是按照24dp的大小来写的，需要增加一些适配的方法
 */
@Composable
fun Checkbox(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    size: Dp = 16.dp,
    radiusPercentage: Int = 30,
    checkmarkColor: Color = Color.White,
    onCheckedChanged: (Boolean) -> Unit
) {
    val density = LocalDensity.current

    // ---- Parameters

    val shape = remember { RoundedCornerShape(radiusPercentage) }
    val borderStroke = remember {
        BorderStroke(
            width = 0.75.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(121, 121, 121, 255),
                    Color.Transparent
                )
            )
        )
    }
    val checkmarkStroke = remember {
        with(density) {
            Stroke(
                width = 3.dpForCheckBox(size).toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        }
    }

    val color by animateColorAsState(
        targetValue = if (isChecked) BilibiliPink else Color(
            87,
            87,
            87,
            77
        ),
        animationSpec = tween()
    )
    val checkmarkStrokeProgressAnimationSpec = remember {
        tween<Int>(
            easing = EaseInOut
        )
    }

    // ---- Implementations

    Box(
        modifier = modifier
            .size(size)
            .clickAlpha {
                onCheckedChanged(!isChecked)
            }
    ) {
        val transition = updateTransition(isChecked, label = "wearbiliCheckBoxAnimationTransition")

        Box(
            modifier = Modifier

                .background(color, shape)
                .border(
                    border = borderStroke,
                    shape = shape
                )
        ) {
            val checkmarkProgress by transition.animateInt(
                transitionSpec = { checkmarkStrokeProgressAnimationSpec },
                label = "wearbiliCheckBoxAnimationTransitionAnimateIntCheckMarkProgress"
            ) { state ->
                if (state) 12 else 0
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawWithCache {
                        val path = Path().apply {
                            if (checkmarkProgress == 0) {
                                return@apply
                            }

                            moveTo(6.dpForCheckBox(size).toPx(), 12.dpForCheckBox(size).toPx())

                            if (checkmarkProgress < 4) {
                                val firstStrokeX = checkmarkProgress + 6
                                val firstStrokeY = checkmarkProgress + 12
                                lineTo(firstStrokeX.dpForCheckBox(size).toPx(), firstStrokeY.dpForCheckBox(size).toPx())
                            } else {
                                lineTo(10.dpForCheckBox(size).toPx(), 16.dpForCheckBox(size).toPx())
                            }

                            if (checkmarkProgress >= 4) {
                                val secondStrokeX = checkmarkProgress + 6
                                val secondStrokeY = 20 - checkmarkProgress
                                lineTo(secondStrokeX.dpForCheckBox(size).toPx(), secondStrokeY.dpForCheckBox(size).toPx())
                            }
                        }

                        onDrawWithContent {
                            drawPath(
                                path = path,
                                color = checkmarkColor,
                                style = checkmarkStroke
                            )
                        }
                    }
            )
        }
    }
}


private fun Int.dpForCheckBox(currentSize: Dp): Dp {
    return Dp(this.toFloat() * (currentSize.value / 24.dp.value))
}
