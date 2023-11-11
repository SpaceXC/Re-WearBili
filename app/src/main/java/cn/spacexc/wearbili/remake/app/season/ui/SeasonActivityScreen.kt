package cn.spacexc.wearbili.remake.app.season.ui

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import cn.spacexc.bilibilisdk.sdk.season.remote.list.Archive
import cn.spacexc.wearbili.common.domain.video.toShortChinese
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.video.info.ui.VIDEO_TYPE_BVID
import cn.spacexc.wearbili.remake.common.toUIState
import cn.spacexc.wearbili.remake.common.ui.IconText
import cn.spacexc.wearbili.remake.common.ui.LoadingTip
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.TitleBackgroundHorizontalPadding
import cn.spacexc.wearbili.remake.common.ui.VideoCardWithNoBorder
import cn.spacexc.wearbili.remake.common.ui.spx
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.toLoadingState
import kotlinx.coroutines.flow.Flow

/**
 * Created by XC-Qan on 2023/11/5.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@Composable
fun Activity.SeasonActivityScreen(
    pagingData: Flow<PagingData<Archive>>,
    seasonName: String,
    uploaderName: String,
    ambientColor: Int
) {
    val items = pagingData.collectAsLazyPagingItems()

    TitleBackground(
        title = "合集详情",
        uiState = items.loadState.refresh.toUIState(),
        onBack = ::finish,
        themeColor = Color(ambientColor)
    ) {
        LazyColumn(
            contentPadding = PaddingValues(
                start = TitleBackgroundHorizontalPadding - 2.dp,
                end = TitleBackgroundHorizontalPadding - 2.dp,
                bottom = TitleBackgroundHorizontalPadding - 2.dp,
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
                        fontSize = 14.spx,
                        maxLines = 2,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                    IconText(
                        text = uploaderName,
                        fontSize = 9.spx,
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
            items(items) {
                it?.let { video ->
                    VideoCardWithNoBorder(
                        videoName = video.title,
                        uploader = "",
                        views = video.stat.view.toShortChinese(),
                        coverUrl = video.pic,
                        videoId = video.bvid,
                        videoIdType = VIDEO_TYPE_BVID,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )
                }
            }
            item {
                LoadingTip(loadingState = items.loadState.append.toLoadingState())
            }
        }
    }
} 