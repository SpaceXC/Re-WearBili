package cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer

import android.annotation.SuppressLint
import android.app.Application
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
import cn.spacexc.wearbili.remake.app.cache.domain.database.VideoCacheFileInfo
import cn.spacexc.wearbili.remake.app.cache.domain.database.VideoCacheRepository
import cn.spacexc.wearbili.remake.app.player.videoplayer.danmaku.DanmakuGetter
import cn.spacexc.wearbili.remake.app.player.videoplayer.danmaku.compose.data.DanmakuSegment
import cn.spacexc.wearbili.remake.app.settings.SettingsManager
import cn.spacexc.wearbili.remake.app.video.info.ui.VIDEO_TYPE_BVID
import cn.spacexc.wearbili.remake.common.ToastUtils
import cn.spacexc.wearbili.remake.common.networking.KtorNetworkUtils
import cn.spacexc.wearbili.remake.proto.settings.VideoDecoder
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer
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
class IjkVideoPlayerViewModel @Inject constructor(
    private val application: Application,
    val repository: VideoCacheRepository,
    private val networkUtils: KtorNetworkUtils,
    private val danmakuGetter: DanmakuGetter
) : ViewModel() {


    var currentVideoCid: Long = 0L

    var httpPlayer: IjkMediaPlayer = IjkMediaPlayer().apply {
        setLogEnabled(true)
        IjkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_VERBOSE)
        setOption(
            IjkMediaPlayer.OPT_CATEGORY_FORMAT,
            "user_agent",
            "Mozilla/5.0 BiliDroid/*.*.* (bbcallen@gmail.com)"
        ) //我服了这个user_agent要全小写下划线，谁家header这么写
        setOption(
            IjkMediaPlayer.OPT_CATEGORY_FORMAT,
            "Referer",
            "https://www.bilibili.com/bangumi/play"
        )
        setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48L)  //关闭环路过滤，减轻解码压力
        setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzemaxduration", 100L)  //最大播放探测时间
        setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzeduration", 1L)  //播放探测时间
        setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1)  //精确seek

        if (SettingsManager.getConfiguration().videoDecoder == VideoDecoder.Hardware) {
            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1)
            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1)
            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1)
        }

        if (SettingsManager.getConfiguration().isVideoLowPerformance) {
            setOption(
                IjkMediaPlayer.OPT_CATEGORY_PLAYER,
                "framedrop",
                5
            )  //跳帧处理,放CPU处理较慢时，进行跳帧处理，保证播放流程，画面和声音同步
            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-fps", 24)  //最大fps
        }
    }


    var cachePlayer: IjkMediaPlayer = IjkMediaPlayer().apply {
        setLogEnabled(true)
        IjkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_INFO)
    }    //先别删哦

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

    private var videoHistoryPlayProgress = 0L //历史播放进度

    var videoPlayerAspectRatio by mutableFloatStateOf(16f / 9f)

    val currentPlayProgress = flow {
        while (true) {
            if (cacheVideoInfo != null) {
                emit(cachePlayer.currentPosition)
            } else {
                emit(httpPlayer.currentPosition)
            }
            delay(500)
        }
    }
    var videoDuration by mutableLongStateOf(0L)

    var subtitleList = mutableStateMapOf<String, SubtitleConfig>()

    var currentSubtitleLanguage: String? by mutableStateOf(subtitleList.entries.firstOrNull { !it.value.isAIGenerated }?.key)
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

    var secondarySubtitleLanguage: String? by mutableStateOf(/*subtitleList.entries.lastOrNull() { !it.value.isAIGenerated }?.key*/
        null
    )
    var secondarySubtitleText = flow {
        var index = 0
        while (true) {
            //emit("index: $index")
            index++
            val nextSubtitle =
                if (secondarySubtitleLanguage != null) {
                    //println("currentLanguage: $currentSubtitleLanguage")
                    //println("currentSubtitleContent: ${subtitleList[subtitleList.keys.lastOrNull()]?.currentSubtitle?.content}")
                    subtitleList[secondarySubtitleLanguage]?.currentSubtitle?.content
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
        httpPlayer.apply {
            setOnPreparedListener {
                it.start()
                it.seekTo(videoHistoryPlayProgress)
                startContinuouslyUpdatingSubtitle()
                videoPlayerAspectRatio = it.videoWidth.toFloat() / it.videoHeight.toFloat()
                //httpPlayer.videoSize.width.toFloat() / httpPlayer.videoSize.height.toFloat()
                videoDuration = it.duration
                currentStat = PlayerStats.Playing
                //if (!isReady) {
                viewModelScope.launch {
                    loadInitDanmaku(cid = currentVideoCid)
                    appendDanmaku(cid = currentVideoCid, videoDuration)
                }
                //}
                isReady = true
                startContinuouslyUploadingPlayingProgress()
            }
            setOnCompletionListener {
                currentStat = PlayerStats.Finished
            }
            setOnInfoListener { _, what, _ ->
                when (what) {
                    IMediaPlayer.MEDIA_INFO_BUFFERING_START -> {
                        currentStat = PlayerStats.Buffering
                    }

                    IMediaPlayer.MEDIA_INFO_BUFFERING_END -> {
                        currentStat = PlayerStats.Playing
                    }
                }
                false
            }

        }
        cachePlayer.apply {
            setOnPreparedListener {
                it.start()
                startContinuouslyUpdatingSubtitle()
                videoPlayerAspectRatio = it.videoWidth.toFloat() / it.videoHeight.toFloat()
                //httpPlayer.videoSize.width.toFloat() / httpPlayer.videoSize.height.toFloat()
                videoDuration = cachePlayer.duration
                currentStat = PlayerStats.Playing
                isReady = true
                //startContinuouslyUpdatingSubtitle()
            }
            setOnCompletionListener {
                currentStat = PlayerStats.Finished
            }
            setOnInfoListener { player, what, extra ->
                when (what) {
                    IMediaPlayer.MEDIA_INFO_BUFFERING_START -> {
                        currentStat = PlayerStats.Buffering
                    }

                    IMediaPlayer.MEDIA_INFO_BUFFERING_END -> {
                        currentStat = PlayerStats.Playing
                    }
                }
                false
            }
        }
    }

    fun playVideoFromId(
        videoIdType: String,
        videoId: String,
        videoCid: Long,
        isBangumi: Boolean = false
    ) {
        if (videoCid == currentVideoCid) return
        appendLoadMessage("初始化播放器...")
        currentVideoCid = videoCid
        if (isBangumi) {
            viewModelScope.launch {
                getVideoInfo(videoIdType, videoId)
                //loadInitDanmaku(cid = videoCid)
                appendLoadMessage("加载视频url...")
            }
            viewModelScope.launch {
                val urlResponse =
                    BangumiInfo.getBangumiPlaybackUrl(BANGUMI_ID_TYPE_CID, videoCid)
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

                httpPlayer.setDataSource(
                    videoUrl/*.replace("https://", "http://")*/, mapOf(
                        "Referer" to "https://www.bilibili.com/bangumi/play/"
                    )
                )
                httpPlayer.prepareAsync()
                currentStat = PlayerStats.Buffering
            }
        } else {
            appendLoadMessage("加载视频url...")
            viewModelScope.launch {
                val urlResponse =
                    VideoInfo.getLowResolutionVideoPlaybackUrl(videoIdType, videoId, videoCid)
                val urlData = urlResponse.data?.data
                videoHistoryPlayProgress = urlData?.last_play_time ?: 0L
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

                httpPlayer.setDataSource(
                    videoUrl/*.replace("https://", "http://")*/, mapOf(
                        "Referer" to "https//www.bilibili.com/"
                    )
                )
                httpPlayer.prepareAsync()
                currentStat = PlayerStats.Buffering
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
                    subtitleLanguage = it.first,
                    isAIGenerated = it.first.startsWith("ai-")  //感觉有点简单粗暴...
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
            cachePlayer.dataSource = videoFile.absolutePath


            cachePlayer.prepareAsync()
            //isVideoControllerVisible = true
            //endregion

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
            //subtitleList.logd("subtitles")
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
                val response = VideoInfo.getSubtitle("https:${subtitle.subtitle_url}")
                response.data?.body?.let { subtitles ->
                    val temp = subtitleList
                    temp[subtitle.lan] = SubtitleConfig(
                        subtitleList = subtitles,
                        subtitleLanguageCode = subtitle.lan,
                        subtitleLanguage = subtitle.lan_doc,
                        isAIGenerated = subtitle.lan_doc.startsWith("ai-")  //感觉有点简单粗暴...
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
                },
                viewModelScope.async {
                    val specialDanmakus = danmakuGetter.getSpecialDanmakus(cid)
                    commandDanmakus = specialDanmakus.commandDmsList
                    val imageDanmakusTask = specialDanmakus.expressionsList.map { expressions ->
                        viewModelScope.async {
                            expressions.dataList.map { expression ->
                                val tempMap = HashMap<List<String>, ImageBitmap>()
                                viewModelScope.async {
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
        currentSubtitleLanguage = subtitleList.entries.firstOrNull { !it.value.isAIGenerated }?.key
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

    private fun appendLoadMessage(message: String, needLineWrapping: Boolean = true) {
        loadingMessage += if (needLineWrapping) "\n$message" else message
        Log.d(TAG, "appendLoadMessage: $message")
    }

    override fun onCleared() {
        super.onCleared()
        httpPlayer.release()
        cachePlayer.release()
    }
}

data class SubtitleConfig(
    val currentSubtitleIndex: Int = 0,
    val currentSubtitle: Subtitle? = null,
    val subtitleList: List<Subtitle>,
    val subtitleLanguageCode: String,
    val subtitleLanguage: String,
    val isAIGenerated: Boolean
)

enum class PlayerStats {
    Loading,
    Playing,
    Buffering,
    Paused,
    Finished
}