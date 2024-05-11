package cn.spacexc.wearbili.remake.app.cache.create.ui

import android.app.Activity
import android.content.Intent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import cn.spacexc.bilibilisdk.sdk.video.info.remote.info.web.Page
import cn.spacexc.wearbili.remake.app.cache.list.CacheListActivity
import cn.spacexc.wearbili.remake.common.ToastUtils
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.WearBiliAnimatedVisibility
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.wearBiliAnimateDpAsState
import kotlinx.coroutines.launch

/**
 * Created by XC-Qan on 2023/9/6.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@Composable
fun Activity.CreateNewCacheScreen(
    viewModel: CreateNewCacheViewModel,
) {
    TitleBackground(
        title = "新建缓存",
        onBack = ::finish,
        uiState = viewModel.uiState,
        onRetry = {}) {
        var selectedPages by remember {
            mutableStateOf(emptyList<Page>())
        }
        val localDensity = LocalDensity.current
        var confirmButtonHeight by remember {
            mutableStateOf(0.dp)
        }
        val lazyColumnButtonContentPadding by wearBiliAnimateDpAsState(
            targetValue = if (selectedPages.isNotEmpty()) confirmButtonHeight else 0.dp
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(
                start = 12.dp,
                end = 12.dp,
                top = 6.dp,
                bottom = 6.dp + lazyColumnButtonContentPadding
            )
        ) {
            viewModel.videoPages.forEach { page ->
                item {
                    Card(isHighlighted = selectedPages.contains(page), onClick = {
                        val temp = selectedPages.toMutableList()
                        if (selectedPages.contains(page)) {
                            temp.remove(page)
                        } else {
                            temp.add(page)
                        }
                        selectedPages = temp
                    }) {
                        Text(text = page.part, fontFamily = wearbiliFontFamily, fontSize = 13.sp)
                    }
                }
            }
        }

        WearBiliAnimatedVisibility(
            visible = selectedPages.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            enter = slideInVertically { it / 2 } + fadeIn(),
            exit = slideOutVertically { it / 2 } + fadeOut(),
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .onSizeChanged {
                        confirmButtonHeight = with(localDensity) {
                            it.height.toDp()
                        }
                    },
                outerPaddingValues = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                innerPaddingValues = PaddingValues(12.dp),
                shape = RoundedCornerShape(30),
                onClick = {
                    viewModel.viewModelScope.launch {
                        viewModel.cacheVideos(selectedPages)
                        ToastUtils.showSnackBar(
                            "缓存任务创建成功！",
                            Icons.Default.Check,
                            Icons.AutoMirrored.Default.ArrowForwardIos
                        ) {
                            startActivity(
                                Intent(
                                    this@CreateNewCacheScreen,
                                    CacheListActivity::class.java
                                )
                            )
                        }
                        finish()
                    }
                }
            ) {
                androidx.compose.material3.Text(
                    text = "确定",
                    fontSize = 13.sp,
                    fontFamily = wearbiliFontFamily,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}