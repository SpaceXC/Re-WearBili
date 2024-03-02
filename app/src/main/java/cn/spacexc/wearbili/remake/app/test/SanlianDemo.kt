package cn.spacexc.wearbili.remake.app.test

import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cn.spacexc.wearbili.remake.common.ui.clickVfx
import cn.spacexc.wearbili.remake.common.ui.rememberMutableInteractionSource
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

@Composable
fun SanlianDemo(
    isFinished: Boolean,
    onFinish: () -> Unit,
    onFinishedClick: () -> Unit
) {
    var hitProgress by remember { mutableStateOf(0) }
    var hitJob by remember { mutableStateOf<Job?>(null) }
    val interactionSource = rememberMutableInteractionSource()
    val isPressed by interactionSource.collectIsPressedAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = isPressed, block = {
        if (isPressed) {
            hitJob?.cancel()
            hitJob = scope.async {
                delay(1000)
                if (isFinished) {
                    onFinishedClick()
                    return@async
                }
                // 手指按下后，逐步减少hitProgress，使圆弧角度逆时针增加
                while (hitProgress > -360) {
                    delay(15)
                    hitProgress -= 4
                }
                onFinish()
            }
        } else {
            hitJob?.cancel()
            if (isFinished) {
                hitProgress = 0
                return@LaunchedEffect
            }
            hitJob = scope.async {
                // 手指抬起时， 增加hitProgress，使圆弧逐步缩短
                while (hitProgress < 0) {
                    delay(8)
                    hitProgress += 4
                }
            }
        }
    })


    Text(
        text = hitProgress.toString(),
        modifier = Modifier.clickVfx(interactionSource = interactionSource)
    )
}