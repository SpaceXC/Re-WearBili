package cn.spacexc.wearbili.remake.app.welcome

import android.app.Activity
import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import cn.spacexc.wearbili.remake.app.welcome.screens.PrivacyScreen
import cn.spacexc.wearbili.remake.app.welcome.screens.StartScreen

@Composable
fun Activity.WelcomeScreen(
    viewModel: WelcomeViewModel,
    currentScreenIndex: Int,
    onNext: () -> Unit,
    onPrev: () -> Unit
) {
    AnimatedContent(targetState = currentScreenIndex, label = "") { index ->
        when (index) {
            0 -> StartScreen(viewModel) {
                onNext()
            }

            1 -> PrivacyScreen()
        }
    }
}