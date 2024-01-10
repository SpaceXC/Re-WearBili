package cn.spacexc.wearbili.remake.ui.login.qrcode.tv.domain.remote.qrcode

data class QrCode(
    val code: Int,
    val `data`: Data,
    val status: Boolean,
    val ts: Int
)