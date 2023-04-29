package cn.spacexc.wearbili.remake.app.main.dynamic.ui

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import cn.spacexc.wearbili.remake.app.main.dynamic.domain.paging.DynamicPagingSource
import cn.spacexc.wearbili.remake.common.domain.network.KtorNetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by XC-Qan on 2023/4/27.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@HiltViewModel
class DynamicViewModel @Inject constructor(
    private val networkUtils: KtorNetworkUtils
) : ViewModel() {
    val scrollState = LazyListState(0)
    val dynamicFlow = Pager(
        PagingConfig(pageSize = 1)
    ) { DynamicPagingSource(networkUtils) }.flow.cachedIn(viewModelScope)
}