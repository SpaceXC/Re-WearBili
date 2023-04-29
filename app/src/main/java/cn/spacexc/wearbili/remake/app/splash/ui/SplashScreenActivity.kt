package cn.spacexc.wearbili.remake.app.splash.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import cn.spacexc.wearbili.remake.LEANCLOUD_APP_ID
import cn.spacexc.wearbili.remake.LEANCLOUD_APP_KEY
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.login.ui.LoginActivity
import cn.spacexc.wearbili.remake.app.main.ui.MainActivity
import cn.spacexc.wearbili.remake.common.EncryptUtils
import cn.spacexc.wearbili.remake.common.ToastUtils
import cn.spacexc.wearbili.remake.common.domain.data.DataManager
import cn.spacexc.wearbili.remake.common.domain.log.logd
import cn.spacexc.wearbili.remake.common.domain.manager.UserManager
import cn.spacexc.wearbili.remake.common.domain.network.KtorNetworkUtils
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.request.header
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by XC-Qan on 2023/3/21.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity : ComponentActivity() {
    @Inject
    lateinit var networkUtils: KtorNetworkUtils

    @Inject
    lateinit var dataManager: DataManager

    @Inject
    lateinit var userManager: UserManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            networkUtils.get<String>("https://bilibili.com")    // 每次启动获取最新的cookie
            logd(networkUtils.getCookies())
            networkUtils.getCookie("buvid3").logd()
            if (userManager.isUserLoggedIn()) {
                val response = userManager.userMid()?.let { uid ->
                    val currentTime = System.currentTimeMillis()
                    //https://mae7lops.lc-cn-n1-shared.com/1.1/classes/ActivatedUIDs?where={\"uid\":\"$uid\"}
                    networkUtils.get<LeanCloudUserSearch>("https://mae7lops.lc-cn-n1-shared.com/1.1/classes/ActivatedUIDs?where={\"uid\":\"$uid\"}") {
                        header("X-LC-Id", LEANCLOUD_APP_ID)
                        header(
                            "X-LC-Sign",
                            "${EncryptUtils.md5("$currentTime$LEANCLOUD_APP_KEY")},$currentTime"
                        )
                    }
                }
                response?.data?.results?.let {
                    if (it.isNotEmpty()) {
                        startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                        finish()
                        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out)
                    } else {
                        ToastUtils.showText("现暂未开放内测申请, 请耐心等待正式版")
                        startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                        finish()
                        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out)
                    }
                    return@launch
                }
                startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                finish()
                overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out)
            } else {
                startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                finish()
                overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out)
            }
        }
        setContent {
            SplashScreen()
        }
    }

    data class LeanCloudUserSearch(
        val results: List<Result>?
    )

    data class Result(
        val createdAt: String,
        val objectId: String,
        val uid: String,
        val addSource: String,
        val updatedAt: String
    )
}