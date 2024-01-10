package cn.spacexc.wearbili.remake.ui.main.profile.detail.following.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState
import cn.spacexc.bilibilisdk.sdk.user.follow.following.FollowedUserInfo
import cn.spacexc.bilibilisdk.sdk.user.follow.following.remote.user.Data
import cn.spacexc.wearbili.common.exception.DataLoadFailedException

/**
 * Created by XC-Qan on 2023/7/7.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class FollowingPagingSource(private val tagId: Long) : PagingSource<Int, Data>() {
    override fun getRefreshKey(state: PagingState<Int, Data>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Data> {
        val currentPage = params.key ?: 1
        val itemsPerPage = 20
        val response = FollowedUserInfo.getFollowedUsersByTag(
            itemsPerPage = itemsPerPage,
            page = currentPage,
            tagId = tagId
        )
        if (response.code != 0 || response.data?.data == null) {
            return LoadResult.Error(DataLoadFailedException())
        }
        val list = response.data?.data ?: emptyList()

        val nextKey = if (list.size < itemsPerPage) null else currentPage + 1
        return LoadResult.Page(
            data = list,
            prevKey = if (currentPage == 1) null else currentPage,
            nextKey = nextKey
        )
    }
}