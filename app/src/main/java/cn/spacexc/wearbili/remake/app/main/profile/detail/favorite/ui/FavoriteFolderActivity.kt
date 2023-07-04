package cn.spacexc.wearbili.remake.app.main.profile.detail.favorite.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

/**
 * Created by XC-Qan on 2023/6/27.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class FavoriteFolderActivity : ComponentActivity() {
    private val viewModel by viewModels<FavoriteFolderViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //viewModel.getFolders()
        setContent {
            FavoriteFoldersScreen(viewModel = viewModel, onBack = ::finish)
        }
    }
}