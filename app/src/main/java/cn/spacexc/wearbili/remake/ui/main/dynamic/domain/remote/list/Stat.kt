package cn.spacexc.wearbili.remake.ui.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class Stat(
    @SerializedName("danmaku")
    val danmaku: String,
    @SerializedName("play")
    val play: String
)