package cn.spacexc.wearbili.remake.app.main.profile.detail.favorite.folders.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.spacexc.bilibilisdk.sdk.user.favorite.info.FavoriteInfo
import cn.spacexc.bilibilisdk.sdk.user.favorite.info.remote.metadata.FavoriteFolderMetaData
import cn.spacexc.wearbili.common.domain.log.logd
import cn.spacexc.wearbili.remake.common.UIState
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

/**
 * Created by XC-Qan on 2023/6/24.
 * I'm wery cute so pwees be n-ice(cweam) to wy hode!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class FavoriteFolderViewModel : ViewModel() {
    var uiState: UIState by mutableStateOf(UIState.Loading)
    var folders by mutableStateOf(emptyList<FavoriteFolderMetaData?>())

    fun getFolders() {
        viewModelScope.launch {
            val response = FavoriteInfo.getAllFavoriteFolders()
            if (response.code != 0 || response.data?.data?.list.isNullOrEmpty()) {
                uiState = UIState.Failed(response.code)
                return@launch
            }
            val list = (response.data?.data?.list ?: emptyList())
            val temp = buildList<FavoriteFolderMetaData?> {
                repeat(list.size) {
                    add(null)
                }
            }.toMutableList()
            val tasks = list.mapIndexed { index, folder ->
                viewModelScope.async {
                    val metadataResponse = FavoriteInfo.getFavouriteFolderMetadata(folder.id)
                    metadataResponse.data?.let { metadata ->
                        temp[index] = metadata
                    }
                }
            }
            tasks.awaitAll()
            folders = temp
            logd(folders)
            uiState = UIState.Success
        }
    }
}