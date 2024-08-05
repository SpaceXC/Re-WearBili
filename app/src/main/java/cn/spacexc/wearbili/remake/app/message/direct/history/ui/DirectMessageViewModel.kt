package cn.spacexc.wearbili.remake.app.message.direct.history.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.spacexc.bilibilisdk.sdk.message.MessageInfo
import cn.spacexc.bilibilisdk.sdk.message.data.direct.history.Message
import cn.spacexc.wearbili.remake.common.UIState
import kotlinx.coroutines.launch

class DirectMessageViewModel : ViewModel() {
    var messages by mutableStateOf(emptyList<Message>())
        private set
    var uiState: UIState by mutableStateOf(UIState.Loading)

    fun getMessages(talkerMid: Long) {
        viewModelScope.launch {
            val response = MessageInfo.getDirectMessageHistory(talkerMid)
            if(response.code != 0) {
                uiState = UIState.Failed(response.code)
                return@launch
            }
            messages = response.data?.data?.messages ?: emptyList()
            uiState = UIState.Success
        }
    }
}