package cn.spacexc.wearbili.remake.app.settings

import cn.spacexc.bilibilisdk.data.DataManager
import cn.spacexc.wearbili.remake.app.Application

/**
 * Created by XC-Qan on 2023/4/6.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

const val SETTING_RECOMMENDATION_SOURCE = "recommendationSource"
const val SETTING_IS_DARK_THEME = "isDarkTheme"

//const val SETTING_IS_DEBUG = "isDebug"
const val SETTING_IS_LOW_PERFORMANCE = "isLowPerformance"
const val SETTING_CURRENT_PLAYER = "currentPlayer"

object SettingsManager {
    val dataManager: DataManager = Application.getApplication().dataManager

    val isDarkTheme = dataManager.getBoolFlow(SETTING_IS_DARK_THEME, false)
    val recommendSource = dataManager.getStringFlow(SETTING_RECOMMENDATION_SOURCE, "web")
    const val isDebug = false
    val isLowPerformance = dataManager.getBoolFlow(SETTING_IS_LOW_PERFORMANCE, false)
    val currentPlayer = dataManager.getStringFlow(SETTING_CURRENT_PLAYER, "videoPlayer")
}