package cn.spacexc.wearbili.remake.app

import cn.spacexc.wearbili.remake.APP_CENTER_SECRET
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by XC-Qan on 2023/3/23.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

const val TAG = "Re:WearBili"
const val APP_VERSION = "Ver-AL 0.1.0"

@HiltAndroidApp
class Application : android.app.Application() {
    override fun onCreate() {
        super.onCreate()
        mApplication = this
        AppCenter.start(
            this, APP_CENTER_SECRET,
            Analytics::class.java, Crashes::class.java
        )
    }

    companion object {
        lateinit var mApplication: android.app.Application
        fun getApplication(): android.app.Application = mApplication
    }
}