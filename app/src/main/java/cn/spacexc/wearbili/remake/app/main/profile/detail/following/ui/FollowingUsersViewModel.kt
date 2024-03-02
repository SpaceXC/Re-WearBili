package cn.spacexc.wearbili.remake.app.main.profile.detail.following.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import cn.spacexc.bilibilisdk.sdk.user.follow.following.FollowedUserInfo
import cn.spacexc.bilibilisdk.sdk.user.follow.following.remote.tags.FollowedUserTag
import cn.spacexc.wearbili.remake.app.main.profile.detail.following.domain.FollowingPagingSource
import cn.spacexc.wearbili.remake.common.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
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
    var followedUserTags = MutableStateFlow<List<FollowedUserTag>>(emptyList())

    var followedUsers = followedUserTags.map { list ->
        list.map { tag ->
            FollowingPagingSource(tag.tagid)
        }.map {
            Pager(config = PagingConfig(pageSize = 1)) {
                it
            }
        }.map {
            it.flow.cachedIn(viewModelScope)
        }
    }

    fun getFollowedUserTags() {
        viewModelScope.launch {
            val response = FollowedUserInfo.getFollowedUserTags()
            if (response.code != 0) {
                uiState = UIState.Failed(response.code)
                return@launch
            }
            followedUserTags.value = response.data?.data ?: emptyList()
            uiState = UIState.Success
        }
    }
}