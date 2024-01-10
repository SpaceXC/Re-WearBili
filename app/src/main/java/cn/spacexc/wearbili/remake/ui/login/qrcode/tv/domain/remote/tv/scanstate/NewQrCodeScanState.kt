package cn.spacexc.wearbili.remake.ui.login.qrcode.tv.domain.remote.tv.scanstate


data class NewQrCodeScanState(
    val code: Int,
    val `data`: Data?,
    val message: String,
    val ttl: Int
)