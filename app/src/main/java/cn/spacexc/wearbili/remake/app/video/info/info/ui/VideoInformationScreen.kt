package cn.spacexc.wearbili.remake.app.video.info.info.ui

import android.content.Context
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.common.ToastUtils
import cn.spacexc.wearbili.remake.common.UIState
import cn.spacexc.wearbili.remake.common.copyToClipboard
import cn.spacexc.wearbili.remake.common.domain.time.secondToTime
import cn.spacexc.wearbili.remake.common.domain.time.toDateStr
import cn.spacexc.wearbili.remake.common.domain.video.toShortChinese
import cn.spacexc.wearbili.remake.common.ui.BiliImage
import cn.spacexc.wearbili.remake.common.ui.ExpandableText
import cn.spacexc.wearbili.remake.common.ui.IconText
import cn.spacexc.wearbili.remake.common.ui.LargeUserCard
import cn.spacexc.wearbili.remake.common.ui.LoadableBox
import cn.spacexc.wearbili.remake.common.ui.TitleBackgroundHorizontalPadding
import cn.spacexc.wearbili.remake.common.ui.clickVfx
import cn.spacexc.wearbili.remake.common.ui.copyable
import cn.spacexc.wearbili.remake.common.ui.spx

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
    val videoData: cn.spacexc.wearbili.remake.app.video.info.info.remote.Data? = null
)

@Composable
fun VideoInformationScreen(
    state: VideoInformationScreenState,
    context: Context
) {
    LoadableBox(uiState = state.uiState) {
        Column(
            modifier = Modifier
                .verticalScroll(state.scrollState)
                .padding(horizontal = TitleBackgroundHorizontalPadding, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            state.videoData?.let { video ->
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
                Text(text = video.title, style = MaterialTheme.typography.h1)
                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    IconText(
                        text = "${video.stat.view.toShortChinese()}观看",
                        modifier = Modifier.alpha(0.7f),
                        fontSize = 10.5.spx
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
                        fontSize = 10.5.spx
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
                        fontSize = 10.5.spx
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
                        fontSize = 10.5.spx,
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
                        style = MaterialTheme.typography.body1
                    )
                }
                LargeUserCard(
                    avatar = video.owner.face,
                    username = video.owner.name,
                    mid = video.owner.mid
                )
            }
        }
    }
}