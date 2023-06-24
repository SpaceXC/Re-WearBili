package cn.spacexc.wearbili.remake.app.main.profile.detail.history.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import cn.spacexc.wearbili.remake.app.main.profile.detail.history.domain.HistoryPagingSource

/**
 * Created by XC-Qan on 2023/6/24.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class HistoryViewModel : ViewModel() {
    val historyPagerFlow = Pager(config = PagingConfig(20)) {
        HistoryPagingSource()
    }.flow.cachedIn(viewModelScope)
}