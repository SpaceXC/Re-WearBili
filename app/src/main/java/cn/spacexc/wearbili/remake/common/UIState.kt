package cn.spacexc.wearbili.remake.common

import androidx.paging.LoadState

/**
 * Created by XC-Qan on 2023/4/6.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

enum class UIState {
    Loading,
    Success,
    Failed
}

fun LoadState.toUIState(): UIState {
    return when (this) {
        is LoadState.Error -> UIState.Failed
        is LoadState.Loading -> UIState.Loading
        else -> UIState.Success
    }
}