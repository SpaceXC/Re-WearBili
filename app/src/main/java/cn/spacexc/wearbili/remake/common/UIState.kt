package cn.spacexc.wearbili.remake.common

import androidx.paging.LoadState
import cn.spacexc.wearbili.common.exception.PagingDataLoadFailedException

/**
 * Created by XC-Qan on 2023/4/6.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

fun getMessageByCode(code: Int): String {
    return when (code) {
        -101 -> "你还没有登录啊啊啊啊"
        -1 -> "网络开小差啦"
        -404 -> "你好像来到了没有内容的荒原..."
        -400 -> "网络请求好像出了些问题..."
        else -> "加载失败啦"
    }
}

sealed class UIState(val errorMessage: String?) {
    data object Loading : UIState(null)
    data object Success : UIState(null)
    class Failed(errorMessage: String?) : UIState(errorMessage) {
        constructor(errorCode: Int) : this(getMessageByCode(errorCode))
    }
}
fun LoadState.toUIState(): UIState {
    return when (this) {
        is LoadState.Error -> {
            val code = (this.error as PagingDataLoadFailedException).code
            UIState.Failed(getMessageByCode(code))
        }
        is LoadState.Loading -> UIState.Loading
        else -> UIState.Success
    }
}