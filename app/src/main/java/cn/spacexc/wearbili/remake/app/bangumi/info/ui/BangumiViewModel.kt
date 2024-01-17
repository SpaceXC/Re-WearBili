package cn.spacexc.wearbili.remake.app.bangumi.info.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.spacexc.bilibilisdk.sdk.bangumi.info.BangumiInfo
import cn.spacexc.bilibilisdk.sdk.bangumi.info.remote.Episode
import cn.spacexc.bilibilisdk.sdk.bangumi.info.remote.Result
import cn.spacexc.bilibilisdk.sdk.video.info.VideoInfo
import cn.spacexc.bilibilisdk.sdk.video.info.remote.playerinfo.Subtitle
import cn.spacexc.wearbili.common.domain.network.KtorNetworkUtils
import cn.spacexc.wearbili.remake.app.Application
import cn.spacexc.wearbili.remake.app.cache.domain.database.VideoCacheRepository
import cn.spacexc.wearbili.remake.app.video.info.ui.VIDEO_TYPE_BVID
import cn.spacexc.wearbili.remake.common.ToastUtils
import cn.spacexc.wearbili.remake.common.UIState
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
    var uiState by mutableStateOf(UIState.Loading)
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
                uiState = UIState.Failed
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

    suspend fun cacheBangumi(
    )/*: List<Pair<Pair<String, Long>, String?>>*/ {
        uiState = UIState.Loading
        val videoBvid = getCurrentSelectedEpisode()?.bvid ?: ""
        val response =
            BangumiInfo.getBangumiPlaybackUrl(BANGUMI_ID_TYPE_EPID, currentSelectedEpid)
        if (response.code != 0) {
            ToastUtils.showText("缓存任务创建失败!")
            return
        }
        val url = response.data?.result?.durl?.firstOrNull()?.url
        if (url == null) {
            ToastUtils.showText("下载失败了！原因：下载链接获取失败")
            return
        }
        queryDownload(url, videoBvid, getSubtitles() ?: emptyList())
        //return tasks.awaitAll()
    }

    private suspend fun queryDownload(
        url: String,
        videoBvid: String,
        subtitles: List<Subtitle>
    ) {

    }

}