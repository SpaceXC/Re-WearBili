package cn.spacexc.wearbili.remake.app.main.profile.detail.favorite.detail.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState
import cn.spacexc.bilibilisdk.sdk.user.favorite.info.FavoriteInfo
import cn.spacexc.bilibilisdk.sdk.user.favorite.info.remote.content.Media
import cn.spacexc.wearbili.common.exception.PagingDataLoadFailedException

/**
 * Created by XC-Qan on 2023/8/19.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class FavoriteFolderDetailPagingSource(private val folderId: Long) : PagingSource<Int, Media>() {
    override fun getRefreshKey(state: PagingState<Int, Media>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Media> {
        val currentKey = params.key ?: 1
        val response = FavoriteInfo.getFavoriteFolderContentById(folderId = folderId, page = currentKey)
        if(response.code != 0) {
            return LoadResult.Error(
                PagingDataLoadFailedException(
                    apiUrl = response.apiUrl,
                    code = response.code
                )
            )
        }
        val list = response.data?.data?.medias ?: emptyList()
        return LoadResult.Page(
            prevKey = if(currentKey == 1) null else currentKey - 1,
            nextKey = if(response.data?.data?.has_more == true) currentKey + 1 else null,
            data = list
        )
    }
}