package cn.spacexc.wearbili.remake.app.video.info.comment.ui

import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import cn.spacexc.wearbili.remake.app.TAG
import cn.spacexc.wearbili.remake.app.video.info.comment.domain.CommentContentData
import cn.spacexc.wearbili.remake.app.video.info.comment.domain.paging.CommentPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by XC-Qan on 2023/4/28.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val networkUtils: cn.spacexc.wearbili.remake.common.networking.KtorNetworkUtils,
) : ViewModel() {
    val commentPagingSources = HashMap<String, Flow<PagingData<CommentContentData>>>()
    private val commentLazyColumnScrollState = HashMap<String, LazyListState>()

    @Composable
    fun commentListFlow(oid: String?): Flow<PagingData<CommentContentData>>? {
        if (oid.isNullOrEmpty()) return null
        if (commentPagingSources[oid] != null) commentPagingSources[oid]
        else
            commentPagingSources[oid] = Pager(config = PagingConfig(pageSize = 1)) {
                CommentPagingSource(
                    networkUtils = networkUtils,
                    oid = oid
                )
            }.flow.cachedIn(viewModelScope)

        val currentPagingSource = commentPagingSources[oid]
        Log.d(TAG, "commentListFlow: $currentPagingSource")
        return currentPagingSource
    }

    fun getScrollState(oid: String?): LazyListState? {
        if (oid == null) return null
        if(commentLazyColumnScrollState[oid] == null) {
            commentLazyColumnScrollState[oid] = LazyListState()
        }
        return commentLazyColumnScrollState[oid]
    }
}