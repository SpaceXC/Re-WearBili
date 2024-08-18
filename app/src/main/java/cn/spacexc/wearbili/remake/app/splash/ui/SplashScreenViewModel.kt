package cn.spacexc.wearbili.remake.app.splash.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import cn.spacexc.bilibilisdk.sdk.user.webi.WebiSignature
import cn.spacexc.bilibilisdk.utils.EncryptUtils
import cn.spacexc.bilibilisdk.utils.UserUtils
import cn.spacexc.wearbili.common.WearBiliResponse
import cn.spacexc.wearbili.common.domain.data.DataStoreManager
import cn.spacexc.wearbili.common.isZeroOrNull
import cn.spacexc.wearbili.remake.app.Application
import cn.spacexc.wearbili.remake.app.crash.ui.FEEDBACK_SERVER_BASE_URL
import cn.spacexc.wearbili.remake.app.login.qrcode.web.ui.QrCodeLoginScreen
import cn.spacexc.wearbili.remake.app.main.ui.HomeScreen
import cn.spacexc.wearbili.remake.app.settings.user.SwitchUserScreen
import cn.spacexc.wearbili.remake.app.splash.remote.Version
import cn.spacexc.wearbili.remake.app.update.ui.UpdateScreen
import cn.spacexc.wearbili.remake.common.ToastUtils
import cn.spacexc.wearbili.remake.common.networking.KtorNetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val networkUtils: KtorNetworkUtils,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    fun initApp(
        navController: NavController
    ) {
        viewModelScope.launch {
            val response =
                networkUtils.get<WearBiliResponse<Version>>("$FEEDBACK_SERVER_BASE_URL/version/latest")
            if (response.code != 0) {
                ToastUtils.showText("更新检查失败")
            }
            val latestVersion = response.data?.body?.versionCode ?: 0
            if (Application.getReleaseNumber() < latestVersion) {
                //TODO goto update
                navController.navigate(UpdateScreen(Json.encodeToString(response.data?.body)))
                return@launch
            }
            WebiSignature.getWebiSignature()    //保存新的webi签名
            if (dataStoreManager.getString("buvid").isNullOrEmpty()) {
                dataStoreManager.saveString("buvid", EncryptUtils.generateBuvid())
            }
            if (UserUtils.isUserLoggedIn()) {
                navController.navigate(HomeScreen(null)) {
                    popUpTo(0)
                }
            } else {
                println(UserUtils.mid())
                println(UserUtils.getUsers())
                if (UserUtils.mid().isZeroOrNull() && UserUtils.getUsers().isNotEmpty()) {
                    navController.navigate(SwitchUserScreen)
                } else {
                    navController.navigate(QrCodeLoginScreen)
                    navController.clearBackStack<QrCodeLoginScreen>()
                }
            }
        }
    }
}