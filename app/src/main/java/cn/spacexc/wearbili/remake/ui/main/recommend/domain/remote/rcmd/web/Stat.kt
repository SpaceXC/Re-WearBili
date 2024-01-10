package cn.spacexc.wearbili.remake.ui.main.recommend.domain.remote.rcmd.web


import com.google.gson.annotations.SerializedName

data class Stat(
    @SerializedName("danmaku")
    val danmaku: Int,
    @SerializedName("like")
    val like: Int,
    @SerializedName("view")
    val view: Int
)