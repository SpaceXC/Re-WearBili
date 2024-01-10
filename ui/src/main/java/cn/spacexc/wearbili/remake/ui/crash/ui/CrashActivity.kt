package cn.spacexc.wearbili.remake.app.crash.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.developer.crashx.CrashActivity

/**
 * Created by XC-Qan on 2023/7/13.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class CrashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val logs = "${
            CrashActivity.getAllErrorDetailsFromIntent(
                this,
                intent
            )
        }\n\n${CrashActivity.getStackTraceFromIntent(intent)}"
        setContent {
            CrashActivityScreen(crashLog = logs)
        }
    }
}