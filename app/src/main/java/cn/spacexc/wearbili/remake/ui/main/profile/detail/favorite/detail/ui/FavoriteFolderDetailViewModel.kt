package cn.spacexc.wearbili.remake.ui.main.profile.detail.favorite.detail.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import cn.spacexc.bilibilisdk.sdk.user.favorite.info.remote.content.Media
import cn.spacexc.wearbili.remake.ui.main.profile.detail.favorite.detail.domain.FavoriteFolderDetailPagingSource
import kotlinx.coroutines.flow.Flow

/**
 * Created by XC-Qan on 2023/8/19.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class FavoriteFolderDetailViewModel : ViewModel() {
    var dataFlow: Flow<PagingData<Media>>? = null
    fun getPagerFlow(folderId: Long): Flow<PagingData<Media>> {
        if (dataFlow == null) {
            dataFlow = Pager(config = PagingConfig(pageSize = 1)) {
                FavoriteFolderDetailPagingSource(folderId)
            }.flow.cachedIn(viewModelScope)
        }
        return dataFlow!!
    }
}