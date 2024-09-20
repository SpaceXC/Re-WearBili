package cn.spacexc.wearbili.remake.app.season.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import cn.spacexc.wearbili.common.domain.video.toShortChinese
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.video.info.ui.VIDEO_TYPE_BVID
import cn.spacexc.wearbili.remake.common.toUIState
import cn.spacexc.wearbili.remake.common.ui.IconText
import cn.spacexc.wearbili.remake.common.ui.LoadingTip
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.VideoCardWithNoBorder
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.titleBackgroundHorizontalPadding
import cn.spacexc.wearbili.remake.common.ui.toLoadingState

/**
 * Created by XC-Qan on 2023/11/5.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@kotlinx.serialization.Serializable
data class SeasonScreen(
    val seasonName: String,
    val seasonId: Long,
    val uploaderName: String,
    val uploaderMid: Long,
    val ambientImage: String
)

@Composable
fun SeasonScreen(
    navController: NavController,
    viewModel: SeasonViewModel = hiltViewModel(),
    seasonId: Long,
    seasonName: String,
    uploaderMid: Long,
    uploaderName: String,
    ambientImage: String
) {
    val pagingData = remember {
        viewModel.getPager(uploaderMid, seasonId)
    }
    val items = pagingData.collectAsLazyPagingItems()

    TitleBackground(
        navController = navController,
        title = "合集详情",
        uiState = items.loadState.refresh.toUIState(),
        onBack = navController::navigateUp,
        themeImageUrl = ambientImage,
        networkUtils = viewModel.networkUtils
    ) {
        LazyColumn(
            contentPadding = PaddingValues(
                start = titleBackgroundHorizontalPadding() - 2.dp,
                end = titleBackgroundHorizontalPadding() - 2.dp,
                bottom = titleBackgroundHorizontalPadding() - 2.dp,
                top = 2.dp
            ),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = seasonName,
                        color = Color.White,
                        fontFamily = wearbiliFontFamily,
                        fontSize = 14.sp,
                        maxLines = 2,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                    IconText(
                        text = uploaderName,
                        fontSize = 9.sp,
                        modifier = Modifier.alpha(0.7f)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_uploader),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }
            items(items.itemCount) {
                items[it]?.let { video ->
                    VideoCardWithNoBorder(
                        videoName = video.title,
                        uploader = "",
                        views = video.stat.view.toShortChinese(),
                        coverUrl = video.pic,
                        videoId = video.bvid,
                        videoIdType = VIDEO_TYPE_BVID,
                        modifier = Modifier.padding(horizontal = 6.dp),
                        navController = navController
                    )
                }
            }
            item {
                LoadingTip(
                    loadingState = items.loadState.append.toLoadingState(),
                    onRetry = items::retry
                )
            }
        }
    }
} 