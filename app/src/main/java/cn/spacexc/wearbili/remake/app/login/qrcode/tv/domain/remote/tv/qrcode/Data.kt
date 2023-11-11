package cn.spacexc.wearbili.remake.app.login.qrcode.tv.domain.remote.tv.qrcode


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("auth_code")
    val authCode: String,
    val url: String
)