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
import cn.spacexc.wearbili.remake.app.Application
import cn.spacexc.wearbili.remake.app.cache.domain.database.VideoCacheRepository
import cn.spacexc.wearbili.remake.app.player.audio.ui.Media3AudioPlayerViewModel
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.PARAM_IS_BANGUMI
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.PARAM_IS_CACHE
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.PARAM_VIDEO_CID
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.PARAM_VIDEO_ID
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.PARAM_VIDEO_ID_TYPE
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.VIDEO_TYPE_BVID
import cn.spacexc.wearbili.remake.common.ToastUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
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

    lateinit var viewModel: Media3AudioPlayerViewModel

    var cid = 0L

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val isCache = intent.getBooleanExtra(PARAM_IS_CACHE, false)

            val videoIdType = intent.getStringExtra(PARAM_VIDEO_ID_TYPE) ?: VIDEO_TYPE_BVID
            val videoId = intent.getStringExtra(PARAM_VIDEO_ID)
            val videoCid = intent.getLongExtra(PARAM_VIDEO_CID, 0L)
            val isBangumi = intent.getBooleanExtra(PARAM_IS_BANGUMI, false)

            if (videoCid != cid && videoCid != 0L) {
                cid = videoCid
                try {
                    viewModel.player.stop()
                    viewModel.player.release()
                } catch (_: Exception) {

                }
                viewModel = Media3AudioPlayerViewModel(
                    Application.getApplication(),
                    repository,
                    lifecycleScope
                )
                if (isCache) {
                    lifecycleScope.launch {
                        repository.getTaskInfoByVideoCid(videoCid)?.let {
                            viewModel.cacheVideoInfo = it
                        }
                    }
                    viewModel.playVideoFromLocalFile(videoCid)
                } else {
                    viewModel.playVideoFromId(
                        videoIdType,
                        videoId!!,
                        videoCid,
                        isBangumi
                    ) { title ->
                        updateNotification(videoCid, videoId, videoIdType, title)
                    }
                }

                createChannelId()
                val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.icon_outline_radio)
                    .setContentTitle("Re:WearBili正在播放")
                    .setContentText(videoId ?: "未知音频")
                    .setContentIntent(
                        PendingIntent.getActivity(
                            this,
                            0,
                            Intent(this, AudioPlayerActivity::class.java).apply {
                                putExtra(
                                    cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID_TYPE,
                                    videoIdType
                                )
                                putExtra(
                                    cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID,
                                    videoId
                                )
                                putExtra(
                                    cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_CID,
                                    videoCid
                                )
                                //putExtras(intent)
                            },
                            PendingIntent.FLAG_IMMUTABLE
                        )
                    )
                    .build()
                startForeground(NOTIFICATION_ID, notification)      //id must be positive!
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun updateNotification(
        videoCid: Long,
        videoId: String,
        videoIdType: String,
        videoTitle: String
    ) {
        createChannelId()
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.icon_outline_radio)
            .setContentTitle("Re:WearBili正在播放")
            .setContentText(videoTitle)
            .setContentIntent(
                PendingIntent.getActivity(
                    this,
                    0,
                    Intent(this, AudioPlayerActivity::class.java).apply {
                        putExtra(
                            cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID_TYPE,
                            videoIdType
                        )
                        putExtra(
                            cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID,
                            videoId
                        )
                        putExtra(
                            cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_CID,
                            videoCid
                        )
                        //putExtras(intent)
                    },
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
            .build()
        startForeground(NOTIFICATION_ID, notification)      //id must be positive!
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

    override fun onLowMemory() {
        super.onLowMemory()
        ToastUtils.showText("系统运行内存不足")
    }

    fun exitService() {
        //videoCid = 0
        lifecycleScope.cancel()
        viewModel.player.stop()
        viewModel.player.release()
        stopSelf()
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