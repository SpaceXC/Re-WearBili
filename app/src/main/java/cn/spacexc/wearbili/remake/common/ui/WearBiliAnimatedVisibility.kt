package cn.spacexc.wearbili.remake.common.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cn.spacexc.wearbili.remake.app.settings.SettingsManager

/**
 * Created by XC-Qan on 2023/4/16.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@Composable
fun WearBiliAnimatedVisibility(
    visible: Boolean,
    modifier: Modifier = Modifier,
    enter: EnterTransition = fadeIn() + expandIn(),
    exit: ExitTransition = shrinkOut() + fadeOut(),
    label: String = "AnimatedVisibility",
    content: @Composable /*AnimatedVisibilityScope.*/() -> Unit
) {
    if (SettingsManager.getInstance().isLowPerformance) {
        if (visible) content()
    } else {
        androidx.compose.animation.AnimatedVisibility(visible, modifier, enter, exit, label) {
            content()
        }
    }

}