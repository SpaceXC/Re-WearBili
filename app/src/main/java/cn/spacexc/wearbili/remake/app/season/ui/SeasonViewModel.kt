package cn.spacexc.wearbili.remake.app.season.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import cn.spacexc.wearbili.remake.app.season.domain.pager.SeasonPagingSource
import cn.spacexc.wearbili.remake.common.networking.KtorNetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by XC-Qan on 2023/11/5.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@HiltViewModel
class SeasonViewModel @Inject constructor(
    val networkUtils: KtorNetworkUtils
) : ViewModel() {
    fun getPager(mid: Long, seasonId: Long) =
        Pager(config = PagingConfig(pageSize = 1)) {
            SeasonPagingSource(mid, seasonId)
        }.flow.cachedIn(viewModelScope)
}