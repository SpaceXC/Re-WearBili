package cn.spacexc.wearbili.remake.app.welcome.screens

import android.app.Activity
import android.view.SurfaceView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Badge
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import cn.spacexc.wearbili.remake.app.welcome.WelcomeViewModel
import cn.spacexc.wearbili.remake.app.welcome.components.CirclePosition
import cn.spacexc.wearbili.remake.app.welcome.components.PinkCircle
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.rememberMutableInteractionSource
import cn.spacexc.wearbili.remake.common.ui.spx
import cn.spacexc.wearbili.remake.common.ui.theme.time.DefaultTimeSource
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import kotlinx.coroutines.delay

@Composable
fun DeprecatedStartScreen(
    onToNext: () -> Unit
) {
    val timeSource = DefaultTimeSource("HH:mm")
    val timeText = timeSource.currentTime
    Box(modifier = Modifier.fillMaxSize()) {
        PinkCircle(CirclePosition.TOP_START)
        PinkCircle(CirclePosition.BOTTOM_END)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Row(modifier = Modifier) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = timeText,
                    fontFamily = wearbiliFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.spx
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically)
            ) {
                Text(
                    text = "欢迎使用",
                    fontSize = 17.spx,
                    fontFamily = wearbiliFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = Color(
                        255,
                        255,
                        255,
                        128
                    )
                )
                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        text = "WearBili",
                        fontSize = 24.spx,
                        fontFamily = wearbiliFontFamily,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    ReBadge()
                }
                Card(
                    outerPaddingValues = PaddingValues(horizontal = 0.dp, vertical = 15.dp),
                    innerPaddingValues = PaddingValues(15.dp),
                    shape = RoundedCornerShape(14.dp),
                    borderWidth = 0.55.dp,
                    onClick = onToNext
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "开始使用", fontFamily = wearbiliFontFamily, fontSize = 13.spx)
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Activity.StartScreen(viewModel: WelcomeViewModel, onToNext: () -> Unit) {
    val localDensity = LocalDensity.current
    val timeSource = DefaultTimeSource("HH:mm")
    val timeText = timeSource.currentTime
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val floatingHintAnimation by infiniteTransition.animateFloat(
        initialValue = with(localDensity) { -5.dp.toPx() },
        targetValue = with(localDensity) { 5.dp.toPx() },
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(
                durationMillis = 1100,
                easing = EaseInOutCubic
            ), repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    var isWelcomeAnimationPerformed by remember {
        mutableStateOf(false)
    }


    LaunchedEffect(key1 = Unit) {
        //delay(1000)
        isWelcomeAnimationPerformed = true
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = rememberMutableInteractionSource(),
                indication = ripple()
            ) {

            }

    ) {
        AndroidView(
            factory = { context -> SurfaceView(context) },
            modifier = Modifier.fillMaxHeight()
        ) { surfaceView ->
            viewModel.player.setDisplay(surfaceView.holder)
        }
        Row(modifier = Modifier.padding(12.dp)) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = timeText,
                fontFamily = wearbiliFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.spx
            )
        }
        Text(
            text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        brush = Brush.horizontalGradient(
                            listOf(
                                Color(213, 77, 121),
                                Color(165, 59, 94)
                            )
                        )
                    )
                ) {
                    append("Re:")
                }
                withStyle(
                    SpanStyle(
                        brush = Brush.horizontalGradient(
                            listOf(
                                Color(215, 215, 215),
                                Color(249, 249, 249)
                            )
                        )
                    )
                ) {
                    append("WearBili")
                }
            },
            modifier = Modifier.align(Alignment.Center),
            fontFamily = wearbiliFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.weight(3f))
            AnimatedVisibility(
                visible = isWelcomeAnimationPerformed,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                enter = fadeIn(
                    tween(durationMillis = 1000)
                ) + slideInVertically(
                    tween(durationMillis = 1000)
                ) { it / 2 }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        //.offset { IntOffset(x = 0, y = floatingHintAnimation.toInt()) }
                        .graphicsLayer {
                            translationY = floatingHintAnimation
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Badge(backgroundColor = Color.White, modifier = Modifier.size(5.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "轻点屏幕以开始",
                        fontFamily = wearbiliFontFamily,
                        fontSize = 12.sp,
                        color = Color.White,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}

@Composable
fun InfiniteRippleEffect(
    color: Color = Color.Blue,
    rippleRadius: Float = 50f,
    animationDurationMillis: Int = 1000
) {
    val density = LocalDensity.current
    val rippleRadiusPx = with(density) { rippleRadius.dp.toPx() }

    var ripplePosition by remember { mutableStateOf(Offset.Zero) }
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = animationDurationMillis,
                easing = FastOutSlowInEasing,
                delayMillis = 3000
            ),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    LaunchedEffect(Unit) {
        while (true) {
            ripplePosition = Offset(rippleRadiusPx * pulse, rippleRadiusPx * pulse)
            delay(10)
        }
    }

    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        drawRipple(ripplePosition, rippleRadiusPx, color)
    }
}

private fun DrawScope.drawRipple(
    position: Offset,
    radius: Float,
    color: Color
) {
    drawCircle(color, radius, position)
}

@Composable
fun ReBadge() {
    Text(
        text = "Re",
        modifier = Modifier
            .offset(y = 3.dp)
            .background(
                color = BilibiliPink, shape = RoundedCornerShape(
                    topStart = 7.dp, topEnd = 7.dp, bottomEnd = 7.dp, bottomStart = 2.dp
                )
            )
            .padding(
                start = 6.dp,
                end = 6.dp,
                top = 2.dp,
                bottom = 2.dp
            ),
        fontFamily = wearbiliFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 8.spx
    )
}