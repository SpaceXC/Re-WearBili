package cn.spacexc.wearbili.remake.app.message.direct.sessions.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState
import cn.spacexc.bilibilisdk.sdk.message.MessageInfo
import cn.spacexc.bilibilisdk.sdk.message.data.direct.list.Session
import cn.spacexc.bilibilisdk.sdk.user.profile.UserProfileInfo
import cn.spacexc.wearbili.common.exception.PagingDataLoadFailedException

class DirectMessageListPagingSource : PagingSource<Int, Session>() {
    private var notificationCursors = HashMap<Int, Long?>()

    override fun getRefreshKey(state: PagingState<Int, Session>) = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Session> {
        val page = params.key ?: 1
        val offset = if (page == 1) null else notificationCursors[page]
        val response = MessageInfo.getDirectMessages(lastMessageCursor = offset)
        if (response.code != 0) {
            return LoadResult.Error(PagingDataLoadFailedException(response.apiUrl, response.code))
        }
        val list = response.data?.data?.session_list ?: emptyList()
        if (list.isNotEmpty()) {
            notificationCursors[page + 1] = list.last().session_ts
        }
        else {
            return LoadResult.Page(
                prevKey = if (page <= 1) null else page - 1,
                nextKey = null,
                data = emptyList()
            )
        }
        val userCardsResponse = UserProfileInfo.getUserCardsInBatch(list.map { it.talker_id })
        if(userCardsResponse.code != 0) {
            return LoadResult.Error(PagingDataLoadFailedException(response.apiUrl, userCardsResponse.code))
        }
        val userCards = userCardsResponse.data?.data!!
        return LoadResult.Page(
            prevKey = if (page <= 1) null else page - 1,
            nextKey = if (list.isEmpty()) null else page + 1,
            data = list.map {
                it.copy(userCard = userCards[it.talker_id])
            }
        )
    }
}