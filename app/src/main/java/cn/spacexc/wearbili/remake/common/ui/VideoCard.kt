package cn.spacexc.wearbili.remake.common.ui

import android.content.Context
import android.content.Intent
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileCopy
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.common.domain.log.logd
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.Application
import cn.spacexc.wearbili.remake.app.cache.domain.database.STATE_COMPLETED
import cn.spacexc.wearbili.remake.app.cache.domain.database.STATE_DOWNLOADING
import cn.spacexc.wearbili.remake.app.cache.domain.database.STATE_FAILED
import cn.spacexc.wearbili.remake.app.cache.domain.database.STATE_FETCHING
import cn.spacexc.wearbili.remake.app.cache.domain.database.STATE_IDLE
import cn.spacexc.wearbili.remake.app.cache.domain.database.VideoCacheFileInfo
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.Media3PlayerActivity
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.PARAM_IS_CACHE
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.PARAM_VIDEO_CID
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID_TYPE
import cn.spacexc.wearbili.remake.app.video.info.ui.VIDEO_TYPE_BVID
import cn.spacexc.wearbili.remake.app.video.info.ui.VideoInformationActivity
import cn.spacexc.wearbili.remake.common.ui.theme.AppTheme
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Created by Xiaochang on 2022/9/17.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VideoCard(
    modifier: Modifier = Modifier,
    videoName: String,
    uploader: String,
    views: String,
    badge: String? = "",
    coverUrl: String,
    videoId: String? = null,
    videoIdType: String? = null,
    context: Context = Application.getApplication()
) {
    Card(modifier = modifier, onClick = {
        if (!videoId.isNullOrEmpty() && !videoIdType.isNullOrEmpty()) {
            Intent(context, VideoInformationActivity::class.java).apply {
                putExtra(PARAM_VIDEO_ID, videoId)
                putExtra(PARAM_VIDEO_ID_TYPE, videoIdType)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(this)
            }
        }
    }) {
        VideoCardContent(
            videoName = videoName,
            uploader = uploader,
            views = views,
            coverUrl = coverUrl,
            badge = badge
        )
    }
}

@Composable
fun VideoCardWithNoBorder(
    modifier: Modifier = Modifier,
    videoName: String,
    uploader: String,
    views: String,
    badge: String? = "",
    coverUrl: String,
    videoId: String? = null,
    videoIdType: String? = null,
    context: Context = Application.getApplication()
) {
    Box(modifier = modifier
        .padding(vertical = 2.dp)
        .clickVfx {
            if (!videoId.isNullOrEmpty() && !videoIdType.isNullOrEmpty()) {
                Intent(context, VideoInformationActivity::class.java).apply {
                    putExtra(PARAM_VIDEO_ID, videoId)
                    putExtra(PARAM_VIDEO_ID_TYPE, videoIdType)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(this)
                }
            }
        }) {
        VideoCardContent(
            videoName = videoName,
            uploader = uploader,
            views = views,
            coverUrl = coverUrl,
            badge = badge
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VideoCardContent(
    videoName: String,
    uploader: String,
    views: String,
    badge: String? = "",
    coverUrl: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp, horizontal = 2.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(modifier = Modifier.weight(6f)) {
                BiliImage(
                    url = coverUrl,
                    contentDescription = "$videoName 封面",
                    modifier = Modifier
                        .aspectRatio(16f / 10f)
                        .clip(RoundedCornerShape(6.dp)),
                    contentScale = ContentScale.FillBounds
                )
                if (!badge.isNullOrEmpty()) {
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(BilibiliPink)
                            .padding(2.dp)
                            .align(Alignment.TopEnd),
                        //.fillMaxSize()
                        //.offset(y = (1).dp)
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = badge,
                            fontSize = 8.spx,
                            fontFamily = wearbiliFontFamily,
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    }

                }
            }
            Text(
                text = videoName,
                style = AppTheme.typography.h3,
                maxLines = 3,
                modifier = Modifier.weight(7f),
                overflow = TextOverflow.Ellipsis
            )
        }

        FlowRow {
            if (views.isNotEmpty()) {
                val inlineTextContent = mapOf("viewCountIcon" to InlineTextContent(
                    placeholder = Placeholder(
                        width = 12.spx,
                        height = 12.spx,
                        placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_view_count),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(end = 2.dp)
                    )
                })
                Text(
                    text = buildAnnotatedString {
                        if (views.isNotEmpty()) {
                            appendInlineContent("viewCountIcon")
                            append(views)
                        }
                    },
                    fontSize = 9.spx,
                    modifier = Modifier.alpha(0.7f),
                    color = Color.White,
                    inlineContent = inlineTextContent,
                    //maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            if (uploader.isNotEmpty()) {
                val inlineTextContent = mapOf("uploaderIcon" to InlineTextContent(
                    placeholder = Placeholder(
                        width = 12.spx,
                        height = 12.spx,
                        placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_uploader),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(end = 2.dp)
                    )
                })
                Text(
                    text = buildAnnotatedString {
                        if (uploader.isNotEmpty()) {
                            appendInlineContent("uploaderIcon")
                            append(uploader)
                        }
                    },
                    fontSize = 9.spx,
                    modifier = Modifier.alpha(0.7f),
                    color = Color.White,
                    inlineContent = inlineTextContent,
                    //maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun VideoCacheCard(
    modifier: Modifier = Modifier,
    cacheInfo: VideoCacheFileInfo,
    context: Context = Application.getApplication()
) {
    val progress by animateFloatAsState(targetValue = cacheInfo.downloadProgress / 100f, label = "")
    var isSourceFileExist by remember {
        mutableStateOf(true)
    }
    val scope = rememberCoroutineScope()
    Card(modifier = modifier, onClick = {
        if (cacheInfo.state == STATE_COMPLETED) {
            val videoFile =
                File(
                    context.filesDir,
                    "videoCaches/${cacheInfo.videoCid}/${cacheInfo.videoCid}.video.mp4"
                )
            logd(videoFile.absolutePath)
            if (videoFile.exists()) {
                context.startActivity(Intent(context, Media3PlayerActivity::class.java).apply {
                    putExtra(PARAM_IS_CACHE, true)
                    putExtra(PARAM_VIDEO_CID, cacheInfo.videoCid)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                })
            } else {
                isSourceFileExist = false
            }
        }
    }) {
        if (isSourceFileExist) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp, horizontal = 2.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(modifier = Modifier.weight(6f)) {
                        if (cacheInfo.state == STATE_COMPLETED) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current).data(
                                    File(
                                        context.filesDir,
                                        "videoCaches/${cacheInfo.videoCid}/${cacheInfo.videoCid}.cover.png"
                                    ).path.logd("image path")
                                ).crossfade(true).build(),
                                contentDescription = "${cacheInfo.videoName} 封面",
                                modifier = Modifier
                                    .aspectRatio(4f / 3f)
                                    .clip(RoundedCornerShape(6.dp)),
                                contentScale = ContentScale.FillHeight
                            )
                        } else {
                            BiliImage(
                                url = cacheInfo.videoCover,
                                contentDescription = "${cacheInfo.videoName} 封面",
                                modifier = Modifier
                                    .aspectRatio(4f / 3f)
                                    .clip(RoundedCornerShape(6.dp)),
                                contentScale = ContentScale.FillHeight
                            )
                        }
                    }
                    Column(modifier = Modifier.weight(7f)) {
                        Text(
                            text = cacheInfo.videoName,
                            style = AppTheme.typography.h3,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        IconText(
                            text = cacheInfo.videoUploaderName,
                            modifier = Modifier.alpha(0.7f),
                            maxLines = 1,
                            fontSize = 9.spx
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_uploader),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                tint = Color.White
                            )
                        }
                        IconText(
                            text = cacheInfo.videoPartName,
                            modifier = Modifier.alpha(0.7f),
                            maxLines = 1,
                            fontSize = 9.spx
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.VideoLibrary,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                tint = Color.White
                            )
                        }
                        IconText(
                            text = cacheInfo.downloadFileSize,
                            modifier = Modifier.alpha(0.7f),
                            maxLines = 1,
                            fontSize = 9.spx
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.FileCopy,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                tint = Color.White
                            )
                        }
                    }
                }
                if (cacheInfo.state != STATE_COMPLETED) {
                    Text(
                        text = when (cacheInfo.state) {
                            STATE_IDLE -> "等待中..."
                            STATE_FETCHING -> "获取缓存信息中"
                            STATE_DOWNLOADING -> "正在下载 ${cacheInfo.downloadProgress}%"
                            STATE_FAILED -> "下载失败了！"
                            else -> "${cacheInfo.downloadProgress}%"
                        },
                        style = AppTheme.typography.body1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    LinearProgressIndicator(
                        progress = progress, modifier = Modifier
                            .fillMaxWidth()
                            .clip(
                                CircleShape
                            ), color = BilibiliPink
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp, horizontal = 2.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "源文件丢失", style = AppTheme.typography.h2)
                Text(
                    text = "找不到已缓存的源文件，可能已经被删除。你可以在数据库中删除这条记录，或者跳转到此视频详情页重新缓存。",
                    style = AppTheme.typography.body1
                )

                Card(modifier = Modifier.fillMaxWidth(),
                    innerPaddingValues = PaddingValues(6.dp),
                    shape = RoundedCornerShape(8.dp),
                    fillMaxSize = false,
                    onClick = {
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                Application.getApplication().repository.deleteExistingTasks(
                                    cacheInfo
                                )
                            }
                        }
                    }) {
                    Text(text = "删除记录")
                }
                Card(modifier = Modifier.fillMaxWidth(),
                    innerPaddingValues = PaddingValues(6.dp),
                    shape = RoundedCornerShape(8.dp),
                    fillMaxSize = false,
                    onClick = {
                        context.startActivity(Intent(
                            context, VideoInformationActivity::class.java
                        ).apply {
                            putExtra(PARAM_VIDEO_ID_TYPE, VIDEO_TYPE_BVID)
                            putExtra(PARAM_VIDEO_ID, cacheInfo.videoBvid)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        })
                    }) {
                    Text(text = "跳转详情")
                }
            }
        }
    }

}