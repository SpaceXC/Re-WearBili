package cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer

import android.annotation.SuppressLint
import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem.fromUri
import androidx.media3.common.Player
import androidx.media3.common.Player.Listener
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.MergingMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import bilibili.community.service.dm.v1.CommandDm
import bilibili.community.service.dm.v1.DmSegMobileReply
import cn.spacexc.bilibilisdk.sdk.bangumi.info.BANGUMI_ID_TYPE_CID
import cn.spacexc.bilibilisdk.sdk.bangumi.info.BangumiInfo
import cn.spacexc.bilibilisdk.sdk.video.action.VideoAction
import cn.spacexc.bilibilisdk.sdk.video.info.VideoInfo
import cn.spacexc.bilibilisdk.sdk.video.info.remote.subtitle.Subtitle
import cn.spacexc.bilibilisdk.sdk.video.info.remote.subtitle.SubtitleFile
import cn.spacexc.bilibilisdk.utils.UserUtils
import cn.spacexc.wearbili.common.domain.log.TAG
import cn.spacexc.wearbili.common.domain.log.logd
import cn.spacexc.wearbili.common.domain.network.KtorNetworkUtils
import cn.spacexc.wearbili.remake.app.cache.domain.database.VideoCacheFileInfo
import cn.spacexc.wearbili.remake.app.cache.domain.database.VideoCacheRepository
import cn.spacexc.wearbili.remake.app.player.videoplayer.danmaku.DanmakuGetter
import cn.spacexc.wearbili.remake.app.player.videoplayer.danmaku.compose.data.DanmakuSegment
import cn.spacexc.wearbili.remake.common.ToastUtils
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import java.io.File
import javax.inject.Inject


/*@UnstableApi*/
/**
 * Created by XC-Qan on 2023/6/28.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@HiltViewModel
@SuppressLint("UnsafeOptInUsageError")
class Media3VideoPlayerViewModel @Inject constructor(
    private val application: Application,
    private val repository: VideoCacheRepository,
    private val networkUtils: KtorNetworkUtils,
    private val danmakuGetter: DanmakuGetter
) : ViewModel() {
    private var httpDataSourceFactory = DefaultHttpDataSource.Factory()
    private var httpMediaSourceFactory: MediaSource.Factory
    lateinit var httpPlayer: Player
    lateinit var cachePlayer: Player

    var currentStat by mutableStateOf(PlayerStats.Loading)
    var loadingMessage by mutableStateOf("")
    var isVideoControllerVisible by mutableStateOf(false)

    var isReady by mutableStateOf(false)

    var videoInfo: cn.spacexc.bilibilisdk.sdk.video.info.remote.info.web.WebVideoInfo? by mutableStateOf(
        null
    )
    var cacheVideoInfo: VideoCacheFileInfo? by mutableStateOf(
        null
    )
    var currentVideoCid: Long = 0L

    var videoPlayerAspectRatio by mutableFloatStateOf(16f / 9f)

    val currentPlayProgress = flow {
        while (true) {
            if (cacheVideoInfo != null) {
                emit(cachePlayer.currentPosition)
            } else {
                emit(httpPlayer.currentPosition)
            }
            delay(10)
        }
    }
    var videoDuration by mutableLongStateOf(0L)

    var subtitleList = mutableStateMapOf<String, SubtitleConfig>()

    var currentSubtitleLanguage: String? by mutableStateOf(subtitleList.keys.lastOrNull())
    var currentSubtitleText = flow {
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

    //var danmakuInputStream = MutableStateFlow<InputStream?>(null)
    var danmakuList by mutableStateOf(listOf<DanmakuSegment>())
    var imageDanmakus by mutableStateOf(mapOf<List<String>, ImageBitmap>())
    private val imageDanmakusLock = Mutex()
    var commandDanmakus by mutableStateOf(listOf<CommandDm>())
    private val danmakuListLock = Mutex()

    var onlineCount by mutableStateOf("-")

    /**
     * A: 权重（时长）
     * B: 显示内容
     * C: 开始时间点
     */
    var videoChapters by mutableStateOf(listOf<Triple<Int, String, Int>>())

    var videoCastUrl = ""

    init {
        val headers = HashMap<String, String>()
        headers["User-Agent"] = "Mozilla/5.0 BiliDroid/*.*.* (bbcallen@gmail.com)"
        headers["Referer"] = "https://bilibili.com/"
        httpDataSourceFactory = DefaultHttpDataSource.Factory()
        httpDataSourceFactory.setUserAgent("Mozilla/5.0 BiliDroid/*.*.* (bbcallen@gmail.com)")
        httpDataSourceFactory.setDefaultRequestProperties(headers)
            .setAllowCrossProtocolRedirects(true)

        httpMediaSourceFactory =
            DefaultMediaSourceFactory(application)
                .setDataSourceFactory(httpDataSourceFactory)
        httpPlayer = ExoPlayer.Builder(application)
            .setRenderersFactory(
                DefaultRenderersFactory(application).setExtensionRendererMode(
                    DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON
                )
            )
            .setMediaSourceFactory(httpMediaSourceFactory)
            .build()

        cachePlayer = ExoPlayer.Builder(application)
            .setRenderersFactory(
                DefaultRenderersFactory(application).setExtensionRendererMode(
                    DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON
                )
            )
            .setMediaSourceFactory(DefaultMediaSourceFactory(application))
            .build()

        httpPlayer.addListener(object : Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                when (playbackState) {
                    Player.STATE_READY -> {
                        videoPlayerAspectRatio =
                            httpPlayer.videoSize.width.toFloat() / httpPlayer.videoSize.height.toFloat()
                        videoDuration = httpPlayer.duration
                        currentStat = PlayerStats.Playing
                        if (!isReady) {
                            viewModelScope.launch {
                                loadInitDanmaku(cid = currentVideoCid)
                                appendDanmaku(cid = currentVideoCid, videoDuration)
                            }
                        }
                        isReady = true
                        //isVideoControllerVisible = true
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
        cachePlayer.addListener(object : Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                when (playbackState) {
                    Player.STATE_READY -> {
                        videoPlayerAspectRatio =
                            cachePlayer.videoSize.width.toFloat() / cachePlayer.videoSize.height.toFloat()

                        videoDuration = cachePlayer.duration
                        currentStat = PlayerStats.Playing
                        isReady = true
                        //isVideoControllerVisible = true
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
        currentVideoCid = videoCid
        if (isBangumi) {
            viewModelScope.launch {
                getVideoInfo(videoIdType, videoId)
                //loadInitDanmaku(cid = videoCid)
                appendLoadMessage("加载视频url...")
            }
            viewModelScope.launch {
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

                httpPlayer.setMediaItem(mediaSource.mediaItem)
                httpPlayer.playWhenReady = true
                httpPlayer.prepare()
            }
        } else {
            appendLoadMessage("加载视频url...")
            viewModelScope.launch {
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

                    httpPlayer.setMediaItem(mediaSource.mediaItem)
                    httpPlayer.playWhenReady = true
                    httpPlayer.prepare()
                    //isVideoControllerVisible = true
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

                    httpPlayer.setMediaItem(mergeSource.mediaItem)
                    httpPlayer.playWhenReady = true
                    httpPlayer.prepare()
                    //isVideoControllerVisible = true
                }
            }
            viewModelScope.launch {
                getVideoInfo(videoIdType, videoId)
                loadSubtitleAndVideoChapters()
                startContinuouslyUpdatingSubtitle()
            }
        }
        startContinuouslyUploadingPlayingProgress()

    }

    fun playVideoFromLocalFile(
        videoCid: Long
    ) {
        currentVideoCid = videoCid
        viewModelScope.launch {
            cacheVideoInfo = repository.getTaskInfoByVideoCid(videoCid)
            if (cacheVideoInfo == null) {
                appendLoadMessage("好像没有这个缓存耶...")
                return@launch
            }

            val downloadPath = File(application.filesDir, "videoCaches/$videoCid")

            //region load subtitle
            val subtitleFiles = cacheVideoInfo!!.downloadedSubtitleFileNames.map {
                it.key to Gson().fromJson(
                    String(
                        File(
                            downloadPath,
                            it.value
                        ).readBytes()
                    ), SubtitleFile::class.java
                )
            }.map {
                it.first to SubtitleConfig(
                    subtitleList = it.second.body,
                    subtitleLanguageCode = it.first,
                    subtitleLanguage = it.first
                )
            }
            subtitleList = subtitleFiles.toMutableStateMap()
            //endregion

            //region load danmaku
            val segments = (cacheVideoInfo!!.videoDurationMillis / (6 * 60 * 1000)).toInt() + 1
            Log.d(TAG, "playVideoFromLocalFile: 将加载${segments}段弹幕！")
            danmakuList = buildList {
                (1..segments).forEach { index ->
                    val danmakuFile = File(downloadPath, "$videoCid.danmaku.seg$index")
                    try {
                        val danmakuReply = DmSegMobileReply.parseFrom(danmakuFile.readBytes())
                        danmakuReply.elemsList/*?.filter {danmakus -> danmakus.weight > 4 }*/?.let { danmakuList ->
                            add(DanmakuSegment(index, danmakuList))
                        }
                        val advanceDanmaku = danmakuReply.elemsList.filter { it.mode == 7 }
                        println(advanceDanmaku)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            Log.d(TAG, "playVideoFromLocalFile: 加载了${danmakuList.size}段弹幕！")
            //endregion

            //region load video
            val videoFile = File(downloadPath, "$videoCid.video.mp4")
            val mediaSource = ProgressiveMediaSource.Factory(DefaultDataSource.Factory(application))
                .createMediaSource(fromUri(Uri.fromFile(videoFile)))

            cachePlayer.setMediaItem(mediaSource.mediaItem)
            cachePlayer.playWhenReady = true
            cachePlayer.prepare()
            //isVideoControllerVisible = true
            //endregion
            startContinuouslyUpdatingSubtitle()
        }
    }

    private suspend fun loadSubtitleAndVideoChapters() {
        val response = VideoInfo.getVideoPlayerInfo(
            cn.spacexc.wearbili.remake.app.video.info.ui.VIDEO_TYPE_AID,
            videoInfo?.data?.aid?.toString() ?: "",
            videoInfo?.data?.cid ?: 0
        ).data?.data
        val urls = response?.subtitle?.subtitles
        urls.logd("subtitles0")
        urls?.let {
            initSubtitle(urls)
            subtitleList.logd("subtitles")
        }
        val chapters = response?.view_points?.map {
            Triple(it.to - it.from, it.content, it.from)
        } ?: emptyList()
        videoChapters = chapters
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
        viewModelScope.launch {
            getOnlineCount()
        }
    }

    private suspend fun updateSubtitle() {
        val tasks = subtitleList.entries.map { entry ->
            viewModelScope.async {
                val currentPosition = currentPlayProgress.first()
                val config = entry.value
                if (config.currentSubtitle != null) {
                    if (currentPosition !in (config.currentSubtitle.from * 1000).toLong()..(config.currentSubtitle.to * 1000).toLong()) {
                        if (config.currentSubtitleIndex + 1 < config.subtitleList.size) {
                            val nextSubtitle = config.subtitleList[config.currentSubtitleIndex + 1]
                            if (currentPosition in (nextSubtitle.from * 1000).toLong()..(nextSubtitle.to * 1000).toLong()) {
                                subtitleList[entry.key] = subtitleList[entry.key]!!.copy(
                                    currentSubtitle = nextSubtitle,
                                    currentSubtitleIndex = config.currentSubtitleIndex + 1
                                )
                            } else {
                                val currentSubtitle =
                                    config.subtitleList.indexOfFirst { currentPosition in (it.from * 1000).toLong()..(it.to * 1000).toLong() }
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
                        config.subtitleList.indexOfFirst { currentPosition in (it.from * 1000).toLong()..(it.to * 1000).toLong() }
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

    private suspend fun loadInitDanmaku(cid: Long) {
        appendLoadMessage("加载弹幕内容...")
        try {
            listOf(
                viewModelScope.async {
                    val danmaku = danmakuGetter.getDanmaku(cid, 1)
                    danmakuList = listOf(
                        DanmakuSegment(
                            1,
                            danmakuList = danmaku.elemsList/*.filter { it.weight > 4 }*/
                        )
                    )
                    val advanceDanmaku = danmaku.elemsList.filter { it.mode == 7 }
                    Log.d(TAG, "loadInitDanmaku: $advanceDanmaku")
                },
                viewModelScope.async {
                    val specialDanmakus = danmakuGetter.getSpecialDanmakus(cid)
                    commandDanmakus = specialDanmakus.commandDmsList
                    val imageDanmakusTask = specialDanmakus.expressionsList.map { expressions ->
                        viewModelScope.async {
                            expressions.dataList.map { expression ->
                                val tempMap = HashMap<List<String>, ImageBitmap>()
                                viewModelScope.async {
                                    Log.d(TAG, "loadInitDanmaku-image: $expression")
                                    val imageBitmap = networkUtils.getImageBitmap(
                                        expression.url.replace(
                                            "http://",
                                            "https://"
                                        )
                                    )
                                    if (imageBitmap != null) {
                                        imageDanmakusLock.lock()
                                        try {
                                            tempMap[expression.keywordList.map { "[$it]" }] =
                                                imageBitmap
                                            imageDanmakus = tempMap
                                        } finally {
                                            imageDanmakusLock.unlock()
                                        }
                                    }
                                }
                            }.awaitAll()    //单个expression的任务（还是没搞懂什么意思，不过api就是这么返回的，我也没办法（
                        }
                    }
                    imageDanmakusTask.awaitAll()    //几个expressions的任务（说真的我没搞懂）
                }
            ).awaitAll()    //普通弹幕和特殊弹幕两个任务
        } catch (e: Exception) {
            e.printStackTrace()
            appendLoadMessage("加载弹幕内容失败! $e")
        }
    }

    private suspend fun appendDanmaku(
        cid: Long,
        videoDuration: Long
    ) {
        val segments = (videoDuration / (6 * 60 * 1000)).toInt() + 1
        Log.d(TAG, "appendDanmaku: 加载弹幕！共${segments}段")
        val tasks = (2..segments).map { index ->
            viewModelScope.async {
                try {
                    val danmaku = danmakuGetter.getDanmaku(cid, index)
                    danmakuListLock.lock()
                    try {
                        val temp = danmakuList.toMutableList()
                        temp.add(
                            DanmakuSegment(
                                index,
                                danmakuList = danmaku.elemsList.filter { it.weight > 4 })
                        )
                        danmakuList = temp
                    } finally {
                        danmakuListLock.unlock()
                    }
                    Log.d(TAG, "appendDanmaku: 第${index}段弹幕加载成功")
                } catch (e: Exception) {
                    e.printStackTrace()
                    ToastUtils.showText("第${index}段弹幕加载失败！")
                }
            }
        }
        tasks.awaitAll()
        Log.d(
            TAG,
            "appendDanmaku: 弹幕加载完成！共${danmakuList.flatMap { it.danmakuList }.size}条弹幕！"
        )
    }

    private fun startContinuouslyUpdatingSubtitle() {
        Log.d(TAG, "startContinuouslyUpdatingSubtitle")
        currentSubtitleLanguage = subtitleList.keys.firstOrNull()
        viewModelScope.launch {
            while (currentPlayProgress.first() >= 0) {
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
                            progress = httpPlayer.currentPosition.div(1000).toInt()
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