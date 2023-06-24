package cn.spacexc.wearbili.remake.app.main.profile.detail.following.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cn.spacexc.wearbili.remake.common.ui.TitleBackground

/**
 * Created by XC-Qan on 2023/6/23.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class FollowingUsersActivity : ComponentActivity() {
    private val viewModel by viewModels<FollowingUsersViewModel>()

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getFollowedUserTags()
        setContent {
            val pagerState = rememberPagerState()
            var currentTagName by remember {
                mutableStateOf("")
            }

            TitleBackground(title = currentTagName, uiState = viewModel.uiState) {
                HorizontalPager(pageCount = viewModel.followedUserTags.size) { page ->
                    Text(text = "$page ${viewModel.followedUserTags[page].name}")
                    LazyColumn(modifier = Modifier.fillMaxSize()) {

                    }
                    LaunchedEffect(key1 = page, block = {
                        if (viewModel.followedUserTags.isNotEmpty()) {
                            currentTagName = viewModel.followedUserTags[page].name
                        }
                    })
                }
            }

        }
    }
}