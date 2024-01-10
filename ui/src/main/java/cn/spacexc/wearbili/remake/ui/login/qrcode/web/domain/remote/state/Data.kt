package cn.spacexc.wearbili.remake.app.login.qrcode.web.domain.remote.state


import com.google.gson.annotations.SerializedName

data class Data(
    val code: Int,
    val message: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    val timestamp: Long,
    val url: String
)