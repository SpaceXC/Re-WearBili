package cn.spacexc.wearbili.remake.ui.main.profile.detail.favorite.detail.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

/**
 * Created by XC-Qan on 2023/8/19.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */
const val PARAM_FAVOURITE_FOLDER_ID = "folderId"
const val PARAM_FAVOURITE_FOLDER_NAME = "folderName"

class FavouriteFolderDetailActivity : ComponentActivity() {
    val viewModel by viewModels<FavoriteFolderDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val folderId = intent.getLongExtra(PARAM_FAVOURITE_FOLDER_ID, 0L)
        val folderName = intent.getStringExtra(PARAM_FAVOURITE_FOLDER_NAME)
        //viewModel.setPagerFlow(folderId)
        setContent {
            //FavouriteFolderDetailScreen(viewModel = viewModel, folderName = folderName ?: "")
        }
    }
}