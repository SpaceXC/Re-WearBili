package cn.spacexc.wearbili.remake.app.feedback.ui.issues

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import cn.spacexc.wearbili.common.domain.time.toDateStr
import cn.spacexc.wearbili.remake.common.toUIState
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.LoadingTip
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.toLoadingState

@Composable
fun Activity.AllIssuesScreen(viewModel: AllIssuesViewModel) {
    val items = viewModel.pager.collectAsLazyPagingItems()
    TitleBackground(
        title = "我的提交",
        onRetry = items::retry,
        uiState = items.loadState.refresh.toUIState(),
        onBack = ::finish
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(items.itemCount) {
                items[it]?.let { item ->
                    Card {
                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text(
                                text = item.exceptionDescription,
                                fontSize = 13.sp,
                                fontFamily = wearbiliFontFamily,
                                color = Color.White,
                                fontWeight = FontWeight.Medium,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = item.shortId,
                                fontSize = 10.sp,
                                fontFamily = wearbiliFontFamily,
                                color = Color.White
                            )
                            Text(
                                text = "${item.reportTime.toDateStr()}上报",
                                fontSize = 10.sp,
                                fontFamily = wearbiliFontFamily,
                                color = Color.White
                            )
                            Text(
                                text = "状态：${
                                    when (item.status) {
                                        "pending" -> "待办"
                                        else -> "未知"

                                    }
                                }",
                                fontSize = 10.sp,
                                fontFamily = wearbiliFontFamily,
                                color = Color.White
                            )
                        }
                    }
                }
            }
            item {
                LoadingTip(items.loadState.append.toLoadingState(), items::retry)
            }
        }
    }
}