package cn.spacexc.wearbili.remake.app.login.qrcode.tv.ui

import androidx.compose.ui.graphics.ImageBitmap

/**
 * Created by XC-Qan on 2023/4/4.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

sealed class TVQrCodeLoginStatus(val code: Int) {
    data object Loading : TVQrCodeLoginStatus(1)
    data object Success : TVQrCodeLoginStatus(0)
    data object Pending : TVQrCodeLoginStatus(86039)  //用户没有扫码
    data object Waiting : TVQrCodeLoginStatus(86090)  //用户扫了码但是还没有确认
    data object Timeout : TVQrCodeLoginStatus(86038)
    data object Failed : TVQrCodeLoginStatus(2)
    data object GettingKey : TVQrCodeLoginStatus(3)
    data object FailedGettingKey : TVQrCodeLoginStatus(4)
}

data class TVQrCodeLoginScreenState(
    val qrCodeBitmap: ImageBitmap?,
    val currentLoginStatus: TVQrCodeLoginStatus
)
