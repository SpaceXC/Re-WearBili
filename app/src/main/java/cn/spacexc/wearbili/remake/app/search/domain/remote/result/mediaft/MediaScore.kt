package cn.spacexc.wearbili.remake.app.search.domain.remote.result.mediaft


import com.google.gson.annotations.SerializedName

data class MediaScore(
    @SerializedName("score")
    val score: Int,
    @SerializedName("user_count")
    val userCount: Int
)