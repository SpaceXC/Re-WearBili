package cn.spacexc.wearbili.remake.app.main.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cn.spacexc.wearbili.remake.app.main.dynamic.ui.DynamicViewModel
import cn.spacexc.wearbili.remake.app.main.profile.ui.ProfileViewModel
import cn.spacexc.wearbili.remake.app.main.recommend.ui.RecommendViewModel
import cn.spacexc.wearbili.remake.app.settings.SettingsManager
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

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //profileViewModel.getProfile()
        setContent {
            val pagerState = rememberPagerState {
                3
            }
            val recommendSource by SettingsManager.recommendSource.collectAsState(initial = "app")
            LaunchedEffect(key1 = recommendSource, block = {
                recommendViewModel.getRecommendVideos(true, recommendSource)
            })
            MainActivityScreen(
                context = this,
                pagerState = pagerState,
                recommendScreenState = recommendViewModel.screenState,
                recommendSource = recommendSource,
                onRecommendRefresh = { isRefresh ->
                    recommendViewModel.getRecommendVideos(
                        isRefresh,
                        recommendSource
                    )
                },
                dynamicViewModel = dynamicViewModel,
                profileScreenState = profileViewModel.screenState,
                onProfileRetry = {
                    profileViewModel.getProfile()
                }
            )
        }
    }
}