package cn.spacexc.wearbili.remake.app.login.qrcode.web.domain.remote.qrcode

data class QrCodeKey(
    val code: Int,
    val `data`: Data,
    val message: String,
    val ttl: Int
)