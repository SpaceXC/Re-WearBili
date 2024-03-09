package cn.spacexc.wearbili.remake.livestreamplayer

import android.app.Application
import cn.spacexc.bilibilisdk.BilibiliSdkManager

/**
 * Created by XC-Qan on 2023/10/13.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class LiveStreamPlayerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        BilibiliSdkManager.initSdk()
    }
}