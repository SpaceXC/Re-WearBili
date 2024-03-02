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
import androidx.paging.LoadState

/**
 * Created by XC-Qan on 2023/4/9.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

enum class LoadingState {
    Loading,
    NoMore,
    Failed
}

@Composable
fun LoadingTip(
    loadingState: LoadingState = LoadingState.Loading,
    onRetry: () -> Unit// = {}
) = Text(
    text = buildAnnotatedString {
        if (loadingState == LoadingState.Loading) {
            appendInlineContent("loadingIndicator")
        }
        append(
            when (loadingState) {
                LoadingState.Loading -> "玩命加载中"
                LoadingState.NoMore -> "别翻啦，到底啦（￣︶￣）↗"
                LoadingState.Failed -> "加载失败啦！戳我重试！"
            }
        )
    },
    fontSize = 9.5.spx,
    color = Color.White,
    modifier = Modifier
        .alpha(0.6f)
        .fillMaxWidth()
        .padding(4.dp)
        .clickVfx {
            if (loadingState == LoadingState.Failed) {
                onRetry()
            }
        },
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

fun LoadState.toLoadingState(): LoadingState {
    return when (this) {
        is LoadState.Loading -> LoadingState.Loading
        is LoadState.Error -> LoadingState.Failed
        else -> LoadingState.NoMore
        //is LoadState.NotLoading -> LoadingState.Loading
    }
}