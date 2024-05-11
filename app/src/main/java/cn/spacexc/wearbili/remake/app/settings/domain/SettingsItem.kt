package cn.spacexc.wearbili.remake.app.settings.domain

import androidx.compose.runtime.Composable

/**
 * Created by XC-Qan on 2023/8/7.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

data class SettingsItem(
    val icon: @Composable () -> Unit,
    val name: String,
    val description: String,
    val action: ((Option?) -> Unit)? = null,
    val isOn: (() -> Boolean)? = null,
    val displayedValue: (() -> String)? = null,
    val currentValue: (() -> Any)? = null,
    val options: List<Option>? = null
) {
    data class Option(
        val option: Any,
        val displayedValue: String,
    )
}