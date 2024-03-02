package cn.spacexc.wearbili.remake.app.space.ui.videos.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState
import cn.spacexc.bilibilisdk.sdk.user.profile.UserProfileInfo
import cn.spacexc.bilibilisdk.sdk.user.profile.remote.video.app.Item
import cn.spacexc.wearbili.common.exception.PagingDataLoadFailedException

class UploaderVideosPagingSource(private val mid: Long) : PagingSource<Int, Item>() {
    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        val page = params.key ?: 1
        val response = UserProfileInfo.getUserVideoListApp(page = page, mid = mid)
        if (response.code != 0 || response.data == null) {
            return LoadResult.Error(
                PagingDataLoadFailedException(
                    apiUrl = response.apiUrl,
                    code = response.code
                )
            )
        }
        val list = response.data?.data?.item ?: emptyList()
        return LoadResult.Page(
            data = list,
            prevKey = if (page == 1) null else page - 1,
            nextKey = if (list.isEmpty()) null else page + 1
        )
    }
}