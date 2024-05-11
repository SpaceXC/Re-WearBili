package cn.spacexc.wearbili.remake.app.message.system.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState
import cn.spacexc.bilibilisdk.sdk.message.MessageInfo
import cn.spacexc.bilibilisdk.sdk.message.data.system.SystemNotify
import cn.spacexc.wearbili.common.exception.PagingDataLoadFailedException

class SystemNotificationPagingSource : PagingSource<Int, SystemNotify>() {
    private var notificationCursors = HashMap<Int, Long?>()

    override fun getRefreshKey(state: PagingState<Int, SystemNotify>) = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SystemNotify> {
        val page = params.key ?: 1
        val offset = if (page == 1) null else notificationCursors[page]
        val response = MessageInfo.getSystemNotifications(lastNotificationCursor = offset)
        if (response.code != 0) {
            return LoadResult.Error(PagingDataLoadFailedException(response.apiUrl, response.code))
        }
        val list = response.data?.data ?: emptyList()
        if (list.isNotEmpty()) {
            notificationCursors[page + 1] = list.last().cursor
        }
        return LoadResult.Page(
            prevKey = if (page <= 1) null else page - 1,
            nextKey = if (list.isEmpty()) null else page + 1,
            data = list
        )
    }
}