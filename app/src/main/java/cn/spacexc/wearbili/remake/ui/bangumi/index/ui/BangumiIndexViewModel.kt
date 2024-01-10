package cn.spacexc.wearbili.remake.ui.bangumi.index.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import cn.spacexc.wearbili.remake.ui.bangumi.index.domain.paging.BangumiFollowIndexPagingSource

/**
 * Created by XC-Qan on 2023/8/8.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class BangumiIndexViewModel : ViewModel() {
    val pager = Pager(config = PagingConfig(pageSize = 1)) {
        BangumiFollowIndexPagingSource()
    }.flow.cachedIn(viewModelScope)
}