package cn.spacexc.wearbili.remake.app.search.ui

import android.content.Context
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.remake.common.ui.AutoResizedText
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.IconText
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.clickVfx
import cn.spacexc.wearbili.remake.common.ui.spx
import cn.spacexc.wearbili.remake.common.ui.theme.AppTheme
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest

/**
 * Created by XC-Qan on 2023/4/30.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchActivityScreen(
    searchViewModel: SearchViewModel,
    onBack: () -> Unit,
    context: Context
) {
    val localDensity = LocalDensity.current
    val hotWords by searchViewModel.hotSearchedWords.collectAsState()
    val searchHistory by searchViewModel.searchHistory.collectAsState(initial = emptyList())
    var hotWordItemHeight by remember {
        mutableStateOf(100.dp)
    }
    Text(
        text = "",
        style = AppTheme.typography.body1,
        modifier = Modifier.onSizeChanged {
            hotWordItemHeight = with(localDensity) { it.height.toDp() }
        })  //这个Text是用来获取高度以设置下面LazyStaggeredGrid的高度以及热搜类型图片的高度的，不可或缺，要和下面热搜词的大小同步
    context.TitleBackground(title = "搜索", onBack = onBack) {
        var searchInputValue by remember {
            mutableStateOf("")
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(searchViewModel.scrollState),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                Spacer(modifier = Modifier.height(4.dp))
                Card(
                    innerPaddingValues = PaddingValues(vertical = 10.dp, horizontal = 8.dp),
                    outerPaddingValues = PaddingValues(0.dp),
                    isClickEnabled = false,
                    //modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            if (searchInputValue.isEmpty()) {
                                AutoResizedText(
                                    text = "搜些什么ヾ(≧▽≦*)o",
                                    style = TextStyle(
                                        fontSize = 14.spx,
                                        fontFamily = wearbiliFontFamily,
                                        color = Color.White
                                    ),
                                    modifier = Modifier
                                        .alpha(0.6f)
                                        .align(Alignment.CenterStart)
                                )
                            }
                            BasicTextField(
                                value = searchInputValue,
                                onValueChange = { searchInputValue = it },
                                singleLine = true,
                                textStyle = TextStyle(
                                    fontSize = 14.spx,
                                    fontFamily = wearbiliFontFamily,
                                    color = Color.White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(
                                        Alignment.CenterStart
                                    ),
                                cursorBrush = SolidColor(value = BilibiliPink),
                                keyboardActions = KeyboardActions(onSearch = {
                                    if (searchInputValue.isNotEmpty()) {
                                        searchViewModel.addSearchHistory(searchInputValue)
                                        context.startActivity(
                                            Intent(
                                                context,
                                                SearchResultActivity::class.java
                                            ).apply {
                                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                                putExtra(PARAM_KEYWORD, searchInputValue)
                                            }
                                        )
                                    }
                                }),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            )
                        }
                        Spacer(modifier = Modifier.width(2.dp))
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            tint = Color.White,
                            contentDescription = null,
                            modifier = Modifier.clickVfx {
                                if (searchInputValue.isNotEmpty()) {
                                    searchViewModel.addSearchHistory(searchInputValue)
                                    context.startActivity(
                                        Intent(context, SearchResultActivity::class.java).apply {
                                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                            putExtra(PARAM_KEYWORD, searchInputValue)
                                        }
                                    )
                                }
                            }
                        )
                    }

                }


            }
            if (searchHistory.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    IconText(
                        text = "搜索记录",
                        fontSize = 14.spx,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.History,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconText(
                        text = "清空", fontSize = 12.spx, modifier = Modifier
                            .alpha(0.8f)
                            .clickVfx(onClick = searchViewModel::deleteAllSearchHistory)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            tint = Color.White
                        )
                    }
                }
            }
            LazyHorizontalStaggeredGrid(
                rows = StaggeredGridCells.Fixed(count = if (searchHistory.size > 4) 2 else 1),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalItemSpacing = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (searchHistory.isNotEmpty()) ((hotWordItemHeight + 8.dp) * (if (searchHistory.size > 4) 2 else 1) + 4.dp * ((if (searchHistory.size > 4) 2 else 1) - 1)) else 0.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                items(searchHistory) { item ->
                    Card(
                        shape = CircleShape,
                        outerPaddingValues = PaddingValues(),
                        innerPaddingValues = PaddingValues(8.dp),
                        onClick = {
                            context.startActivity(
                                Intent(context, SearchResultActivity::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    putExtra(PARAM_KEYWORD, item)
                                }
                            )
                        }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = item,
                                style = AppTheme.typography.body1
                            )
                        }
                    }
                }
            }
            if (hotWords.isNotEmpty()) {
                IconText(
                    text = "bilibili热搜",
                    fontSize = 14.spx,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LocalFireDepartment,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            LazyHorizontalStaggeredGrid(
                rows = StaggeredGridCells.Fixed(count = 3),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalItemSpacing = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height((hotWordItemHeight + 8.dp) * 3 + 8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                items(hotWords) { item ->
                    Card(
                        shape = CircleShape,
                        outerPaddingValues = PaddingValues(),
                        innerPaddingValues = PaddingValues(8.dp),
                        onClick = {
                            context.startActivity(
                                Intent(context, SearchResultActivity::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    putExtra(PARAM_KEYWORD, item.keyword)
                                }
                            )
                        }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (item.icon.isNotEmpty()) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(item.icon.replace("http://", "https://"))
                                        .crossfade(true).build(),
                                    contentDescription = null,
                                    modifier = Modifier.height(hotWordItemHeight),
                                    imageLoader = ImageLoader(LocalContext.current).newBuilder()
                                        .components {
                                            if (SDK_INT >= 28) {
                                                add(ImageDecoderDecoder.Factory())
                                            } else {
                                                add(GifDecoder.Factory())
                                            }
                                        }.build()
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                            }
                            Text(
                                text = item.show_name,
                                style = AppTheme.typography.body1
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
} 