package cn.spacexc.wearbili.remake.common.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.IntSize
import cn.spacexc.wearbili.remake.app.settings.LocalConfiguration

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
    if (LocalConfiguration.current.hasAnimation) {
        animateContentSize(
            animationSpec, finishedListener
        )
    } else Modifier
}

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.wearBiliAnimateContentPlacement(scope: LazyItemScope): Modifier = composed {
    if (LocalConfiguration.current.hasAnimation) {
        with(scope) {
            animateItemPlacement()
        }
    } else Modifier
}