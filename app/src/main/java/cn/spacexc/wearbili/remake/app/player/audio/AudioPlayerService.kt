package cn.spacexc.wearbili.remake.app.player.audio

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.MainActivity
import cn.spacexc.wearbili.remake.app.cache.domain.database.VideoCacheRepository
import cn.spacexc.wearbili.remake.app.player.audio.ui.IjkPlayerAudioPlayerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by XC-Qan on 2023/10/16.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

const val CHANNEL_ID = "reWearBiliAudioPlayerNotificationChannelId"
const val NOTIFICATION_ID = 1

@AndroidEntryPoint
class AudioPlayerService : LifecycleService() {

    @Inject
    lateinit var repository: VideoCacheRepository

    lateinit var viewModel: IjkPlayerAudioPlayerViewModel


    private var subtitleUpdateJob: Job? = null
    private var subtitleTimeUpdateJob: Job? = null


    private var currentCid: Long = 0

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createChannelId()
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.icon_outline_radio)
            .setContentTitle("Re:WearBili音频播放")
            .setContentIntent(
                PendingIntent.getActivity(
                    this,
                    0,
                    Intent(this, MainActivity::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
            .build()
        startForeground(NOTIFICATION_ID, notification)

        println("Service started!")
        viewModel = IjkPlayerAudioPlayerViewModel(application, repository, lifecycleScope)

        return super.onStartCommand(intent, flags, startId)
    }

    fun playAudio(videoIdType: String, videoId: String, videoCid: Long) {
        if (currentCid != videoCid) {
            currentCid = videoCid
            AudioPlayerManager.currentPlayerTask.value = AudioPlayerTask(
                isCache = false,
                videoIdType = videoIdType,
                videoId = videoId,
                videoCid = videoCid,
                isBangumi = false
            )
            viewModel.player.stop()
            viewModel.player.release()
            subtitleUpdateJob?.cancel()
            subtitleUpdateJob?.cancel()
            viewModel = IjkPlayerAudioPlayerViewModel(application, repository, lifecycleScope)
            viewModel.playVideoFromId(videoIdType, videoId, videoCid, false) {
                updateSubtitle()
            }
        }
    }

    private fun updateSubtitle() {
        AudioPlayerManager.currentPlayingSubtitle = null
        subtitleTimeUpdateJob = lifecycleScope.launch {
            viewModel.currentPlayProgress.collect {
                AudioPlayerManager.currentProgress = it.toDouble() / 1000
                delay(10)
            }
        }
        subtitleUpdateJob = lifecycleScope.launch {
            viewModel.currentSubtitle.collect {
                if (it != null) {
                    AudioPlayerManager.currentPlayingSubtitle = it
                }
            }
        }
    }

    private fun createChannelId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = "WearBili音频播放"
            val descriptionText = "允许WearBili在后台播放音频"
            val importance = NotificationManager.IMPORTANCE_LOW
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    fun exitService() {
        //videoCid = 0
        //viewModel.player.stop()
        viewModel.player.release()
        AudioPlayerManager.currentVideo = ""
        AudioPlayerManager.currentPlayingSubtitle = null
        AudioPlayerManager.currentProgress = 0.0
        AudioPlayerManager.isSubtitleOn = false
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return AudioPlayerBinder()
    }

    inner class AudioPlayerBinder : Binder() {
        val service = this@AudioPlayerService
    }

    override fun onDestroy() {
        super.onDestroy()
        exitService()
    }
}