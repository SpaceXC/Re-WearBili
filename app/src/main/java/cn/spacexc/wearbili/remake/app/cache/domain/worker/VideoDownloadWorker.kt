package cn.spacexc.wearbili.remake.app.cache.domain.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import cn.spacexc.bilibilisdk.sdk.bangumi.info.BANGUMI_ID_TYPE_CID
import cn.spacexc.bilibilisdk.sdk.bangumi.info.BangumiInfo
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
            val isBangumi = params.inputData.getBoolean("isBangumi", false)
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

            val duration: Long
            val videoUrl = if (!isBangumi) {
                val videoUrlResponse = VideoInfo.getLowResolutionVideoPlaybackUrl(
                    VIDEO_TYPE_BVID,
                    videoId = bvid,
                    videoCid = cid
                )
                duration =
                    videoUrlResponse.data?.data?.timelength ?: 0 //哇！kotlin竟然还能这么玩！变量只要只改变一次就可以是val！
                videoUrlResponse.data?.data?.durl?.firstOrNull()?.url
            } else {
                val bangumiUrlResponse = BangumiInfo.getBangumiPlaybackUrl(
                    BANGUMI_ID_TYPE_CID,
                    cid
                )
                duration = bangumiUrlResponse.data?.result?.timelength ?: 0
                bangumiUrlResponse.data?.result?.durl?.firstOrNull()?.url
            } ?: ""/*return Result.failure()*/

            val subtitleResponse = VideoInfo.getVideoPlayerInfo(
                VIDEO_TYPE_BVID,
                bvid,
                cid
            ).data?.data
            val subtitleUrls = subtitleResponse?.subtitle?.subtitles ?: emptyList()
            //if (subtitleUrls.isNullOrEmpty()) return Result.failure()

            val danmakuSegmentsCount = (duration / (6 * 60 * 1000)).toInt() + 1
            repository.getTaskInfoByVideoCid(cid)?.let {
                repository.updateExistingTasks(it.copy(videoDurationMillis = duration))
            }
            //endregion

            //region download cover
            val coverFile = File(downloadPath, "$cid.cover.png")
            if (coverFile.exists()) coverFile.delete()
            coverFile.createNewFile()
            ktorNetworkUtils.downloadFile(coverUrl, coverFile, {
                repository.getTaskInfoByVideoCid(cid) != null
            }, { succeed ->
                if (!succeed) {
                    repository.getTaskInfoByVideoCid(cid)?.let {
                        repository.updateExistingTasks(it.copy(warnings = it.warnings + "封面下载失败 "))
                    }
                }
            })
            //endregion

            //region download subtitle
            val downloadedSubtitles = HashMap<String, String>()
            subtitleUrls.forEach { subtitle ->
                val subtitleFile = File(downloadPath, "$cid.subtitle.${subtitle.lan}.json")
                if (subtitleFile.exists()) subtitleFile.delete()
                subtitleFile.createNewFile()
                ktorNetworkUtils.downloadFile("https:" + subtitle.subtitle_url, subtitleFile, {
                    repository.getTaskInfoByVideoCid(cid) != null
                }, { succeed ->
                    if (!succeed) {
                        repository.getTaskInfoByVideoCid(cid)?.let {
                            repository.updateExistingTasks(it.copy(warnings = it.warnings + "字幕${subtitle.lan}下载失败 "))
                        }
                    } else {
                        downloadedSubtitles[subtitle.lan_doc] = "$cid.subtitle.${subtitle.lan}.json"
                    }
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
                    repository.getTaskInfoByVideoCid(cid)?.let {
                        repository.updateExistingTasks(it.copy(warnings = it.warnings + "第${index}段弹幕下载失败 "))
                    }
                    /*repository.getTaskInfoByVideoCid(cid)?.let {
                        repository.updateExistingTasks(it.copy(state = STATE_FAILED))
                    }*/
                    e.printStackTrace()
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
                    repository.getTaskInfoByVideoCid(cid) != null   //如果找不到对应cid的缓存（被删除了）就停止下载
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
