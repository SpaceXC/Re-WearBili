package cn.spacexc.wearbili.remake.ui.login.qrcode.tv.domain.remote.tv.qrcode


data class NewQrCode(
    val code: Int,
    val `data`: Data,
    val message: String,
    val ttl: Int
)