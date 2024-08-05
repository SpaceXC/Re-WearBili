package cn.spacexc.wearbili.remake.app.feedback.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState
import cn.spacexc.bilibilisdk.utils.UserUtils
import cn.spacexc.wearbili.common.exception.PagingDataLoadFailedException
import cn.spacexc.wearbili.remake.app.crash.remote.ErrorLog
import cn.spacexc.wearbili.remake.app.crash.ui.FEEDBACK_SERVER_BASE_URL
import cn.spacexc.wearbili.remake.app.feedback.domain.remote.IssueResponse
import cn.spacexc.wearbili.remake.common.networking.KtorNetworkUtils

class IssuesPagingSource (
    private val networkUtils: KtorNetworkUtils,
) : PagingSource<Int, ErrorLog>() {
    override fun getRefreshKey(state: PagingState<Int, ErrorLog>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ErrorLog> {
        val page = params.key ?: 1
        val response =
            networkUtils.get<IssueResponse>("$FEEDBACK_SERVER_BASE_URL/log/all?page=$page&itemsPerPage=10&uid=${UserUtils.mid()}")
        if (response.code != 0) {
            return LoadResult.Error(PagingDataLoadFailedException(response.apiUrl, response.code))
        }
        val result = response.data?.body ?: emptyList()
        println(result)
        return LoadResult.Page(
            prevKey = if (page == 1) null else page - 1,
            nextKey = if (result.isEmpty()) null else page + 1,
            data = result
        )
    }
}