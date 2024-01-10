package cn.spacexc.wearbili.remake.ui.search.domain.remote.hot


import com.google.gson.annotations.SerializedName

data class Trending(
    val list: List<TrendingWord>,
    val title: String,
    @SerializedName("top_list")
    val topList: List<Any>,
    val trackid: String
)