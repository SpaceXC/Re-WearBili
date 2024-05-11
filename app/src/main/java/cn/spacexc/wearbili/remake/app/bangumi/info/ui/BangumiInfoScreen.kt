package cn.spacexc.wearbili.remake.app.bangumi.info.ui

import BiliTextIcon
import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.SendToMobile
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import cn.spacexc.wearbili.common.domain.color.parseColor
import cn.spacexc.wearbili.common.domain.video.toShortChinese
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.bangumi.info.episodes.BangumiEpisodeListActivity
import cn.spacexc.wearbili.remake.app.bangumi.info.episodes.PARAM_BANGUMI_EPISODE_SECTION_INDEX
import cn.spacexc.wearbili.remake.app.link.qrcode.PARAM_QRCODE_CONTENT
import cn.spacexc.wearbili.remake.app.link.qrcode.PARAM_QRCODE_MESSAGE
import cn.spacexc.wearbili.remake.app.link.qrcode.QrCodeActivity
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.Media3PlayerActivity
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.PARAM_IS_BANGUMI
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_CID
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID_TYPE
import cn.spacexc.wearbili.remake.app.video.info.ui.VIDEO_TYPE_BVID
import cn.spacexc.wearbili.remake.common.ui.BiliImage
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.CardBorderColor
import cn.spacexc.wearbili.remake.common.ui.CardBorderWidth
import cn.spacexc.wearbili.remake.common.ui.IconText
import cn.spacexc.wearbili.remake.common.ui.LoadableBox
import cn.spacexc.wearbili.remake.common.ui.OutlinedRoundButton
import cn.spacexc.wearbili.remake.common.ui.clickAlpha
import cn.spacexc.wearbili.remake.common.ui.theme.AppTheme
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.titleBackgroundHorizontalPadding
import cn.spacexc.wearbili.remake.common.ui.wearBiliAnimateColorAsState
import cn.spacexc.wearbili.remake.common.ui.wearBiliAnimateDpAsState
import cn.spacexc.wearbili.remake.common.ui.wearBiliAnimateFloatAsState
import kotlinx.coroutines.launch

/**
 * Created by XC-Qan on 2023/7/19.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@OptIn(ExperimentalLayoutApi::class)
@Composable
        /*@UnstableApi*/
fun Activity.BangumiInfoScreen(
    viewModel: BangumiViewModel,
    bangumiIdType: String,
    bangumiId: Long
) {
    val localDensity = LocalDensity.current
    val episodeDetailLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            val epid = result.data?.getLongExtra(PARAM_BANGUMI_ID, 0L)
            viewModel.currentSelectedEpid = epid ?: viewModel.currentSelectedEpid
        }
    LoadableBox(uiState = viewModel.uiState, onRetry = {
        viewModel.getBangumiInfo(bangumiIdType, bangumiId)
    }) {
        viewModel.bangumiInfo?.let { bangumi ->
            LaunchedEffect(key1 = viewModel.currentSelectedEpid, block = {
                if (viewModel.currentSelectedEpid != 0L) {
                    if (bangumi.episodes.find { it.ep_id == viewModel.currentSelectedEpid } != null) {
                        val index =
                            bangumi.episodes.indexOfFirst { it.ep_id == viewModel.currentSelectedEpid }
                        viewModel.bangumiInfoScreenScrollState.animateScrollToItem(1)
                        viewModel.episodeScrollState.animateScrollToItem(index)
                    } else {
                        val section =
                            bangumi.section?.find { section ->
                                section.episodes.find { episode ->
                                    episode.ep_id == viewModel.currentSelectedEpid
                                } != null //当前遍历到的section下的episode中包含当前选中的epid
                            }
                        val sectionIndex =
                            bangumi.section?.indexOf(section)
                        val sectionScrollState = viewModel.getSectionScrollState(section?.id ?: 0)
                        val episodeIndex =
                            section?.episodes?.indexOfFirst { it.ep_id == viewModel.currentSelectedEpid }
                        viewModel.bangumiInfoScreenScrollState.animateScrollToItem(2 + sectionIndex!!)
                        sectionScrollState.animateScrollToItem(episodeIndex!!)//因为要是没有sections的话，小朋友根本就选不中section中的episode呀
                    }
                }
            })

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                state = viewModel.bangumiInfoScreenScrollState,
                contentPadding = PaddingValues(vertical = 6.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = titleBackgroundHorizontalPadding() - 2.dp),
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
                                bangumi.rating?.let {
                                    Text(
                                        text = buildAnnotatedString {
                                            withStyle(
                                                style = SpanStyle(
                                                    color = Color(249, 157, 87, 255),
                                                    fontFamily = wearbiliFontFamily,
                                                    fontSize = 10.sp
                                                )
                                            ) {
                                                append(bangumi.rating?.score.toString())
                                            }
                                            withStyle(
                                                style = SpanStyle(
                                                    color = Color(249, 157, 87, 255),
                                                    fontFamily = wearbiliFontFamily,
                                                    fontSize = 8.sp
                                                )
                                            ) {
                                                append("分")
                                            }
                                        },
                                        color = Color.White,
                                        fontSize = 7.sp,
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
                            }

                            Spacer(modifier = Modifier.width(6.dp))

                            Column(
                                modifier = Modifier
                                    .weight(5f)
                                    //.requiredHeight(coverHeight)
                                    .requiredSizeIn(minHeight = coverHeight)
                                //.height(coverHeight)
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
                                        .padding(
                                            start = 6.dp,
                                            end = 10.dp,
                                            top = 5.dp,
                                            bottom = 5.dp
                                        ),
                                    verticalAlignment = CenterVertically
                                ) {
                                    IconText(
                                        text = "追番",
                                        color = Color.White,
                                        fontSize = 11.sp,
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
                            fontSize = 10.sp,
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
                                fontSize = 11.sp
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
                                fontSize = 11.sp
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
                                fontSize = 11.sp
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
                                fontSize = 11.sp
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
                }

                item {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = titleBackgroundHorizontalPadding() - 2.dp)
                            .clickAlpha {
                                episodeDetailLauncher.launch(
                                    Intent(
                                        this@BangumiInfoScreen,
                                        BangumiEpisodeListActivity::class.java
                                    ).apply {
                                        putExtra(PARAM_BANGUMI_EPISODE_SECTION_INDEX, 0)
                                        putExtra(PARAM_BANGUMI_ID_TYPE, BANGUMI_ID_TYPE_SSID)
                                        putExtra(PARAM_BANGUMI_ID, bangumi.season_id)
                                    }
                                )
                            }
                    ) {
                        Text(
                            text = "选集(${bangumi.episodes.size})",
                            style = AppTheme.typography.h1
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        BiliTextIcon(icon = "ea1c", size = AppTheme.typography.h1.fontSize)
                    }
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = titleBackgroundHorizontalPadding() - 2.dp),
                        state = viewModel.episodeScrollState
                    ) {
                        bangumi.episodes.forEachIndexed { index, episode ->
                            item {
                                val cardBorderColor by wearBiliAnimateColorAsState(
                                    targetValue = if (viewModel.currentSelectedEpid == episode.ep_id) BilibiliPink else CardBorderColor
                                )
                                val cardBorderWidth by wearBiliAnimateDpAsState(
                                    targetValue = if (viewModel.currentSelectedEpid == episode.ep_id) 1.dp else CardBorderWidth,
                                )
                                Card(
                                    borderColor = cardBorderColor,
                                    borderWidth = cardBorderWidth,
                                    onClick = {
                                        viewModel.currentSelectedEpid = episode.ep_id
                                    }) {
                                    Column {
                                        Row(verticalAlignment = CenterVertically) {
                                            Text(
                                                text = episode.long_title.ifEmpty { episode.title },
                                                style = AppTheme.typography.h2,
                                                fontSize = 12.sp
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
                                                        fontSize = 8.sp,
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
                                            fontSize = 9.sp,
                                            modifier = Modifier.alpha(0.8f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                bangumi.section?.forEachIndexed { index, section ->
                    item {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = titleBackgroundHorizontalPadding() - 2.dp)
                                .clickAlpha {
                                    episodeDetailLauncher.launch(
                                        Intent(
                                            this@BangumiInfoScreen,
                                            BangumiEpisodeListActivity::class.java
                                        ).apply {
                                            putExtra(PARAM_BANGUMI_EPISODE_SECTION_INDEX, index + 1)
                                            putExtra(PARAM_BANGUMI_ID_TYPE, BANGUMI_ID_TYPE_SSID)
                                            putExtra(PARAM_BANGUMI_ID, bangumi.season_id)
                                        }
                                    )
                                }
                        ) {
                            Text(
                                text = "${section.title}(${section.episodes.size})",
                                style = AppTheme.typography.h1
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            BiliTextIcon(icon = "ea1c", size = AppTheme.typography.h1.fontSize)
                        }
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = titleBackgroundHorizontalPadding() - 2.dp),
                            state = viewModel.getSectionScrollState(section.id)
                        ) {
                            section.episodes.forEachIndexed { index, episode ->
                                item {
                                    val cardBorderColor by wearBiliAnimateColorAsState(
                                        targetValue = if (viewModel.currentSelectedEpid == episode.ep_id && viewModel.currentSelectedEpid != 0L) BilibiliPink else CardBorderColor
                                    )
                                    val cardBorderWidth by wearBiliAnimateDpAsState(
                                        targetValue = if (viewModel.currentSelectedEpid == episode.ep_id && viewModel.currentSelectedEpid != 0L) 1.dp else CardBorderWidth,
                                    )
                                    Card(
                                        borderColor = cardBorderColor,
                                        borderWidth = cardBorderWidth,
                                        onClick = {
                                            if (episode.ep_id == 0L) {
                                                /*startActivity(Intent(this@BangumiInfoScreen, LinkProcessActivity::class.java).apply {
                                                    putExtra("url", episode.link)
                                                })*/
                                            } else {
                                                viewModel.currentSelectedEpid = episode.ep_id
                                            }
                                        }) {
                                        Column {
                                            Text(
                                                text = "${episode.title} ${episode.long_title}",
                                                style = AppTheme.typography.h2,
                                                fontSize = 12.sp
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "EP${index.plus(1)}",
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
                }
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = titleBackgroundHorizontalPadding() - 2.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            val playButtonAlpha by wearBiliAnimateFloatAsState(
                                targetValue = if (viewModel.currentSelectedEpid != 0L) 1f else 0.3f

                            )
                            OutlinedRoundButton(
                                clickable = viewModel.currentSelectedEpid != 0L,
                                icon = {
                                    Row(
                                        modifier = Modifier.align(Alignment.Center),
                                        verticalAlignment = CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.PlayCircle,
                                            tint = Color.White,
                                            contentDescription = "播放按钮"
                                        )
                                        androidx.compose.material.Text(
                                            text = "播放 ",    //这个空格是为了让图标和文字视觉剧中，万万不能删
                                            style = AppTheme.typography.h3
                                        )
                                    }
                                },
                                text = "用内置播放器播放",
                                modifier = Modifier.weight(2f),
                                buttonModifier = Modifier
                                    .aspectRatio(2f / 1f)
                                    .alpha(playButtonAlpha),
                                onClick = {
                                    var episode =
                                        bangumi.episodes.find { it.ep_id == viewModel.currentSelectedEpid }
                                    if (episode == null) {
                                        val section = bangumi.section?.find {
                                            it.episodes.find { epInSection ->
                                                epInSection.ep_id == viewModel.currentSelectedEpid
                                            } != null
                                        }
                                        episode =
                                            section?.episodes?.find { it.ep_id == viewModel.currentSelectedEpid }
                                    }
                                    if (episode != null) {
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
                                    }
                                }
                            )
                            OutlinedRoundButton(
                                icon = {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Outlined.SendToMobile,
                                        tint = Color.White,
                                        contentDescription = "在手机上播放按钮",
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                },
                                text = "手机播放",
                                modifier = Modifier.weight(1f),
                                buttonModifier = Modifier.aspectRatio(1f),
                                onClick = {
                                    startActivity(
                                        Intent(
                                            this@BangumiInfoScreen,
                                            QrCodeActivity::class.java
                                        ).apply {
                                            putExtra(PARAM_QRCODE_MESSAGE, "扫码在手机上观看")
                                            if (viewModel.getCurrentSelectedEpisode() == null) {
                                                putExtra(
                                                    PARAM_QRCODE_CONTENT,
                                                    "https://www.bilibili.com/bangumi/play/ss${bangumi.season_id}"
                                                )
                                            } else {
                                                putExtra(
                                                    PARAM_QRCODE_CONTENT,
                                                    "https://www.bilibili.com/bangumi/play/ep${viewModel.getCurrentSelectedEpisode()?.ep_id}"
                                                )

                                            }
                                        })
                                }
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
                                        verticalAlignment = CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.History,
                                            tint = Color.White,
                                            contentDescription = "稍后再看按钮"
                                        )
                                    }
                                },
                                text = "稍后再看",
                                modifier = Modifier.weight(1f),
                                buttonModifier = Modifier.aspectRatio(1f)
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
                                clickable = viewModel.currentSelectedEpid != 0L,
                                onClick = {
                                    if (viewModel.currentSelectedEpid != 0L) {
                                        viewModel.viewModelScope.launch {
                                            with(this@BangumiInfoScreen) {
                                                with(viewModel) {
                                                    cacheBangumi()
                                                }
                                            }
                                        }
                                    }
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
}