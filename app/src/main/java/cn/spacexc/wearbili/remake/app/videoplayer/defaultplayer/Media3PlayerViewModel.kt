package cn.spacexc.wearbili.remake.app.videoplayer.defaultplayer

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem.fromUri
import androidx.media3.common.Player
import androidx.media3.common.Player.Listener
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.MergingMediaSource
import cn.spacexc.bilibilisdk.sdk.bangumi.info.BANGUMI_ID_TYPE_CID
import cn.spacexc.bilibilisdk.sdk.bangumi.info.BangumiInfo
import cn.spacexc.bilibilisdk.sdk.video.action.VideoAction
import cn.spacexc.bilibilisdk.sdk.video.info.VideoInfo
import cn.spacexc.bilibilisdk.sdk.video.info.remote.subtitle.Subtitle
import cn.spacexc.bilibilisdk.utils.UserUtils
import cn.spacexc.wearbili.common.domain.log.TAG
import cn.spacexc.wearbili.common.domain.log.logd
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.InputStream

/*@UnstableApi*/
/**
 * Created by XC-Qan on 2023/6/28.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class Media3PlayerViewModel(application: Application) : AndroidViewModel(application) {
    private var httpDataSourceFactory = DefaultHttpDataSource.Factory()
    private var cacheDataSourceFactory: DataSource.Factory
    private var httpMediaSourceFactory: MediaSource.Factory
    lateinit var player: Player

    var currentStat by mutableStateOf(PlayerStats.Loading)
    var loadingMessage by mutableStateOf("")
    var isVideoControllerVisible by mutableStateOf(false)

    var videoInfo: cn.spacexc.bilibilisdk.sdk.video.info.remote.info.web.WebVideoInfo? by mutableStateOf(
        null
    )

    var videoPlayerAspectRatio by mutableStateOf(16f / 9f)

    val currentPlayProgress = flow {
        while (true) {
            emit(player.currentPosition)
            delay(10)
        }
    }
    var videoDuration by mutableStateOf(0L)

    var subtitleList = mutableStateMapOf<String, SubtitleConfig>()

    var currentSubtitleLanguage: String? by mutableStateOf(subtitleList.keys.lastOrNull())
    var currentSubtitleText = flow {
        //emit("字幕测试")
        var index = 0
        while (true) {
            //emit("index: $index")
            index++
            val nextSubtitle =
                if (currentSubtitleLanguage != null) {
                    //println("currentLanguage: $currentSubtitleLanguage")
                    //println("currentSubtitleContent: ${subtitleList[subtitleList.keys.lastOrNull()]?.currentSubtitle?.content}")
                    subtitleList[currentSubtitleLanguage]?.currentSubtitle?.content
                } else null
            emit(nextSubtitle)
            delay(5)
        }
    }

    var danmakuInputStream = MutableStateFlow<InputStream?>(null)

    var onlineCount by mutableStateOf("-")

    var videoCastUrl = ""

    init {
        val headers = HashMap<String, String>()
        headers["User-Agent"] = "Mozilla/5.0 BiliDroid/*.*.* (bbcallen@gmail.com)"
        headers["Referer"] = "https://bilibili.com/"
        httpDataSourceFactory = DefaultHttpDataSource.Factory()
        httpDataSourceFactory.setUserAgent("Mozilla/5.0 BiliDroid/*.*.* (bbcallen@gmail.com)")
        httpDataSourceFactory.setDefaultRequestProperties(headers)
            .setAllowCrossProtocolRedirects(true)

        cacheDataSourceFactory = CacheDataSource.Factory()
            .setCache(ExoPlayerUtils.getInstance(getApplication()).getCache())
            .setUpstreamDataSourceFactory(httpDataSourceFactory)
            .setCacheWriteDataSinkFactory(null) // Disable writing.

        httpMediaSourceFactory =
            DefaultMediaSourceFactory(application).setDataSourceFactory(httpDataSourceFactory)
        player = ExoPlayer.Builder(application)
            .setRenderersFactory(
                DefaultRenderersFactory(application).setExtensionRendererMode(
                    DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON
                )
            )
            .setMediaSourceFactory(httpMediaSourceFactory)
            .build()

        player.addListener(object : Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                when (playbackState) {
                    Player.STATE_READY -> {
                        videoPlayerAspectRatio = player.videoSize.width.toFloat()
                            .logd("width")!! / player.videoSize.height.toFloat().logd("height")!!
                        videoDuration = player.duration
                        Log.d(TAG, "onPlaybackStateChanged: startUpdatingSubtitles")
                        currentStat = PlayerStats.Playing
                        isVideoControllerVisible = true
                    }

                    Player.STATE_BUFFERING -> {
                        appendLoadMessage("缓冲中...")
                        currentStat = PlayerStats.Buffering
                    }

                    Player.STATE_ENDED -> {
                        currentStat = PlayerStats.Finished
                    }

                    Player.STATE_IDLE -> {

                    }
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                currentStat = if (isPlaying) PlayerStats.Playing else PlayerStats.Paused
            }
        })
    }

    fun playVideoFromId(
        videoIdType: String,
        videoId: String,
        videoCid: Long,
        isBangumi: Boolean = false,
        isLowResolution: Boolean = true
    ) {
        appendLoadMessage("初始化播放器...")
        viewModelScope.launch {
            if (isBangumi) {
                getVideoInfo(videoIdType, videoId)
                loadDanmaku(cid = videoCid)
                appendLoadMessage("加载视频url...")
                val urlResponse = BangumiInfo.getBangumiPlaybackUrl(BANGUMI_ID_TYPE_CID, videoCid)
                val urlData = urlResponse.data?.result
                if (urlData == null) {
                    appendLoadMessage(
                        "出错！${urlResponse.code}: ${urlResponse.message}",
                        needLineWrapping = false
                    )
                    return@launch
                }
                val videoUrl = urlData.durl.last { it.url.isNotEmpty() }.url
                videoCastUrl = videoUrl
                appendLoadMessage("成功!", needLineWrapping = false)

                val mediaSource: MediaSource = httpMediaSourceFactory
                    .createMediaSource(fromUri(videoUrl))

                player.setMediaItem(mediaSource.mediaItem)
                player
                player.playWhenReady = true
                player.prepare()
            } else {
                getVideoInfo(videoIdType, videoId)
                loadSubtitle()
                loadDanmaku(cid = videoCid)
                appendLoadMessage("加载视频url...")
                if (isLowResolution) {
                    val urlResponse =
                        VideoInfo.getLowResolutionVideoPlaybackUrl(videoIdType, videoId, videoCid)
                    val urlData = urlResponse.data?.data
                    if (urlData == null) {
                        appendLoadMessage(
                            "出错！${urlResponse.code}: ${urlResponse.message}",
                            needLineWrapping = false
                        )
                        return@launch
                    }
                    val videoUrl = urlData.durl.first { it.url.isNotEmpty() }.url
                    videoCastUrl = videoUrl
                    appendLoadMessage("成功!", needLineWrapping = false)

                    val mediaSource: MediaSource = httpMediaSourceFactory
                        .createMediaSource(fromUri(videoUrl))

                    player.setMediaItem(mediaSource.mediaItem)
                    player.playWhenReady = true
                    player.prepare()
                    startContinuouslyUpdatingSubtitle()
                } else {
                    val urlResponse =
                        VideoInfo.getVideoPlaybackUrls(videoIdType, videoId, videoCid)
                    val urlData = urlResponse.data?.data
                    if (urlData == null) {
                        //TODO 处理错误
                        appendLoadMessage(
                            "出错！${urlResponse.code}: ${urlResponse.message}",
                            needLineWrapping = false
                        )
                        return@launch
                    }
                    val videoUrl =
                        urlData.dash.video.last { it.baseUrl.isNotEmpty() }.baseUrl.logd("videoUrl")!!
                    val audioUrl =
                        urlData.dash.audio.last { it.baseUrl.isNotEmpty() }.baseUrl.logd("audioUrl")!!

                    val videoSource: MediaSource = httpMediaSourceFactory
                        .createMediaSource(fromUri(videoUrl))
                    val audioSource: MediaSource = httpMediaSourceFactory
                        .createMediaSource(fromUri(audioUrl))

                    val mergeSource: MediaSource = MergingMediaSource(videoSource, audioSource)
                    appendLoadMessage("成功!", needLineWrapping = false)


                    player.setMediaItem(mergeSource.mediaItem)
                    player.playWhenReady = true
                    player.prepare()
                    startContinuouslyUpdatingSubtitle()
                }
            }
            startContinuouslyUploadingPlayingProgress()
        }
    }

    private suspend fun loadSubtitle() {
        val urls = VideoInfo.getVideoPlayerInfo(
            cn.spacexc.wearbili.remake.app.video.info.ui.VIDEO_TYPE_AID,
            videoInfo?.data?.aid?.toString() ?: "",
            videoInfo?.data?.cid ?: 0
        ).data?.data?.subtitle?.subtitles
        urls.logd("subtitles0")
        urls?.let {
            initSubtitle(urls)
            subtitleList.logd("subtitles")
        }
    }

    private suspend fun initSubtitle(urls: List<cn.spacexc.bilibilisdk.sdk.video.info.remote.playerinfo.Subtitle>) {
        appendLoadMessage("加载字幕...")
        val tasks = urls.map { subtitle ->
            viewModelScope.async {
                appendLoadMessage("加载\"${subtitle.lan}\"字幕...")
                Log.d(TAG, "initSubtitle1: $subtitle")
                val response = VideoInfo.getSubtitle("https:${subtitle.subtitle_url}")
                response.data?.body?.let { subtitles ->
                    Log.d(TAG, "initSubtitle2: result for $subtitles")
                    val temp = subtitleList
                    temp[subtitle.lan] = SubtitleConfig(
                        subtitleList = subtitles,
                        subtitleLanguageCode = subtitle.lan,
                        subtitleLanguage = subtitle.lan_doc
                    )
                    subtitleList = temp
                    appendLoadMessage("加载\"${subtitle.lan}\"字幕成功!")
                }
            }
        }
        tasks.awaitAll()
        appendLoadMessage("字幕加载完成")
    }

    private suspend fun getVideoInfo(
        videoIdType: String,
        videoId: String
    ) {
        appendLoadMessage("获取视频信息...")
        val response =
            VideoInfo.getVideoInfoByIdWeb(videoIdType, videoId)//.logd("subtitleResponse")!!
        print("Obtained Video Info")
        if (response.code != 0 || response.data == null || response.data?.data == null) return
        videoInfo = response.data
        //delay(1000L)
        appendLoadMessage("获取成功", needLineWrapping = false)
        getOnlineCount()
    }

    private suspend fun updateSubtitle() {
        val tasks = subtitleList.entries.map { entry ->
            viewModelScope.async {
                val config = entry.value
                if (config.currentSubtitle != null) {
                    if (player.currentPosition !in (config.currentSubtitle.from * 1000).toLong()..(config.currentSubtitle.to * 1000).toLong()) {
                        if (config.currentSubtitleIndex + 1 < config.subtitleList.size) {
                            val nextSubtitle = config.subtitleList[config.currentSubtitleIndex + 1]
                            if (player.currentPosition in (nextSubtitle.from * 1000).toLong()..(nextSubtitle.to * 1000).toLong()) {
                                subtitleList[entry.key] = subtitleList[entry.key]!!.copy(
                                    currentSubtitle = nextSubtitle,
                                    currentSubtitleIndex = config.currentSubtitleIndex + 1
                                )
                            } else {
                                val currentSubtitle =
                                    config.subtitleList.indexOfFirst { player.currentPosition in (it.from * 1000).toLong()..(it.to * 1000).toLong() }
                                if (currentSubtitle != -1) {
                                    subtitleList[entry.key] = subtitleList[entry.key]!!.copy(
                                        currentSubtitle = config.subtitleList[currentSubtitle],
                                        currentSubtitleIndex = currentSubtitle
                                    )
                                } else {
                                    subtitleList[entry.key] =
                                        subtitleList[entry.key]!!.copy(currentSubtitle = null)
                                }
                            }
                        } else {
                            subtitleList[entry.key] =
                                subtitleList[entry.key]!!.copy(currentSubtitle = null)
                        }
                    }
                } else {
                    val currentSubtitle =
                        config.subtitleList.indexOfFirst { player.currentPosition in (it.from * 1000).toLong()..(it.to * 1000).toLong() }
                    if (currentSubtitle != -1) {
                        subtitleList[entry.key] = subtitleList[entry.key]!!.copy(
                            currentSubtitle = config.subtitleList[currentSubtitle],
                            currentSubtitleIndex = currentSubtitle
                        )
                    } else {
                        subtitleList[entry.key] =
                            subtitleList[entry.key]!!.copy(currentSubtitle = null)
                    }
                }
            }
        }
        tasks.awaitAll()
        //println("subtitleUpdated: ${subtitleList.map { "${it.key}: ${it.value.currentSubtitle}" }}")
    }

    private suspend fun loadDanmaku(cid: Long) {
        appendLoadMessage("加载弹幕内容...")
        danmakuInputStream.value = VideoInfo.getVideoDanmaku(cid)
        appendLoadMessage("成功", needLineWrapping = false)
    }

    private fun startContinuouslyUpdatingSubtitle() {
        Log.d(TAG, "startContinuouslyUpdatingSubtitle")
        currentSubtitleLanguage = subtitleList.keys.firstOrNull()
        viewModelScope.launch {
            while (player.currentPosition >= 0) {
                updateSubtitle()
                delay(5)
            }
        }
    }

    private fun startContinuouslyUploadingPlayingProgress() {
        viewModelScope.launch {
            if (UserUtils.isUserLoggedIn() && UserUtils.csrf() != null) {
                videoInfo?.let {
                    while (true) {
                        VideoAction.updateHistory(
                            aid = it.data.aid,
                            cid = it.data.cid,
                            progress = player.currentPosition.div(1000).toInt()
                        )
                        delay(2000)
                    }
                }
            }
        }
    }

    private suspend fun getOnlineCount() {
        videoInfo?.let {
            appendLoadMessage("获取在线观看人数...")
            val response = VideoInfo.getOnlineCount(VIDEO_TYPE_BVID, it.data.bvid, it.data.cid)
            onlineCount = response.data?.data?.total ?: "-"
            appendLoadMessage(
                if (onlineCount != "-") "成功!" else "失败!",
                needLineWrapping = false
            )
        }

    }

    fun appendLoadMessage(message: String, needLineWrapping: Boolean = true) {
        loadingMessage += if (needLineWrapping) "\n$message" else message
        Log.d(TAG, "appendLoadMessage: $message")
    }

}

data class SubtitleConfig(
    val currentSubtitleIndex: Int = 0,
    val currentSubtitle: Subtitle? = null,
    val subtitleList: List<Subtitle>,
    val subtitleLanguageCode: String,
    val subtitleLanguage: String
)

enum class PlayerStats {
    Loading,
    Playing,
    Buffering,
    Paused,
    Finished
}