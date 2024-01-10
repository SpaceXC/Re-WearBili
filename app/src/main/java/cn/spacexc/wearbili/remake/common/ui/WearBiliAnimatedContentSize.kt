package cn.spacexc.wearbili.remake.common.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.IntSize
import cn.spacexc.wearbili.remake.ui.settings.SettingsManager

/**
 * Created by XC-Qan on 2023/4/16.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

fun Modifier.wearBiliAnimatedContentSize(
    animationSpec: FiniteAnimationSpec<IntSize> = spring(),
    finishedListener: ((initialValue: IntSize, targetValue: IntSize) -> Unit)? = null
): Modifier = composed {
    val isLowPerformance by SettingsManager.isLowPerformance.collectAsState(initial = false)
    if (!isLowPerformance) {
        animateContentSize(
            animationSpec, finishedListener
        )
    } else Modifier
}