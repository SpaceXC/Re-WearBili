package cn.spacexc.wearbili.remake.app.space.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import cn.spacexc.bilibilisdk.sdk.user.profile.UserProfileInfo
import cn.spacexc.bilibilisdk.sdk.user.profile.remote.info.space.Data
import cn.spacexc.bilibilisdk.sdk.user.profile.remote.video.app.Item
import cn.spacexc.wearbili.remake.common.networking.KtorNetworkUtils
import cn.spacexc.wearbili.remake.app.main.dynamic.domain.remote.list.DynamicItem
import cn.spacexc.wearbili.remake.app.space.ui.dynamic.domain.UserSpaceDynamicPagingSource
import cn.spacexc.wearbili.remake.app.space.ui.videos.domain.UploaderVideosPagingSource
import cn.spacexc.wearbili.remake.common.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserSpaceViewModel @Inject constructor(
    val networkUtils: KtorNetworkUtils
) : ViewModel() {
    var uiState: UIState by mutableStateOf(UIState.Loading)
    var info: Data? by mutableStateOf(null)
    var stat: cn.spacexc.bilibilisdk.sdk.user.profile.remote.stat.Data? by mutableStateOf(null)

    fun getUserSpace(mid: Long) {
        viewModelScope.launch {
            val infoResponse = UserProfileInfo.getUserInfoByMid(mid)
            if (infoResponse.code != 0 || infoResponse.data == null) {
                uiState = UIState.Failed(infoResponse.code)
                return@launch
            }
            info = infoResponse.data!!.data
            uiState = UIState.Success
        }
        viewModelScope.launch {
            val statResponse = UserProfileInfo.getUserStatByMid(mid)
            if (statResponse.code != 0 || statResponse.data == null) {
                //uiState = UIState.Failed(statResponse.code)
                return@launch
            }
            stat = statResponse.data!!.data
        }
    }

    fun getUserVideosPagingItems(mid: Long): Flow<PagingData<Item>> {
        return Pager(config = PagingConfig(pageSize = 1)) {
            UploaderVideosPagingSource(mid)
        }.flow.cachedIn(viewModelScope)
    }

    fun getUserDynamicPagingItems(mid: Long): Flow<PagingData<DynamicItem>> {
        return Pager(config = PagingConfig(pageSize = 1)) {
            UserSpaceDynamicPagingSource(networkUtils, mid)
        }.flow.cachedIn(viewModelScope)
    }
}