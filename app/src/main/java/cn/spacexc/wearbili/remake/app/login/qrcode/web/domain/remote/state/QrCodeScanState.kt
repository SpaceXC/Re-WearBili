package cn.spacexc.wearbili.remake.app.login.qrcode.web.domain.remote.state


data class QrCodeScanState(
    val code: Int,
    val `data`: Data,
    val message: String,
    val ttl: Int
)