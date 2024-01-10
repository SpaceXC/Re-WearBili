package cn.spacexc.wearbili.remake.ui.season.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import cn.spacexc.wearbili.remake.ui.season.domain.pager.SeasonPagingSource

/**
 * Created by XC-Qan on 2023/11/5.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class SeasonViewModel : ViewModel() {
    fun getPager(mid: Long, seasonId: Long) =
        Pager(config = PagingConfig(pageSize = 1)) {
            SeasonPagingSource(mid, seasonId)
        }.flow.cachedIn(viewModelScope)
}