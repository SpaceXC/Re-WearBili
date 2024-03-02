package cn.spacexc.wearbili.remake.app.splash.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import cn.spacexc.bilibilisdk.sdk.user.webi.WebiSignature
import cn.spacexc.bilibilisdk.utils.EncryptUtils
import cn.spacexc.wearbili.common.domain.data.DataStoreManager
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.login.qrcode.web.ui.QrCodeLoginActivity
import cn.spacexc.wearbili.remake.app.main.ui.MainActivity
import cn.spacexc.wearbili.remake.common.ui.GradientSlider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
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
/*@UnstableApi*/
class SplashScreenActivity : ComponentActivity() {
    @Inject
    lateinit var networkUtils: cn.spacexc.wearbili.common.domain.network.KtorNetworkUtils

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    @Inject
    lateinit var userManager: cn.spacexc.wearbili.common.domain.user.UserManager

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { granted ->
            if (granted.all { it.value }) {
                //initApp()
            }
            initApp()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplashScreen()
            //UIPreview()
        }
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.REQUEST_INSTALL_PACKAGES,
                Manifest.permission.INSTALL_PACKAGES,
            )
        )
        //initApp()
    }

    @Composable
    private fun UIPreview() {
        /*var value by remember {
            mutableFloatStateOf(0f)
        }
        GradientSlider(value = value, onValueChanged = {
            value = it
        }, range = 0f..10f)*/
        //ImageWithZoomAndPan(imageUrl = "http://i1.hdslb.com/bfs/archive/7d75542d12b40b96f21d4c691ed2355868a292b6.jpg")
        /*Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White), contentAlignment = Alignment.Center
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                DanmakuChip(commandDanmakuType = COMMAND_VOTE) {

                }
                DanmakuChip(commandDanmakuType = COMMAND_SUBSCRIBE) {

                }
                DanmakuChip(commandDanmakuType = COMMAND_VIDEO) {

                }
                DanmakuChip(commandDanmakuType = COMMAND_RATE) {

                }
            }

        }*/
        var sliderValue by remember {
            mutableFloatStateOf(0f)
        }
        GradientSlider(value = sliderValue, range = 0f..1f, modifier = Modifier.padding(8.dp)) {
            sliderValue = it
        }
    }

    private fun initApp() {
        lifecycleScope.launch {
            WebiSignature.getWebiSignature()    //保存新的webi签名
            if (dataStoreManager.getString("buvid").isNullOrEmpty()) {
                dataStoreManager.saveString("buvid", EncryptUtils.generateBuvid())
            }
            if (userManager.isUserLoggedIn()) {
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                finish()
                overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out)
            } else {
                startActivity(Intent(this@SplashScreenActivity, QrCodeLoginActivity::class.java))
                finish()
                overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out)
            }
            return@launch
            /*if (userManager.isUserLoggedIn()) {
                val query = LCQuery<LCObject>("ReActivatedUIDs")
                query.whereEqualTo("uid", userManager.mid().toString())
                query.findInBackground().subscribe({ result ->
                    val isUserActivated = !result.isNullOrEmpty()
                    if (isUserActivated) {
                        startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                        finish()
                        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out)
                    } else {
                        lifecycleScope.launch {
                            ToastUtils.showText("这里是内测版吖！然鹅你好像还木有内测资格捏...")
                            userManager.logout()
                            startActivity(
                                Intent(
                                    this@SplashScreenActivity,
                                    QrCodeLoginActivity::class.java
                                )
                            )
                            finish()
                            overridePendingTransition(
                                R.anim.activity_fade_in,
                                R.anim.activity_fade_out
                            )
                        }
                    }
                }, {
                    ToastUtils.showText("内测检查失败！${it.message}")
                })
            } else {
                ToastUtils.showText("你好像还没有登陆诶...")
                startActivity(Intent(this@SplashScreenActivity, QrCodeLoginActivity::class.java))
                finish()
                overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out)
            }*/
        }
    }

    @Parcelize
    data class LeanCloudAppUpdatesSearch(
        val results: List<AppUpdatesResult>?
    ) : Parcelable

    data class UserResult(
        val createdAt: String,
        val objectId: String,
        val uid: String,
        val addSource: String,
        val updatedAt: String
    )

    @Parcelize
    data class AppUpdatesResult(
        val createdAt: String,
        val objectId: String,
        val channel: String,
        val versionName: String,
        val versionCode: Int,
        val downloadAddress: String,
        val mandatory: Boolean,
        val updateLog: String,
        val updatedAt: String
    ) : Parcelable
}