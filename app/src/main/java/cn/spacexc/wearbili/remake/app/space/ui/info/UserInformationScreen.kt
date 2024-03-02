package cn.spacexc.wearbili.remake.app.space.ui.info

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import cn.spacexc.wearbili.remake.app.space.ui.UserSpaceViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Activity.UserInformationScreen(
    viewModel: UserSpaceViewModel
) {
    val pagerState = rememberPagerState { 2 }
    VerticalPager(state = pagerState) { page ->
        when (page) {
            0 -> BasicInformationScreen(
                viewModel = viewModel
            )

            1 -> DetailInformationScreen(viewModel = viewModel)
        }
    }
}