package cn.spacexc.wearbili.remake.app.video.info.info.ui.v2.action

import BiliTextIcon
import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.spacexc.wearbili.common.domain.log.logd
import cn.spacexc.wearbili.common.domain.video.toShortChinese
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.cache.create.ui.CreateNewCacheActivity
import cn.spacexc.wearbili.remake.app.cache.create.ui.PARAM_VIDEO_BVID
import cn.spacexc.wearbili.remake.app.link.qrcode.PARAM_QRCODE_CONTENT
import cn.spacexc.wearbili.remake.app.link.qrcode.PARAM_QRCODE_MESSAGE
import cn.spacexc.wearbili.remake.app.link.qrcode.QrCodeActivity
import cn.spacexc.wearbili.remake.app.player.audio.AudioPlayerActivity
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.Media3PlayerActivity
import cn.spacexc.wearbili.remake.app.season.ui.PARAM_AMBIENT_IMAGE
import cn.spacexc.wearbili.remake.app.season.ui.PARAM_MID
import cn.spacexc.wearbili.remake.app.season.ui.PARAM_SEASON_ID
import cn.spacexc.wearbili.remake.app.season.ui.PARAM_SEASON_NAME
import cn.spacexc.wearbili.remake.app.season.ui.PARAM_UPLOADER_NAME
import cn.spacexc.wearbili.remake.app.season.ui.SeasonActivity
import cn.spacexc.wearbili.remake.app.settings.LocalConfiguration
import cn.spacexc.wearbili.remake.app.video.action.coin.ui.CoinActivity
import cn.spacexc.wearbili.remake.app.video.action.favourite.ui.PARAM_VIDEO_AID
import cn.spacexc.wearbili.remake.app.video.action.favourite.ui.VideoFavouriteFoldersActivity
import cn.spacexc.wearbili.remake.app.video.info.info.ui.VideoInformationScreenState
import cn.spacexc.wearbili.remake.app.video.info.info.ui.VideoInformationViewModel
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_CID
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID_TYPE
import cn.spacexc.wearbili.remake.app.video.info.ui.VIDEO_TYPE_BVID
import cn.spacexc.wearbili.remake.common.ToastUtils
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.IconText
import cn.spacexc.wearbili.remake.common.ui.LargeUserCard
import cn.spacexc.wearbili.remake.common.ui.OutlinedRoundButton
import cn.spacexc.wearbili.remake.common.ui.VfxOutlinedRoundButton
import cn.spacexc.wearbili.remake.common.ui.clickVfx
import cn.spacexc.wearbili.remake.common.ui.isRound
import cn.spacexc.wearbili.remake.common.ui.rememberMutableInteractionSource
import cn.spacexc.wearbili.remake.common.ui.theme.AppTheme
import cn.spacexc.wearbili.remake.common.ui.titleBackgroundHorizontalPadding
import cn.spacexc.wearbili.remake.common.ui.toOfficialVerify
import cn.spacexc.wearbili.remake.common.ui.wearBiliAnimateColorAsState
import cn.spacexc.wearbili.remake.proto.settings.Player
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import like.rememberLikeButtonState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Activity.VideoActionsScreen(
    state: VideoInformationScreenState,
    context: Activity,
    videoInformationViewModel: VideoInformationViewModel,
    videoIdType: String,
    videoId: String
) {
    val localDensity = LocalDensity.current
    val scope = rememberCoroutineScope()

    val likeColor by wearBiliAnimateColorAsState(targetValue = if (videoInformationViewModel.isLiked) BilibiliPink else Color.White)
    val coinColor by wearBiliAnimateColorAsState(targetValue = if (videoInformationViewModel.isCoined) BilibiliPink else Color.White)
    val favColor by wearBiliAnimateColorAsState(targetValue = if (videoInformationViewModel.isFav) BilibiliPink else Color.White)

    val likeButtonState = rememberLikeButtonState()
    val coinButtonState = rememberLikeButtonState()
    val favButtonState = rememberLikeButtonState()

    //region 一键三连动画效果相关区域
    //哥们儿真不知道三连怎么翻
    val isSanLianed by remember(
        key1 = videoInformationViewModel.isLiked,
        key2 = videoInformationViewModel.isCoined,
        key3 = videoInformationViewModel.isFav
    ) {
        mutableStateOf(videoInformationViewModel.isLiked && videoInformationViewModel.isCoined && videoInformationViewModel.isFav)
    }
    var sanlianHitProgress by remember { mutableFloatStateOf(0f) }
    var sanlianHitJob by remember { mutableStateOf<Job?>(null) }
    val sanlianInteractionSource = rememberMutableInteractionSource()
    val isSanlianPressed by sanlianInteractionSource.collectIsPressedAsState()
    val sanlianAnimationScope = rememberCoroutineScope()

    LaunchedEffect(key1 = isSanlianPressed, block = {
        if (isSanlianPressed) {
            sanlianHitJob?.cancel()
            sanlianHitJob = sanlianAnimationScope.async {
                if (sanlianHitProgress == 0f) delay(700)
                if (isSanLianed) {
                    ToastUtils.showText("你已经三连过咯！")
                    return@async
                }
                // 手指按下后，逐步减少hitProgress，使圆弧角度逆时针增加
                while (sanlianHitProgress > -360) {
                    delay(15)
                    sanlianHitProgress -= 4
                }
                videoInformationViewModel.sanlian(
                    videoIdType,
                    videoId
                ) { isLiked, isCoined, isFav ->
                    sanlianAnimationScope.launch {
                        if (isLiked) {
                            likeButtonState.unlike()
                            likeButtonState.like(sanlianAnimationScope)
                        } else likeButtonState.unlike()
                    }
                    sanlianAnimationScope.launch {
                        if (isCoined) {
                            coinButtonState.unlike()
                            coinButtonState.like(sanlianAnimationScope)
                        } else coinButtonState.unlike()
                    }
                    sanlianAnimationScope.launch {
                        if (isFav) {
                            favButtonState.unlike()
                            favButtonState.like(sanlianAnimationScope)
                        } else favButtonState.unlike()
                    }
                }
                ToastUtils.showText("三连爆棚，感谢推荐！")
                sanlianHitProgress = 0f
            }
        } else {
            sanlianHitJob?.cancel()
            if (isSanLianed) {
                sanlianHitProgress = 0f
                return@LaunchedEffect
            }
            sanlianHitJob = sanlianAnimationScope.async {
                // 手指抬起时， 增加hitProgress，使圆弧逐步缩短
                while (sanlianHitProgress < 0) {
                    delay(8)
                    sanlianHitProgress += 4
                }
            }
        }
    })
    //endregion

    val currentPlayer =
        LocalConfiguration.current.defaultPlayer //by SettingsManager.currentPlayer.collectAsState(initial = "videoPlayer")

    val favouriteRequestActivityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            logd("back from favourite activity!")
            val isStillFavourite = result.data?.getBooleanExtra("isStillFavourite", true)
            if (isStillFavourite != null) {
                videoInformationViewModel.isFav = isStillFavourite
            }
            if (isStillFavourite == true) {
                ToastUtils.showText("别让我在收藏夹吃灰哦")
            }
        }
    )
    val coinRequestActivityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            logd("back from favourite activity!")
            val isCoined = result.data?.getBooleanExtra("isCoined", true)
            if (isCoined != null) {
                videoInformationViewModel.isCoined = isCoined
                val count = result.data?.getIntExtra("coinCount", 0) ?: 0
                videoInformationViewModel.hasCoinedCount = count
            }
            if (isCoined == true) {
                ToastUtils.showText("投币成功！")
            }
        }
    )

    Column(
        modifier = Modifier
            .verticalScroll(state.scrollState)
            .padding(vertical = if (isRound()) 24.dp else 8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        state.videoData?.view?.let { video ->
            Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
                if (video.staff.isNullOrEmpty()) {
                    state.videoData.card.card.let { user ->
                        LargeUserCard(
                            modifier = Modifier.padding(horizontal = titleBackgroundHorizontalPadding() - 2.dp),
                            avatar = video.owner.face,
                            username = video.owner.name,
                            mid = user.mid,
                            officialVerify = (user.officialVerify?.type).toOfficialVerify(),
                            usernameColor = user.vip?.nicknameColor ?: "#FFFFFF",
                            userInfo = "${user.fans.toShortChinese()}粉丝"
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = titleBackgroundHorizontalPadding() - 2.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        video.staff?.forEach {
                            LargeUserCard(
                                avatar = it.face,
                                username = it.name,
                                mid = it.mid,
                                officialVerify = it.official?.type.toOfficialVerify(),
                                usernameColor = it.vip?.nicknameColor ?: "#FFFFFF",
                                userInfo = it.title,
                                isFillMaxWidth = false
                            )
                        }
                    }
                }
                video.ugcSeason?.let { season ->
                    Card(
                        shape = CircleShape,
                        modifier = Modifier.padding(horizontal = titleBackgroundHorizontalPadding() - 2.dp),
                        innerPaddingValues = PaddingValues(
                            horizontal = 14.dp,
                            vertical = 12.dp
                        ),
                        onClick = {
                            context.startActivity(
                                Intent(
                                    context,
                                    SeasonActivity::class.java
                                ).apply {
                                    putExtra(PARAM_SEASON_NAME, season.title)
                                    putExtra(PARAM_SEASON_ID, season.id)
                                    putExtra(PARAM_MID, season.mid)
                                    putExtra(PARAM_UPLOADER_NAME, video.owner.name)
                                    putExtra(
                                        PARAM_AMBIENT_IMAGE,
                                        video.pic.replace("http://", "https://")
                                    )
                                })
                        }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            var textHeight by remember {
                                mutableStateOf(0.dp)
                            }
                            IconText(
                                text = season.title,
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .weight(1f)
                                    .onSizeChanged {
                                        textHeight = with(localDensity) { it.height.toDp() }
                                    }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_group_outlined),
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(textHeight * 0.9f)
                            )
                        }
                    }
                }
            }

            if (video.pages.size > 1) {
                androidx.compose.material3.Text(
                    text = "分集(${video.pages.size})",
                    style = AppTheme.typography.h1,
                    modifier = Modifier
                        .padding(horizontal = titleBackgroundHorizontalPadding() - 2.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = titleBackgroundHorizontalPadding() - 2.dp),
                    state = rememberLazyListState()
                ) {
                    video.pages.forEachIndexed { index, page ->
                        item {
                            Card(
                                modifier = Modifier.clickVfx(onClick = {
                                    when (currentPlayer) {
                                        Player.VideoPlayer -> {
                                            Intent(
                                                context,
                                                Media3PlayerActivity::class.java
                                            ).apply {
                                                putExtra(
                                                    PARAM_VIDEO_ID_TYPE,
                                                    VIDEO_TYPE_BVID
                                                )
                                                putExtra(PARAM_VIDEO_ID, video.bvid)
                                                putExtra(
                                                    PARAM_VIDEO_CID,
                                                    page.cid.logd("cid")
                                                )
                                                context.startActivity(this)
                                            }
                                        }

                                        Player.AudioPlayer -> {
                                            Intent(
                                                context,
                                                AudioPlayerActivity::class.java
                                            ).apply {
                                                putExtra(
                                                    PARAM_VIDEO_ID_TYPE,
                                                    VIDEO_TYPE_BVID
                                                )
                                                putExtra(PARAM_VIDEO_ID, video.bvid)
                                                putExtra(
                                                    PARAM_VIDEO_CID,
                                                    page.cid.logd("cid")
                                                )
                                                context.startActivity(this)
                                            }
                                        }

                                        else -> {
                                            ToastUtils.showText("你乱改配置文件搞得人家不知道要用什么播放器...")
                                        }
                                    }

                                },
                                    onLongClick = {
                                        Intent(context, AudioPlayerActivity::class.java).apply {
                                            putExtra(PARAM_VIDEO_ID_TYPE, VIDEO_TYPE_BVID)
                                            putExtra(PARAM_VIDEO_ID, video.bvid)
                                            putExtra(PARAM_VIDEO_CID, page.cid)
                                            context.startActivity(this)
                                        }
                                        //TODO 跳转到播放器设置
                                    })
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        androidx.compose.material3.Text(
                                            text = page.part,
                                            style = AppTheme.typography.h2,
                                            fontSize = 12.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))
                                    androidx.compose.material3.Text(
                                        text = "P${index.plus(1)}",
                                        style = AppTheme.typography.h3,
                                        fontSize = 9.sp,
                                        modifier = Modifier.alpha(0.8f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = titleBackgroundHorizontalPadding() - 2.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                    /*.drawBehind {
                        drawRect(
                            size = androidx.compose.ui.geometry.Size(
                                height = size.height,
                                width = sanlianHitProgress / -360 * size.width
                            ),
                            color = BilibiliPink, style = Stroke(width = 5f),
                        )
                        //drawArc(color = BilibiliPink, startAngle = -90f, sweepAngle = sanlianHitProgress, useCenter = false, size = size)
                    }*/,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    VfxOutlinedRoundButton(
                        interactionSource = sanlianInteractionSource, icon = {
                            BiliTextIcon(
                                icon = "EAB3",
                                modifier = Modifier.align(Alignment.Center),
                                color = likeColor
                            )
                        },
                        shape = RoundedCornerShape(40),
                        count = if (videoInformationViewModel.isLiked) video.stat.like + 1 else video.stat.like,
                        modifier = Modifier.weight(1f),
                        buttonModifier = Modifier.aspectRatio(1f),
                        onClick = {
                            videoInformationViewModel.likeVideo(
                                cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.VIDEO_TYPE_BVID,
                                video.bvid
                            )
                            false
                        },
                        outlineProgress = sanlianHitProgress / -360f,
                        likeButtonState = likeButtonState
                    )
                    VfxOutlinedRoundButton(
                        icon = {
                            BiliTextIcon(
                                icon = "EA76",
                                modifier = Modifier.align(Alignment.Center),
                                color = coinColor
                            )
                        },
                        shape = RoundedCornerShape(40),
                        count = video.stat.coin + videoInformationViewModel.hasCoinedCount,
                        modifier = Modifier.weight(1f),
                        buttonModifier = Modifier.aspectRatio(1f),
                        onClick = {
                            coinRequestActivityLauncher.launch(
                                Intent(
                                    context,
                                    CoinActivity::class.java
                                ).apply {
                                    putExtra(PARAM_VIDEO_ID_TYPE, videoIdType)
                                    putExtra(PARAM_VIDEO_ID, videoId)
                                }
                            )
                            false
                        },
                        outlineProgress = sanlianHitProgress / -360f,
                        likeButtonState = coinButtonState
                    )
                    VfxOutlinedRoundButton(
                        icon = {
                            BiliTextIcon(
                                icon = "EB20",
                                modifier = Modifier.align(Alignment.Center),
                                color = favColor
                            )
                        },
                        shape = RoundedCornerShape(40),
                        count = if (videoInformationViewModel.isFav) video.stat.favorite + 1 else video.stat.favorite,
                        modifier = Modifier.weight(1f),
                        buttonModifier = Modifier.aspectRatio(1f),
                        onClick = {
                            favouriteRequestActivityLauncher.launch(Intent(
                                context,
                                VideoFavouriteFoldersActivity::class.java
                            ).apply {
                                putExtra(PARAM_VIDEO_AID, video.aid)
                            })
                            false
                        },
                        outlineProgress = sanlianHitProgress / -360f,
                        likeButtonState = favButtonState
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OutlinedRoundButton(
                        icon = {
                            BiliTextIcon(icon = "EA25", modifier = Modifier.align(Alignment.Center))
                        },
                        shape = RoundedCornerShape(40),
                        text = "稍后再看",
                        modifier = Modifier.weight(1f),
                        buttonModifier = Modifier.aspectRatio(1f),
                        onClick = {
                            videoInformationViewModel.addToWatchLater(
                                videoIdType = VIDEO_TYPE_BVID,
                                videoId = video.bvid
                            )
                        }
                    )
                    OutlinedRoundButton(
                        icon = {
                            BiliTextIcon(icon = "EAA2", modifier = Modifier.align(Alignment.Center))
                        },
                        shape = RoundedCornerShape(40),
                        text = "缓存",
                        modifier = Modifier.weight(1f),
                        buttonModifier = Modifier.aspectRatio(1f),
                        onClick = {
                            context.startActivity(
                                Intent(
                                    context,
                                    CreateNewCacheActivity::class.java
                                ).apply {
                                    putExtra(PARAM_VIDEO_BVID, video.bvid)
                                })
                        }

                    )
                    OutlinedRoundButton(
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_view_on_phone),
                                contentDescription = null,
                                modifier = Modifier
                                    .align(
                                        Alignment.Center
                                    )
                                    .size(24.dp),
                                tint = Color.White
                            )
                        },
                        shape = RoundedCornerShape(40),
                        text = "手机观看",
                        modifier = Modifier.weight(1f),
                        buttonModifier = Modifier.aspectRatio(1f),
                        onClick = {
                            context.startActivity(
                                Intent(
                                    context,
                                    QrCodeActivity::class.java
                                ).apply {
                                    putExtra(
                                        PARAM_QRCODE_CONTENT,
                                        "https://bilibili.com/video/${video.bvid}"
                                    )
                                    putExtra(PARAM_QRCODE_MESSAGE, "用手机Bilibili扫码以观看")
                                })
                        }

                    )
                    //Spacer(modifier = Modifier.weight(1f))  //placeholder

                }
            }
        }
    }
}