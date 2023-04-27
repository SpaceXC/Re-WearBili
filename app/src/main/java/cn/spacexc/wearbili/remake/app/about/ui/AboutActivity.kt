package cn.spacexc.wearbili.remake.app.about.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.lazy.LazyListState

/**
 * Created by XC-Qan on 2023/4/21.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class AboutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val state = LazyListState()
        setContent { AboutScreen(onBack = ::finish, state = state) }
    }
}