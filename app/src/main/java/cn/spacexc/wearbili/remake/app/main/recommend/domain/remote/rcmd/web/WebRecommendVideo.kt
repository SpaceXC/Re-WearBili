package cn.spacexc.wearbili.remake.app.main.recommend.domain.remote.rcmd.web


import com.google.gson.annotations.SerializedName

data class WebRecommendVideo(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("ttl")
    val ttl: Int
)