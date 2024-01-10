package cn.spacexc.wearbili.remake.app.link

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import cn.spacexc.wearbili.common.domain.network.KtorNetworkUtils
import cn.spacexc.wearbili.common.domain.video.VideoUtils
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID_TYPE
import cn.spacexc.wearbili.remake.app.video.info.ui.VIDEO_TYPE_BVID
import cn.spacexc.wearbili.remake.app.video.info.ui.VideoInformationActivity
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by XC-Qan on 2023/10/29.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class LinkProcessActivity : ComponentActivity() {
    @Inject
    lateinit var ktorNetworkUtils: KtorNetworkUtils

    val state = MutableLiveData(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun processUrl() {
        lifecycleScope.launch {
            val uri = getUrl()
            if (VideoUtils.isBV(uri.path?.replace("/", "") ?: "")) {
                state.value = 1
                startActivity(
                    Intent(
                        this@LinkProcessActivity,
                        VideoInformationActivity::class.java
                    ).apply {
                        putExtra(PARAM_VIDEO_ID, uri.path?.replace("/", ""))
                        putExtra(PARAM_VIDEO_ID_TYPE, VIDEO_TYPE_BVID)
                    })
                finish()
            } else if (uri.host != "b23.tv") {
                state.value = 2
            } else {
                val redirectUrl = ktorNetworkUtils.getRedirectUrl(uri.toString())
                if (redirectUrl != null) {
                    val redirectUri = Uri.parse(redirectUrl)
                    if (VideoUtils.isBV(redirectUri.path?.replace("/video/", "") ?: "")) {
                        state.value = 1
                        startActivity(
                            Intent(
                                this@LinkProcessActivity,
                                VideoInformationActivity::class.java
                            ).apply {
                                putExtra(PARAM_VIDEO_ID, redirectUri.path?.replace("/video/", ""))
                                putExtra(PARAM_VIDEO_ID_TYPE, VIDEO_TYPE_BVID)
                            })
                        finish()
                    } else {
                        state.value = 2
                    }
                }
            }
        }
    }

    private fun getUrl(): Uri {
        return intent.data ?: Uri.parse(intent.getStringExtra("url"))
    }
}