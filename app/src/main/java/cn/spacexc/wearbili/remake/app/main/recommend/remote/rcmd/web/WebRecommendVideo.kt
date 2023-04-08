package cn.spacexc.wearbili.remake.app.main.recommend.remote.rcmd.web


import cn.spacexc.wearbili.remake.app.main.recommend.remote.rcmd.web.Data
import com.google.gson.annotations.SerializedName

data class WebRecommendVideo(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: cn.spacexc.wearbili.remake.app.main.recommend.remote.rcmd.web.Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("ttl")
    val ttl: Int
)