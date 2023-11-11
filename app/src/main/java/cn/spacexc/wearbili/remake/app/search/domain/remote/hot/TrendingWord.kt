package cn.spacexc.wearbili.remake.app.search.domain.remote.hot


import com.google.gson.annotations.SerializedName

data class TrendingWord(
    val goto: String,
    val icon: String,
    val keyword: String,
    @SerializedName("show_name")
    val showName: String,
    val uri: String
)