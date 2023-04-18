package cn.spacexc.wearbili.remake.common.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Created by XC-Qan on 2023/4/9.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@Composable
fun LoadingTip(
    hasMore: Boolean = true
) = Text(
    text = buildAnnotatedString {
        if (hasMore) appendInlineContent("loadingIndicator")
        append(if (hasMore) " 努力加载中" else "没有更多噜")
    },
    fontSize = 9.spx,
    color = Color.White,
    modifier = Modifier
        .alpha(0.6f)
        .fillMaxWidth()
        .padding(4.dp),
    textAlign = TextAlign.Center,
    inlineContent = mapOf(
        "loadingIndicator" to InlineTextContent(
            placeholder = Placeholder(
                width = 11.spx,
                height = 11.spx,
                placeholderVerticalAlign = PlaceholderVerticalAlign.Center
            )
        ) {
            CircularProgressIndicator(
                color = BilibiliPink,
                modifier = Modifier.fillMaxSize(),
                strokeWidth = 2.dp
            )
        }
    )
)