package cn.spacexc.wearbili.remake.ui.login.qrcode.web.domain.remote.state


data class QrCodeScanState(
    val code: Int,
    val `data`: Data,
    val message: String,
    val ttl: Int
)