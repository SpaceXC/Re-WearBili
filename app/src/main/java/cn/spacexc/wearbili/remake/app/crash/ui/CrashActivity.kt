package cn.spacexc.wearbili.remake.app.crash.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

/**
 * Created by XC-Qan on 2023/7/13.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

const val PARAM_EXCEPTION_STACKTRACE = "exceptionStacktrace"
const val PARAM_EXCEPTION_DESCRIPTION = "exceptionDescription"

class CrashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val description = intent.getStringExtra(PARAM_EXCEPTION_DESCRIPTION) ?: ""
        val stacktrace = intent.getStringExtra(PARAM_EXCEPTION_STACKTRACE) ?: ""
        setContent {
            CrashActivityScreen(stacktrace = stacktrace, description = description)
        }
    }
}