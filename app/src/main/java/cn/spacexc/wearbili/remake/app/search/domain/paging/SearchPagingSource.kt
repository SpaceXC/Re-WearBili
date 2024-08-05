package cn.spacexc.wearbili.remake.app.search.domain.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import cn.spacexc.wearbili.common.exception.PagingDataLoadFailedException
import cn.spacexc.wearbili.remake.app.search.domain.remote.result.Search
import cn.spacexc.wearbili.remake.app.search.domain.remote.result.mediaft.SearchedMediaFt
import cn.spacexc.wearbili.remake.app.search.domain.remote.result.user.SearchedUser
import cn.spacexc.wearbili.remake.app.search.domain.remote.result.video.SearchedVideo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URLEncoder

/**
 * Created by XC-Qan on 2023/5/9.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class SearchPagingSource(
    private val networkUtils: cn.spacexc.wearbili.remake.common.networking.KtorNetworkUtils,
    private val keyword: String,
) : PagingSource<Int, SearchObject>() {
    override fun getRefreshKey(state: PagingState<Int, SearchObject>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchObject> {
        val page = params.key ?: 1
        val url =
            "https://api.bilibili.com/x/web-interface/search/all/v2?__refresh__=true&_extra=&context=&page_size=20&order=&duration=&platform=pc&highlight=1&single_column=0&source_tag=3&page=$page&keyword=${
                withContext(Dispatchers.IO) {
                    URLEncoder.encode(keyword, "utf-8")
                }
            } "
        val response = networkUtils.get<Search>(url)
        if (response.code != 0) return LoadResult.Error(
            PagingDataLoadFailedException(
                apiUrl = response.apiUrl,
                code = response.code
            )
        )
        val result = response.data?.data?.result ?: emptyList()
        if (page == 1) {
            val userList = result.find { it.resultType == "bili_user" }
            val userTree = Gson().toJsonTree(userList?.data)
            val userItems: List<SearchedUser> = Gson().fromJson(
                userTree,
                object : TypeToken<List<SearchedUser>>() {}.type
            )
            val userSearchObjectList = userItems.map { SearchObject("bili_user", it) }

            val mediaList = result.find { it.resultType == "media_ft" }
            val mediaTree = Gson().toJsonTree(mediaList?.data)
            val mediaItems: List<SearchedMediaFt> = Gson().fromJson(
                mediaTree,
                object : TypeToken<List<SearchedMediaFt>>() {}.type
            )
            val mediaSearchObjectList = mediaItems.map { SearchObject("media_ft", it) }

            val bangumiTreeList = result.find { it.resultType == "media_bangumi" }
            val bangumiTree = Gson().toJsonTree(bangumiTreeList?.data)
            val bangumiItems: List<SearchedMediaFt> = Gson().fromJson(
                bangumiTree,
                object : TypeToken<List<SearchedMediaFt>>() {}.type
            )
            val bangumiSearchObjectList = bangumiItems.map { SearchObject("media_bangumi", it) }

            val videoTreeList = result.find { it.resultType == "video" }
            val videoTree = Gson().toJsonTree(videoTreeList?.data)
            val videoItems: List<SearchedVideo> = Gson().fromJson(
                videoTree,
                object : TypeToken<List<SearchedVideo>>() {}.type
            )
            val videoSearchObjectList = videoItems.map { SearchObject("video", it) }

            /*val video = result.find { it.resultType == "video" }?.data?.map { SearchObject(type = "video", item = it) } ?: emptyList()
            val mediaFt = result.find { it.resultType == "media_ft" }?.data?.map { SearchObject(type = "media_ft", item = it) } ?: emptyList()
            val biliUser = result.find { it.resultType == "bili_user" }?.data?.map { SearchObject(type = "bili_user", item = it) } ?: emptyList()
            val mediaBangumi = result.find { it.resultType == "media_bangumi" }?.data?.map { SearchObject(type = "media_bangumi", item = it) } ?: emptyList()*/
            return LoadResult.Page(
                data = userSearchObjectList + bangumiSearchObjectList + mediaSearchObjectList + videoSearchObjectList,
                prevKey = null,
                nextKey = if (page >= (response.data?.data?.numPages ?: 0)) null else page + 1
            )
        } else {
            //val videos = result.find { it.resultType == "video" }?.data?.map { it as SearchedVideo }?.map { SearchObject(type = "video", item = it) } ?: emptyList()
            val videoTreeList = result.find { it.resultType == "video" }
            val videoTree = Gson().toJsonTree(videoTreeList?.data)
            val videoItems: List<SearchedVideo>? = Gson().fromJson(
                videoTree,
                object : TypeToken<List<SearchedVideo>>() {}.type
            )
            val videoSearchObjectList =
                (videoItems ?: emptyList()).map { SearchObject("video", it) }
            return LoadResult.Page(
                data = videoSearchObjectList,
                prevKey = page - 1,
                nextKey = if (page >= (response.data?.data?.numPages ?: 0)) null else page + 1
            )
        }
    }
}