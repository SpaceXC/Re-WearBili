package cn.spacexc.wearbili.remake.app.cache.create.ui

import android.app.Application
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
import cn.spacexc.wearbili.remake.app.cache.domain.database.VideoCacheFileInfo
import cn.spacexc.wearbili.remake.app.cache.domain.database.VideoCacheRepository
import cn.spacexc.wearbili.remake.app.cache.domain.worker.VideoDownloadWorker
import cn.spacexc.wearbili.remake.app.video.info.ui.VIDEO_TYPE_BVID
import cn.spacexc.wearbili.remake.common.ToastUtils
import cn.spacexc.wearbili.remake.common.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

    suspend fun cacheVideos(
        videoParts: List<Page>,
    ) {

        videoParts.forEach { page ->
            queryDownload(
                videoCid = page.cid,
                videoBvid = videoInfo?.data?.bvid ?: "",
                videoPartName = page.part,
                videoCover = videoInfo?.data?.pic ?: ""
            )
        }
    }

    private suspend fun queryDownload(
        videoCid: Long,
        videoBvid: String,
        videoPartName: String,
        videoCover: String
    ) {
        val downloadCacheInfo = VideoCacheFileInfo(
            videoBvid = videoBvid,
            videoName = videoInfo?.data?.title ?: "",
            videoPartName = videoPartName,
            videoCover = videoInfo?.data?.pic ?: "",
            videoUploaderName = videoInfo?.data?.owner?.name ?: "",
            videoCid = videoCid,
        )
        try {
            repository.insertNewTasks(downloadCacheInfo)
            val videoDownloadRequest = OneTimeWorkRequestBuilder<VideoDownloadWorker>()
                .setInputData(
                    workDataOf(
                        "videoCid" to videoCid,
                        "videoBvid" to videoBvid,
                        "videoCover" to videoCover
                    )
                )
                .setConstraints(
                    Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
                )
                .build()
            WorkManager.getInstance(application).enqueue(videoDownloadRequest)
        } catch (e: Exception) {
            uiState = UIState.Success
            ToastUtils.showText("下载失败了！${e.message}")
        }
    }
}