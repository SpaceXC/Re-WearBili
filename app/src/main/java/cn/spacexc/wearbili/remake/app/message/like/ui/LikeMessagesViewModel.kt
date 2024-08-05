package cn.spacexc.wearbili.remake.app.message.like.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import cn.spacexc.wearbili.remake.app.message.like.domain.LikeMessagesPagingSource

class LikeMessagesViewModel : ViewModel() {
    val flow = Pager(config = PagingConfig(1)) {
        LikeMessagesPagingSource()
    }.flow.cachedIn(viewModelScope)
}