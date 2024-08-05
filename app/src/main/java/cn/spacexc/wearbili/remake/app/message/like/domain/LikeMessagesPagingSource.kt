package cn.spacexc.wearbili.remake.app.message.like.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState
import cn.spacexc.bilibilisdk.sdk.message.MessageInfo
import cn.spacexc.bilibilisdk.sdk.message.data.like.LikeMessage
import cn.spacexc.wearbili.common.exception.PagingDataLoadFailedException

class LikeMessagesPagingSource : PagingSource<Int, LikeMessage>() {
    private val timestamps = HashMap<Int, Long>()

    override fun getRefreshKey(state: PagingState<Int, LikeMessage>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LikeMessage> {
        val page = params.key ?: 1
        val timestamp = timestamps[page]
        val response = MessageInfo.getLikeMessages(timestamp)
        if (response.code != 0) {
            return LoadResult.Error(PagingDataLoadFailedException(response.apiUrl, response.code))
        }
        val list = (response.data?.data?.total?.items ?: return LoadResult.Page(emptyList(), page - 1, null)) + (response.data?.data?.latest?.items ?: return LoadResult.Page(emptyList(), page - 1, null))
        if(list.isEmpty()) return LoadResult.Page(emptyList(), page - 1, null)
        timestamps[page + 1] = list.last().like_time
        return LoadResult.Page(
            list,
            if (page == 1) null else page - 1,
            if (response.data?.data?.total?.cursor?.is_end == true) null else page + 1
        )
    }
}