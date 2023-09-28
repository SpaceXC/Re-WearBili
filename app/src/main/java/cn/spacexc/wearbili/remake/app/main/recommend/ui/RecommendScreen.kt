package cn.spacexc.wearbili.remake.app.main.recommend.ui

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.common.domain.video.toShortChinese
import cn.spacexc.wearbili.remake.app.main.recommend.domain.remote.rcmd.app.Item
import cn.spacexc.wearbili.remake.app.video.info.ui.VIDEO_TYPE_AID
import cn.spacexc.wearbili.remake.app.video.info.ui.VIDEO_TYPE_BVID
import cn.spacexc.wearbili.remake.common.UIState
import cn.spacexc.wearbili.remake.common.ui.AutoResizedText
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.LoadableBox
import cn.spacexc.wearbili.remake.common.ui.LoadingTip
import cn.spacexc.wearbili.remake.common.ui.TitleBackgroundHorizontalPadding
import cn.spacexc.wearbili.remake.common.ui.VideoCard
import cn.spacexc.wearbili.remake.common.ui.lazyRotateInput
import cn.spacexc.wearbili.remake.common.ui.spx

/**
 * Created by XC-Qan on 2023/4/6.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

data class RecommendScreenState(
    val isRefreshing: Boolean = false,
    val videoList: List<Any> = emptyList(),
    val uiState: UIState = UIState.Loading,
    val scrollState: LazyListState = LazyListState()
)

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun RecommendScreen(
    state: RecommendScreenState,
    context: Context,
    recommendSource: String,
    onFetch: (isRefresh: Boolean) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val pullRefreshState =
        rememberPullRefreshState(refreshing = state.isRefreshing, onRefresh = {
            onFetch(true)
        }, refreshThreshold = 40.dp)
    val localDensity = LocalDensity.current
    val scope = rememberCoroutineScope()
    LoadableBox(
        uiState = state.uiState, modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        var showQuickToolBar by remember {
            mutableStateOf(true)
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .lazyRotateInput(focusRequester, state.scrollState),
            contentPadding = PaddingValues(
                start = TitleBackgroundHorizontalPadding,
                end = TitleBackgroundHorizontalPadding,
                bottom = 6.dp
            ),
            state = state.scrollState
        ) {
            if (showQuickToolBar) {
                item(key = "quickToolBar") {
                    Column {
                        val dismissState = rememberDismissState()
                        if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                            showQuickToolBar = false
                        }
                        SwipeToDismiss(
                            state = dismissState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateItemPlacement(),
                            directions = setOf(DismissDirection.EndToStart),
                            dismissThresholds = { direction ->
                                FractionalThreshold(if (direction == DismissDirection.StartToEnd) 0.25f else 0.5f)
                            },
                            background = {}
                        ) {
                            Card(
                                innerPaddingValues = PaddingValues(
                                    vertical = 10.dp,
                                    horizontal = 8.dp
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    var textHeight by remember {
                                        mutableStateOf(0.dp)
                                    }
                                    Icon(
                                        imageVector = Icons.Outlined.PushPin,
                                        contentDescription = null,
                                        tint = BilibiliPink,
                                        modifier = Modifier.rotate(32.1f)//.size(textHeight * 0.45f)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column(modifier = Modifier.onSizeChanged {
                                        textHeight = with(localDensity) { it.height.toDp() }
                                    }) {
                                        Text(
                                            text = "快捷功能区",
                                            //fontFamily = wearbiliFontFamily,
                                            style = MaterialTheme.typography.h1.copy(fontSize = 12.spx)
                                        )
                                        AutoResizedText(
                                            text = "点击设置快捷入口\n左划以隐藏此卡片\n可在“设置”中重新设置",
                                            //fontFamily = wearbiliFontFamily,
                                            style = MaterialTheme.typography.body1.copy(fontSize = 11.spx),
                                            maxLines = 3,
                                            modifier = Modifier.alpha(0.6f)
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Divider(
                            color = Color(48, 48, 48), modifier = Modifier
                                .clip(
                                    CircleShape
                                )
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth(0.2f)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }

            when (recommendSource) {
                "app" -> {
                    (state.videoList as List<Item>/* 这里真的没事的（确信 */).forEach {
                        if (it.goto == "av") {
                            item(key = it.uri) {
                                VideoCard(
                                    videoName = it.title,
                                    uploader = it.args.up_name ?: "",
                                    views = it.cover_left_text_2,
                                    coverUrl = it.cover ?: "",
                                    context = context,
                                    videoIdType = if (it.bvid.isNullOrEmpty()) VIDEO_TYPE_AID else VIDEO_TYPE_BVID,
                                    videoId = it.bvid ?: it.param,
                                    modifier = Modifier.animateItemPlacement()
                                )
                            }
                        }
                    }
                }

                "web" -> {
                    (state.videoList as List<cn.spacexc.wearbili.remake.app.main.recommend.domain.remote.rcmd.web.Item>/* 这里真的没事的（确信 */).forEach {
                        if (it.goto == "av") {
                            item(key = it.bvid) {
                                VideoCard(
                                    videoName = it.title,
                                    uploader = it.owner?.name ?: "",
                                    views = it.stat?.view?.toShortChinese()
                                        ?: "",
                                    coverUrl = it.pic,
                                    context = context,
                                    videoId = it.bvid,
                                    videoIdType = VIDEO_TYPE_BVID,
                                    modifier = Modifier.animateItemPlacement()
                                )
                            }
                        }
                    }
                }
            }
            item {
                LoadingTip()
                LaunchedEffect(key1 = Unit) {
                    onFetch(false)
                }
            }
        }
        LaunchedEffect(key1 = Unit, block = {
            focusRequester.requestFocus()
        })
        PullRefreshIndicator(
            refreshing = state.isRefreshing, state = pullRefreshState,
            modifier = Modifier.align(
                Alignment.TopCenter
            ),
        )
    }
}