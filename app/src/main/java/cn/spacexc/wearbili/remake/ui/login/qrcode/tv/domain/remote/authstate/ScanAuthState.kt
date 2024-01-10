package cn.spacexc.wearbili.remake.ui.login.qrcode.tv.domain.remote.authstate

data class ScanAuthState(
    val `data`: Any,    //登录成功前为int，成功后为object
    val message: String,
    val status: Boolean
)