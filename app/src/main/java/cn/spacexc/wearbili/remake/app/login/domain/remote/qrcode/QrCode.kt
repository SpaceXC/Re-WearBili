package cn.spacexc.wearbili.remake.app.login.domain.remote.qrcode

data class QrCode(
    val code: Int,
    val `data`: Data,
    val status: Boolean,
    val ts: Int
)