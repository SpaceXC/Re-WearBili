package cn.spacexc.wearbili.remake.app.login.qrcode.tv.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import cn.spacexc.bilibilisdk.data.DataManager
import cn.spacexc.bilibilisdk.utils.UserUtils
import cn.spacexc.wearbili.common.domain.log.logd
import cn.spacexc.wearbili.common.domain.network.NetworkResponse
import cn.spacexc.wearbili.remake.app.login.qrcode.tv.domain.remote.tv.qrcode.NewQrCode
import cn.spacexc.wearbili.remake.app.login.qrcode.tv.domain.remote.tv.scanstate.NewQrCodeScanState
import cn.spacexc.wearbili.remake.app.login.qrcode.tv.domain.remote.tv.sso.WebSSOList
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.request.header
import io.ktor.http.Cookie
import io.ktor.http.Url
import io.ktor.util.date.GMTDate
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
class TVQrCodeLoginViewModel @Inject constructor(
    private val networkUtils: cn.spacexc.wearbili.common.domain.network.KtorNetworkUtils,
    private val qrCodeUtil: cn.spacexc.wearbili.common.domain.qrcode.QRCodeUtil,
    private val cookiesManager: cn.spacexc.wearbili.common.domain.network.cookie.KtorCookiesManager,
    private val dataManager: DataManager
) : ViewModel() {
    var screenState by mutableStateOf(
        TVQrCodeLoginScreenState(
            qrCodeBitmap = null,
            currentLoginStatus = TVQrCodeLoginStatus.Loading
        )
    )

    suspend fun startLogin(onFinishedLogin: () -> Unit) {
        screenState = screenState.copy(currentLoginStatus = TVQrCodeLoginStatus.Loading)
        //val qrCode = networkUtils.get<QrCode>("https://passport.bilibili.com/qrcode/getLoginUrl")
        val qrCode: NetworkResponse<NewQrCode> =
            networkUtils.postWithAppSign(
                host = "https://passport.bilibili.com/x/passport-tv-login/qrcode/auth_code",
                form = mapOf(
                    "ts" to System.currentTimeMillis().toString(),
                    "local_id" to (UserUtils.getBuvid() ?: "0"),
                    "device" to "phone",
                    "platform" to "android",
                    "mobi_app" to "android_hd"
                ),
                appKey = "dfca71928277209b",
                appSec = "b5475a8825547a4fc26c7d518eaaa02e"
            )
        if (qrCode.code != 0) {
            screenState = screenState.copy(currentLoginStatus = TVQrCodeLoginStatus.Failed)
            return
        }
        val oauthCode = qrCode.data?.data?.authCode
        val url = qrCode.data?.data?.url
        val bitmap = qrCodeUtil.createQRCodeBitmap(
            url, 128, 128,
            cn.spacexc.wearbili.common.domain.qrcode.ERROR_CORRECTION_M
        )
        screenState = screenState.copy(
            qrCodeBitmap = bitmap?.asImageBitmap(),
            currentLoginStatus = TVQrCodeLoginStatus.Pending
        )
        while (true) {
            /*val authState = networkUtils.post<ScanAuthState>(
                "https://passport.bilibili.com/qrcode/getLoginInfo?oauthKey=$oauthCode",
                form = mapOf()
            )*/
            val authState = networkUtils.postWithAppSign<NewQrCodeScanState>(
                "https://passport.bilibili.com/x/passport-tv-login/qrcode/poll",
                appKey = "dfca71928277209b",
                appSec = "b5475a8825547a4fc26c7d518eaaa02e",
                form = mapOf(
                    "auth_code" to (oauthCode ?: ""),
                    "local_id" to (UserUtils.getBuvid() ?: "0"),
                    "device" to "phone",
                    "ts" to System.currentTimeMillis().toString(),
                    "platform" to "android",
                    "mobi_app" to "android_hd"
                )
            )

            if (authState.code != 0) {
                screenState = screenState.copy(currentLoginStatus = TVQrCodeLoginStatus.Failed)
                return
            }
            authState.data.logd()
            if (authState.data?.data == null) {
                when (authState.data?.code) {
                    TVQrCodeLoginStatus.Pending.code -> {
                        screenState =
                            screenState.copy(currentLoginStatus = TVQrCodeLoginStatus.Pending)
                    }

                    TVQrCodeLoginStatus.Waiting.code -> {
                        screenState =
                            screenState.copy(currentLoginStatus = TVQrCodeLoginStatus.Waiting)
                    }

                    TVQrCodeLoginStatus.Timeout.code -> {
                        screenState =
                            screenState.copy(currentLoginStatus = TVQrCodeLoginStatus.Timeout)
                        break
                    }

                    else -> {}
                }
            } else {
                val accessKey = authState.data?.data?.accessToken
                dataManager.saveString("accessKey", accessKey ?: "")
                screenState = screenState.copy(currentLoginStatus = TVQrCodeLoginStatus.GettingKey)
                authState.data?.data?.cookieInfo?.cookies?.forEach {
                    //it.logd("cookie")
                    cookiesManager.addCookie(
                        Url(".bilibili.com"), Cookie(
                            name = it.name,
                            value = it.value,
                            httpOnly = it.httpOnly == 1,
                            secure = it.secure == 1,
                            expires = GMTDate(it.expires * 1000)
                        )
                    )
                    it.logd("saved cookie")
                }
                val sso = networkUtils.post<WebSSOList>(
                    "https://passport.bilibili.com/x/passport-login/web/sso/list", form = mapOf(
                        "csrf" to (authState.data?.data?.cookieInfo?.cookies?.find { it.name == "bili_jct" }?.value
                            ?: "")
                    )
                )
                //sso.toString()
                sso.data?.data?.sso.logd("sso")
                networkUtils.getWithAppSign<String>(
                    host = authState.data?.data?.sso?.first()
                        ?: "https://passport.bilibili.com/api/v2/sso",
                    origParams = "access_key=$accessKey&actionKey=appkey&platform=android&mobi_app=android_hd&ts=${System.currentTimeMillis()}",
                    appKey = "dfca71928277209b",
                    appSec = "b5475a8825547a4fc26c7d518eaaa02e"
                ) {
                    authState.data?.data?.cookieInfo?.cookies?.forEach {
                        header("cookie", "${it.name}=${it.value}")
                    }
                }
                onFinishedLogin()
                break
                //return
                /*screenState = screenState.copy(currentLoginStatus = TVQrCodeLoginStatus.GettingKey)
                val accessKeyResponse =
                    networkUtils.get<AccessKeyGetter>("https://passport.bilibili.com/login/app/third?appkey=27eb53fc9058f8c3&api=http://link.acg.tv/forum.php&sign=67ec798004373253d60114caaad89a8c")
                accessKeyResponse.data.logd("accessKeyResponse")
                if (accessKeyResponse.code != 0) {
                    screenState =
                        screenState.copy(currentLoginStatus = TVQrCodeLoginStatus.FailedGettingKey)
                    logd("请求不到confirm url")
                    break
                }
                val confirmUrl = accessKeyResponse.data?.data?.confirm_uri?.logd()
                if (confirmUrl.isNullOrEmpty()) {
                    screenState =
                        screenState.copy(currentLoginStatus = TVQrCodeLoginStatus.FailedGettingKey)
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
                        screenState.copy(currentLoginStatus = TVQrCodeLoginStatus.FailedGettingKey)
                    logd("没有重定向")
                    break
                }
                val redirectUrl = Uri.parse(redirectUrlString)
                val accessKey = redirectUrl.getQueryParameter("access_key")
                if (accessKey.isNullOrEmpty()) {
                    screenState =
                        screenState.copy(currentLoginStatus = TVQrCodeLoginStatus.FailedGettingKey)
                    break
                }
                screenState = screenState.copy(currentLoginStatus = TVQrCodeLoginStatus.Success)
                dataManager.saveString("accessKey", accessKey)
                onFinishedLogin()
                break*/
            }
            delay(1500)
        }
    }
}