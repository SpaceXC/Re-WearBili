package cn.spacexc.wearbili.remake.app.settings.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.spacexc.bilibilisdk.sdk.user.profile.UserProfileInfo
import cn.spacexc.bilibilisdk.sdk.user.profile.remote.info.space.UserSpaceInfo
import cn.spacexc.bilibilisdk.utils.UserUtils
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class SwitchUserViewModel : ViewModel() {
    var userInfo by mutableStateOf(emptyList<UserSpaceInfo?>())
        private set
    private val lock = Mutex()

    fun getUserInfo() {
        viewModelScope.launch {
            userInfo = buildList {
                UserUtils.getUsers().forEach {
                    add(null)
                }
            }
            val tasks = UserUtils.getUsers().mapIndexed { index, mid ->
                viewModelScope.async {
                    val response = UserProfileInfo.getUserInfoByMid(mid)
                    lock.withLock {
                        val temp = userInfo.toMutableList()
                        temp[index] = response.data
                        userInfo = temp
                    }
                }
            }
            tasks.awaitAll()
        }
    }
}