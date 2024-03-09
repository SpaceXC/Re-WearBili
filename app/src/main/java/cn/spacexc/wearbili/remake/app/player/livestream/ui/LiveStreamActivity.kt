package cn.spacexc.wearbili.remake.app.player.livestream.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import cn.spacexc.wearbili.remake.app.settings.ProvideConfiguration
import cn.spacexc.wearbili.remake.common.ui.theme.ProvideLocalDensity
import tv.danmaku.ijk.media.player.IjkMediaPlayer

const val PARAM_ROOM_ID = "roomId"

class LiveStreamActivity : ComponentActivity() {
    val viewModel by viewModels<LiveStreamViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )
        IjkMediaPlayer.loadLibrariesOnce(null)
        IjkMediaPlayer.native_profileBegin("libijkplayer.so")
        val roomId = intent.getLongExtra(PARAM_ROOM_ID, 0L)
        viewModel.playLiveStreamFromRoomId(roomId)
        setContent {
            ProvideConfiguration {
                ProvideLocalDensity {
                    LiveStreamScreen(
                        viewModel = viewModel
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.httpPlayer.stop()
        viewModel.httpPlayer.release()
    }
}