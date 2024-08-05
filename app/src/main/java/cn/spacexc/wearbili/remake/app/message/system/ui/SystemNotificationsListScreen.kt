package cn.spacexc.wearbili.remake.app.message.system.ui

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import appendBiliIcon
import cn.spacexc.bilibilisdk.sdk.message.data.system.SystemNotify
import cn.spacexc.wearbili.common.domain.log.TAG
import cn.spacexc.wearbili.common.domain.log.logd
import cn.spacexc.wearbili.remake.common.toUIState
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.LoadingTip
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.toLoadingState
import com.google.gson.Gson

data class SystemNotificationContent(
    val web: String
)

@Composable
fun NotificationRichText(
    originalText: String
) {
    Log.d(TAG, "NotificationRichText: $originalText")
    val content = try {
        val body = Gson().fromJson(originalText, SystemNotificationContent::class.java)
        buildAnnotatedString {
            body.web.split("http").forEach {
                if (it.startsWith("://") || it.startsWith("s://")) {
                    withStyle(SpanStyle(BilibiliPink)) {
                        withStyle(
                            SpanStyle(
                                fontSize = 14.sp,
                                baselineShift = BaselineShift(-0.25f)
                            )
                        ) {
                            appendBiliIcon("ea69")
                        }
                        append("网页链接")
                    }
                } else {
                    append(it)
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        val contentBlocks = originalText.split("#{")
        buildAnnotatedString {
            contentBlocks.logd("contentBlocks")
            contentBlocks.forEach { contentBlock ->
                contentBlock.logd("contentBlock")
                if (!contentBlock.contains("}") && !contentBlock.contains("{")) {
                    append(contentBlock)
                } else {
                    contentBlock.logd("contentBlock")
                    val lastCurlyBraceIndex = contentBlock.lastIndexOf("}")
                    val linkContent =
                        contentBlock.substring(0..lastCurlyBraceIndex).logd("linkContent")!!
                    val contentAfterLink =
                        contentBlock.substring(lastCurlyBraceIndex + 1..<contentBlock.length)

                    val extractedContents = linkContent.split("}{")
                        .map { it.replace("{", "").replace("}", "").replace("\"", "") }
                    extractedContents.logd("extractedContents")
                    val linkDescription = extractedContents[0]
                    val linkUrl = extractedContents[1]

                    withStyle(SpanStyle(BilibiliPink)) {
                        append(linkDescription)
                    }
                    append(contentAfterLink)
                }
            }
        }
    }
    Text(
        text = content,
        color = Color.White,
        fontFamily = wearbiliFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 10.5.sp
    )
}

@Composable
fun Activity.SystemNotificationScreen(
    viewModel: SystemNotificationViewModel
) {
    val lazyListData = viewModel.flow.collectAsLazyPagingItems()
    TitleBackground(
        title = "系统通知",
        onRetry = lazyListData::retry,
        onBack = ::finish,
        uiState = lazyListData.loadState.refresh.toUIState()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 8.dp),
            //verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(lazyListData.itemCount) { index ->
                lazyListData[index]?.let {
                    SystemNotificationCard(
                        notification = it
                    )
                }
            }
            item {
                LoadingTip(lazyListData.loadState.append.toLoadingState(), lazyListData::retry)
            }
        }
    }
}

@Composable
fun SystemNotificationCard(
    notification: SystemNotify
) {
    Card {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = notification.title,
                color = Color.White,
                fontFamily = wearbiliFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 12.5.sp
            )
            NotificationRichText(originalText = notification.content)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = notification.timeAt,
                color = Color.White,
                fontFamily = wearbiliFontFamily,
                fontSize = 9.sp,
                modifier = Modifier.alpha(0.7f)
            )
        }
    }
}