package cn.spacexc.wearbili.remake.ui.login.qrcode.tv.domain.remote.tv.scanstate


import com.google.gson.annotations.SerializedName

data class TokenInfo(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("expires_in")
    val expiresIn: Int,
    val mid: Long,
    @SerializedName("refresh_token")
    val refreshToken: String
)