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

object SettingsManager {
    val dataManager: DataManager = Application.getApplication().dataManager

    val isDarkTheme = dataManager.getBoolFlow("isDarkTheme", false)
    val recommendSource = dataManager.getStringFlow("recommendationSource", "web")
    const val isDebug = false
    val isLowPerformance = dataManager.getBoolFlow("isLowPerformance", false)
}