package cn.spacexc.wearbili.remake.app.message.reply.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import cn.spacexc.wearbili.remake.app.message.reply.domain.ReplyMessagesPagingSource

class ReplyMessageViewModel : ViewModel() {
    val flow = Pager(config = PagingConfig(1)) {
        ReplyMessagesPagingSource()
    }.flow.cachedIn(viewModelScope)
}