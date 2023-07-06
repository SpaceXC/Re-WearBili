package cn.spacexc.wearbili.remake.app.main.profile.detail.favorite.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import cn.spacexc.bilibilisdk.sdk.user.favorite.FavoriteInfo
import cn.spacexc.bilibilisdk.sdk.user.favorite.remote.metadata.FavoriteFolderMetaData
import cn.spacexc.wearbili.remake.common.UIState
import kotlinx.coroutines.flow.flow

/**
 * Created by XC-Qan on 2023/6/24.
 * I'm wery cute so pwees be n-ice(cweam) to wy hode!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class FavoriteFolderViewModel : ViewModel() {
    var uiState by mutableStateOf(UIState.Loading)
    var folders = flow<List<FavoriteFolderMetaData>> {
        val response = FavoriteInfo.getAllFavoriteFolders()
        if (response.code != 0 || response.data?.data?.list.isNullOrEmpty()) {
            uiState = UIState.Failed
            return@flow
        }
        val list = (response.data?.data?.list ?: emptyList())
        val temp = ArrayList<FavoriteFolderMetaData>(list.size)
        list.forEachIndexed { index, folder ->
            val metaDataResponse = FavoriteInfo.getFavouriteFolderMetadata(folder.id)
            metaDataResponse.data?.let {
                temp[index] = it
            }
            emit(temp)
            uiState = UIState.Success
            return@flow
        }
        uiState = UIState.Failed
    }
}