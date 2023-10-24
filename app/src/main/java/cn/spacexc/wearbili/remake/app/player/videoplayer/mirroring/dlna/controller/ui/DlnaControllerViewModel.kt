package cn.spacexc.wearbili.remake.app.player.videoplayer.mirroring.dlna.controller.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.spacexc.bilibilisdk.sdk.video.info.VideoInfo
import cn.spacexc.wearbili.remake.app.player.videoplayer.mirroring.dlna.CastObject
import cn.spacexc.wearbili.remake.app.player.videoplayer.mirroring.dlna.WearbiliCastManager
import com.android.cast.dlna.dmc.DLNACastManager
import com.android.cast.dlna.dmc.control.ICastInterface
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * Created by XC-Qan on 2023/10/22.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class DlnaControllerViewModel : ViewModel() {
    var currentPosition by mutableLongStateOf(0L)
    var videoDuration by mutableLongStateOf(1L)
    var isPlaying by mutableStateOf(false)
    fun castVideo(cid: Long, videoName: String, isBangumi: Boolean) {
        viewModelScope.launch {
            val response = VideoInfo.getVideoPlayUrlForTv(cid, isBangumi)
            if (response.code != 0) {
                return@launch
            }
            response.data?.data?.durl?.firstOrNull()?.url?.let { url ->
                DLNACastManager.getInstance().cast(
                    WearbiliCastManager.currentDevice(),
                    CastObject.newInstance(
                        url,
                        UUID.randomUUID().toString(),
                        videoName
                    )
                )
                DLNACastManager.getInstance().play()
                DLNACastManager.getInstance().registerActionCallbacks(
                    object : ICastInterface.PlayEventListener {
                        override fun onSuccess(result: Void?) {
                            isPlaying = true
                        }

                        override fun onFailed(errMsg: String) {
                        }
                    },
                    object : ICastInterface.PauseEventListener {
                        override fun onSuccess(result: Void?) {
                            isPlaying = false
                        }

                        override fun onFailed(errMsg: String) {

                        }
                    }
                )
                updatePosition()
            }
        }
    }

    private fun updatePosition() {
        viewModelScope.launch {
            while (true) {
                WearbiliCastManager.currentDevice()?.let {
                    DLNACastManager.getInstance().getPositionInfo(it) { positionInfo, _ ->
                        positionInfo?.trackElapsedSeconds
                    }
                }
                delay(800)
            }
        }
    }
}