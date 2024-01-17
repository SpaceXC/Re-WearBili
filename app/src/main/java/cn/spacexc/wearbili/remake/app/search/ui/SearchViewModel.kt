package cn.spacexc.wearbili.remake.app.search.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ScrollState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.spacexc.bilibilisdk.data.DataManager
import cn.spacexc.wearbili.common.domain.log.logd
import cn.spacexc.wearbili.common.domain.network.KtorNetworkUtils
import cn.spacexc.wearbili.common.domain.video.VideoUtils
import cn.spacexc.wearbili.remake.app.search.domain.SearchHistory
import cn.spacexc.wearbili.remake.app.search.domain.remote.hot.TrendingWord
import cn.spacexc.wearbili.remake.app.search.domain.remote.hot.TrendingWordList
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID_TYPE
import cn.spacexc.wearbili.remake.app.video.info.ui.VIDEO_TYPE_AID
import cn.spacexc.wearbili.remake.app.video.info.ui.VIDEO_TYPE_BVID
import cn.spacexc.wearbili.remake.app.video.info.ui.VideoInformationActivity
import cn.spacexc.wearbili.remake.common.ToastUtils
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by XC-Qan on 2023/4/30.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

val liveRoomIdRegex = Regex("^live\\d+\$")
val aidVideo = Regex("^av\\d+\$")

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val networkUtils: KtorNetworkUtils,
    private val dataManager: DataManager
) : ViewModel() {
    val scrollState = ScrollState(0)
    private val _hotSearchedWords = MutableStateFlow(emptyList<TrendingWord>())
    val hotSearchedWords = _hotSearchedWords.asStateFlow()
    val searchHistory =
        dataManager.getStringFlow("searchHistory", "{\"history\":[]}").map {
            Gson().fromJson(it, SearchHistory::class.java).history
        }.map {
            val temp = it.toMutableList()
            while (temp.size > 8) {
                temp.removeFirst()
            }
            temp
        }.map { it.asReversed() }

    fun getHotSearch() {
        viewModelScope.launch {
            val response =
                networkUtils.get<TrendingWordList>("https://api.bilibili.com/x/web-interface/wbi/search/square?limit=10&platform=web")
            if (response.code != 0) return@launch
            _hotSearchedWords.value = response.data?.data?.trending?.list ?: emptyList()
        }
    }

    fun addSearchHistory(word: String) {
        if (word.isNotEmpty()) {
            viewModelScope.launch {
                try {
                    val temp = searchHistory.first().toMutableList()
                    temp.remove(word)
                    temp.add(word)
                    dataManager.saveString("searchHistory", Gson().toJson(SearchHistory(temp)))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun deleteAllSearchHistory() {
        viewModelScope.launch {
            dataManager.saveString("searchHistory", "{\"history\":[]}")
        }
    }

    fun searchByKeyword(context: Context, keyword: String) {
        keyword.logd("search keyword")
        if (keyword.matches(liveRoomIdRegex)) {
            val data = Uri.parse("wearbili-live://player")
            val intent = Intent(Intent.ACTION_VIEW, data).apply {
                putExtra("liveStreamRoomId", keyword.replace("live", "").toLong())
            }
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            try {
                context.startActivity(intent)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                ToastUtils.showText("没有安装直播播放器哦")
            }
            /*context.startActivity(Intent(context, LiveStreamPlayerActivity::class.java).apply {
                putExtra(PARAM_LIVE_ROOM_ID, keyword.replace("live", "").toLong())
            })*/
            return
        }
        if (VideoUtils.isAV(keyword)) {
            context.startActivity(Intent(context, VideoInformationActivity::class.java).apply {
                putExtra(PARAM_VIDEO_ID_TYPE, VIDEO_TYPE_AID)
                putExtra(PARAM_VIDEO_ID, keyword.replace("av", ""))
            })
            return
        }
        if (VideoUtils.isBV(keyword)) {
            context.startActivity(Intent(context, VideoInformationActivity::class.java).apply {
                putExtra(PARAM_VIDEO_ID_TYPE, VIDEO_TYPE_BVID)
                putExtra(PARAM_VIDEO_ID, keyword)
            })
            return
        }
        context.startActivity(
            Intent(context, SearchResultActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                putExtra(PARAM_KEYWORD, keyword)
            }
        )
    }
}