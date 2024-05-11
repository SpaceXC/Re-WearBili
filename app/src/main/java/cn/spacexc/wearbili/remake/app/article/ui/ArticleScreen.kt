package cn.spacexc.wearbili.remake.app.article.ui

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.spacexc.wearbili.common.domain.time.toDateStr
import cn.spacexc.wearbili.remake.app.article.util.TYPE_QUOTE
import cn.spacexc.wearbili.remake.app.image.ImageViewerActivity
import cn.spacexc.wearbili.remake.app.image.PARAM_IMAGE_URLS
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.clickVfx
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.titleBackgroundHorizontalPadding
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun Activity.ArticleScreen(
    viewModel: ArticleViewModel,
    cvid: Long
) {
    TitleBackground(
        title = "",
        uiState = viewModel.uiState,
        themeImageUrl = viewModel.articleInfo?.readInfo?.bannerUrl ?: "",
        networkUtils = viewModel.ktorNetworkUtils,
        onRetry = { viewModel.getArticle(cvid) },
        onBack = ::finish
    ) {
        /**
        颜色参考
         */
        /*Column(
            modifier = Modifier
                .verticalScroll(viewModel.readerScrollState)
                .fillMaxSize()
                .padding(
                    PaddingValues(
                        horizontal = TitleBackgroundHorizontalPadding(), vertical = 6.dp
                    )
                ), verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            colorMap.forEach { (color, hex) ->
                Text(text = color, color = parseColor("#$hex"), fontWeight = FontWeight.Bold)
            }
        }*/
        Column(
            modifier = Modifier
                .verticalScroll(viewModel.readerScrollState)
                .fillMaxSize()
                .padding(
                    PaddingValues(
                        horizontal = titleBackgroundHorizontalPadding(), vertical = 6.dp
                    )
                ), verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            viewModel.articleInfo?.let { info ->
                Text(
                    text = info.readInfo.title,
                    fontFamily = wearbiliFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "${info.readInfo.publishTime.times(1000).toDateStr()}发布",
                    fontFamily = wearbiliFontFamily,
                    fontSize = 11.sp,
                    modifier = Modifier.alpha(0.7f)
                )
                if (info.readInfo.bannerUrl.isNotEmpty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(info.readInfo.bannerUrl).crossfade(true).build(),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(vertical = 3.dp)
                            .fillMaxWidth()
                            .clip(
                                RoundedCornerShape(6.dp)
                            )
                    )
                    Divider(
                        color = Color(48, 48, 48), modifier = Modifier
                            .clip(
                                CircleShape
                            )
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth(0.2f)
                    )
                }
            }
            viewModel.articleNodes.forEach {
                if (it.text != null) {
                    if (it.text.type == TYPE_QUOTE) {
                        Card(
                            isClickEnabled = false,
                            innerPaddingValues = PaddingValues(
                                start = 8.dp,
                                end = 8.dp,
                                top = 10.dp,
                                bottom = 10.dp
                            )
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                /*Image(
                                    painter = painterResource(id = R.drawable.icon_blockquote),
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp).alpha(0.7f).padding(bottom = 4.dp)
                                )*/
                                Text(
                                    text = "「",
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .alpha(0.7f),
                                    fontFamily = wearbiliFontFamily,
                                    fontSize = 20.sp
                                )
                                Text(
                                    text = it.text.content,
                                    color = it.text.color,
                                    fontWeight = if (it.text.isBold) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 4.dp),
                                    textAlign = if (it.text.isCenter) TextAlign.Center else TextAlign.Unspecified,
                                    fontFamily = wearbiliFontFamily
                                )
                                Text(
                                    text = "」",
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .alpha(0.7f)
                                        .offset(y = 14.dp),
                                    fontFamily = wearbiliFontFamily,
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.End
                                )
                            }
                        }
                    } else {
                        Text(
                            text = it.text.content,
                            color = it.text.color,
                            fontWeight = if (it.text.isBold) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = if (it.text.isCenter) TextAlign.Center else TextAlign.Unspecified,
                            fontFamily = wearbiliFontFamily
                        )
                    }
                }
                if (it.image != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(it.image.imageUrl).crossfade(true).build(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(
                                RoundedCornerShape(6.dp)
                            )
                            .apply {
                                if (it.image.width != 0f && it.image.height != 0f) {
                                    aspectRatio(it.image.width / it.image.height)
                                }
                            }
                            .clickVfx {
                                startActivity(
                                    Intent(
                                        this@ArticleScreen,
                                        ImageViewerActivity::class.java
                                    ).apply {
                                        putExtra(PARAM_IMAGE_URLS, arrayOf(it.image.imageUrl))
                                    })
                            }
                    )
                    if (it.image.imageCaption.isNotEmpty()) {
                        Text(
                            text = it.image.imageCaption,
                            fontSize = 9.sp,
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .alpha(0.7f)
                                .padding(top = 1.dp),
                            textAlign = TextAlign.Center,
                            fontFamily = wearbiliFontFamily
                        )
                    }
                }
            }
        }
    }
}