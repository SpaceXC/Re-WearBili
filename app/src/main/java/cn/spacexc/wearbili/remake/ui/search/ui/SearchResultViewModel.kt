package cn.spacexc.wearbili.remake.ui.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import cn.spacexc.wearbili.common.domain.network.KtorNetworkUtils
import cn.spacexc.wearbili.remake.ui.search.domain.paging.SearchObject
import cn.spacexc.wearbili.remake.ui.search.domain.paging.SearchPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by XC-Qan on 2023/5/3.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@HiltViewModel
class SearchResultViewModel @Inject constructor(
    private val networkUtils: KtorNetworkUtils
) : ViewModel() {
    fun getSearchResultFlow(keyword: String): Flow<PagingData<SearchObject>> {
        return Pager(config = PagingConfig(pageSize = 1)) {
            SearchPagingSource(
                networkUtils = networkUtils,
                keyword = keyword
            )
        }.flow.cachedIn(viewModelScope)
    }
}