package cn.spacexc.wearbili.remake.app.welcome

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import java.io.File

class WelcomeViewModel(application: Application) : AndroidViewModel(application) {
    val player = IjkMediaPlayer().apply {
        val videoFile = File(application.filesDir, "video_silk_background.mp4")
        setDataSource(videoFile.absolutePath)
        setOnPreparedListener {
            it.start()
            it.isLooping = true
        }
        prepareAsync()
    }
}