package cn.spacexc.wearbili.remake.app.player.livestream.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.spacexc.bilibilisdk.sdk.stream.info.StreamInfo
import kotlinx.coroutines.launch
import tv.danmaku.ijk.media.player.IjkMediaPlayer

class LiveStreamViewModel : ViewModel() {
    var httpPlayer: IjkMediaPlayer = IjkMediaPlayer().apply {
        setLogEnabled(true)
        IjkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_VERBOSE)
        setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48L)  //关闭环路过滤，减轻解码压力
        setOption(
            IjkMediaPlayer.OPT_CATEGORY_FORMAT,
            "user_agent",
            "Mozilla/5.0 BiliDroid/*.*.* (bbcallen@gmail.com)"
        )
        //if (SettingsManager.getConfiguration().videoDecoder == VideoDecoder.Hardware) {
            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1)
            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1)
            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1)


        //if (SettingsManager.getConfiguration().isVideoLowPerformance) {
            setOption(
                IjkMediaPlayer.OPT_CATEGORY_PLAYER,
                "framedrop",
                5
            )  //跳帧处理,放CPU处理较慢时，进行跳帧处理，保证播放流程，画面和声音同步
            //setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-fps", 24)  //最大fps
        //}

        setOnPreparedListener {
            aspectRatio = it.videoWidth.toFloat() / it.videoHeight.toFloat()
            it.start()
        }
    }
    var aspectRatio by mutableFloatStateOf(16f / 9f)
    var isControllerVisible by mutableStateOf(false)

    fun playLiveStreamFromRoomId(roomId: Long) {
        viewModelScope.launch {
            val response = StreamInfo.getLiveStreamPlayUrlFromRoomId(roomId)
            val url = response.data?.data?.durl?.first()?.url ?: ""
            httpPlayer.dataSource = url
            httpPlayer.prepareAsync()
        }
    }
}