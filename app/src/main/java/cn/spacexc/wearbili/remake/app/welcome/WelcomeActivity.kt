package cn.spacexc.wearbili.remake.app.welcome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cn.spacexc.wearbili.remake.common.ui.theme.WearBiliTheme

class WelcomeActivity : ComponentActivity() {
    var currentScreenIndex by mutableIntStateOf(0)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearBiliTheme {
                WelcomeScreen(
                    currentScreenIndex,
                    { currentScreenIndex++ },
                    { currentScreenIndex-- })
            }
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (currentScreenIndex == 0) finish() else currentScreenIndex--
            }
        })
    }
}