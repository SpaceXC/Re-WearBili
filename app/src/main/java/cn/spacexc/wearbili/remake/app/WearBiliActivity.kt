package cn.spacexc.wearbili.remake.app

//import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import cn.spacexc.bilibilisdk.sdk.user.webi.WebiSignature
import cn.spacexc.bilibilisdk.utils.EncryptUtils
import cn.spacexc.wearbili.common.domain.data.DataStoreManager
import cn.spacexc.wearbili.remake.common.ToastUtils
import cn.spacexc.wearbili.remake.ui.Application
import cn.spacexc.wearbili.remake.ui.bangumi.index.ui.bangumiIndex
import cn.spacexc.wearbili.remake.ui.bangumi.index.ui.navigateToBangumiIndex
import cn.spacexc.wearbili.remake.ui.bangumi.info.ui.bangumiScreen
import cn.spacexc.wearbili.remake.ui.bangumi.info.ui.navigateToBangumiDetail
import cn.spacexc.wearbili.remake.ui.bangumi.timeline.ui.bangumiTimeline
import cn.spacexc.wearbili.remake.ui.bangumi.timeline.ui.navigateToBangumiTimeline
import cn.spacexc.wearbili.remake.ui.main.profile.detail.favorite.detail.ui.favouriteFolderDetailScreen
import cn.spacexc.wearbili.remake.ui.main.profile.detail.favorite.detail.ui.navigateToFavouriteFolder
import cn.spacexc.wearbili.remake.ui.main.profile.detail.favorite.folders.ui.favouriteFolders
import cn.spacexc.wearbili.remake.ui.main.profile.detail.favorite.folders.ui.navigateToFavouriteFolders
import cn.spacexc.wearbili.remake.ui.main.profile.detail.following.ui.followingUsers
import cn.spacexc.wearbili.remake.ui.main.profile.detail.following.ui.navigateToFollowingUsers
import cn.spacexc.wearbili.remake.ui.main.profile.detail.history.ui.history
import cn.spacexc.wearbili.remake.ui.main.profile.detail.history.ui.navigateToHistory
import cn.spacexc.wearbili.remake.ui.main.profile.detail.watchlater.ui.navigateToWatchLater
import cn.spacexc.wearbili.remake.ui.main.profile.detail.watchlater.ui.watchLater
import cn.spacexc.wearbili.remake.ui.main.ui.mainScreen
import cn.spacexc.wearbili.remake.ui.main.ui.navigateToMain
import cn.spacexc.wearbili.remake.ui.search.ui.navigateToSearch
import cn.spacexc.wearbili.remake.ui.search.ui.navigateToSearchResult
import cn.spacexc.wearbili.remake.ui.search.ui.searchResult
import cn.spacexc.wearbili.remake.ui.search.ui.searchScreen
import cn.spacexc.wearbili.remake.ui.season.ui.navigateToSeason
import cn.spacexc.wearbili.remake.ui.season.ui.seasonScreen
import cn.spacexc.wearbili.remake.ui.splash.ui.SplashScreenRoute
import cn.spacexc.wearbili.remake.ui.splash.ui.splashScreen
import cn.spacexc.wearbili.remake.ui.video.info.ui.navigateToVideoInformationScreen
import cn.spacexc.wearbili.remake.ui.video.info.ui.videoInformation
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.request.header
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WearBiliActivity : ComponentActivity() {
    @Inject
    lateinit var networkUtils: cn.spacexc.wearbili.common.domain.network.KtorNetworkUtils

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    @Inject
    lateinit var userManager: cn.spacexc.wearbili.common.domain.user.UserManager

    enum class InitializationState {
        Success,
        NeedLogin,
        UpdateAvailable
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberSwipeDismissableNavController()
            SwipeDismissableNavHost(
                navController = navController,
                startDestination = SplashScreenRoute,
            ) {
                mainScreen(
                    onJumpToFavourite = navController::navigateToFavouriteFolders,
                    onJumpToWatchLater = navController::navigateToWatchLater,
                    onJumpToSettings = {},
                    onJumpToHistory = navController::navigateToHistory,
                    onJumpToFollowed = navController::navigateToFollowingUsers,
                    onJumpToCache = {},
                    onJumpToAbout = {},
                    onJumpToBangumiIndex = navController::navigateToBangumiIndex,
                    onJumpToImage = { images, index -> },
                    onJumpToVideo = navController::navigateToVideoInformationScreen,
                    onJumpToSearch = navController::navigateToSearch,
                    onJumpToBangumiDetail = navController::navigateToBangumiDetail
                )

                splashScreen {
                    initApp { state ->
                        when (state) {
                            InitializationState.Success -> navController.navigateToMain()
                            InitializationState.NeedLogin -> Unit
                            InitializationState.UpdateAvailable -> Unit
                        }
                    }
                }

                favouriteFolderDetailScreen(
                    onBack = navController::navigateUp,
                    onJumpToVideo = navController::navigateToVideoInformationScreen
                )
                favouriteFolders(
                    onBack = navController::navigateUp,
                    onJumpToFolder = navController::navigateToFavouriteFolder
                )
                followingUsers(onBack = navController::navigateUp)
                history(
                    onBack = navController::navigateUp,
                    onJumpToVideo = navController::navigateToVideoInformationScreen
                )
                watchLater(
                    onBack = navController::navigateUp,
                    onJumpToVideo = navController::navigateToVideoInformationScreen
                )
                seasonScreen(
                    onBack = navController::navigateUp,
                    onJumpToVideo = navController::navigateToVideoInformationScreen
                )
                bangumiIndex(
                    onBack = navController::navigateUp,
                    onJumpToBangumiDetail = navController::navigateToBangumiDetail,
                    onJumpToBangumiTimeline = navController::navigateToBangumiTimeline
                )
                bangumiTimeline(
                    onBack = navController::navigateUp,
                    onJumpToBangumiDetail = navController::navigateToBangumiDetail
                )
                searchScreen(
                    onBack = navController::navigateUp,
                    onJumpToResult = navController::navigateToSearchResult,
                    onJumpToVideo = navController::navigateToVideoInformationScreen
                )
                searchResult(
                    onBack = navController::navigateUp,
                    onJumpToVideo = navController::navigateToVideoInformationScreen,
                    onJumpToBangumiDetail = navController::navigateToBangumiDetail
                )
                videoInformation(
                    onBack = navController::navigateUp,
                    onJumpToVideo = navController::navigateToVideoInformationScreen,
                    onJumpToBangumiDetail = { idType, id ->
                        navController.navigateUp()
                        navController.navigateToBangumiDetail(idType, id)
                    },
                    onJumpToSearch = navController::navigateToSearch,
                    onJumpToImage = { urls, index -> },
                    onJumpToSeason = navController::navigateToSeason
                )
                bangumiScreen(
                    onBack = navController::navigateUp,
                    onJumpToVideo = navController::navigateToVideoInformationScreen,
                    onJumpToSearch = navController::navigateToSearch,
                    onJumpToImage = { urls, index -> }
                )
            }
        }
    }

    private fun initApp(onInitFinished: (InitializationState) -> Unit) {
        /*lifecycleScope.launch {
            val danmaku = DanmakuGetter(networkUtils).getDanmaku(197407064, 1)
            Log.d(TAG, "initApp: ${danmaku.elemsList}")
        }
        return*/
        /*lifecycleScope.launch {
            if (!UserUtils.isUserLoggedIn()) {
                ToastUtils.showText("你好像还没有登陆诶...")
                onInitFinished(InitializationState.NeedLogin)
                return@launch
            } else {
                onInitFinished(InitializationState.Success)
                return@launch
            }
        }*/


        val currentTime = System.currentTimeMillis()    //后面leancloud签名用到，别删
        lifecycleScope.launch {
            //VideoInfo.getVideoInfoApp(VIDEO_TYPE_AID, "954781099").logd()
            networkUtils.get<String>("https://bilibili.com")    // 每次启动获取最新的cookie
            WebiSignature.getWebiSignature()    //保存新的webi签名

            if (dataStoreManager.getString("buvid").isNullOrEmpty()) {
                dataStoreManager.saveString("buvid", EncryptUtils.generateBuvid())
            }

            val appUpdatesResponse =
                networkUtils.get<LeanCloudAppUpdatesSearch>("https://mae7lops.lc-cn-n1-shared.com/1.1/classes/AppUpdates") {
                    header("X-LC-Id", cn.spacexc.wearbili.common.LEANCLOUD_APP_ID)
                    header(
                        "X-LC-Sign",
                        "${cn.spacexc.wearbili.common.EncryptUtils.md5("$currentTime${cn.spacexc.wearbili.common.LEANCLOUD_APP_KEY}")},$currentTime"
                    )
                }
            appUpdatesResponse.data?.results?.let { updateLists ->
                updateLists.firstOrNull { it.versionCode.toLong() > Application.getVersionCode() }
                    ?.let { version ->
                        val latestSkippedVersion =
                            dataStoreManager.getInt("latestSkippedVersion") ?: 0
                        if (version.versionCode > latestSkippedVersion) {
                            onInitFinished(InitializationState.UpdateAvailable)
                            return@launch
                        }
                    }
            }
            if (userManager.isUserLoggedIn()) {
                val response = userManager.mid()?.let { uid ->
                    networkUtils.get<LeanCloudUserSearch>("https://mae7lops.lc-cn-n1-shared.com/1.1/classes/ReActivatedUIDs?where={\"uid\":\"$uid\"}") {
                        header("X-LC-Id", cn.spacexc.wearbili.common.LEANCLOUD_APP_ID)
                        header(
                            "X-LC-Sign",
                            "${cn.spacexc.wearbili.common.EncryptUtils.md5("$currentTime${cn.spacexc.wearbili.common.LEANCLOUD_APP_KEY}")},$currentTime"
                        )
                    }
                }
                response?.data?.results?.let {
                    if (it.isNotEmpty()) {
                        onInitFinished(InitializationState.Success)
                    } else {
                        ToastUtils.showText("这里是内测版吖！然鹅你好像还木有内测资格捏...")
                        userManager.logout()
                        onInitFinished(InitializationState.NeedLogin)
                    }
                    return@launch
                }
                ToastUtils.showText("刚刚的内测检查失败噜！可以重新登陆再来一次嘛？（如果你已经登录了账号，也可以直接重启应用的说！）")
                onInitFinished(InitializationState.NeedLogin)
            } else {
                ToastUtils.showText("你好像还没有登陆诶...")
                onInitFinished(InitializationState.NeedLogin)
            }
        }
    }


}