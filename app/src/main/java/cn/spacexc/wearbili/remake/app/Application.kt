package cn.spacexc.wearbili.remake.app

import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by XC-Qan on 2023/3/23.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

const val TAG = "Re:WearBili"

@HiltAndroidApp
class Application : android.app.Application() {
    override fun onCreate() {
        super.onCreate()
        mApplication = this
    }

    companion object {
        lateinit var mApplication: android.app.Application
        fun getApplication(): android.app.Application = mApplication
    }
}