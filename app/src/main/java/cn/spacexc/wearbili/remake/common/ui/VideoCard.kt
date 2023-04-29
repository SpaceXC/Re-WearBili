package cn.spacexc.wearbili.remake.common.ui

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.Application
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID_TYPE
import cn.spacexc.wearbili.remake.app.video.info.ui.VideoInformationActivity
import cn.spacexc.wearbili.remake.common.ui.theme.AppTheme

/**
 * Created by Xiaochang on 2022/9/17.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */
@Composable
fun VideoCard(
    videoName: String,
    uploader: String,
    views: String,
    coverUrl: String,
    modifier: Modifier = Modifier,
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
                BiliImage(
                    url = coverUrl,
                    contentDescription = "$videoName 封面",
                    modifier = Modifier
                        .weight(6f)
                        .aspectRatio(16f / 10f)
                        .clip(RoundedCornerShape(6.dp)),
                    contentScale = ContentScale.FillBounds
                )
                Text(
                    text = videoName,
                    style = AppTheme.typography.h3,
                    maxLines = 3,
                    modifier = Modifier.weight(7f),
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (uploader.isNotEmpty() && views.isNotEmpty()) {
                val inlineTextContent = mapOf(
                    "viewCountIcon" to InlineTextContent(
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
                    },
                    "uploaderIcon" to InlineTextContent(
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
                    }
                )
                Text(
                    text = buildAnnotatedString {
                        if (views.isNotEmpty()) {
                            appendInlineContent("viewCountIcon")
                            append(views)
                            append("  ")
                        }
                        if (uploader.isNotEmpty()) {
                            appendInlineContent("uploaderIcon")
                            append(uploader)
                        }
                    },
                    fontSize = 9.spx,
                    modifier = Modifier.alpha(0.7f),
                    color = Color.White,
                    inlineContent = inlineTextContent,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }
    }

}
