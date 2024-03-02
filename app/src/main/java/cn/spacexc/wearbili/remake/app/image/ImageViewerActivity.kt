package cn.spacexc.wearbili.remake.app.image

import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import cn.spacexc.wearbili.remake.common.ui.ArrowTitleBackgroundWithCustomBackground
import cn.spacexc.wearbili.remake.common.ui.shimmerPlaceHolder
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

/**
 * Created by XC-Qan on 2023/11/19.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

const val PARAM_IMAGE_URLS = "imageUrls"
const val PARAM_SELECTED_INDEX = "selectedIndex"

class ImageViewerActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val images = intent.getStringArrayExtra(PARAM_IMAGE_URLS) ?: emptyArray()
        val selectedIndex = intent.getIntExtra(PARAM_SELECTED_INDEX, 0)
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        setContent {
            ArrowTitleBackgroundWithCustomBackground(background = {
                val pagerState = rememberPagerState {
                    images.size
                }
                LaunchedEffect(key1 = Unit, block = {
                    pagerState.scrollToPage(selectedIndex)
                })
                Box {
                    HorizontalPager(state = pagerState) { page ->
                        var isLoading by remember {
                            mutableStateOf(true)
                        }
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(images[page])
                                .crossfade(true).build(),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .zoomable(rememberZoomState())
                                .shimmerPlaceHolder(isLoading),
                            onState = {
                                isLoading = when (it) {
                                    is AsyncImagePainter.State.Success -> false
                                    is AsyncImagePainter.State.Loading -> true
                                    else -> true
                                }
                            }
                        )
                    }
                }
            }, onBack = ::finish) {

            }
        }
    }
}