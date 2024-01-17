package cn.spacexc.wearbili.remake.app.cache.domain.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import cn.spacexc.bilibilisdk.sdk.video.info.VideoInfo
import cn.spacexc.wearbili.common.domain.network.KtorNetworkUtils
import cn.spacexc.wearbili.common.formatBytes
import cn.spacexc.wearbili.remake.app.cache.domain.database.STATE_COMPLETED
import cn.spacexc.wearbili.remake.app.cache.domain.database.STATE_DOWNLOADING
import cn.spacexc.wearbili.remake.app.cache.domain.database.STATE_FAILED
import cn.spacexc.wearbili.remake.app.cache.domain.database.STATE_FETCHING
import cn.spacexc.wearbili.remake.app.cache.domain.database.VideoCacheRepository
import cn.spacexc.wearbili.remake.app.player.videoplayer.danmaku.DanmakuGetter
import cn.spacexc.wearbili.remake.app.video.info.ui.VIDEO_TYPE_BVID
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import java.io.File

@HiltWorker
class VideoDownloadWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val params: WorkerParameters,
    private var ktorNetworkUtils: KtorNetworkUtils,
    private var repository: VideoCacheRepository,
    private var danmakuGetter: DanmakuGetter
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        runCatching {
            if (repository.getAllUncompletedTasks().first().size > 2) {
                delay(1000)
            }

            //region 准备工作
            val cid = params.inputData.getLong("videoCid", 0L)
            val bvid = params.inputData.getString("videoBvid") ?: return Result.failure()
            val coverUrl = params.inputData.getString("videoCover") ?: return Result.failure()
            val cachePath = File(appContext.filesDir, "/videoCaches")
            val downloadPath = File(cachePath, "/$cid")
            if (downloadPath.exists()) {
                downloadPath.delete()
            } else {
                downloadPath.mkdir()
            }
            //endregion

            //region 获取各种url
            repository.getTaskInfoByVideoCid(cid)?.let {
                repository.updateExistingTasks(it.copy(state = STATE_FETCHING))
            }
            val videoUrlResponse = VideoInfo.getLowResolutionVideoPlaybackUrl(
                VIDEO_TYPE_BVID,
                videoId = bvid,
                videoCid = cid
            )
            //if (videoUrlResponse.data == null) return Result.failure()
            val videoUrl =
                videoUrlResponse.data?.data?.durl?.firstOrNull()?.url
                    ?: ""/*return Result.failure()*/

            val subtitleResponse = VideoInfo.getVideoPlayerInfo(
                VIDEO_TYPE_BVID,
                bvid,
                cid
            ).data?.data
            val subtitleUrls = subtitleResponse?.subtitle?.subtitles ?: emptyList()
            //if (subtitleUrls.isNullOrEmpty()) return Result.failure()

            val duration = videoUrlResponse.data?.data?.timelength ?: 0
            val danmakuSegmentsCount = (duration / (6 * 60 * 1000)).toInt() + 1
            repository.getTaskInfoByVideoCid(cid)?.let {
                repository.updateExistingTasks(it.copy(videoDurationMillis = duration))
            }
            //endregion

            //region download cover
            val coverFile = File(downloadPath, "$cid.cover.png")
            if (coverFile.exists()) coverFile.delete()
            coverFile.createNewFile()
            ktorNetworkUtils.downloadFile(coverUrl, coverFile, {}, {})
            //这里我也不管了，封面下载失败就失败了吧（
            //endregion

            //region download subtitle
            val downloadedSubtitles = HashMap<String, String>()
            subtitleUrls.forEach { subtitle ->
                val subtitleFile = File(downloadPath, "$cid.subtitle.${subtitle.lan}.json")
                if (subtitleFile.exists()) subtitleFile.delete()
                subtitleFile.createNewFile()
                ktorNetworkUtils.downloadFile("https:" + subtitle.subtitle_url, subtitleFile, {}, {
                    downloadedSubtitles[subtitle.lan_doc] = "$cid.subtitle.${subtitle.lan}.json"
                })
                //同上
            }
            repository.getTaskInfoByVideoCid(cid)?.let {
                repository.updateExistingTasks(it.copy(downloadedSubtitleFileNames = downloadedSubtitles.toMap()))
            }
            //endregion

            //region download danmaku
            (1..danmakuSegmentsCount).forEach { index ->
                try {
                    val danmakuFile = File(downloadPath, "$cid.danmaku.seg$index")
                    if (danmakuFile.exists()) danmakuFile.delete()
                    val danmaku = danmakuGetter.getDanmakuBin(cid, index)
                    if (danmaku != null) {
                        danmakuFile.appendBytes(danmaku)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    //just printing some stack trace, not dealing with these exceptions(((
                }
            }
            //endregion

            //region download video
            val videoFile = File(downloadPath, "$cid.video.mp4")
            if (videoFile.exists()) videoFile.delete()
            videoFile.createNewFile()
            repository.getTaskInfoByVideoCid(cid)?.let {
                repository.updateExistingTasks(it.copy(state = STATE_DOWNLOADING))
            }
            ktorNetworkUtils.downloadFile(
                videoUrl,
                videoFile,
                onProgressUpdate = { progress ->
                    repository.getTaskInfoByVideoCid(cid)?.let {
                        repository.updateExistingTasks(
                            it.copy(
                                downloadProgress = progress.times(100).toInt(),
                                downloadFileSize = formatBytes(videoFile.length())
                            )
                        )
                    }
                },
                onDownloadFinished = { succeeded ->
                    if (succeeded) {
                        repository.getTaskInfoByVideoCid(cid)?.let {
                            repository.updateExistingTasks(
                                it.copy(
                                    state = STATE_COMPLETED,
                                    downloadProgress = 100
                                )
                            )
                        }
                    } else {
                        repository.getTaskInfoByVideoCid(cid)?.let {
                            repository.updateExistingTasks(it.copy(state = STATE_FAILED))
                        }
                    }
                }
            )
            //endregion

            return Result.success() //反正我也不在这里处理下载结果
        }.onFailure {
            it.printStackTrace()
        }
        return Result.success() //反正我也不在这里处理下载结果
    }
}
