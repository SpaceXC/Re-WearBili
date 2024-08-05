package cn.spacexc.wearbili.remake.app.feedback.ui.issues

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import cn.spacexc.wearbili.remake.app.feedback.domain.IssuesPagingSource
import cn.spacexc.wearbili.remake.common.networking.KtorNetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AllIssuesViewModel @Inject constructor(
    private val networkUtils: KtorNetworkUtils,
) : ViewModel() {
    val pager = Pager(config = PagingConfig(pageSize = 1)) {
        IssuesPagingSource(networkUtils)
    }.flow.cachedIn(viewModelScope)
}