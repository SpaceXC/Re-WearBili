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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cn.spacexc.wearbili.remake.common.ui.BiliImage

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
            /*val pagerState = rememberPagerState {
                images.size
            }
            LaunchedEffect(key1 = Unit, block = {
                pagerState.scrollToPage(selectedIndex)
            })
            Box {
                HorizontalPager(state = pagerState) { page ->
                    //ImageWithZoomAndPan(imageUrl = images[page])
                    var isLoading by remember {
                        mutableStateOf(true)
                    }
                    AndroidView(
                        factory = { context -> PhotoView(context) },
                        modifier = Modifier
                            .fillMaxSize()
                            .placeholder(
                                visible = isLoading,
                                highlight = PlaceholderHighlight.shimmer(),
                                color = PlaceholderDefaults.color()
                            )
                    ) { photoView ->
                        val imageLoader = ImageLoader.Builder(photoView.context)
                            .crossfade(true)
                            .components {
                                if (SDK_INT >= 28) {
                                    add(ImageDecoderDecoder.Factory())
                                } else {
                                    add(GifDecoder.Factory())
                                }
                            }
                            .eventListener(object : EventListener {
                                override fun onSuccess(
                                    request: ImageRequest,
                                    result: SuccessResult
                                ) {
                                    super.onSuccess(request, result)
                                    isLoading = false
                                }
                            })
                            .build()
                        val request = ImageRequest.Builder(photoView.context)
                            .data(images[page])
                            .crossfade(true)
                            .target(photoView)

                            .build()
                        imageLoader.enqueue(request)
                    }
                }
            }*/

            val pagerState = rememberPagerState {
                images.size
            }
            LaunchedEffect(key1 = Unit, block = {
                pagerState.scrollToPage(selectedIndex)
            })
            Box {
                HorizontalPager(state = pagerState) { page ->
                    //ImageWithZoomAndPan(imageUrl = images[page])
                    BiliImage(
                        url = images[page],
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            /*var currentPage by remember {
                mutableIntStateOf(0)
            }
            AndroidView(factory = { context -> ViewPager2(context) }, modifier = Modifier.fillMaxSize()) { viewPager ->
                viewPager.adapter =
                    ImageViewerAdapter(viewPager.context).apply { submitList(images.toList()) }
                //viewPager.setCurrentItem(selectedIndex, false)
                viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        currentPage = position + 1
                    }
                })
            }*/
        }
    }
}