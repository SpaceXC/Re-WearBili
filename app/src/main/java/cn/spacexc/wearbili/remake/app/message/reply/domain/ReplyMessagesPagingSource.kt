package cn.spacexc.wearbili.remake.app.message.reply.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState
import cn.spacexc.bilibilisdk.sdk.message.MessageInfo
import cn.spacexc.bilibilisdk.sdk.message.data.reply.ReplyMessage
import cn.spacexc.wearbili.common.exception.PagingDataLoadFailedException

class ReplyMessagesPagingSource : PagingSource<Int, ReplyMessage>() {
    private val timestamps = HashMap<Int, Long>()

    override fun getRefreshKey(state: PagingState<Int, ReplyMessage>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ReplyMessage> {
        val page = params.key ?: 1
        val timestamp = timestamps[page]
        val response = MessageInfo.getReplyMessages(timestamp)
        if (response.code != 0) {
            return LoadResult.Error(PagingDataLoadFailedException(response.apiUrl, response.code))
        }
        val list = response.data?.data?.items ?: return LoadResult.Page(emptyList(), page - 1, null)
        if(list.isEmpty()) return LoadResult.Page(emptyList(), page - 1, null)
        timestamps[page + 1] = list.last().reply_time
        return LoadResult.Page(
            list,
            if (page == 1) null else page - 1,
            if (response.data?.data?.cursor?.is_end == true) null else page + 1
        )
    }
}