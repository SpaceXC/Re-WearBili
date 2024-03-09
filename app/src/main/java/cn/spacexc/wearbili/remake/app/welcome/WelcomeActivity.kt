package cn.spacexc.wearbili.remake.app.welcome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import cn.spacexc.wearbili.remake.common.ui.theme.WearBiliTheme
import tv.danmaku.ijk.media.player.IjkMediaPlayer

class WelcomeActivity : ComponentActivity() {
    var currentScreenIndex by mutableIntStateOf(0)
    val viewModel by viewModels<WelcomeViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        IjkMediaPlayer.loadLibrariesOnce(null)
        IjkMediaPlayer.native_profileBegin("libijkplayer.so")
        setContent {
            WearBiliTheme {
                WelcomeScreen(
                    viewModel,
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