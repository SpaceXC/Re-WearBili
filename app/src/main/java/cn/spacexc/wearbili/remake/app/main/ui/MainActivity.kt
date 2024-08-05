package cn.spacexc.wearbili.remake.app.main.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.paging.compose.collectAsLazyPagingItems
import cn.spacexc.bilibilisdk.utils.UserUtils
import cn.spacexc.wearbili.remake.app.main.dynamic.ui.DynamicViewModel
import cn.spacexc.wearbili.remake.app.main.profile.ui.ProfileViewModel
import cn.spacexc.wearbili.remake.app.main.recommend.ui.RecommendViewModel
import cn.spacexc.wearbili.remake.app.settings.SettingsManager
import cn.spacexc.wearbili.remake.app.splash.remote.Version
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by XC-Qan on 2023/4/6.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val recommendViewModel by viewModels<RecommendViewModel>()
    private val dynamicViewModel by viewModels<DynamicViewModel>()
    private val profileViewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //throw RuntimeException("这是一个用于测试异常的异常")
        profileViewModel.getProfile()
        val versionInfo = if (Build.VERSION.SDK_INT >= 33) intent.getParcelableExtra(
            "updateInfo",
            Version::class.java
        ) else intent.getParcelableExtra("updateInfo")
        setContent {
            val currentUid by UserUtils.midFlow().collectAsState(initial = 0)
            val dynamicData = dynamicViewModel.dynamicFlow.collectAsLazyPagingItems()
            LaunchedEffect(key1 = currentUid) {
                if(currentUid != 0L && currentUid != null) {
                    profileViewModel.getProfile()
                    recommendViewModel.getRecommendVideos(true, SettingsManager.getConfiguration().recommendSource)
                    dynamicData.refresh()
                }
            }
            MainActivityScreen(
                context = this,
                recommendViewModel = recommendViewModel,
                dynamicViewModel = dynamicViewModel,
                profileScreenState = profileViewModel.screenState,
                updateInfo = versionInfo,
                onProfileRetry = {
                    profileViewModel.getProfile()
                }
            )
        }
    }
}