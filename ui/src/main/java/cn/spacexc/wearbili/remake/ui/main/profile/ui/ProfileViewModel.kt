package cn.spacexc.wearbili.remake.app.main.profile.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.spacexc.bilibilisdk.sdk.user.profile.UserProfileInfo
import cn.spacexc.wearbili.remake.common.UIState
import kotlinx.coroutines.launch

/**
 * Created by XC-Qan on 2023/4/11.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class ProfileViewModel : ViewModel() {
    private val scrollState = ScrollState(0)
    var screenState by mutableStateOf(
        ProfileScreenState(
            null, UIState.Loading, scrollState
        )
    )

    fun getProfile() {
        screenState = screenState.copy(uiState = UIState.Loading)
        viewModelScope.launch {
            val response = UserProfileInfo.getCurrentUserInfo()
            if (response.code != 0 && response.data?.code != 0) {
                screenState = screenState.copy(uiState = UIState.Failed)
                return@launch
            }
            response.data?.data?.let { user ->

                screenState = screenState.copy(
                    user = user,
                    uiState = UIState.Success
                )
            }
        }
    }
}