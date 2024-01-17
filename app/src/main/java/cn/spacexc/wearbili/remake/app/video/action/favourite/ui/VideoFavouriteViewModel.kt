package cn.spacexc.wearbili.remake.app.video.action.favourite.ui

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.spacexc.bilibilisdk.sdk.user.favorite.action.FavoriteActions
import cn.spacexc.bilibilisdk.sdk.user.favorite.info.FavoriteInfo
import cn.spacexc.bilibilisdk.sdk.user.favorite.info.remote.list.Folder
import cn.spacexc.wearbili.common.domain.log.logd
import cn.spacexc.wearbili.remake.common.ToastUtils
import cn.spacexc.wearbili.remake.common.UIState
import kotlinx.coroutines.launch

/**
 * Created by XC-Qan on 2023/8/17.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class VideoFavouriteViewModel : ViewModel() {
    var folders by mutableStateOf(emptyList<Folder>())
    var uiState by mutableStateOf(UIState.Loading)

    var idsToAdd by mutableStateOf(listOf<Long>())
    var idsToDelete by mutableStateOf(listOf<Long>())

    private var videoAid = 0L

    fun getFolders(
        videoAid: Long
    ) {
        this.videoAid = videoAid
        viewModelScope.launch {
            val response = FavoriteInfo.getAllFavoriteFolders(videoAid = videoAid)
            if (response.code != 0) {
                uiState = UIState.Failed
                return@launch
            }
            folders = response.data?.data?.list ?: emptyList()
            uiState = UIState.Success
            return@launch
        }
    }

    fun confirmAction(context: Context, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            uiState = UIState.Loading
            val response = FavoriteActions.commitFavorite(
                videoAid = videoAid,
                idsToAdd = idsToAdd,
                idsToDelete = idsToDelete
            )
            if(response.code != 0) {
                uiState = UIState.Success   //因为要展示原来的页面啦，所以是success
                ToastUtils.showText(content = "操作失败啦！${response.code}: ${response.message}")
                return@launch
            }
            val previousFavouriteFolders = folders.filter { it.fav_state == 1 }.map { it.id }.logd("previousFavouriteFolders")!!
            val temp = previousFavouriteFolders.toMutableList()
            temp.addAll(idsToAdd.logd("idsToAdd")!!)
            temp.removeAll(idsToDelete.logd("idsToDelete")!!)
            callback(temp.isNotEmpty())
        }
    }
}