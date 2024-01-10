package cn.spacexc.wearbili.remake.ui.login.qrcode.tv.domain.remote.tv.scanstate

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("cookie_info")
    val cookieInfo: CookieInfo,
    @SerializedName("expires_in")
    val expiresIn: Int,
    val hint: String,
    @SerializedName("is_new")
    val isNew: Boolean,
    val mid: Long,
    @SerializedName("refresh_token")
    val refreshToken: String,
    val sso: List<String>,
    @SerializedName("token_info")
    val tokenInfo: TokenInfo
)