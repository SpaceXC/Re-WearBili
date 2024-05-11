package cn.spacexc.wearbili.remake.app.message.system.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import cn.spacexc.wearbili.remake.app.message.system.domain.SystemNotificationPagingSource

class SystemNotificationViewModel : ViewModel() {
    val flow = Pager(config = PagingConfig(1)) {
        SystemNotificationPagingSource()
    }.flow.cachedIn(viewModelScope)
}