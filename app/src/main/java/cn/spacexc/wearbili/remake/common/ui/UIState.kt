package cn.spacexc.wearbili.remake.common.ui

/**
 * Created by XC-Qan on 2023/4/6.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

sealed class UIState {
 object Loading: UIState()
 object Success: UIState()
 object Failed: UIState()
}
