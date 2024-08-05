package cn.spacexc.wearbili.remake.app.message.at.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import cn.spacexc.wearbili.remake.app.message.at.domain.AtMessagesPagingSource

class AtMessageViewModel : ViewModel() {
    val flow = Pager(config = PagingConfig(1)) {
        AtMessagesPagingSource()
    }.flow.cachedIn(viewModelScope)
}