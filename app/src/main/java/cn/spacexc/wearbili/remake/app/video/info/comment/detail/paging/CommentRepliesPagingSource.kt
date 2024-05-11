package cn.spacexc.wearbili.remake.app.video.info.comment.detail.paging

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.paging.PagingSource
import androidx.paging.PagingState
import cn.spacexc.bilibilisdk.sdk.comment.CommentInfo
import cn.spacexc.bilibilisdk.sdk.comment.remote.detail.item.CommentReplyItem
import cn.spacexc.wearbili.common.exception.PagingDataLoadFailedException

class CommentRepliesPagingSource(
    val rootRpid: Long,
    val videoAid: Long
) : PagingSource<Int, CommentReplyItem>() {
    var rootComment: CommentReplyItem? by mutableStateOf(null)
    override fun getRefreshKey(state: PagingState<Int, CommentReplyItem>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CommentReplyItem> {
        val page = params.key ?: 1
        val response = CommentInfo.getCommentRepliesDetail(
            rootRpid,
            1,
            videoAid,
            page = page
        )
        if (response.data == null) {
            return LoadResult.Error(PagingDataLoadFailedException(response.apiUrl, response.code))
        }
        val list = response.data?.data?.replies ?: emptyList()
        if (rootComment == null) {
            rootComment = response.data?.data?.root
        }
        return LoadResult.Page(
            data = list,
            prevKey = if (page == 1) null else page - 1,
            nextKey = if (list.isEmpty()) null else page + 1
        )
    }
}