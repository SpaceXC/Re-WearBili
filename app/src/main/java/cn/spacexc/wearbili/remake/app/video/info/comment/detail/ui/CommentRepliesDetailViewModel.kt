package cn.spacexc.wearbili.remake.app.video.info.comment.detail.ui

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import cn.spacexc.bilibilisdk.sdk.comment.remote.detail.item.CommentReplyItem
import cn.spacexc.wearbili.remake.app.video.info.comment.detail.paging.CommentRepliesPagingSource

class CommentRepliesDetailViewModel : ViewModel() {
    var pagingDataSource: CommentRepliesPagingSource? = null
    var pager: Pager<Int, CommentReplyItem>? = null

    fun initPaging(
        rootRpid: Long,
        videoAid: Long
    ) {
        pagingDataSource = CommentRepliesPagingSource(rootRpid, videoAid)
        if (pager == null) {
            pager = Pager(config = PagingConfig(1)) {
                pagingDataSource!!
            }
        }
    }
}