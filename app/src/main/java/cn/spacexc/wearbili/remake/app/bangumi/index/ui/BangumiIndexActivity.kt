package cn.spacexc.wearbili.remake.app.bangumi.index.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels


/**
 * Created by XC-Qan on 2023/8/8.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

/*@UnstableApi*/
class BangumiIndexActivity : ComponentActivity() {
    val viewModel by viewModels<BangumiIndexViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BangumiIndexScreen(viewModel = viewModel)
        }
    }
}