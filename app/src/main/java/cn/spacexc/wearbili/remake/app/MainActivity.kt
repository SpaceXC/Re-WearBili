package cn.spacexc.wearbili.remake.app

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.paging.compose.collectAsLazyPagingItems
import cn.spacexc.wearbili.remake.app.about.ui.AboutScreen
import cn.spacexc.wearbili.remake.app.article.ui.ArticleScreen
import cn.spacexc.wearbili.remake.app.bangumi.index.ui.BangumiIndexScreen
import cn.spacexc.wearbili.remake.app.bangumi.info.episodes.ui.BangumiEpisodeListScreen
import cn.spacexc.wearbili.remake.app.bangumi.info.ui.BangumiScreen
import cn.spacexc.wearbili.remake.app.bangumi.timeline.ui.BangumiTimelineScreen
import cn.spacexc.wearbili.remake.app.cache.create.ui.CreateNewCacheScreen
import cn.spacexc.wearbili.remake.app.cache.list.CacheListScreen
import cn.spacexc.wearbili.remake.app.feedback.ui.issues.AllIssuesScreen
import cn.spacexc.wearbili.remake.app.image.ImageViewerScreen
import cn.spacexc.wearbili.remake.app.link.qrcode.QrCodeScreen
import cn.spacexc.wearbili.remake.app.login.qrcode.web.ui.LoginScreen
import cn.spacexc.wearbili.remake.app.login.qrcode.web.ui.QrCodeLoginScreen
import cn.spacexc.wearbili.remake.app.main.profile.detail.favorite.detail.ui.FavouriteFolderDetailScreen
import cn.spacexc.wearbili.remake.app.main.profile.detail.favorite.folders.ui.FavoriteFoldersScreen
import cn.spacexc.wearbili.remake.app.main.profile.detail.following.ui.FollowingUsersScreen
import cn.spacexc.wearbili.remake.app.main.profile.detail.following.ui.FollowingUsersViewModel
import cn.spacexc.wearbili.remake.app.main.profile.detail.history.ui.HistoryScreen
import cn.spacexc.wearbili.remake.app.main.profile.detail.watchlater.ui.WatchLaterScreen
import cn.spacexc.wearbili.remake.app.main.ui.HomeScreen
import cn.spacexc.wearbili.remake.app.message.at.ui.AtMessageScreen
import cn.spacexc.wearbili.remake.app.message.direct.history.ui.DirectMessageScreen
import cn.spacexc.wearbili.remake.app.message.like.ui.LikeMessagesScreen
import cn.spacexc.wearbili.remake.app.message.reply.ui.ReplyMessageScreen
import cn.spacexc.wearbili.remake.app.message.system.ui.SystemNotificationScreen
import cn.spacexc.wearbili.remake.app.message.system.ui.SystemNotificationsListScreen
import cn.spacexc.wearbili.remake.app.message.ui.MessageScreen
import cn.spacexc.wearbili.remake.app.player.audio.AudioPlayerService
import cn.spacexc.wearbili.remake.app.player.audio.ui.AudioPlayerScreen
import cn.spacexc.wearbili.remake.app.player.cast.discover.DeviceDiscoverScreen
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.IjkVideoPlayerScreen
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.IjkVideoPlayerViewModel
import cn.spacexc.wearbili.remake.app.search.ui.SearchResultScreen
import cn.spacexc.wearbili.remake.app.search.ui.SearchScreen
import cn.spacexc.wearbili.remake.app.season.ui.SeasonScreen
import cn.spacexc.wearbili.remake.app.settings.LocalConfiguration
import cn.spacexc.wearbili.remake.app.settings.ProvideConfiguration
import cn.spacexc.wearbili.remake.app.settings.experimantal.ExperimentalFunctionsScreen
import cn.spacexc.wearbili.remake.app.settings.scaling.ScaleAdjustingScreen
import cn.spacexc.wearbili.remake.app.settings.toolbar.ui.QuickToolbarCustomizationScreen
import cn.spacexc.wearbili.remake.app.settings.ui.SettingsScreen
import cn.spacexc.wearbili.remake.app.settings.user.SwitchUserScreen
import cn.spacexc.wearbili.remake.app.space.ui.UserSpaceScreen
import cn.spacexc.wearbili.remake.app.splash.remote.Version
import cn.spacexc.wearbili.remake.app.splash.remote.parcelableType
import cn.spacexc.wearbili.remake.app.splash.ui.SplashScreen
import cn.spacexc.wearbili.remake.app.update.ui.UpdateScreen
import cn.spacexc.wearbili.remake.app.video.action.coin.ui.CoinScreen
import cn.spacexc.wearbili.remake.app.video.action.favourite.ui.VideoFavouriteFoldersScreen
import cn.spacexc.wearbili.remake.app.video.info.comment.detail.ui.CommentRepliesDetailScreen
import cn.spacexc.wearbili.remake.app.video.info.ui.VideoInformationScreen
import cn.spacexc.wearbili.remake.app.welcome.screens.StartScreen
import cn.spacexc.wearbili.remake.common.ui.theme.ProvideLocalDensity
import cn.spacexc.wearbili.remake.common.ui.theme.WearBiliTheme
import cn.spacexc.wearbili.remake.common.ui.wearBiliAnimateDpAsState
import cn.spacexc.wearbili.remake.common.ui.wearBiliAnimateFloatAsState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

var isRotated by mutableStateOf(false)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var ijkVideoPlayerViewModel: IjkVideoPlayerViewModel? = null
    var audioPlayerService: AudioPlayerService? = null

    @OptIn(ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProvideConfiguration {
                WearBiliTheme {
                    var screenSize by remember {
                        mutableStateOf(DpSize(1.dp, 1.dp))
                    }
                    val density = LocalDensity.current
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .onSizeChanged {
                            screenSize = with(density) {
                                it
                                    .toSize()
                                    .toDpSize()
                            }
                        }
                    ) {
                        val rotation by wearBiliAnimateFloatAsState(targetValue = if (isRotated) 90f else 0f)
                        val height by wearBiliAnimateDpAsState(targetValue = if (isRotated) screenSize.width else screenSize.height)
                        val width by wearBiliAnimateDpAsState(targetValue = if (isRotated) screenSize.height else screenSize.width)
                        Box(
                            modifier = Modifier
                                .requiredSizeIn(
                                    maxWidth = screenSize.height,
                                    maxHeight = screenSize.height
                                )
                                .rotate(rotation)
                                .size(width, height)
                                .align(Alignment.Center)
                        ) {
                            val navController = rememberNavController()
                            val configuration = LocalConfiguration.current
                            SharedTransitionLayout {
                                NavHost(navController = navController,
                                    startDestination = if (configuration.isInitialized) SplashScreen else SplashScreen,
                                    enterTransition = {
                                        slideInHorizontally(
                                            tween(
                                                400,
                                                0
                                            )
                                        ) { it } + fadeIn(tween(400))
                                    },
                                    exitTransition = {
                                        try {
                                            targetState.toRoute<IjkVideoPlayerScreen>()
                                            fadeOut()
                                        } catch (_: Exception) {
                                            slideOutHorizontally(
                                                tween(
                                                    400,
                                                    150
                                                )
                                            ) { -it } + fadeOut(
                                                tween(
                                                    400
                                                )
                                            )
                                        }
                                    },
                                    popEnterTransition = {
                                        try {
                                            initialState.toRoute<ImageViewerScreen>()
                                            fadeIn()
                                        } catch (_: Exception) {
                                            try {
                                                initialState.toRoute<IjkVideoPlayerScreen>()
                                                fadeIn()
                                            } catch (_: Exception) {
                                                slideInHorizontally(
                                                    tween(
                                                        400,
                                                        50
                                                    )
                                                ) { -it } + fadeIn(
                                                    tween(
                                                        400
                                                    )
                                                )
                                            }
                                        }
                                    },
                                    popExitTransition = {
                                        slideOutHorizontally(tween(300, 0)) { it } + fadeOut(
                                            tween(300)
                                        )
                                    }
                                ) {
                                    composable<StartScreen> {
                                        StartScreen(navController = navController)
                                    }
                                    composable<SplashScreen>(
                                        typeMap = mapOf(typeOf<Version?>() to parcelableType<Version?>()),
                                        enterTransition = {
                                            fadeIn()
                                        }) {
                                        SplashScreen(navController)
                                    }
                                    composable<HomeScreen>(typeMap = mapOf(typeOf<Version?>() to parcelableType<Version?>())) {
                                        val (version) = it.toRoute<HomeScreen>()
                                        HomeScreen(
                                            updateInfo = version?.let {
                                                Json.decodeFromString(
                                                    version
                                                )
                                            },
                                            navController = navController,
                                            animatedVisibilityScope = this
                                        )
                                    }
                                    composable<VideoInformationScreen> {
                                        val (videoIdType, videoId) = it.toRoute<VideoInformationScreen>()
                                        ijkVideoPlayerViewModel = hiltViewModel()
                                        VideoInformationScreen(
                                            videoId = videoId,
                                            videoIdType = videoIdType,
                                            navController = navController,
                                            animatedVisibilityScope = this,
                                            ijkVideoPlayerViewModel = ijkVideoPlayerViewModel!!
                                        )
                                    }
                                    composable<AboutScreen> {
                                        AboutScreen(navController = navController)
                                    }
                                    composable<UpdateScreen>(typeMap = mapOf(typeOf<Version?>() to parcelableType<Version?>())) {
                                        val (versionInfo) = it.toRoute<UpdateScreen>()
                                        UpdateScreen(
                                            versionInfo = Json.decodeFromString(versionInfo),
                                            navController = navController
                                        )
                                    }
                                    composable<SearchScreen> {
                                        val (default) = it.toRoute<SearchScreen>()
                                        SearchScreen(
                                            navController = navController,
                                            defaultSearchKeyword = default
                                        )
                                    }
                                    composable<SearchResultScreen> {
                                        val (keyword) = it.toRoute<SearchResultScreen>()
                                        SearchResultScreen(
                                            navController = navController,
                                            keyword = keyword
                                        )
                                    }
                                    composable<FollowingUsersScreen> {
                                        val viewModel: FollowingUsersViewModel = viewModel()
                                        FollowingUsersScreen(
                                            navController = navController,
                                            viewModel = viewModel,
                                            lazyPagingItems = viewModel.followedUsers.map { it.value.collectAsLazyPagingItems() })
                                    }
                                    composable<HistoryScreen> {
                                        HistoryScreen(navController = navController)
                                    }
                                    composable<WatchLaterScreen> {
                                        WatchLaterScreen(navController = navController)
                                    }
                                    composable<FavoriteFoldersScreen> {
                                        FavoriteFoldersScreen(navController = navController)
                                    }
                                    composable<FavouriteFolderDetailScreen> {
                                        val (folderId, folderName) = it.toRoute<FavouriteFolderDetailScreen>()
                                        FavouriteFolderDetailScreen(
                                            folderId = folderId,
                                            folderName = folderName,
                                            navController = navController
                                        )
                                    }
                                    composable<IjkVideoPlayerScreen>(
                                        enterTransition = { fadeIn() },
                                        exitTransition = { fadeOut() }
                                    ) {
                                        val (isCache, videoIdType, videoId, videoCid, isBangumi) = it.toRoute<IjkVideoPlayerScreen>()
                                        if (ijkVideoPlayerViewModel == null) {
                                            ijkVideoPlayerViewModel = hiltViewModel()
                                        }
                                        ProvideConfiguration {
                                            ProvideLocalDensity {
                                                IjkVideoPlayerScreen(
                                                    viewModel = ijkVideoPlayerViewModel!!,
                                                    isCacheVideo = isCache,
                                                    videoIdType = videoIdType,
                                                    videoId = videoId,
                                                    videoCid = videoCid,
                                                    isBangumi = isBangumi,
                                                    navController = navController,
                                                    animatedVisibilityScope = this
                                                )
                                            }
                                        }
                                    }
                                    composable<AudioPlayerScreen> {
                                        val (videoIdType, videoId, videoCid) = it.toRoute<AudioPlayerScreen>()
                                        audioPlayerService?.let { service ->
                                            LaunchedEffect(key1 = Unit) {
                                                service.playAudio(videoIdType, videoId, videoCid)
                                            }
                                            AudioPlayerScreen(
                                                service = service,
                                                navController = navController
                                            )
                                        }
                                    }
                                    composable<QuickToolbarCustomizationScreen> {
                                        QuickToolbarCustomizationScreen(navController = navController)
                                    }
                                    composable<DeviceDiscoverScreen> {
                                        DeviceDiscoverScreen(navController = navController)
                                    }
                                    composable<BangumiScreen> {
                                        LaunchedEffect(key1 = Unit) {
                                            ijkVideoPlayerViewModel = null
                                        }
                                        val (idType, id) = it.toRoute<BangumiScreen>()
                                        BangumiScreen(
                                            bangumiIdType = idType,
                                            bangumiId = id,
                                            navController = navController,
                                            animatedVisibilityScope = this
                                        )
                                    }
                                    composable<SettingsScreen> {
                                        SettingsScreen(navController)
                                    }
                                    composable<ScaleAdjustingScreen> {
                                        ScaleAdjustingScreen(navController = navController)
                                    }
                                    composable<ExperimentalFunctionsScreen> {
                                        ExperimentalFunctionsScreen(navController = navController)
                                    }
                                    composable<BangumiIndexScreen> {
                                        BangumiIndexScreen(navController = navController)
                                    }
                                    composable<BangumiTimelineScreen> {
                                        BangumiTimelineScreen(navController = navController)
                                    }
                                    composable<VideoFavouriteFoldersScreen> {
                                        val (aid) = it.toRoute<VideoFavouriteFoldersScreen>()
                                        VideoFavouriteFoldersScreen(
                                            navController = navController,
                                            videoAid = aid
                                        )
                                    }
                                    composable<CacheListScreen> {
                                        LaunchedEffect(key1 = Unit) {
                                            ijkVideoPlayerViewModel = null
                                        }
                                        CacheListScreen(navController = navController)
                                    }
                                    composable<CreateNewCacheScreen> {
                                        val (bvid) = it.toRoute<CreateNewCacheScreen>()
                                        CreateNewCacheScreen(
                                            videoBvid = bvid,
                                            navController = navController
                                        )
                                    }
                                    composable<QrCodeLoginScreen> {
                                        LoginScreen(navController = navController)
                                    }
                                    composable<SeasonScreen> {
                                        val (seasonName, seasonId, uploaderName, uploaderId, ambientImage) = it.toRoute<SeasonScreen>()
                                        SeasonScreen(
                                            navController = navController,
                                            seasonId = seasonId,
                                            seasonName = seasonName,
                                            uploaderMid = uploaderId,
                                            uploaderName = uploaderName,
                                            ambientImage = ambientImage
                                        )
                                    }
                                    composable<ImageViewerScreen>(
                                        enterTransition = {
                                            fadeIn()
                                        },
                                        exitTransition = {
                                            fadeOut()
                                        },
                                        popEnterTransition = {
                                            fadeIn()
                                        },
                                        popExitTransition = {
                                            fadeOut()
                                        }
                                    ) {
                                        val (images, index) = it.toRoute<ImageViewerScreen>()
                                        ImageViewerScreen(
                                            navController = navController,
                                            images = images,
                                            selectedIndex = index,
                                            animatedVisibilityScope = this
                                        )
                                    }
                                    composable<ArticleScreen> {
                                        val (cvid) = it.toRoute<ArticleScreen>()
                                        ArticleScreen(
                                            navController = navController,
                                            cvid = cvid
                                        )
                                    }
                                    composable<UserSpaceScreen> {
                                        val (mid) = it.toRoute<UserSpaceScreen>()
                                        UserSpaceScreen(
                                            navController = navController,
                                            mid = mid,
                                            animatedVisibilityScope = this
                                        )
                                    }
                                    composable<QrCodeScreen> {
                                        val (content, message) = it.toRoute<QrCodeScreen>()
                                        QrCodeScreen(
                                            navController = navController,
                                            qrCodeContent = content,
                                            qrCodeMessage = message
                                        )
                                    }
                                    composable<CoinScreen> {
                                        val (idType, id) = it.toRoute<CoinScreen>()
                                        CoinScreen(
                                            navController = navController,
                                            videoIdType = idType,
                                            videoId = id
                                        )
                                    }
                                    composable<SwitchUserScreen> {
                                        SwitchUserScreen(navController = navController)
                                    }
                                    composable<BangumiEpisodeListScreen> {
                                        val (idType, id, index) = it.toRoute<BangumiEpisodeListScreen>()
                                        BangumiEpisodeListScreen(
                                            navController = navController,
                                            bangumiIdType = idType,
                                            bangumiId = id,
                                            screenIndex = index
                                        )
                                    }
                                    composable<SystemNotificationsListScreen> {
                                        SystemNotificationScreen(navController = navController)
                                    }
                                    composable<MessageScreen> {
                                        MessageScreen(navController = navController)
                                    }
                                    composable<LikeMessagesScreen> {
                                        LikeMessagesScreen(navController = navController)
                                    }
                                    composable<ReplyMessageScreen> {
                                        ReplyMessageScreen(navController = navController)
                                    }
                                    composable<AtMessageScreen> {
                                        AtMessageScreen(navController = navController)
                                    }
                                    composable<DirectMessageScreen> {
                                        val (name, mid) = it.toRoute<DirectMessageScreen>()
                                        DirectMessageScreen(
                                            talkerName = name,
                                            talkerMid = mid,
                                            navController = navController
                                        )
                                    }
                                    composable<CommentRepliesDetailScreen> {
                                        val (rootRpid, videoAid, uploaderMid) = it.toRoute<CommentRepliesDetailScreen>()
                                        CommentRepliesDetailScreen(
                                            rootRpid = rootRpid,
                                            videoAid = videoAid,
                                            uploaderMid = uploaderMid,
                                            navController = navController,
                                            animatedVisibilityScope = this
                                        )
                                    }
                                    composable<AllIssuesScreen> {
                                        AllIssuesScreen(navController = navController)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        val bindingIntent = Intent(this, AudioPlayerService::class.java).apply {
            putExtras(intent)
        }
        startService(bindingIntent)
        bindService(bindingIntent, object : ServiceConnection {
            override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
                audioPlayerService = (service as AudioPlayerService.AudioPlayerBinder).service
            }

            override fun onServiceDisconnected(p0: ComponentName?) {
                audioPlayerService = null
            }

        }, Service.BIND_AUTO_CREATE)
    }
}