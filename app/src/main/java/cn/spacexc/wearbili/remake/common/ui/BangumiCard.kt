package cn.spacexc.wearbili.remake.common.ui

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.common.domain.color.parseColor
import cn.spacexc.wearbili.remake.app.bangumi.info.ui.BANGUMI_ID_TYPE_SSID
import cn.spacexc.wearbili.remake.app.bangumi.info.ui.BangumiActivity
import cn.spacexc.wearbili.remake.app.bangumi.info.ui.PARAM_BANGUMI_ID
import cn.spacexc.wearbili.remake.app.bangumi.info.ui.PARAM_BANGUMI_ID_TYPE
import cn.spacexc.wearbili.remake.common.ui.theme.AppTheme
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily

/**
 * Created by XC-Qan on 2023/8/8.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

/*@UnstableApi*/
@Composable
fun LargeBangumiCard(
    title: String,
    cover: String,
    score: Float,
    badge: List<String>? = null,
    badgeColor: List<String>? = null,
    areas: List<String>,
    updateInformation: String,
    bangumiIdType: String = BANGUMI_ID_TYPE_SSID,
    bangumiId: Long,
    context: Context
) {
    val localDensity = LocalDensity.current
    Card(onClick = {
        context.startActivity(Intent(context, BangumiActivity::class.java).apply {
            putExtra(
                PARAM_BANGUMI_ID, bangumiId
            )
            putExtra(
                PARAM_BANGUMI_ID_TYPE, bangumiIdType
            )
        })
    }) {
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
                    url = cover,
                    contentDescription = "${title}封面",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3f / 4f)
                        .clip(RoundedCornerShape(8.dp))
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
                            append(score.toString())
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
                        .align(Alignment.BottomStart)
                        .clip(
                            RoundedCornerShape(topEnd = 12.dp)
                        )
                        .background(
                            Color(67, 67, 67, 255)
                        )
                        .padding(
                            start = 4.dp,
                            end = 4.dp,
                            top = 3.dp,
                            bottom = 2.dp
                        ),
                    maxLines = 2
                )
                Column(
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.TopEnd),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    badge?.forEachIndexed { index, badge ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(3.dp))
                                .background(
                                    badgeColor
                                        ?.get(index)
                                        .parseColor() ?: BilibiliPink
                                )
                                .padding(2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            androidx.compose.material.Text(
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


            }

            Spacer(modifier = Modifier.width(6.dp))

            Column(
                modifier = Modifier
                    .weight(5f)
                    .height(coverHeight)
            ) {
                Text(
                    text = title,
                    style = AppTheme.typography.h2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${areas.joinToString(separator = ", ")}, $updateInformation",
                    modifier = Modifier.alpha(0.6f),
                    style = AppTheme.typography.body1
                )
            }
        }
    }
}

@Composable
fun SmallBangumiCard(
    modifier: Modifier = Modifier,
    title: String,
    cover: String,
    epName: String,
    bangumiIdType: String = BANGUMI_ID_TYPE_SSID,
    bangumiId: Long,
    context: Context
) {
    val localDensity = LocalDensity.current
    Card(onClick = {
        context.startActivity(Intent(context, BangumiActivity::class.java).apply {
            putExtra(
                PARAM_BANGUMI_ID, bangumiId
            )
            putExtra(
                PARAM_BANGUMI_ID_TYPE, bangumiIdType
            )
        })
    }, modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            var coverHeight by remember {
                mutableStateOf(0.dp)
            }

            BiliImage(
                url = cover,
                contentDescription = "${title}封面",
                modifier = Modifier
                    .weight(3f)
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .onSizeChanged {
                        coverHeight = with(localDensity) { it.height.toDp() }
                    }
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column(
                modifier = Modifier
                    .weight(5f)
                    .height(coverHeight)
            ) {
                Text(
                    text = title,
                    style = AppTheme.typography.h2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = epName,
                    modifier = Modifier.alpha(0.6f),
                    style = AppTheme.typography.body1
                )
            }
        }
    }
}