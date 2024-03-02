package cn.spacexc.wearbili.remake.app.video.action.coin.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import cn.spacexc.bilibilisdk.sdk.video.action.VideoAction
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID_TYPE
import cn.spacexc.wearbili.remake.app.video.info.ui.VIDEO_TYPE_BVID
import cn.spacexc.wearbili.remake.common.ToastUtils
import cn.spacexc.wearbili.remake.common.UIState
import kotlinx.coroutines.launch

class CoinActivity : ComponentActivity() {
    var uiState: UIState by mutableStateOf(UIState.Success)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val videoIdType = intent.getStringExtra(PARAM_VIDEO_ID_TYPE) ?: VIDEO_TYPE_BVID
        val videoId = intent.getStringExtra(PARAM_VIDEO_ID) ?: ""
        setContent {
            CoinScreen(uiState) { count ->
                lifecycleScope.launch {
                    uiState = UIState.Loading
                    val response = VideoAction.coinVideo(videoIdType, videoId, count)
                    if (response.code != 0) {
                        ToastUtils.showText(response.message ?: "投币失败了！")
                        uiState = UIState.Success
                    } else {
                        setResult(0, Intent().apply {
                            putExtra("isCoined", true)
                            putExtra("coinCount", count)
                        })
                        finish()
                    }
                }
            }
        }
    }
}