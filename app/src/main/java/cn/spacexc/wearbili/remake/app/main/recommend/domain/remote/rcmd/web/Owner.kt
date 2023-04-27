package cn.spacexc.wearbili.remake.app.main.recommend.domain.remote.rcmd.web


import com.google.gson.annotations.SerializedName

data class Owner(
    @SerializedName("face")
    val face: String,
    @SerializedName("mid")
    val mid: Long,
    @SerializedName("name")
    val name: String
)