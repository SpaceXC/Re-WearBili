package cn.spacexc.wearbili.remake.ui.update

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import cn.spacexc.wearbili.remake.BuildConfig
import cn.spacexc.wearbili.remake.ui.splash.ui.SplashScreenActivity

/**
 * Created by XC-Qan on 2023/8/24.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class UpdateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        intent.setPackage(context.packageName)
        intent.flags = 0
        intent.data = null
        if (action == Intent.ACTION_MY_PACKAGE_REPLACED) {
            var mainIntent: Intent? = Intent(context, SplashScreenActivity::class.java)
            try {
                mainIntent = context.packageManager
                    .getLaunchIntentForPackage(BuildConfig.APPLICATION_ID)
            } catch (ignored: Exception) {
            }
            startLauncherActivity(context, mainIntent)
        }
    }

    private fun startLauncherActivity(context: Context, intent: Intent?) {
        context.startActivity(intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }
}