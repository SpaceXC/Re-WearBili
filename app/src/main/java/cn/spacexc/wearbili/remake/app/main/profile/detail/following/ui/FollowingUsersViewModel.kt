package cn.spacexc.wearbili.remake.app.main.profile.detail.following.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.spacexc.bilibilisdk.sdk.user.follow.following.FollowedUserInfo
import cn.spacexc.bilibilisdk.sdk.user.follow.following.remote.tags.FollowedUserTag
import cn.spacexc.wearbili.remake.common.UIState
import kotlinx.coroutines.launch

/**
 * Created by XC-Qan on 2023/6/23.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class FollowingUsersViewModel : ViewModel() {
    var uiState by mutableStateOf(UIState.Loading)
    var followedUserTags by mutableStateOf(emptyList<FollowedUserTag>())

    fun getFollowedUserTags() {
        viewModelScope.launch {
            val response = FollowedUserInfo.getFollowedUserTags()
            if (response.code != 0) {
                uiState = UIState.Failed
                return@launch
            }
            followedUserTags = response.data?.data ?: emptyList()
            uiState = UIState.Success
        }
    }
}