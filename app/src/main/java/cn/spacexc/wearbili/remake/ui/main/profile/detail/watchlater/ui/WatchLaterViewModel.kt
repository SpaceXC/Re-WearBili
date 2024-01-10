package cn.spacexc.wearbili.remake.ui.main.profile.detail.watchlater.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.spacexc.bilibilisdk.sdk.user.watchlater.info.WatchLaterInfo
import cn.spacexc.bilibilisdk.sdk.user.watchlater.info.remote.WatchLaterItem
import cn.spacexc.bilibilisdk.sdk.video.action.VideoAction
import cn.spacexc.wearbili.common.domain.log.logd
import cn.spacexc.wearbili.remake.common.ToastUtils
import cn.spacexc.wearbili.remake.common.UIState
import kotlinx.coroutines.launch

/**
 * Created by XC-Qan on 2023/6/24.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class WatchLaterViewModel : ViewModel() {
    var uiState by mutableStateOf(UIState.Loading)
    var watchLaterList by mutableStateOf(listOf<WatchLaterItem>())
    var isRefreshing by mutableStateOf(false)

    init {
        getWatchLaterItems()
    }

    fun getWatchLaterItems() {
        viewModelScope.launch {
            val response = WatchLaterInfo.getAllWatchLater()
            if (response.code != 0) {
                uiState = UIState.Failed
                return@launch
            }
            watchLaterList = response.data?.data?.list ?: emptyList()
            uiState = UIState.Success
            isRefreshing = false
        }
    }

    fun removeFromWatchLater(
        aid: Long
    ) {
        viewModelScope.launch {
            logd("删除稍后再看")
            val response = VideoAction.removeFromWatchLater(videoId = aid.toString())
            if (response.code != 0) {
                ToastUtils.showText("移除失败！${response.code}: ${response.message}")
            } else {
                ToastUtils.showText("移除成功！")
                isRefreshing = true
                getWatchLaterItems()
            }
        }
    }
}