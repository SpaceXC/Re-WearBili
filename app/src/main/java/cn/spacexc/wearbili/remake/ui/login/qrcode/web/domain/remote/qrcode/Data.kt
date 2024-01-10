package cn.spacexc.wearbili.remake.ui.login.qrcode.web.domain.remote.qrcode


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("qrcode_key")
    val qrcodeKey: String,
    val url: String
)