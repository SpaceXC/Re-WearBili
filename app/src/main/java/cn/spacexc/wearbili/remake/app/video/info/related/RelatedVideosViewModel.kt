package cn.spacexc.wearbili.remake.app.video.info.related

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.spacexc.bilibilisdk.sdk.video.info.VideoInfo
import cn.spacexc.bilibilisdk.sdk.video.info.remote.related.Data
import cn.spacexc.wearbili.remake.common.UIState
import kotlinx.coroutines.launch

/**
 * Created by XC-Qan on 2023/7/7.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class RelatedVideosViewModel : ViewModel() {
    var videos by mutableStateOf(emptyList<Data>())
    var uiState by mutableStateOf(UIState.Loading)

    fun getRelatedVideos(
        videoIdType: String,
        videoId: String
    ) {
        viewModelScope.launch {
            val response = VideoInfo.getRelatedVideosById(videoIdType, videoId)
            if (response.code != 0) {
                uiState = UIState.Failed
                return@launch
            }
            videos = response.data?.data ?: emptyList()
            uiState = UIState.Success
        }
    }
}