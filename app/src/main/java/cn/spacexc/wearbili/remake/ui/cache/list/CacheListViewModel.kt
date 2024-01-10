package cn.spacexc.wearbili.remake.ui.cache.list

import androidx.lifecycle.ViewModel
import cn.spacexc.wearbili.remake.ui.cache.domain.database.VideoCacheRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by XC-Qan on 2023/9/9.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@HiltViewModel
class CacheListViewModel @Inject constructor(
    repository: VideoCacheRepository
) : ViewModel() {
    val completedTasks = repository.getAllCompletedTasks()
    val unCompletedTasks = repository.getAllUncompletedTasks()
}