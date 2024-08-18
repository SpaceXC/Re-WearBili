package cn.spacexc.wearbili.remake.app.bangumi.info.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import cn.spacexc.bilibilisdk.sdk.bangumi.info.BangumiInfo
import cn.spacexc.bilibilisdk.sdk.bangumi.info.remote.Episode
import cn.spacexc.bilibilisdk.sdk.bangumi.info.remote.Result
import cn.spacexc.bilibilisdk.sdk.video.info.VideoInfo
import cn.spacexc.wearbili.remake.app.Application
import cn.spacexc.wearbili.remake.app.cache.domain.database.VideoCacheFileInfo
import cn.spacexc.wearbili.remake.app.cache.domain.database.VideoCacheRepository
import cn.spacexc.wearbili.remake.app.cache.domain.worker.VideoDownloadWorker
import cn.spacexc.wearbili.remake.app.video.info.ui.VIDEO_TYPE_BVID
import cn.spacexc.wearbili.remake.common.ToastUtils
import cn.spacexc.wearbili.remake.common.UIState
import cn.spacexc.wearbili.remake.common.networking.KtorNetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readBytes
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by XC-Qan on 2023/7/19.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

const val BANGUMI_ID_TYPE_EPID = "ep_id"
const val BANGUMI_ID_TYPE_SSID = "season_id"

@HiltViewModel
class BangumiViewModel @Inject constructor(
    private val ktorNetworkUtils: KtorNetworkUtils,
    private val application: Application,
    private val repository: VideoCacheRepository
) : ViewModel() {
    var bangumiInfo: Result? by mutableStateOf(null)
    var uiState: UIState by mutableStateOf(UIState.Loading)
    var imageBitmap: Bitmap? by mutableStateOf(null)

    val bangumiInfoScreenScrollState = LazyListState()

    private val sectionsScrollState = HashMap<Long, LazyListState>()
    val episodeScrollState = LazyListState()

    var currentSelectedEpid by mutableLongStateOf(0L)

    fun getCurrentSelectedEpisode(): Episode? {
        var episode = bangumiInfo?.episodes?.find { it.ep_id == currentSelectedEpid }
        if (episode == null) {
            val section = bangumiInfo?.section?.find {
                it.episodes.find { epInSection ->
                    epInSection.ep_id == currentSelectedEpid
                } != null
            }
            episode = section?.episodes?.find { it.ep_id == currentSelectedEpid }
        }
        return episode
    }

    fun getBangumiInfo(
        bangumiIdType: String = BANGUMI_ID_TYPE_EPID,
        bangumiId: Long
    ) {
        viewModelScope.launch {
            val response = BangumiInfo.getBangumiInfo(bangumiIdType, bangumiId)
            if (response.code != 0) {
                uiState = UIState.Failed(response.code)
                return@launch
            }
            bangumiInfo = response.data?.result
            uiState = UIState.Success
            getImageBitmap(response.data?.result?.cover ?: "")
        }
    }

    private fun getImageBitmap(url: String) {
        viewModelScope.launch {
            try {
                val response: HttpResponse = ktorNetworkUtils.client.get(url) {
                    //accept(ContentType.Application.Json)
                }
                if (response.status == HttpStatusCode.OK) {
                    val bytes = response.readBytes()
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    if (bitmap != null) {
                        imageBitmap = bitmap
                    }
                } else {
                    MainScope().launch {
                        ToastUtils.showText("${response.status.value}")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getSectionScrollState(id: Long): LazyListState {
        if (sectionsScrollState[id] == null) {
            sectionsScrollState[id] = LazyListState()
        }
        return sectionsScrollState[id]!!
    }

    private suspend fun getSubtitles() = VideoInfo.getVideoPlayerInfo(
        VIDEO_TYPE_BVID,
        getCurrentSelectedEpisode()?.bvid ?: "",
        getCurrentSelectedEpisode()?.cid ?: 0,
    ).data?.data?.subtitle?.subtitles

    suspend fun cacheBangumi(navController: NavController) {
        val videoBvid = getCurrentSelectedEpisode()?.bvid ?: ""
        val videoCid = getCurrentSelectedEpisode()?.cid ?: 0
        val episodeName = getCurrentSelectedEpisode()?.long_title?.ifEmpty {
            getCurrentSelectedEpisode()?.title ?: ""
        } ?: ""
        val coverUrl = getCurrentSelectedEpisode()?.cover ?: ""
        queryDownload(videoBvid, videoCid, bangumiInfo?.title ?: "", episodeName, coverUrl)
        ToastUtils.showSnackBar(
            "缓存任务创建成功！",
            Icons.Default.Check,
            Icons.AutoMirrored.Default.ArrowForwardIos
        ) {
            //startActivity(Intent(this@cacheBangumi, CacheListActivity::class.java))
        }
    }

    private suspend fun queryDownload(
        bvid: String,
        cid: Long,
        bangumiName: String,
        episodeName: String,
        coverUrl: String
    ) {
        val downloadCacheInfo = VideoCacheFileInfo(
            videoBvid = bvid,
            videoName = episodeName,
            videoPartName = bangumiName,    //大标题为ep标题，小标题为番剧名
            videoCover = coverUrl,
            videoUploaderName = "番剧",
            videoCid = cid,
        )
        try {
            repository.insertNewTasks(downloadCacheInfo)
            val videoDownloadRequest = OneTimeWorkRequestBuilder<VideoDownloadWorker>()
                .setInputData(
                    workDataOf(
                        "videoCid" to cid,
                        "videoBvid" to bvid,
                        "videoCover" to coverUrl,
                        "isBangumi" to true
                    )
                )
                /*.setConstraints(
                    Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
                )*/
                .build()
            WorkManager.getInstance(application).enqueue(videoDownloadRequest)
        } catch (e: Exception) {
            uiState = UIState.Success
            ToastUtils.showText("下载失败了！${e.message}")
        }
    }
}