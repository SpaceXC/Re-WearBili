package cn.spacexc.wearbili.remake.app.main.profile.detail.watchlater.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.spacexc.bilibilisdk.sdk.user.watchlater.WatchLaterInfo
import cn.spacexc.bilibilisdk.sdk.user.watchlater.remote.WatchLaterItem
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

    fun getWatchLaterItems() {
        viewModelScope.launch {
            val response = WatchLaterInfo.getAllWatchLater()
            if (response.code != 0) {
                uiState = UIState.Failed
                return@launch
            }
            watchLaterList = response.data?.data?.list ?: emptyList()
            uiState = UIState.Success
        }
    }
}