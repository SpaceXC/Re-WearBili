package cn.spacexc.wearbili.remake.app.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class StatX(
    @SerializedName("danmaku")
    val danmaku: String,
    @SerializedName("play")
    val play: String
)