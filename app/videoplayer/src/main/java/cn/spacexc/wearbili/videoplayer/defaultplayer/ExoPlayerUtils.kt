package cn.spacexc.wearbili.videoplayer.defaultplayer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.content.ContextCompat.getSystemService
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.DatabaseProvider
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.HttpDataSource
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadIndex
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadNotificationHelper
import androidx.media3.exoplayer.scheduler.Requirements
import java.io.File
import java.util.concurrent.Executor

/**
 * Created by XC-Qan on 2022/7/30.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

const val DOWNLOAD_NOTIFICATION_CHANNEL_ID = "WearBiliChannelID"

@UnstableApi
class ExoPlayerUtils(context: Context) {

    private var downloadManager: DownloadManager
    private var databaseProvider: DatabaseProvider
    private var downloadCache: Cache
    private var dataSourceFactory: HttpDataSource.Factory
    private var downloadExecutor: Executor
    private var requirements: Requirements
    private var downloadIndex: DownloadIndex


    fun getDownloadManager(): DownloadManager {
        return downloadManager
    }

    init {
        databaseProvider = StandaloneDatabaseProvider(context)

        downloadCache = SimpleCache(
            getDownloadDirectory(context)!!,
            NoOpCacheEvictor(),
            databaseProvider
        )

        // Create a factory for reading the data from the network.
        dataSourceFactory = DefaultHttpDataSource.Factory()

        dataSourceFactory.setDefaultRequestProperties(
            HashMap<String, String>().apply {
                this["User-Agent"] = "Mozilla/5.0 BiliDroid/*.*.* (bbcallen@gmail.com)"
                this["Referer"] = "https://bilibili.com/"
            }
        )

        downloadExecutor = Executor { obj: Runnable -> obj.run() }


// Create the download manager.
        downloadManager = DownloadManager(
            context,
            databaseProvider,
            downloadCache,
            dataSourceFactory,
            downloadExecutor
        )
        downloadIndex = downloadManager.downloadIndex
        downloadManager.addListener(object : DownloadManager.Listener {
            override fun onDownloadChanged(
                downloadManager: DownloadManager,
                download: Download,
                finalException: Exception?
            ) {
                super.onDownloadChanged(downloadManager, download, finalException)
            }
        })
        requirements = Requirements(Requirements.NETWORK)
        downloadManager.requirements = requirements
        downloadManager.maxParallelDownloads = 3
    }

    @Synchronized
    private fun getDownloadDirectory(context: Context): File? {
        var downloadDirectory = context.getExternalFilesDir(null)
        if (downloadDirectory == null) {
            downloadDirectory = context.filesDir
        }
        return downloadDirectory
    }

    @Synchronized
    fun getDownloadNotificationHelper(
        context: Context
    ): DownloadNotificationHelper {
        createNotificationChannelId(context)
        return DownloadNotificationHelper(context, DOWNLOAD_NOTIFICATION_CHANNEL_ID)
    }

    private fun createNotificationChannelId(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = "WearBili缓存服务"
            val descriptionText = "WearBili视频缓存"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(DOWNLOAD_NOTIFICATION_CHANNEL_ID, name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager =
                getSystemService(context, NotificationManager::class.java) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

    }

    fun getCache(): Cache {
        return downloadCache
    }

    fun getDownloadedVideos(): List<Download> {
        val list = mutableListOf<Download>()
        val downloads = downloadIndex.getDownloads(Download.STATE_COMPLETED, Download.STATE_FAILED)
        downloads.moveToPosition(-1)
        while (downloads.moveToNext()) {
            list.add(downloads.download)
            //downloadManager.downloadIndex.getDownloads().moveToNext()
        }
        return list
    }

    fun getDownloadingVideos(): List<Download> {
        return downloadManager.currentDownloads
    }

    /*fun downloadVideo(
        coverUrl: String,
        title: String,
        partName: String,
        bvid: String,
        cid: Long,
        isHighResolution: Boolean,
        subtitleUrl: String?,
        onTaskAdded: () -> Unit
    ) {
        val callback = object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                MainScope().launch {
                    ToastUtils.showText("网络异常")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val result = Gson().fromJson(response.body?.string(), VideoStreamsFlv::class.java)
                val downloadRequest =
                    if (isHighResolution) DownloadRequest.Builder(
                        "$cid///$title///$partName",
                        Uri.parse(result.data.durl[0].url)
                    )
                        .build() else DownloadRequest.Builder(
                        "$cid///$title///$partName",
                        Uri.parse(result.data.durl.last().url)
                    )
                        .build()
                DownloadService.sendAddDownload(
                    Application.context!!,
                    cn.spacexc.wearbili.service.DownloadService::class.java,
                    downloadRequest,
                    false
                )
                val danmakuDownloadWorkRequest = OneTimeWorkRequestBuilder<DanmakuDownloadWorker>()
                    .setInputData(
                        workDataOf(
                            "cid" to cid.toString()
                        )
                    )
                    .setConstraints(
                        Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
                    )
                    .build()
                WorkManager.getInstance(Application.context!!).enqueue(danmakuDownloadWorkRequest)

                val coverPicDownloadWorker = OneTimeWorkRequestBuilder<ImageDownloadWorker>()
                    .setInputData(
                        workDataOf(
                            "cid" to cid.toString(),
                            "coverUrl" to coverUrl
                        )
                    )
                    .setConstraints(
                        Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
                    )
                    .build()
                WorkManager.getInstance(Application.context!!).enqueue(coverPicDownloadWorker)

                if (subtitleUrl != null) {
                    val subtitleDownloadWorker = OneTimeWorkRequestBuilder<SubtitleDownloadWorker>()
                        .setInputData(
                            workDataOf(
                                "fileUrl" to subtitleUrl,
                                "fileName" to "subtitle_$cid.json",
                                "filePath" to "subtitle/",
                                "cid" to cid.toString()
                            )
                        )
                        .setConstraints(
                            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
                                .build()
                        )
                        .build()
                    WorkManager.getInstance(Application.context!!).enqueue(subtitleDownloadWorker)
                }
                MainScope().launch {
                    ToastUtils.showText("已添加到下载队列")
                    onTaskAdded()
                }
            }
        }
        VideoManager.getVideoUrl(bvid, cid, callback, isHighResolution)
    }*/

    companion object {
        var instance: ExoPlayerUtils? = null
        fun getInstance(context: Context): ExoPlayerUtils {
            if (instance == null) instance = ExoPlayerUtils(context)
            return instance!!
        }
    }
}