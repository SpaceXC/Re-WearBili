package cn.spacexc.wearbili.remake.ui.search.domain.remote.result.mediaft


import com.google.gson.annotations.SerializedName

data class Ep(
    @SerializedName("badges")
    val badges: Any?,
    @SerializedName("cover")
    val cover: String,
    @SerializedName("id")
    val id: Long,
    @SerializedName("index_title")
    val indexTitle: String,
    @SerializedName("long_title")
    val longTitle: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String
)