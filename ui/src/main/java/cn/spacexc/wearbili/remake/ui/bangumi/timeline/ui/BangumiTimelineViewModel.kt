package cn.spacexc.wearbili.remake.app.bangumi.timeline.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.spacexc.bilibilisdk.sdk.bangumi.info.BangumiInfo
import cn.spacexc.bilibilisdk.sdk.bangumi.info.timeline.BangumiTimeline
import cn.spacexc.bilibilisdk.sdk.bangumi.info.timeline.Episode
import cn.spacexc.bilibilisdk.sdk.bangumi.info.timeline.Result
import cn.spacexc.wearbili.remake.common.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * Created by XC-Qan on 2023/8/9.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class BangumiTimelineViewModel : ViewModel() {
    var uiState by mutableStateOf(UIState.Loading)
    var timelineData: List<Pair<Pair<String, Int>, Map<String, List<Episode>>>> by mutableStateOf(
        emptyList()
    )

    var currentDate by mutableStateOf("")

    fun getBangumiTimeline() {
        viewModelScope.launch {
            val response = BangumiInfo.getBangumiTimeline()
            if (response.code != 0) {
                uiState = UIState.Failed
                return@launch
            }
            response.data?.result?.let { results ->
                val list = mutableListOf<Pair<Pair<String, Int>, Map<String, List<Episode>>>>()
                results.forEach { result ->
                    val episodes = result.episodes.groupBy { episode -> episode.pub_time }
                    val pair = Pair(
                        first = Pair(first = result.date, second = result.day_of_week),
                        second = episodes
                    )
                    list.add(pair)
                }
                timelineData = list
                currentDate = results.find { it.is_today == 1 }?.date ?: ""
                uiState = UIState.Success
                return@launch
            }
            uiState = UIState.Failed
            return@launch
        }
    }
}