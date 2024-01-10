package cn.spacexc.wearbili.remake.ui.login.qrcode.tv.domain.remote.tv.scanstate


import com.google.gson.annotations.SerializedName

data class Cooky(
    val expires: Long,
    @SerializedName("http_only")
    val httpOnly: Int,
    val name: String,
    val secure: Int,
    val value: String
)