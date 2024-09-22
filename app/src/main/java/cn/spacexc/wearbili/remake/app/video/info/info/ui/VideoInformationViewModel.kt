package cn.spacexc.wearbili.remake.app.video.info.info.ui

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import cn.spacexc.bilibilisdk.sdk.video.action.VideoAction
import cn.spacexc.bilibilisdk.sdk.video.info.VideoInfo
import cn.spacexc.bilibilisdk.utils.UserUtils
import cn.spacexc.wearbili.common.domain.log.logd
import cn.spacexc.wearbili.remake.app.bangumi.info.ui.BANGUMI_ID_TYPE_EPID
import cn.spacexc.wearbili.remake.app.bangumi.info.ui.BangumiScreen
import cn.spacexc.wearbili.remake.common.ToastUtils
import cn.spacexc.wearbili.remake.common.UIState
import cn.spacexc.wearbili.remake.common.networking.KtorNetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
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
    val ktorNetworkUtils: KtorNetworkUtils,
    private val application: Application
) : ViewModel() {
    var state by mutableStateOf(
        VideoInformationScreenState()
    )

    var isLiked by mutableStateOf(false)
    var isFav by mutableStateOf(false)
    var isCoined by mutableStateOf(false)

    var hasCoinedCount by mutableIntStateOf(0)

    fun getVideoInfo(
        videoIdType: String,
        videoId: String?,
        navController: NavController,
        onPreload: (Long) -> Unit
    ) {
        state = state.copy(uiState = UIState.Loading)
        viewModelScope.launch {
            if (videoId.isNullOrEmpty()) {
                state = state.copy(uiState = UIState.Failed("视频找不到啦啊啊啊"))
                return@launch
            }
            val response =
                VideoInfo.getVideoDetailedInfoByIdWeb(videoIdType, videoId)
            if (response.code != 0 || response.data?.data == null || response.data?.code != 0) {
                state = state.copy(uiState = UIState.Failed(response.code))
                return@launch
            }
            response.data?.data?.view?.redirectUrl?.let { url ->
                val uri = Uri.parse(url)
                val epid = uri.path?.replace("/bangumi/play/", "")
                if (epid?.startsWith("ep") == true) {
                    val epidLong = epid.replace("ep", "").toLong()
                    /*activity.startActivity(Intent(activity, BangumiActivity::class.java).apply {
                        putExtra(PARAM_BANGUMI_ID_TYPE, BANGUMI_ID_TYPE_EPID)
                        putExtra(PARAM_BANGUMI_ID, epidLong)
                    })
                    activity.finish()*/
                    navController.navigateUp()
                    navController.navigate(BangumiScreen(BANGUMI_ID_TYPE_EPID, epidLong))
                }
            }
            getVideoSanLianState(videoIdType, videoId)
            state = state.copy(uiState = UIState.Success, videoData = response.data?.data)
            state.videoData?.view?.cid?.let { onPreload(it) }
        }
    }

    private suspend fun getVideoSanLianState(
        videoIdType: String,
        videoId: String?
    ) {    //三连有英文吗？
        val tasks = listOf(
            viewModelScope.async {
                isLiked(videoIdType, videoId)
            },
            viewModelScope.async {
                isCoined(videoIdType, videoId)
            },
            viewModelScope.async {
                isFav(videoId)
            }
        )
        tasks.awaitAll()
    }

    private suspend fun isLiked(
        videoIdType: String,
        videoId: String?
    ) {
        isLiked = VideoInfo.isLiked(
            videoIdType,
            videoId
        )
    }

    suspend fun isCoined(
        videoIdType: String,
        videoId: String?
    ) {
        val response = VideoInfo.isCoined(
            videoIdType,
            videoId
        )
        response.data?.data?.let {
            isCoined = it.multiply != 0
            hasCoinedCount = it.multiply
        }
    }

    /**
     * info screen fav checking required
     */
    private suspend fun isFav(
        videoId: String?
    ) {
        isFav = VideoInfo.isFav(videoId)

    }

    fun likeVideo(
        videoIdType: String,
        videoId: String?
    ) {

        isLiked = !isLiked
        viewModelScope.launch {
            val response = VideoAction.likeVideo(videoIdType, videoId, !isLiked)
            if (response.code != 0) {
                isLiked = !isLiked
                ToastUtils.showText("${response.code}: ${response.message}")
            }
            //isLiked(videoIdType, videoId)
            logd(response)
        }
    }

    fun sanlian(
        videoIdType: String,
        videoId: String,
        onFinished: (Boolean, Boolean, Boolean) -> Unit
    ) {
        val likeTemp = isLiked
        val coinTemp = isCoined
        val favTemp = isFav
        val coinCountTemp = hasCoinedCount
        viewModelScope.launch {
            if (UserUtils.isUserLoggedIn()) {
                isLiked = true
                isCoined = true
                isFav = true
                hasCoinedCount = 2
                onFinished(isLiked, isCoined, isFav)
                val response = VideoAction.sanlian(videoIdType, videoId)
                if (response.code != 0) {
                    ToastUtils.showText("${response.message}")
                    response.data?.data.apply {
                        if (this != null) {
                            isLiked = like
                            isCoined = coin
                            isFav = fav
                            hasCoinedCount = multiply
                        } else {
                            isLiked = likeTemp
                            isFav = favTemp
                            isCoined = coinTemp
                            hasCoinedCount = coinCountTemp
                        }
                        onFinished(isLiked, isCoined, isFav)
                    }
                }
            }
        }
    }

    fun addToWatchLater(
        videoIdType: String,
        videoId: String
    ) {
        viewModelScope.launch {
            val response = VideoAction.addToWatchLater(videoIdType, videoId)
            if (response.code != 0) {
                ToastUtils.showText("添加失败！${response.code}: ${response.message}")
            } else {
                ToastUtils.showText("添加成功！记得来看我哦～")
            }
        }
    }

    fun downloadWebMask(
        videoIdType: String,
        videoId: String,
        videoCid: Long
    ) {
        viewModelScope.launch {
            val videoPlayerInformation =
                VideoInfo.getVideoPlayerInfo(videoIdType, videoId, videoCid)
            videoPlayerInformation.data?.data?.dm_mask?.let { maskInfo ->
                val directory = File(application.cacheDir, "/danmakuMask")
                if (!directory.exists()) directory.mkdir()
                val maskFile = File(directory, "danmakuMask$videoCid.webmask")
                VideoInfo.downloadDanmakuWebMask(maskInfo, maskFile) {

                }
            }
        }
    }

    @Throws(FileNotFoundException::class, IOException::class)
    fun getBitmapFromBytes(bytes: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}