package cn.spacexc.wearbili.remake.app.video.info.info.ui

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material.icons.outlined.SendToMobile
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.common.copyToClipboard
import cn.spacexc.wearbili.common.domain.time.secondToTime
import cn.spacexc.wearbili.common.domain.time.toDateStr
import cn.spacexc.wearbili.common.domain.video.toShortChinese
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_CID
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID_TYPE
import cn.spacexc.wearbili.remake.app.video.info.ui.VIDEO_TYPE_BVID
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
    val videoData: cn.spacexc.wearbili.remake.app.video.info.info.remote.Data? = null,
    val isLiked: Boolean = false,
    val isCoined: Boolean = false,
    val isFav: Boolean = false
)

@Composable
fun VideoInformationScreen(
    state: VideoInformationScreenState,
    context: Context,
    videoInformationViewModel: VideoInformationViewModel
) {
    val likeColor by animateColorAsState(targetValue = if (state.isLiked) BilibiliPink else Color.White)
    val coinColor by animateColorAsState(targetValue = if (state.isCoined) BilibiliPink else Color.White)
    val favColor by animateColorAsState(targetValue = if (state.isFav) BilibiliPink else Color.White)

    LoadableBox(uiState = state.uiState) {
        Column(
            modifier = Modifier
                .verticalScroll(state.scrollState)
                .padding(horizontal = TitleBackgroundHorizontalPadding, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            state.videoData?.let { video ->
                Box(modifier = Modifier.clickVfx {
                    try {
                        Intent(Intent.ACTION_MAIN).apply {
                            component = ComponentName(
                                "cn.spacexc.wearbili.videoplayer",
                                "cn.spacexc.wearbili.videoplayer.defaultplayer.VideoPlayerActivity"
                            )
                            putExtra(PARAM_VIDEO_ID_TYPE, VIDEO_TYPE_BVID)
                            putExtra(PARAM_VIDEO_ID, video.bvid)
                            putExtra(PARAM_VIDEO_CID, video.cid.toString())
                            context.startActivity(this)
                        }
                    } catch (e: ActivityNotFoundException) {
                        ToastUtils.showText(content = "你还没有安装播放插件哦")
                    }
                }) {
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
                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
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
                LargeUserCard(
                    avatar = video.owner.face,
                    username = video.owner.name,
                    mid = video.owner.mid
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        OutlinedRoundButton(
                            icon = Icons.Outlined.PlayCircle,
                            text = "播放",
                            modifier = Modifier
                                .weight(2f)
                                .aspectRatio(2f / 1f),
                        ) {

                        }
                        OutlinedRoundButton(
                            icon = Icons.Outlined.ThumbUp,
                            text = "点赞",
                            modifier = Modifier.weight(1f),
                            tint = likeColor
                        ) {
                            videoInformationViewModel.likeVideo(
                                VIDEO_TYPE_BVID,
                                state.videoData.bvid
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        OutlinedRoundButton(
                            icon = Icons.Outlined.MonetizationOn,
                            text = "投币",
                            modifier = Modifier.weight(1f),
                            tint = coinColor
                        ) {

                        }
                        OutlinedRoundButton(
                            icon = Icons.Outlined.Star,
                            text = "收藏",
                            modifier = Modifier.weight(1f),
                            tint = favColor
                        ) {

                        }
                        OutlinedRoundButton(
                            icon = Icons.Outlined.SendToMobile,
                            text = "手机播放",
                            modifier = Modifier.weight(1f)
                        ) {

                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        OutlinedRoundButton(
                            icon = Icons.Outlined.History,
                            text = "稍后再看",
                            modifier = Modifier.weight(1f)
                        ) {

                        }
                        OutlinedRoundButton(
                            icon = Icons.Outlined.Report,
                            text = "举报",
                            modifier = Modifier.weight(1f)
                        ) {

                        }
                        OutlinedRoundButton(
                            icon = Icons.Outlined.FileDownload,
                            text = "缓存",
                            modifier = Modifier.weight(1f)
                        ) {

                        }
                    }
                }
            }
        }
    }
}