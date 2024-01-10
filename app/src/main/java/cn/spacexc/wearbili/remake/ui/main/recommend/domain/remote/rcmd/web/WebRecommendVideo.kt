package cn.spacexc.wearbili.remake.ui.main.recommend.domain.remote.rcmd.web


import com.google.gson.annotations.SerializedName

data class WebRecommendVideo(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: cn.spacexc.wearbili.remake.ui.main.recommend.domain.remote.rcmd.web.Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("ttl")
    val ttl: Int
)