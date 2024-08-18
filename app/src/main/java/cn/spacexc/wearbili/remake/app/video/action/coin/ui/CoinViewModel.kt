package cn.spacexc.wearbili.remake.app.video.action.coin.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import cn.spacexc.bilibilisdk.sdk.video.action.VideoAction
import cn.spacexc.wearbili.remake.common.ToastUtils
import cn.spacexc.wearbili.remake.common.UIState
import kotlinx.coroutines.launch

class CoinViewModel : ViewModel() {
    var uiState: UIState by mutableStateOf(UIState.Success)

    fun coin(count: Int, videoIdType: String, videoId: String, navController: NavController) {
        viewModelScope.launch {
            uiState = UIState.Loading
            val response = VideoAction.coinVideo(videoIdType, videoId, count)
            if (response.code != 0) {
                ToastUtils.showText(response.message ?: "投币失败了！")
                uiState = UIState.Success
            } else {
                /*setResult(0, Intent().apply {
                    putExtra("isCoined", true)
                    putExtra("coinCount", count)
                })
                finish()*/
            }
        }
    }
}