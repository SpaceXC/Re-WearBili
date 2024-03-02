package cn.spacexc.wearbili.remake.app.login.qrcode.web.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import cn.spacexc.wearbili.remake.app.splash.ui.SplashScreenActivity
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Created by XC-Qan on 2023/4/1.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@AndroidEntryPoint
class QrCodeLoginActivity : ComponentActivity() {
    private val viewModel by viewModels<QrCodeLoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var loginJob = lifecycleScope.launch {
            viewModel.startLogin {
                startActivity(Intent(this@QrCodeLoginActivity, SplashScreenActivity::class.java))
                finish()
            }
        }
        setContent {
            TitleBackground(title = "登录", onBack = ::finish, onRetry = {}) {
                LoginScreen(viewModel.screenState) {
                    loginJob.cancel()
                    loginJob = lifecycleScope.launch {
                        viewModel.startLogin {
                            startActivity(
                                Intent(
                                    this@QrCodeLoginActivity,
                                    SplashScreenActivity::class.java
                                )
                            )
                            finish()
                        }
                    }
                }
            }
        }
    }
}