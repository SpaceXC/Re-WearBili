package cn.spacexc.wearbili.remake.app.bangumi.index.domain.paging

import androidx.paging.Pager
import androidx.paging.PagingSource
import androidx.paging.PagingState
import cn.spacexc.bilibilisdk.sdk.bangumi.info.BangumiInfo
import cn.spacexc.bilibilisdk.sdk.bangumi.info.index.remote.BangumiFollowItem
import cn.spacexc.wearbili.common.exception.DataLoadFailedException

/**
 * Created by XC-Qan on 2023/8/8.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class BangumiFollowIndexPagingSource : PagingSource<Int, BangumiFollowItem>() {
    override fun getRefreshKey(state: PagingState<Int, BangumiFollowItem>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BangumiFollowItem> {
        val currentPage = params.key ?: 1
        val response = BangumiInfo.getBangumiFollowIndex(itemsPerPage = 20, page = currentPage)
        if(response.code != 0 || response.data?.data == null) {
            return LoadResult.Error(DataLoadFailedException())
        }
        val list = response.data?.data?.list ?: emptyList()
        return LoadResult.Page(
            data = list,
            prevKey = if(currentPage == 1) null else currentPage - 1,
            nextKey = if(response.data?.data?.has_next == 1) currentPage + 1 else null
        )
    }
}