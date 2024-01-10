package cn.spacexc.wearbili.remake.ui.search.domain.remote.result.mediaft


import com.google.gson.annotations.SerializedName

data class MediaScore(
    @SerializedName("score")
    val score: Float,
    @SerializedName("user_count")
    val userCount: Int
)