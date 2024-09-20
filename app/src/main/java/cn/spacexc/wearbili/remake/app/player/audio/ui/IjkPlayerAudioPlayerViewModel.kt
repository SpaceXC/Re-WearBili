package cn.spacexc.wearbili.remake.app.player.audio.ui

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cn.spacexc.bilibilisdk.sdk.bangumi.info.BANGUMI_ID_TYPE_CID
import cn.spacexc.bilibilisdk.sdk.bangumi.info.BangumiInfo
import cn.spacexc.bilibilisdk.sdk.video.action.VideoAction
import cn.spacexc.bilibilisdk.sdk.video.info.VideoInfo
import cn.spacexc.bilibilisdk.sdk.video.info.remote.subtitle.Subtitle
import cn.spacexc.bilibilisdk.utils.UserUtils
import cn.spacexc.wearbili.common.domain.log.TAG
import cn.spacexc.wearbili.common.domain.log.logd
import cn.spacexc.wearbili.remake.app.cache.domain.database.VideoCacheFileInfo
import cn.spacexc.wearbili.remake.app.cache.domain.database.VideoCacheRepository
import cn.spacexc.wearbili.remake.app.player.audio.AudioPlayerManager
import cn.spacexc.wearbili.remake.app.settings.SettingsManager
import cn.spacexc.wearbili.remake.common.HeartbeatListenable
import cn.spacexc.wearbili.remake.proto.settings.VideoDecoder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer


/*@UnstableApi*/
/**
 * Created by XC-Qan on 2023/6/28.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@SuppressLint("UnsafeOptInUsageError")
/**
 * 这里其实就是稍微小小改了一下的[cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.IjkVideoPlayerViewModel]啦
 */
class IjkPlayerAudioPlayerViewModel /*@Inject constructor*/(
    private val application: Application,
    private val repository: VideoCacheRepository,
    private val scope: CoroutineScope
): HeartbeatListenable<AudioPlayerManager.AudioPlayerHeartbeat> /*: ViewModel()*/ {
    // region view related
    var isAdjustingVolume by mutableStateOf(false)
    var volumeInformation by mutableStateOf("")
    // endregion

    //region player related
    var player = IjkMediaPlayer().apply {
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
        setOption(
            IjkMediaPlayer.OPT_CATEGORY_PLAYER,
            "framedrop",
            5
        )  //跳帧处理,CPU处理较慢时，进行跳帧处理，保证播放流程，画面和声音同步
        setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-fps", 1)  //最大fps
        setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1)  //精确seek

        if (SettingsManager.getConfiguration().videoDecoder == VideoDecoder.Hardware) {
            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1)
            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1)
            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1)
        }
    }
    // endregion

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

    val currentPlayProgress = flow {
        while (true) {
            emit(player.currentPosition)
            delay(10)
        }
    }
    var videoDuration by mutableLongStateOf(0L)

    var subtitleList = mutableStateMapOf<String, SubtitleConfig>()

    var currentSubtitleLanguage: String? by mutableStateOf(null)
    var currentSubtitle = flow {
        var index = 0
        while (true) {
            //emit("index: $index")
            index++
            val nextSubtitle =
                if (currentSubtitleLanguage != null) {
                    //println("currentLanguage: $currentSubtitleLanguage")
                    //println("currentSubtitleContent: ${subtitleList[subtitleList.keys.lastOrNull()]?.currentSubtitle?.content}")
                    subtitleList[currentSubtitleLanguage]?.currentSubtitle
                } else null
            emit(nextSubtitle)
            delay(5)
        }
    }
    var currentSubtitleText = flow {
        //var index = 0
        while (true) {
            //emit("index: $index")
            //index++
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

    var secondarySubtitleLanguage: String? by mutableStateOf(/*subtitleList.entries.lastOrNull() { !it.value.isAIGenerated }?.key*/null)
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

    var videoCastUrl = ""

    init {
        scope.launch {
            while (true) {
                AudioPlayerManager.heartbeat(AudioPlayerManager.AudioPlayerHeartbeat(System.currentTimeMillis()))
                delay(500)
            }
        }
        player.apply {
            setOnPreparedListener {
                it.start()
                videoDuration = it.duration
                //Log.d(TAG, "onPlaybackStateChanged: startUpdatingSubtitles")
                currentStat = PlayerStats.Playing
                isReady = true
                isVideoControllerVisible = true
                startContinuouslyUpdatingSubtitle()
                startContinuouslyUploadingPlayingProgress()
            }
            setOnInfoListener { _, what, _ ->
                when (what) {
                    IMediaPlayer.MEDIA_INFO_BUFFERING_START -> {
                        currentStat = PlayerStats.Buffering
                        appendLoadMessage("缓冲中...")
                    }

                    IMediaPlayer.MEDIA_INFO_BUFFERING_END -> {
                        currentStat = PlayerStats.Playing
                    }
                }
                false
            }
            setOnCompletionListener {
                currentStat = if (isPlaying) PlayerStats.Playing else PlayerStats.Paused
            }
        }
    }

    fun playVideoFromId(
        videoIdType: String,
        videoId: String,
        videoCid: Long,
        isBangumi: Boolean = false,
        onDataFetched: (title: String) -> Unit
    ) {
        appendLoadMessage("初始化播放器...")
        scope.launch {
            if (isBangumi) {
                getVideoInfo(videoIdType, videoId)
                onDataFetched(videoInfo?.data?.title ?: "未知视频")
                appendLoadMessage("加载音频url...")
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

                player.setDataSource(
                    videoUrl, mapOf(
                        "Referer" to "https://www.bilibili.com/bangumi/play/"
                    )
                )
                player.prepareAsync()
                currentStat = PlayerStats.Buffering
            } else {
                getVideoInfo(videoIdType, videoId)
                onDataFetched(videoInfo?.data?.title ?: "未知视频")
                AudioPlayerManager.currentVideo = videoInfo?.data?.title ?: "未知视频"
                loadSubtitle()
                appendLoadMessage("加载音频url...")
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

                player.dataSource = videoUrl
                player.prepareAsync()
                currentStat = PlayerStats.Buffering
            }
        }
    }

    fun playVideoFromLocalFile(
        videoCid: Long
    ) {
        /*scope.launch {
            val cacheFileInfo = repository.getTaskInfoByVideoCid(videoCid)
            if (cacheFileInfo == null) {
                appendLoadMessage("好像没有这个缓存耶...")
                return@launch
            }
            val cacheFolder = File(application.filesDir, "videoCaches/$videoCid")
            val videoFile = File(cacheFolder, cacheFileInfo.downloadedVideoFileName)
            val danmakuFile = File(cacheFolder, cacheFileInfo.downloadedDanmakuFileName)

            val subtitleFiles = cacheFileInfo.downloadedSubtitleFileNames.map {
                it.key to Gson().fromJson(
                    String(
                        File(
                            cacheFolder,
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
            println("subtitleList: ${subtitleList.values}")


            val mediaSource = ProgressiveMediaSource.Factory(DefaultDataSource.Factory(application))
                .createMediaSource(fromUri(Uri.fromFile(videoFile)))

            player.setMediaItem(mediaSource.mediaItem)
            player.playWhenReady = true
            player.prepare()
            startContinuouslyUpdatingSubtitle()
        }*/
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
            //subtitleList.logd("subtitles")
        }
    }

    private suspend fun initSubtitle(urls: List<cn.spacexc.bilibilisdk.sdk.video.info.remote.playerinfo.Subtitle>) {
        appendLoadMessage("加载字幕...")
        val tasks = urls.map { subtitle ->
            scope.async {
                appendLoadMessage("加载\"${subtitle.lan}\"字幕...")
                //Log.d(TAG, "initSubtitle1: $subtitle")
                val response = VideoInfo.getSubtitle("https:${subtitle.subtitle_url}")
                response.data?.body?.let { subtitles ->
                    //Log.d(TAG, "initSubtitle2: result for $subtitles")
                    val temp = subtitleList
                    temp[subtitle.lan] = SubtitleConfig(
                        subtitleList = subtitles.processSubtitleBlanks(),
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

    private fun List<Subtitle>.processSubtitleBlanks(): List<Subtitle> {
        return buildList {
            if (this@processSubtitleBlanks.first().from >= 4) {
                add(
                    Subtitle(
                        "[BLANK_SUSPEND]",
                        from = 0.0,
                        to = this@processSubtitleBlanks.first().from,
                        location = 0
                    )
                )
            }
            this@processSubtitleBlanks.forEachIndexed { index, subtitle ->
                add(subtitle)
                (if (index + 1 == this@processSubtitleBlanks.size) null else this@processSubtitleBlanks[index + 1])?.let { nextSubtitle ->
                    val blankTime = nextSubtitle.from - subtitle.to
                    if (blankTime >= 4) {
                        add(
                            Subtitle(
                                "[BLANK_SUSPEND]",
                                from = subtitle.to,
                                to = nextSubtitle.from - 0.3,
                                location = 0
                            )
                        )
                    }
                }
            }
        }
    }

    private suspend fun getVideoInfo(
        videoIdType: String,
        videoId: String
    ) {
        appendLoadMessage("获取音频信息...")
        val response =
            VideoInfo.getVideoInfoByIdWeb(videoIdType, videoId)
        print("Obtained Video Info")
        if (response.code != 0 || response.data == null || response.data?.data == null) return
        videoInfo = response.data
        //delay(1000L)
        appendLoadMessage("获取成功", needLineWrapping = false)
    }

    private suspend fun updateSubtitle() {
        val tasks = subtitleList.entries.map { entry ->
            scope.async {
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
                                        subtitleList[entry.key]!!.copy(
                                            currentSubtitle = null,
                                            currentSubtitleIndex = -1
                                        )
                                }
                            }
                        } else {
                            subtitleList[entry.key] =
                                subtitleList[entry.key]!!.copy(
                                    currentSubtitle = null,
                                    currentSubtitleIndex = -1
                                )
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
                            subtitleList[entry.key]!!.copy(
                                currentSubtitle = null,
                                currentSubtitleIndex = -1
                            )
                    }
                }
            }
        }
        tasks.awaitAll()
        //println("subtitleUpdated: ${subtitleList.map { "${it.key}: ${it.value.currentSubtitle}" }}")
    }

    private fun startContinuouslyUpdatingSubtitle() {
        //Log.d(TAG, "startContinuouslyUpdatingSubtitle")
        currentSubtitleLanguage =
            subtitleList.entries.firstOrNull { !it.key.startsWith("ai-") }?.key
                ?: subtitleList.keys.firstOrNull() //找出第一个非ai生成的字幕，如果都是ai生成的，则直接取第一个字幕，
        scope.launch {
            while (player.currentPosition >= 0) {
                updateSubtitle()
                delay(5)
            }
        }
    }

    private fun startContinuouslyUploadingPlayingProgress() {
        scope.launch {
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

    private fun appendLoadMessage(message: String, needLineWrapping: Boolean = true) {
        loadingMessage += if (needLineWrapping) "\n$message" else message
        Log.d(TAG, "appendLoadMessage: $message")
    }

    override fun onHeartbeat(): AudioPlayerManager.AudioPlayerHeartbeat {
        return AudioPlayerManager.AudioPlayerHeartbeat(System.currentTimeMillis())
    }

}

data class SubtitleConfig(
    val currentSubtitleIndex: Int = -1,
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