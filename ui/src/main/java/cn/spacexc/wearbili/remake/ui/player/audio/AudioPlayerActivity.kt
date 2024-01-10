package cn.spacexc.wearbili.remake.app.player.audio

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import cn.spacexc.wearbili.remake.app.player.audio.ui.AudioPlayerActivityScreen
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by XC-Qan on 2023/10/15.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@AndroidEntryPoint
class AudioPlayerActivity : ComponentActivity() {
    var audioService: AudioPlayerService? by mutableStateOf(null)
    private var connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val audioPlayerService = (service as AudioPlayerService.AudioPlayerBinder).service
            audioService = audioPlayerService
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bindingIntent = Intent(this, AudioPlayerService::class.java).apply {
            putExtras(intent)
        }
        startService(bindingIntent)
        bindService(bindingIntent, connection, Service.BIND_AUTO_CREATE)

        setContent {
            audioService?.let { service ->
                AudioPlayerActivityScreen(service = service)
            }
            if (audioService == null) {
                LoadingScreen()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
    }

    @Composable
    fun LoadingScreen() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(70, 70, 70, 100)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "音频播放器正在加载哦...",
                color = Color(255, 255, 255, 128),
                fontFamily = wearbiliFontFamily,
                fontWeight = FontWeight.Medium
            )
        }
    }
}