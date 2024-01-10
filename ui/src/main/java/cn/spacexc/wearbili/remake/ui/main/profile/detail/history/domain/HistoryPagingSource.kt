package cn.spacexc.wearbili.remake.app.main.profile.detail.history.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState
import cn.spacexc.bilibilisdk.sdk.user.history.HistoryInfo
import cn.spacexc.bilibilisdk.sdk.user.history.remote.HistoryItem
import cn.spacexc.wearbili.common.exception.DataLoadFailedException

/**
 * Created by XC-Qan on 2023/6/24.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class HistoryPagingSource : PagingSource<Long, HistoryItem>() {
    private var lastTimeStamp = 0L

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, HistoryItem> {
        val currentTimeStamp = params.key ?: 0L
        val response = HistoryInfo.getHistoryByPage(currentTimeStamp)
        if (response.code != 0) {
            return LoadResult.Error(DataLoadFailedException())
        }
        val list = response.data?.data?.list ?: emptyList()
        if (list.isEmpty()) {
            return LoadResult.Error(DataLoadFailedException())
        }
        val nextTimeStamp = list.last().view_at
        return LoadResult.Page(
            prevKey = if (lastTimeStamp == 0L) null else lastTimeStamp,
            nextKey = nextTimeStamp,
            data = list
        ).apply {
            lastTimeStamp = currentTimeStamp
        }
    }

    override fun getRefreshKey(state: PagingState<Long, HistoryItem>): Long? {
        return null
    }
}