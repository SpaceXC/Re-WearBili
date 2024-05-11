package cn.spacexc.wearbili.remake.app.bangumi.info.episodes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.spacexc.bilibilisdk.sdk.bangumi.info.remote.Episode
import cn.spacexc.wearbili.remake.common.UIState
import kotlinx.coroutines.launch

class BangumiEpisodesViewModel : ViewModel() {
    var uiState: UIState by mutableStateOf(UIState.Loading)
    var bangumiInfo: List<Pair<String, List<Episode>>> by mutableStateOf(emptyList())
        private set

    fun getBangumiInfo(bangumiIdType: String, bangumiId: Long) {
        viewModelScope.launch {
            uiState = UIState.Loading
            val response = cn.spacexc.bilibilisdk.sdk.bangumi.info.BangumiInfo.getBangumiInfo(
                bangumiIdType,
                bangumiId
            )
            if (response.code != 0) {
                uiState = UIState.Failed(response.code)
                return@launch
            }
            bangumiInfo = buildList {
                response.data?.result?.let { bangumi ->
                    add(Pair("选集", bangumi.episodes))
                    bangumi.section?.forEach { section ->
                        add(Pair(section.title, section.episodes))
                    }
                }
            }
            uiState = UIState.Success
        }
    }

    fun <T> List<T>.getSafely(index: Int): T {
        return if (index < 0) get(0) else get(index)
    }
}