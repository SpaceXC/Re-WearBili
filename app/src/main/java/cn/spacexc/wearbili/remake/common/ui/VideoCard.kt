package cn.spacexc.wearbili.remake.common.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.remake.R
import coil.compose.AsyncImage
import coil.request.ImageRequest

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
    coverUrl: String
) {
    Card {
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
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(coverUrl.replace("http://", "https://"))
                        .crossfade(true).build(),
                    contentDescription = "$videoName 封面",
                    modifier = Modifier
                        .weight(6f)

                        .aspectRatio(16f / 10f)
                        .clip(RoundedCornerShape(6.dp)),
                    contentScale = ContentScale.FillBounds
                )
                Text(
                    text = videoName,
                    style = MaterialTheme.typography.h3,
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
                            modifier = Modifier.fillMaxSize().padding(end = 2.dp)
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
                            modifier = Modifier.fillMaxSize().padding(end = 2.dp)
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
