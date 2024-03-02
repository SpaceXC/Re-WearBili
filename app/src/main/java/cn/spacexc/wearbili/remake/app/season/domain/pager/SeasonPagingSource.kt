package cn.spacexc.wearbili.remake.app.season.domain.pager

import androidx.paging.PagingSource
import androidx.paging.PagingState
import cn.spacexc.bilibilisdk.sdk.season.SeasonInfo
import cn.spacexc.bilibilisdk.sdk.season.remote.list.Archive
import cn.spacexc.wearbili.common.exception.PagingDataLoadFailedException

/**
 * Created by XC-Qan on 2023/11/5.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class SeasonPagingSource(
    private val mid: Long,
    private val seasonId: Long
) : PagingSource<Int, Archive>() {
    override fun getRefreshKey(state: PagingState<Int, Archive>) = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Archive> {
        val page = params.key ?: 1
        val itemsPerPage = 20
        val response = SeasonInfo.getVideosInSeasonById(mid = mid, seasonId = seasonId, page = page)
        if (response.code != 0) {
            return LoadResult.Error(
                PagingDataLoadFailedException(
                    apiUrl = response.apiUrl,
                    code = response.code
                )
            )
        }
        val list = response.data?.data?.archives
        val nextKey =
            if (page * itemsPerPage >= (response.data?.data?.meta?.total ?: 0)) null else page + 1
        return LoadResult.Page(
            data = list ?: emptyList(),
            prevKey = if (page == 1) null else page - 1,
            nextKey = nextKey
        )
    }
}