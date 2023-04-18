package cn.spacexc.wearbili.remake.app.main.profile.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.spacexc.wearbili.remake.app.main.profile.remote.CurrentUserInfo
import cn.spacexc.wearbili.remake.common.UIState
import cn.spacexc.wearbili.remake.common.domain.network.KtorNetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by XC-Qan on 2023/4/11.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val networkUtils: KtorNetworkUtils
) : ViewModel() {
    private val scrollState = ScrollState(0)
    var screenState by mutableStateOf(
        ProfileScreenState(
            "", "", "", -1, -1, -1.0, -1, UIState.Loading, scrollState, "#FFFFFF"
        )
    )

    fun getProfile() {
        screenState = screenState.copy(uiState = UIState.Loading)
        viewModelScope.launch {
            val response =
                networkUtils.get<CurrentUserInfo>("https://api.bilibili.com/x/space/myinfo")
            if (response.code != 0) {
                screenState = screenState.copy(uiState = UIState.Failed)
                return@launch
            }
            response.data?.data?.let { user ->
                screenState = screenState.copy(
                    username = user.name,
                    avatar = user.face,
                    pendant = user.pendant?.image,
                    level = if (user.is_senior_member == 1) 7 else user.level,   //7 —> 因为硬核会员也只会返回等级为6且user.is_senior_member=1，所以硬核会员姑且可以看成level=7（我说的（（（
                    nicknameColor = user.vip?.nickname_color ?: "#FFFFFF",
                    fans = user.follower,
                    coins = user.coins,
                    uiState = UIState.Success
                )
            }
        }
    }
}