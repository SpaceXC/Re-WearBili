package cn.spacexc.wearbili.remake.app.bangumi.info

import android.app.Activity
import android.content.Intent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import cn.spacexc.wearbili.common.domain.color.parseColor
import cn.spacexc.wearbili.common.domain.video.toShortChinese
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.bangumi.ui.BangumiViewModel
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_CID
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID_TYPE
import cn.spacexc.wearbili.remake.app.video.info.ui.VIDEO_TYPE_BVID
import cn.spacexc.wearbili.remake.app.videoplayer.defaultplayer.Media3PlayerActivity
import cn.spacexc.wearbili.remake.app.videoplayer.defaultplayer.PARAM_IS_BANGUMI
import cn.spacexc.wearbili.remake.common.ui.BiliImage
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.IconText
import cn.spacexc.wearbili.remake.common.ui.LoadableBox
import cn.spacexc.wearbili.remake.common.ui.TitleBackgroundHorizontalPadding
import cn.spacexc.wearbili.remake.common.ui.clickAlpha
import cn.spacexc.wearbili.remake.common.ui.spx
import cn.spacexc.wearbili.remake.common.ui.theme.AppTheme
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily

/**
 * Created by XC-Qan on 2023/7/19.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@OptIn(ExperimentalLayoutApi::class)
@Composable
@UnstableApi
fun Activity.BangumiInfoScreen(
    viewModel: BangumiViewModel
) {
    val localDensity = LocalDensity.current
    LoadableBox(uiState = viewModel.uiState) {
        viewModel.bangumiInfo?.let { bangumi ->
            Column(
                Modifier
                    .verticalScroll(viewModel.bangumiInfoScreenScrollState)
                    .padding(vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = TitleBackgroundHorizontalPadding - 2.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    var isDescriptionExpand by remember {
                        mutableStateOf(false)
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        var coverHeight by remember {
                            mutableStateOf(0.dp)
                        }
                        Box(
                            modifier = Modifier
                                .weight(4f)
                                .clip(RoundedCornerShape(10.dp))
                                .onSizeChanged {
                                    coverHeight = with(localDensity) { it.height.toDp() }
                                }
                        ) {
                            BiliImage(
                                url = bangumi.cover,
                                contentDescription = "${bangumi.title}封面",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(3f / 4f)
                                    .clip(RoundedCornerShape(10.dp))
                            )
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            color = Color(249, 157, 87, 255),
                                            fontFamily = wearbiliFontFamily,
                                            fontSize = 10.spx
                                        )
                                    ) {
                                        append(bangumi.rating.score.toString())
                                    }
                                    withStyle(
                                        style = SpanStyle(
                                            color = Color(249, 157, 87, 255),
                                            fontFamily = wearbiliFontFamily,
                                            fontSize = 8.spx
                                        )
                                    ) {
                                        append("分")
                                    }
                                },
                                color = Color.White,
                                fontSize = 7.spx,
                                fontFamily = wearbiliFontFamily,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .clip(
                                        RoundedCornerShape(topStart = 12.dp)
                                    )
                                    .background(
                                        Color(67, 67, 67, 255)
                                    )
                                    .padding(
                                        start = 7.dp,
                                        end = 4.dp,
                                        top = 3.dp,
                                        bottom = 2.dp
                                    )
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        Column(
                            modifier = Modifier
                                .weight(5f)
                                .height(coverHeight)
                        ) {
                            Text(text = bangumi.title, style = AppTheme.typography.h1)
                            Text(
                                text = "${bangumi.areas.joinToString(separator = ", ") { it.name }}, ${bangumi.new_ep.desc}",
                                modifier = Modifier.alpha(0.6f),
                                style = AppTheme.typography.body1
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(360.dp))
                                    .background(
                                        BilibiliPink
                                    )
                                    .padding(start = 6.dp, end = 10.dp, top = 5.dp, bottom = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconText(
                                    text = "追番",
                                    color = Color.White,
                                    fontSize = 11.spx,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = wearbiliFontFamily
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Add,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    }
                    Text(
                        text = bangumi.evaluate,
                        style = AppTheme.typography.body1,
                        fontSize = 10.spx,
                        modifier = Modifier
                            .clickAlpha { isDescriptionExpand = !isDescriptionExpand }
                            .animateContentSize(animationSpec = tween(durationMillis = 400)),
                        maxLines = if (isDescriptionExpand) Int.MAX_VALUE else 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        val currentSeasonStat =
                            bangumi.seasons.find { it.season_id == bangumi.season_id }?.stat
                        IconText(
                            text = "${currentSeasonStat?.views?.toShortChinese()}观看",
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
                            text = "${currentSeasonStat?.series_follow?.toShortChinese()}系列追番",
                            modifier = Modifier.alpha(0.7f),
                            fontSize = 11.spx
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                tint = Color.White
                            )
                        }
                        IconText(
                            text = bangumi.styles.joinToString(separator = "/"),
                            modifier = Modifier.alpha(0.7f),
                            fontSize = 11.spx
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Sell,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .scale(scaleX = -1f, scaleY = 1f),
                                tint = Color.White
                            )
                        }
                        IconText(
                            text = "${bangumi.stat.danmakus.toShortChinese()}弹幕",
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
                    }


                }
                Text(
                    text = "选集(${bangumi.episodes.size})",
                    style = AppTheme.typography.h1,
                    modifier = Modifier
                        .padding(horizontal = TitleBackgroundHorizontalPadding - 2.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = TitleBackgroundHorizontalPadding - 2.dp)
                ) {
                    bangumi.episodes.forEachIndexed { index, episode ->
                        item {
                            Card(onClick = {
                                Intent(
                                    this@BangumiInfoScreen,
                                    Media3PlayerActivity::class.java
                                ).apply {
                                    putExtra(PARAM_VIDEO_ID_TYPE, VIDEO_TYPE_BVID)
                                    putExtra(PARAM_IS_BANGUMI, true)
                                    putExtra(PARAM_VIDEO_ID, episode.bvid)
                                    putExtra(PARAM_VIDEO_CID, episode.cid)
                                    startActivity(this)
                                }
                            }) {
                                Column {
                                    Row(verticalAlignment = CenterVertically) {
                                        Text(
                                            text = episode.long_title,
                                            style = AppTheme.typography.h2,
                                            fontSize = 12.spx
                                        )
                                        if (!episode.badge.isNullOrEmpty()) {
                                            Box(
                                                modifier = Modifier
                                                    .padding(2.dp)
                                                    .clip(RoundedCornerShape(3.dp))
                                                    .background(
                                                        parseColor(
                                                            episode.badge_info?.bg_color ?: ""
                                                        )
                                                    )
                                                    .padding(2.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = episode.badge ?: "",
                                                    fontSize = 8.spx,
                                                    fontFamily = wearbiliFontFamily,
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Medium,
                                                    textAlign = TextAlign.Center
                                                )
                                            }

                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "EP${index.plus(1)}",
                                        style = AppTheme.typography.h3,
                                        fontSize = 9.spx,
                                        modifier = Modifier.alpha(0.8f)
                                    )
                                }
                            }
                        }
                    }
                }
                bangumi.section.forEach { section ->
                    Text(
                        text = "${section.title}(${section.episodes.size})",
                        style = AppTheme.typography.h1,
                        modifier = Modifier
                            .padding(horizontal = TitleBackgroundHorizontalPadding - 2.dp)
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = TitleBackgroundHorizontalPadding - 2.dp)
                    ) {
                        section.episodes.forEachIndexed { index, episode ->
                            item {
                                Card(onClick = {
                                    Intent(
                                        this@BangumiInfoScreen,
                                        Media3PlayerActivity::class.java
                                    ).apply {
                                        putExtra(PARAM_VIDEO_ID_TYPE, VIDEO_TYPE_BVID)
                                        putExtra(PARAM_IS_BANGUMI, true)
                                        putExtra(PARAM_VIDEO_ID, episode.bvid)
                                        putExtra(PARAM_VIDEO_CID, episode.cid)
                                        startActivity(this)
                                    }
                                }) {
                                    Column {
                                        Text(
                                            text = "${episode.title} ${episode.long_title}",
                                            style = AppTheme.typography.h2,
                                            fontSize = 12.spx
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "EP${index.plus(1)}",
                                            style = AppTheme.typography.h3,
                                            fontSize = 9.spx,
                                            modifier = Modifier.alpha(0.8f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}