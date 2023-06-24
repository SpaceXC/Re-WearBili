package cn.spacexc.wearbili.remake.app.settings

import cn.spacexc.bilibilisdk.data.DataManager
import javax.inject.Inject

/**
 * Created by XC-Qan on 2023/4/6.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class SettingsManager {
    @Inject
    lateinit var dataManager: DataManager
    val isDarkTheme = false
    val recommendSource = "web"
    val isDebug = false
    val isLowPerformance = false

    companion object {
        private var mInstance: SettingsManager? = null
        fun getInstance(): SettingsManager {
            if (mInstance == null) mInstance = SettingsManager()
            return mInstance!!
        }
    }
}