package cn.spacexc.wearbili.remake.app.video.info.info.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.spacexc.bilibilisdk.sdk.video.action.VideoActions
import cn.spacexc.wearbili.common.domain.log.logd
import cn.spacexc.wearbili.common.domain.network.KtorNetworkUtils
import cn.spacexc.wearbili.remake.app.video.info.info.remote.VideoInformation
import cn.spacexc.wearbili.remake.common.ToastUtils
import cn.spacexc.wearbili.remake.common.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by XC-Qan on 2023/4/12.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@HiltViewModel
class VideoInformationViewModel @Inject constructor(
    private val networkUtils: KtorNetworkUtils
) : ViewModel() {
    var state by mutableStateOf(
        VideoInformationScreenState()
    )

    fun getVideoInfo(
        videoIdType: String,
        videoId: String?
    ) {
        viewModelScope.launch {
            if (videoId.isNullOrEmpty()) {
                state = state.copy(uiState = UIState.Failed)
                return@launch
            }
            val response =
                networkUtils.get<VideoInformation>("http://api.bilibili.com/x/web-interface/view?$videoIdType=$videoId")
            if (response.code != 0 || response.data?.data == null || response.data?.code != 0) {
                state = state.copy(uiState = UIState.Failed)
                return@launch
            }
            state = state.copy(uiState = UIState.Success, videoData = response.data?.data)
        }
    }

    fun getVideoSanLianState(
        videoIdType: String,
        videoId: String?
    ) {    //三连有英文吗？
        viewModelScope.launch {
            isLiked(videoIdType, videoId)
        }
        viewModelScope.launch {
            isCoined(videoIdType, videoId)
        }
        viewModelScope.launch {
            isFav(videoId)
        }
        //开协程就可以一起判断三种状态
    }

    private suspend fun isLiked(
        videoIdType: String,
        videoId: String?
    ) {
        state = state.copy(
            isLiked = cn.spacexc.bilibilisdk.sdk.video.info.VideoInformation.isLiked(
                videoIdType,
                videoId
            )
        )
    }

    private suspend fun isCoined(
        videoIdType: String,
        videoId: String?
    ) {
        state = state.copy(
            isCoined = cn.spacexc.bilibilisdk.sdk.video.info.VideoInformation.isCoined(
                videoIdType,
                videoId
            )
        )
    }

    private suspend fun isFav(
        videoId: String?
    ) {
        state = state.copy(
            isFav = cn.spacexc.bilibilisdk.sdk.video.info.VideoInformation.isFav(videoId)
        )
    }

    fun likeVideo(
        videoIdType: String,
        videoId: String?
    ) {
        state = state.copy(
            isLiked = !state.isLiked
        )
        viewModelScope.launch {
            val response = VideoActions.likeVideo(videoIdType, videoId, !state.isLiked)
            if (response.code != 0) {
                state = state.copy(
                    isLiked = !state.isLiked
                )
                ToastUtils.showText("${response.code}: ${response.message}")
            }
            isLiked(videoIdType, videoId)
            logd(response)
        }
    }
}