package cn.spacexc.wearbili.remake.app.cache.create.ui

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import cn.spacexc.bilibilisdk.sdk.video.info.VideoInfo
import cn.spacexc.bilibilisdk.sdk.video.info.remote.info.web.Page
import cn.spacexc.bilibilisdk.sdk.video.info.remote.info.web.WebVideoInfo
import cn.spacexc.bilibilisdk.sdk.video.info.remote.playerinfo.Subtitle
import cn.spacexc.wearbili.remake.app.TAG
import cn.spacexc.wearbili.remake.app.cache.domain.database.VideoCacheFileInfo
import cn.spacexc.wearbili.remake.app.cache.domain.database.VideoCacheRepository
import cn.spacexc.wearbili.remake.app.cache.domain.worker.DanmakuDownloadWorker
import cn.spacexc.wearbili.remake.app.video.info.ui.VIDEO_TYPE_BVID
import cn.spacexc.wearbili.remake.common.ToastUtils
import cn.spacexc.wearbili.remake.common.UIState
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.common.HttpOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

/**
 * Created by XC-Qan on 2023/9/5.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@HiltViewModel
class CreateNewCacheViewModel @Inject constructor(
    val application: Application,
    private val repository: VideoCacheRepository
) : ViewModel() {

    var uiState by mutableStateOf(UIState.Loading)

    var videoPages by mutableStateOf(emptyList<Page>())
    private var videoInfo: WebVideoInfo? by mutableStateOf(null)

    fun getVideoParts(
        videoBvid: String
    ) {
        viewModelScope.launch {
            val response = VideoInfo.getVideoInfoByIdWeb(VIDEO_TYPE_BVID, videoBvid)
            if (response.code != 0) {
                uiState = UIState.Failed
                return@launch
            }
            val parts = response.data?.data?.pages
            if (parts == null) {
                uiState = UIState.Failed
                return@launch
            }
            videoPages = parts
            videoInfo = response.data
            uiState = UIState.Success
        }
    }

    private suspend fun getSubtitles() = VideoInfo.getVideoPlayerInfo(
        cn.spacexc.wearbili.remake.app.video.info.ui.VIDEO_TYPE_AID,
        videoInfo?.data?.aid?.toString() ?: "",
        videoInfo?.data?.cid ?: 0
    ).data?.data?.subtitle?.subtitles

    suspend fun cacheVideos(
        videoParts: List<Page>
    )/*: List<Pair<Pair<String, Long>, String?>>*/ {
        uiState = UIState.Loading
        val videoBvid = videoInfo!!.data.bvid
        val tasks = videoParts.map { part ->
            viewModelScope.async {
                val response = VideoInfo.getLowResolutionVideoPlaybackUrl(
                    VIDEO_TYPE_BVID,
                    videoId = videoBvid,
                    videoCid = part.cid
                )
                if (response.code == 0) {
                    return@async Pair(
                        Pair(part.part, part.cid),
                        response.data?.data?.durl?.firstOrNull()?.url
                    )
                } else {
                    return@async Pair(Pair(part.part, part.cid), null)
                }
            }
        }
        val urls = tasks.awaitAll()
        Log.d(TAG, "cacheVideos: $urls")
        if (urls.any { it.second == null }) {
            uiState = UIState.Success
            ToastUtils.showText("下载失败了！原因：视频下载链接获取失败")
        }
        queryDownload(urls, videoBvid, getSubtitles() ?: emptyList())
        //return tasks.awaitAll()
    }

    private suspend fun queryDownload(
        urlInfo: List<Pair<Pair<String, Long>, String?>>,
        videoBvid: String,
        subtitles: List<Subtitle>
    ) {
        urlInfo.forEach {
            it.second?.let { url ->
                val downloadPath = File(application.filesDir, "/videoCaches/${it.first.second}")
                if (downloadPath.exists()) {
                    downloadPath.delete()
                } else {
                    downloadPath.mkdir()
                }
                val oldTask = repository.getTaskInfoByVideoCid(it.first.second)
                if (oldTask != null) {
                    repository.deleteExistingTasks(oldTask)
                    Aria.download(this).load(oldTask.cacheId).cancel(true)
                }
                val urls = buildList {
                    add(url)
                    add(videoInfo?.data?.pic?.replace("http://", "https://") ?: "")
                    subtitles.forEach { subtitle ->
                        add("https:${subtitle.subtitle_url}")
                    }
                }
                val subtitleUrlMap =
                    subtitles.associate { subtitle -> subtitle.lan to "https:${subtitle.subtitle_url}" }
                val cacheId = Aria
                    .download(this)
                    .loadGroup(urls)
                    .unknownSize()
                    .setDirPath(downloadPath.path)
                    .option(
                        HttpOption().apply {
                            addHeader("Referer", "https://www.bilibili.com/")
                            addHeader(
                                "User-Agent",
                                "Mozilla/5.0 BiliDroid/*.*.* (bbcallen@gmail.com)"
                            )
                        })
                    .create()
                val danmakuDownloadWorkRequest = OneTimeWorkRequestBuilder<DanmakuDownloadWorker>()
                    .setInputData(
                        workDataOf(
                            "cid" to it.first.second
                        )
                    )
                    .setConstraints(
                        Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
                    )
                    .build()
                WorkManager.getInstance(application).enqueue(danmakuDownloadWorkRequest)
                val downloadCacheInfo = VideoCacheFileInfo(
                    cacheId = cacheId,
                    videoBvid = videoBvid,
                    videoUrl = url,
                    videoName = videoInfo?.data?.title ?: "",
                    videoPartName = it.first.first,
                    videoCover = videoInfo?.data?.pic ?: "",
                    videoUploaderName = videoInfo?.data?.owner?.name ?: "",
                    videoCid = it.first.second,
                    videoSubtitleUrls = subtitleUrlMap
                )
                try {
                    repository.insertNewTasks(downloadCacheInfo)
                } catch (e: Exception) {
                    uiState = UIState.Success
                    ToastUtils.showText("下载失败了！${e.message}")
                }
            }
        }
    }
}