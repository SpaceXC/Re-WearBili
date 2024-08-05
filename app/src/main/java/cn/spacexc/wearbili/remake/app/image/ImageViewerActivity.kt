package cn.spacexc.wearbili.remake.app.image

import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import cn.spacexc.wearbili.remake.common.ui.ArrowTitleBackgroundWithCustomBackground
import cn.spacexc.wearbili.remake.common.ui.BiliImage
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
                        BiliImage(url = images[page], contentDescription = null, modifier = Modifier
                            .fillMaxSize()
                            .zoomable(
                                rememberZoomState()
                            )
                        )
                    }
                }
            }, onBack = ::finish) {

            }
        }
    }
}