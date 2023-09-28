package cn.spacexc.wearbili.remake.app.video.info.info.ui

import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.MonetizationOn
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.SendToMobile
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cn.spacexc.bilibilisdk.sdk.video.info.remote.info.app.Data
import cn.spacexc.bilibilisdk.utils.UserUtils
import cn.spacexc.wearbili.common.copyToClipboard
import cn.spacexc.wearbili.common.domain.log.logd
import cn.spacexc.wearbili.common.domain.time.secondToTime
import cn.spacexc.wearbili.common.domain.time.toDateStr
import cn.spacexc.wearbili.common.domain.video.toShortChinese
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.cache.create.ui.CreateNewCacheActivity
import cn.spacexc.wearbili.remake.app.cache.create.ui.PARAM_VIDEO_BVID
import cn.spacexc.wearbili.remake.app.video.action.favourite.ui.PARAM_VIDEO_AID
import cn.spacexc.wearbili.remake.app.video.action.favourite.ui.VideoFavouriteFoldersActivity
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_CID
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID_TYPE
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_WEBI_SIGNATURE_KEY
import cn.spacexc.wearbili.remake.app.video.info.ui.VIDEO_TYPE_BVID
import cn.spacexc.wearbili.remake.app.videoplayer.defaultplayer.Media3PlayerActivity
import cn.spacexc.wearbili.remake.common.ToastUtils
import cn.spacexc.wearbili.remake.common.UIState
import cn.spacexc.wearbili.remake.common.ui.BiliImage
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import cn.spacexc.wearbili.remake.common.ui.ExpandableText
import cn.spacexc.wearbili.remake.common.ui.IconText
import cn.spacexc.wearbili.remake.common.ui.LargeUserCard
import cn.spacexc.wearbili.remake.common.ui.LoadableBox
import cn.spacexc.wearbili.remake.common.ui.OutlinedRoundButton
import cn.spacexc.wearbili.remake.common.ui.TitleBackgroundHorizontalPadding
import cn.spacexc.wearbili.remake.common.ui.clickVfx
import cn.spacexc.wearbili.remake.common.ui.copyable
import cn.spacexc.wearbili.remake.common.ui.spx
import cn.spacexc.wearbili.remake.common.ui.theme.AppTheme
import cn.spacexc.wearbili.remake.common.ui.toOfficialVerify
import kotlinx.coroutines.launch

/**
 * Created by XC-Qan on 2023/4/12.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

data class VideoInformationScreenState(
    val uiState: UIState = UIState.Loading,
    val scrollState: ScrollState = ScrollState(0),
    val videoData: Data? = null
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VideoInformationScreen(
    state: VideoInformationScreenState,
    context: Context,
    videoInformationViewModel: VideoInformationViewModel
) {
    val likeColor by animateColorAsState(targetValue = if (videoInformationViewModel.isLiked) BilibiliPink else Color.White)
    val coinColor by animateColorAsState(targetValue = if (videoInformationViewModel.isCoined) BilibiliPink else Color.White)
    val favColor by animateColorAsState(targetValue = if (videoInformationViewModel.isFav) BilibiliPink else Color.White)

    val scope = rememberCoroutineScope()


    val favouriteRequestActivityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            logd("back from favourite activity!")
            val isStillFavourite = result.data?.getBooleanExtra("isStillFavourite", true)
            if(isStillFavourite != null) {
                videoInformationViewModel.isFav = isStillFavourite
            }
        }
    )

    LoadableBox(uiState = state.uiState) {
        Column(
            modifier = Modifier
                .verticalScroll(state.scrollState)
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            state.videoData?.let { video ->
                Column(modifier = Modifier.padding(horizontal = TitleBackgroundHorizontalPadding - 2.dp)) {
                    Box {
                        BiliImage(
                            url = video.pic,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 10f)
                                .clip(
                                    RoundedCornerShape(8.dp)
                                ),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = video.duration.secondToTime(),
                            color = Color.White,
                            fontSize = 11.spx,
                            modifier = Modifier
                                .padding(end = 8.dp, bottom = 6.dp)
                                .align(Alignment.BottomEnd),
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Text(text = video.title, style = AppTheme.typography.h1)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        IconText(
                            text = "${video.stat.view.toShortChinese()}观看",
                            modifier = Modifier.alpha(0.7f),
                            fontSize = 11.spx
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_view_count),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                tint = Color.White
                            )
                        }

                        IconText(
                            text = video.stat.vt.secondToTime(),
                            modifier = Modifier
                                .alpha(0.7f),
                            fontSize = 11.spx,
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Timer,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                tint = Color.White
                            )
                        }

                        IconText(
                            text = "${video.stat.danmaku.toShortChinese()}弹幕",
                            modifier = Modifier.alpha(0.7f),
                            fontSize = 11.spx
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_danmaku),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                tint = Color.White
                            )
                        }

                        IconText(
                            text = video.pubdate.times(1000).toDateStr("yyyy-MM-dd HH:mm"),
                            modifier = Modifier.alpha(0.7f),
                            fontSize = 11.spx
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_time),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                tint = Color.White
                            )
                        }

                        IconText(
                            text = video.bvid,
                            modifier = Modifier
                                .alpha(0.7f)
                                .clickVfx(onLongClick = {
                                    video.bvid.copyToClipboard(context = context)
                                    ToastUtils.showText(content = "已复制BV号", context = context)
                                }),
                            fontSize = 11.spx,
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Movie,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                tint = Color.White
                            )
                        }
                    }
                    if (video.desc.isNotEmpty()) {
                        ExpandableText(
                            text = video.desc,
                            modifier = Modifier.copyable(video.desc),
                            style = AppTheme.typography.body1
                        )
                    }
                }

                if (video.staff.isNullOrEmpty()) {
                    LargeUserCard(
                        modifier = Modifier.padding(horizontal = TitleBackgroundHorizontalPadding - 2.dp),
                        avatar = video.owner.face,
                        username = video.owner.name,
                        mid = video.owner.mid,
                        officialVerify = video.owner_ext.official_verify.type.toOfficialVerify(),
                        usernameColor = video.owner_ext.vip?.label?.bg_color ?: "#FFFFFF",
                        userInfo = "粉丝 ${video.owner_ext.fans.toShortChinese()}  IP属地 ${video.pub_location ?: "未知"}"
                    )
                } else {
                    Row(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = TitleBackgroundHorizontalPadding - 2.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        video.staff?.forEach {
                            LargeUserCard(
                                avatar = it.face,
                                username = it.name,
                                mid = it.mid,
                                officialVerify = it.official_verify.type.toOfficialVerify(),
                                usernameColor = it.vip?.label?.bg_color ?: "#FFFFFF",
                                userInfo = it.title,
                                isFillMaxWidth = false
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = TitleBackgroundHorizontalPadding - 2.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        OutlinedRoundButton(
                            icon = {
                                Row(
                                    modifier = Modifier.align(Alignment.Center),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.PlayCircle,
                                        tint = Color.White,
                                        contentDescription = null
                                    )
                                    Text(
                                        text = "播放 ",    //这个空格是为了让图标和文字视觉剧中，万万不能删
                                        style = AppTheme.typography.h3
                                    )
                                }
                            },
                            text = "用内置播放器播放",
                            modifier = Modifier.weight(2f),
                            buttonModifier = Modifier.aspectRatio(2f / 1f),
                            onClick = {
                                scope.launch {
                                    Intent(context, Media3PlayerActivity::class.java).apply {
                                        putExtra(PARAM_VIDEO_ID_TYPE, VIDEO_TYPE_BVID)
                                        putExtra(PARAM_VIDEO_ID, video.bvid)
                                        putExtra(PARAM_VIDEO_CID, video.cid.logd("cid"))
                                        putExtra(
                                            PARAM_WEBI_SIGNATURE_KEY, UserUtils.webiSign()
                                        )    //这个方法是suspend
                                        context.startActivity(this)
                                    }
                                }
                            })
                        OutlinedRoundButton(
                            icon = {
                                Icon(
                                    imageVector = Icons.Outlined.SendToMobile,
                                    tint = Color.White,
                                    contentDescription = null,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            },
                            text = "手机播放",
                            modifier = Modifier.weight(1f),
                            buttonModifier = Modifier.aspectRatio(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        OutlinedRoundButton(icon = {
                            Row(
                                modifier = Modifier.align(Alignment.Center),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.ThumbUp,
                                    tint = likeColor,
                                    contentDescription = null
                                )
                            }
                        },
                            text = "点赞",
                            modifier = Modifier.weight(1f),
                            buttonModifier = Modifier.aspectRatio(1f),
                            onClick = {
                                videoInformationViewModel.likeVideo(
                                    cn.spacexc.wearbili.remake.app.videoplayer.defaultplayer.VIDEO_TYPE_BVID,
                                    video.bvid
                                )
                            })
                        OutlinedRoundButton(
                            icon = {
                                Icon(
                                    imageVector = Icons.Outlined.StarOutline,
                                    tint = favColor,
                                    contentDescription = null,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            },
                            text = "收藏",
                            modifier = Modifier.weight(1f),
                            buttonModifier = Modifier.aspectRatio(1f),
                            onClick = {
                                favouriteRequestActivityLauncher.launch(Intent(
                                    context,
                                    VideoFavouriteFoldersActivity::class.java
                                ).apply {
                                    putExtra(PARAM_VIDEO_AID, video.aid)
                                })
                            }
                        )
                        OutlinedRoundButton(
                            icon = {
                                Icon(
                                    imageVector = Icons.Outlined.MonetizationOn,
                                    tint = coinColor,
                                    contentDescription = null,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            },
                            text = "投币",
                            modifier = Modifier.weight(1f),
                            buttonModifier = Modifier.aspectRatio(1f)

                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        OutlinedRoundButton(
                            icon = {
                                Row(
                                    modifier = Modifier.align(Alignment.Center),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.History,
                                        tint = Color.White,
                                        contentDescription = null
                                    )
                                }
                            },
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
                                Icon(
                                    imageVector = Icons.Outlined.FileDownload,
                                    tint = Color.White,
                                    contentDescription = null,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            },
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
                        //Spacer(modifier = Modifier.weight(1f))  //placeholder
                        Spacer(modifier = Modifier.weight(1f))  //placeholder
                    }
                }
            }
        }
    }
}