package cn.spacexc.wearbili.remake.app

import android.os.Build
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
val APP_VERSION_NAME = Application.getVersionName()
val APP_VERSION_CODE = Application.getVersionCode()

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

        fun getVersionName(): String {
            val packageInfo =
                getApplication().packageManager.getPackageInfo(getApplication().packageName, 0)
            return packageInfo.versionName
        }

        fun getVersionCode(): Long {
            val packageInfo =
                getApplication().packageManager.getPackageInfo(getApplication().packageName, 0)
            return if (Build.VERSION.SDK_INT >= 28) packageInfo.longVersionCode else packageInfo.versionCode.toLong()
        }
    }
}