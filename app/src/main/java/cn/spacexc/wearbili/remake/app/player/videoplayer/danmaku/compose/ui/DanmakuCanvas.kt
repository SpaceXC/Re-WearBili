package cn.spacexc.wearbili.remake.app.player.videoplayer.danmaku.compose.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import cn.spacexc.wearbili.common.ifNullOrZero
import cn.spacexc.wearbili.remake.app.player.videoplayer.danmaku.compose.DanmakuCanvasState
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import kotlinx.coroutines.delay

/**
 * Created by XC-Qan on 2023/12/10.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@Composable
fun DanmakuCanvas(
    modifier: Modifier = Modifier,
    state: DanmakuCanvasState,
    textMeasurer: TextMeasurer,
    textSize: Float = 1f,
    playSpeed: Float = 1f
) {
    var framePerSecond by remember {
        mutableIntStateOf(0)
    }
    var currentFrameRate by remember {
        mutableIntStateOf(0)
    }
    LaunchedEffect(key1 = Unit, block = {
        while (true) {
            currentFrameRate = framePerSecond
            framePerSecond = 0
            delay(1000)
        }
    })
    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = modifier.fillMaxSize()) {
            state.displayingDanmakus.forEach { danmaku ->
                if (danmaku.y + danmaku.textHeight <= size.height && danmaku.y >= 0) {
                    drawText(
                        textLayoutResult = textMeasurer.measure(
                            text = AnnotatedString(danmaku.content),
                            style = TextStyle(
                                fontSize = (danmaku.fontSize * 0.55 * textSize).sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = wearbiliFontFamily
                            ),
                            layoutDirection = layoutDirection,
                            density = this@Canvas,
                        ), topLeft = Offset(danmaku.x, danmaku.y), color = danmaku.color,
                        //drawStyle = Stroke(width = 1f, )
                        shadow = Shadow(Color.Black, blurRadius = 1.5f)
                    )
                }
            }
            state.updatedDanmaku(
                textMeasurer,
                this,
                textSize,
                playSpeed,
                framePerSecond.ifNullOrZero { 60 }.toInt()
            )
            framePerSecond++
        }
        Text(
            text = "$currentFrameRate fps, ${state.updateTimer() / 1000f}",
            modifier = Modifier.align(
                Alignment.BottomStart
            ),
            color = Color.White
        )
    }
}