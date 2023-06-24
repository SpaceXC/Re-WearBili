package cn.spacexc.wearbili.remake.app.login.ui

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import cn.spacexc.bilibilisdk.data.DataManager
import cn.spacexc.wearbili.common.domain.log.logd
import cn.spacexc.wearbili.remake.app.login.domain.remote.accesskey.AccessKeyGetter
import cn.spacexc.wearbili.remake.app.login.domain.remote.authstate.ScanAuthState
import cn.spacexc.wearbili.remake.app.login.domain.remote.qrcode.QrCode
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.request.get
import io.ktor.client.statement.request
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * Created by XC-Qan on 2023/4/2.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val networkUtils: cn.spacexc.wearbili.common.domain.network.KtorNetworkUtils,
    private val qrCodeUtil: cn.spacexc.wearbili.common.domain.qrcode.QRCodeUtil,
    private val cookiesManager: cn.spacexc.wearbili.common.domain.network.cookie.KtorCookiesManager,
    private val dataManager: DataManager
) : ViewModel() {
    var screenState by mutableStateOf(
        LoginScreenState(
            qrCodeBitmap = null,
            currentLoginStatus = LoginStatus.Loading
        )
    )

    suspend fun startLogin(onFinishedLogin: () -> Unit) {
        screenState = screenState.copy(currentLoginStatus = LoginStatus.Loading)
        val qrCode = networkUtils.get<QrCode>("https://passport.bilibili.com/qrcode/getLoginUrl")
        if (qrCode.code != 0) {
            screenState = screenState.copy(currentLoginStatus = LoginStatus.Failed)
            return
        }
        val oauthCode = qrCode.data?.data?.oauthKey.logd()
        val url = qrCode.data?.data?.url
        val bitmap = qrCodeUtil.createQRCodeBitmap(
            url, 128, 128,
            cn.spacexc.wearbili.common.domain.qrcode.ERROR_CORRECTION_M
        )
        screenState = screenState.copy(
            qrCodeBitmap = bitmap?.asImageBitmap(),
            currentLoginStatus = LoginStatus.Pending
        )
        while (true) {
            val authState = networkUtils.post<ScanAuthState>(
                "https://passport.bilibili.com/qrcode/getLoginInfo?oauthKey=$oauthCode",
                form = mapOf()
            )
            if (authState.code != 0) {
                screenState = screenState.copy(currentLoginStatus = LoginStatus.Failed)
                return
            }
            authState.data.logd()
            if (authState.data?.status != true) {
                when (authState.data?.data) {
                    -4.0 -> {
                        screenState = screenState.copy(currentLoginStatus = LoginStatus.Pending)
                    }
                    -5.0 -> {
                        screenState = screenState.copy(currentLoginStatus = LoginStatus.Waiting)
                    }
                    -2.0 -> {
                        screenState = screenState.copy(currentLoginStatus = LoginStatus.Timeout)
                        break
                    }
                    else -> {}
                }
            } else {
                screenState = screenState.copy(currentLoginStatus = LoginStatus.GettingKey)
                val accessKeyResponse =
                    networkUtils.get<AccessKeyGetter>("https://passport.bilibili.com/login/app/third?appkey=27eb53fc9058f8c3&api=http://link.acg.tv/forum.php&sign=67ec798004373253d60114caaad89a8c")
                accessKeyResponse.data.logd("accessKeyResponse")
                if (accessKeyResponse.code != 0) {
                    screenState =
                        screenState.copy(currentLoginStatus = LoginStatus.FailedGettingKey)
                    logd("请求不到confirm url")
                    break
                }
                val confirmUrl = accessKeyResponse.data?.data?.confirm_uri?.logd()
                if (confirmUrl.isNullOrEmpty()) {
                    screenState =
                        screenState.copy(currentLoginStatus = LoginStatus.FailedGettingKey)
                    logd("请求不到confirm url")
                    break
                }
                val clientWithoutDirect = HttpClient(CIO) {
                    followRedirects = false
                    install(HttpCookies) {
                        storage = cookiesManager
                    }
                }
                val accessKeyConfirmUrlResponse = clientWithoutDirect.get(confirmUrl)
                accessKeyConfirmUrlResponse.request.headers.logd("request header")
                val redirectUrlString = accessKeyConfirmUrlResponse.headers["Location"]
                if (redirectUrlString.isNullOrEmpty()) {
                    screenState =
                        screenState.copy(currentLoginStatus = LoginStatus.FailedGettingKey)
                    logd("没有重定向")
                    break
                }
                val redirectUrl = Uri.parse(redirectUrlString)
                val accessKey = redirectUrl.getQueryParameter("access_key")
                if (accessKey.isNullOrEmpty()) {
                    screenState =
                        screenState.copy(currentLoginStatus = LoginStatus.FailedGettingKey)
                    break
                }
                screenState = screenState.copy(currentLoginStatus = LoginStatus.Success)
                dataManager.saveString("accessKey", accessKey)
                onFinishedLogin()
                break
            }
            delay(1500)
        }
    }
}