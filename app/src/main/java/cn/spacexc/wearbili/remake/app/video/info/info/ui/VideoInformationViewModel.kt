package cn.spacexc.wearbili.remake.app.video.info.info.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.spacexc.wearbili.remake.app.video.info.info.remote.VideoInformation
import cn.spacexc.wearbili.remake.common.UIState
import cn.spacexc.wearbili.remake.common.domain.network.KtorNetworkUtils
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
            if (response.code != 0 || response.data?.data == null || response.data.code != 0) {
                state = state.copy(uiState = UIState.Failed)
                return@launch
            }
            state = state.copy(uiState = UIState.Success, videoData = response.data.data)
        }
    }
}