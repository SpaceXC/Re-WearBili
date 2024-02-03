package cn.spacexc.wearbili.remake.app.article.ui

import android.app.Activity
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import cn.spacexc.wearbili.common.domain.time.toDateStr
import cn.spacexc.wearbili.remake.app.article.util.TYPE_QUOTE
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.TitleBackgroundHorizontalPadding
import cn.spacexc.wearbili.remake.common.ui.spx
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.wearBiliAnimateColorAsState
import cn.spacexc.wearbili.remake.common.ui.wearBiliAnimateFloatAsState
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun Activity.ArticleScreen(
    viewModel: ArticleViewModel
) {
    var currentColor by remember {
        mutableStateOf(BilibiliPink)
    }
    val color by wearBiliAnimateColorAsState(
        targetValue = currentColor,
        animationSpec = tween(durationMillis = 1000)
    )
    val ambientAlpha by wearBiliAnimateFloatAsState(
        targetValue = if (currentColor == BilibiliPink) 0.6f else 1f,
        animationSpec = tween(durationMillis = 1000)
    )
    LaunchedEffect(key1 = viewModel.imageBitmap, block = {
        viewModel.imageBitmap?.let { bitmap ->
            val palette = Palette.from(bitmap).generate()
            //if(newColor < Color(0x10000000).value.toInt())
            currentColor = Color(palette.getLightMutedColor(BilibiliPink.value.toInt()))
        }
    })
    TitleBackground(
        title = "",
        uiState = viewModel.uiState,
        ambientAlpha = ambientAlpha,
        themeColor = color
    ) {
        /*Column(
            modifier = Modifier
                .verticalScroll(viewModel.readerScrollState)
                .fillMaxSize()
                .padding(
                    PaddingValues(
                        horizontal = TitleBackgroundHorizontalPadding, vertical = 6.dp
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
                        horizontal = TitleBackgroundHorizontalPadding, vertical = 6.dp
                    )
                ), verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            viewModel.articleInfo?.let { info ->
                Text(
                    text = info.readInfo.title,
                    fontFamily = wearbiliFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.spx
                )
                Text(
                    text = "${info.readInfo.publishTime.times(1000).toDateStr()}发布",
                    fontFamily = wearbiliFontFamily,
                    fontSize = 11.spx,
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
                                    fontSize = 20.spx
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
                                    fontSize = 20.spx,
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
                    )
                    if (it.image.imageCaption.isNotEmpty()) {
                        Text(
                            text = it.image.imageCaption,
                            fontSize = 9.spx,
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