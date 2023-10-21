package cn.spacexc.wearbili.remake.livestreamplayer

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Player.Listener
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import cn.spacexc.bilibilisdk.sdk.stream.info.StreamInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import java.net.CookieManager
import java.net.CookiePolicy
import java.net.HttpCookie

/**
 * Created by XC-Qan on 2023/10/12.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@SuppressLint("UnsafeOptInUsageError")
class LiveStreamPlayerViewModel(private val application: Application) :
    AndroidViewModel(application) {
    enum class StreamPlayerState {
        Loading,
        Buffering,
        Playing,
        Failed
    }

    val playerMessage = MutableStateFlow("努力加载中...")
    val playerState = MutableStateFlow(StreamPlayerState.Loading)
    //val bandwidth = MutableStateFlow(0f)

    val player: ExoPlayer

    init {
        val cookieManager = CookieManager()
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
        val client = OkHttpClient.Builder()
            .cookieJar(object : CookieJar {
                /**
                 * Load cookies from the jar for an HTTP request to [url]. This method returns a possibly
                 * empty list of cookies for the network request.
                 *
                 * Simple implementations will return the accepted cookies that have not yet expired and that
                 * [match][Cookie.matches] [url].
                 */
                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    return cookieManager.cookieStore[url.toUri()].map {
                        Cookie.Builder()
                            .name(it.name)
                            .value(it.value)
                            .expiresAt(it.maxAge)
                            .domain(it.domain)
                            .path(it.path)
                            .build()
                    }
                }

                /**
                 * Saves [cookies] from an HTTP response to this store according to this jar's policy.
                 *
                 * Note that this method may be called a second time for a single HTTP response if the response
                 * includes a trailer. For this obscure HTTP feature, [cookies] contains only the trailer's
                 * cookies.
                 */
                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                    cookies.map {
                        cookieManager.cookieStore.add(
                            url.toUri(), HttpCookie(
                                it.name, it.value
                            )
                        )
                    }
                }

            })
            .build()
        val dataSourceFactory = OkHttpDataSource.Factory(client)
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36")
        val httpMediaSourceFactory = DefaultMediaSourceFactory(dataSourceFactory)
        player = ExoPlayer.Builder(application)
            .setMediaSourceFactory(httpMediaSourceFactory.setLiveTargetOffsetMs(5000))
            .build()
        player.addListener(object : Listener {
            override fun onPlayerError(error: PlaybackException) {
                /*if (error.errorCode == PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW) {
                    // Re-initialize player at the live edge.

                } else {

                }*/
                playerState.value = StreamPlayerState.Buffering
                appendLoadingMessage("正在重试...")
                player.seekToDefaultPosition()
                player.prepare()
            }

            override fun onIsLoadingChanged(isLoading: Boolean) {
                super.onIsLoadingChanged(isLoading)
                playerState.value =
                    if (isLoading) StreamPlayerState.Buffering else StreamPlayerState.Playing
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                playerState.value =
                    if (isPlaying) StreamPlayerState.Playing else playerState.value// else StreamPlayerState.Playing
            }

            override fun onEvents(player: Player, events: Player.Events) {
                super.onEvents(player, events)
                when (events) {

                }
            }
        })
    }

    fun playLiveStreamFromRoomId(roomId: Long) {
        viewModelScope.launch {
            val response = StreamInfo.getLiveStreamPlayUrlFromRoomId(roomId)
            val url = response.data?.data?.durl?.firstOrNull()?.url
            if (url.isNullOrEmpty()) {
                appendLoadingMessage("加载失败力！")
                playerState.value = StreamPlayerState.Failed
                return@launch
            }
            appendLoadingMessage("获取视频流url成功！")
            val mediaItem =
                MediaItem.Builder()
                    .setUri(url)
                    .setLiveConfiguration(
                        MediaItem.LiveConfiguration.Builder().setMaxPlaybackSpeed(1.02f).build()
                    )
                    .build()
            println("stream url: $url")
            player.setMediaItem(mediaItem)
            player.prepare()
            player.play()
        }
    }

    private fun appendLoadingMessage(message: String) {
        playerMessage.value = playerMessage.value + "\n" + message
    }
}