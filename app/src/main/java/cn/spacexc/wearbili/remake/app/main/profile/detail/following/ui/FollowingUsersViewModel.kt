package cn.spacexc.wearbili.remake.app.main.profile.detail.following.ui

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import cn.spacexc.bilibilisdk.sdk.user.follow.following.FollowedUserInfo
import cn.spacexc.bilibilisdk.sdk.user.follow.following.remote.tags.FollowedUserTag
import cn.spacexc.bilibilisdk.sdk.user.follow.following.remote.user.Data
import cn.spacexc.wearbili.remake.app.main.profile.detail.following.domain.FollowingPagingSource
import cn.spacexc.wearbili.remake.common.UIState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Created by XC-Qan on 2023/6/23.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class FollowingUsersViewModel : ViewModel() {
    var uiState: UIState by mutableStateOf(UIState.Loading)
    var followedUserTags by mutableStateOf(emptyList<FollowedUserTag>())

    var lazyColumnStates = HashMap<Int, LazyListState>()

    var followedUsers by mutableStateOf(mapOf<Int, Flow<PagingData<Data>>>())

    fun getFollowedUserTags() {
        viewModelScope.launch {
            val response = FollowedUserInfo.getFollowedUserTags()
            if (response.code != 0) {
                uiState = UIState.Failed(response.code)
                return@launch
            }
            val tags = response.data?.data ?: emptyList()
            tags.indices.forEach { lazyColumnStates[it] = LazyListState() }
            followedUserTags = tags
            followedUsers = buildMap {
                followedUserTags.forEachIndexed { index, followedUserTag ->
                    put(
                        index, Pager(config = PagingConfig(1)) {
                            FollowingPagingSource(followedUserTag.tagid)
                        }.flow
                    )
                }
            }
            uiState = UIState.Success
        }
    }
}