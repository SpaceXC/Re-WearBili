package cn.spacexc.wearbili.remake.app.search.ui

import androidx.compose.foundation.ScrollState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.spacexc.bilibilisdk.data.DataManager
import cn.spacexc.wearbili.common.domain.network.KtorNetworkUtils
import cn.spacexc.wearbili.remake.app.search.domain.SearchHistory
import cn.spacexc.wearbili.remake.app.search.domain.remote.hot.HotSearch
import cn.spacexc.wearbili.remake.app.search.domain.remote.hot.HotSearchList
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

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val networkUtils: KtorNetworkUtils,
    private val dataManager: DataManager
) : ViewModel() {
    val scrollState = ScrollState(0)
    private val _hotSearchedWords = MutableStateFlow(emptyList<HotSearch>())
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
                networkUtils.get<HotSearchList>("https://s.search.bilibili.com/main/hotword")
            if (response.code != 0) return@launch
            _hotSearchedWords.value = response.data?.list ?: emptyList()
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
}