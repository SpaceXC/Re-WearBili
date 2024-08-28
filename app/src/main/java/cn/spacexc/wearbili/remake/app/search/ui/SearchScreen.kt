package cn.spacexc.wearbili.remake.app.search.ui

import android.os.Build.VERSION.SDK_INT
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import cn.spacexc.wearbili.remake.app.input.PARAM_INPUT
import cn.spacexc.wearbili.remake.common.ui.AutoResizedText
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.IconText
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.clickVfx
import cn.spacexc.wearbili.remake.common.ui.isRound
import cn.spacexc.wearbili.remake.common.ui.rememberMutableInteractionSource
import cn.spacexc.wearbili.remake.common.ui.theme.AppTheme
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.titleBackgroundHorizontalPadding
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

@kotlinx.serialization.Serializable
data class SearchScreen(val defaultSearchKeyword: String = "")

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = hiltViewModel(),
    defaultSearchKeyword: String = "",
    navController: NavController
) {
    LaunchedEffect(key1 = Unit) {
        searchViewModel.getHotSearch()
    }
    val scope = rememberCoroutineScope()
    val localDensity = LocalDensity.current
    val hotWords by searchViewModel.hotSearchedWords.collectAsState()
    val searchHistory by searchViewModel.searchHistory.collectAsState(initial = emptyList())
    var hotWordItemHeight by remember {
        mutableStateOf(100.dp)
    }

    var searchInputValue by remember {
        mutableStateOf(defaultSearchKeyword)
    }

    val inputResultLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            val input = it.data?.getStringExtra(PARAM_INPUT) ?: searchInputValue
            searchInputValue = input
        }

    Text(
        text = "xjdjdj",
        style = AppTheme.typography.body1,
        color = Color.White,
        modifier = Modifier.onSizeChanged {
            hotWordItemHeight = with(localDensity) { it.height.toDp() }
        }
    )  //这个Text是用来获取高度以设置下面LazyStaggeredGrid的高度以及热搜类型图片的高度的，不可或缺，要和下面热搜词的大小同步

    TitleBackground(title = "搜索", onBack = navController::navigateUp, onRetry = {}) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(searchViewModel.scrollState),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = titleBackgroundHorizontalPadding())) {
                if (isRound()) {
                    Spacer(modifier = Modifier.height(10.dp))
                } else {
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Card(
                    innerPaddingValues = PaddingValues(vertical = 10.dp, horizontal = 8.dp),
                    outerPaddingValues = PaddingValues(0.dp),
                    isClickEnabled = false,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier
                            .weight(1f)
                            .clickable(
                                rememberMutableInteractionSource(), null
                            ) {
                                /*inputResultLauncher.launch(
                                    Intent(
                                        context,
                                        InputActivity::class.java
                                    ).apply {
                                        putExtra(
                                            PARAM_PREV_INPUT, searchInputValue
                                        )
                                    })*/
                            }) {
                            BasicTextField(
                                value = searchInputValue,
                                onValueChange = {
                                    searchInputValue = it
                                },
                                textStyle = TextStyle(
                                    fontSize = 14.sp,
                                    fontFamily = wearbiliFontFamily,
                                    color = Color.White
                                ),
                                modifier = Modifier
                                    .align(Alignment.CenterStart),
                                cursorBrush = SolidColor(BilibiliPink)
                            )
                            if (searchInputValue.isEmpty()) {
                                AutoResizedText(
                                    text = "搜些什么ヾ(≧▽≦*)o",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontFamily = wearbiliFontFamily,
                                        color = Color.White
                                    ),
                                    modifier = Modifier
                                        .alpha(0.6f)
                                        .align(Alignment.CenterStart)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(2.dp))
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            tint = Color.White,
                            contentDescription = null,
                            modifier = Modifier.clickVfx {
                                if (searchInputValue.isNotEmpty()) {
                                    searchViewModel.addSearchHistory(searchInputValue)
                                    searchViewModel.searchByKeyword(navController, searchInputValue)
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
                        .padding(horizontal = titleBackgroundHorizontalPadding())
                ) {
                    IconText(
                        text = "搜索记录",
                        fontSize = 14.sp,
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
                        text = "清空", fontSize = 12.sp, modifier = Modifier
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
                contentPadding = PaddingValues(horizontal = titleBackgroundHorizontalPadding())
            ) {
                items(searchHistory) { item ->
                    Card(
                        shape = CircleShape,
                        outerPaddingValues = PaddingValues(),
                        innerPaddingValues = PaddingValues(8.dp),
                        onClick = {
                            searchViewModel.searchByKeyword(navController, item)
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
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = titleBackgroundHorizontalPadding())
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
                contentPadding = PaddingValues(horizontal = titleBackgroundHorizontalPadding())
            ) {
                items(hotWords) { item ->
                    Card(
                        shape = CircleShape,
                        outerPaddingValues = PaddingValues(),
                        innerPaddingValues = PaddingValues(8.dp),
                        onClick = {
                            searchViewModel.searchByKeyword(navController, item.keyword)
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
                                text = item.showName,
                                style = AppTheme.typography.body1
                            )
                        }
                    }

                }
            }
            if (isRound()) {
                Spacer(modifier = Modifier.height(10.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
} 