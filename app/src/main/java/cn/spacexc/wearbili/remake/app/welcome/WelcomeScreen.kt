package cn.spacexc.wearbili.remake.app.welcome

import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import cn.spacexc.wearbili.remake.app.welcome.screens.PrivacyScreen
import cn.spacexc.wearbili.remake.app.welcome.screens.StartScreen

@Composable
fun WelcomeScreen(
    currentScreenIndex: Int,
    onNext: () -> Unit,
    onPrev: () -> Unit
) {
    AnimatedContent(targetState = currentScreenIndex, label = "") { index ->
        when (index) {
            0 -> StartScreen {
                onNext()
            }

            1 -> PrivacyScreen()
        }
    }
}