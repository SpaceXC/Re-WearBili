package cn.spacexc.wearbili.remake.ui.season.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

/**
 * Created by XC-Qan on 2023/11/5.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

const val PARAM_MID = "mid"
const val PARAM_SEASON_ID = "seasonId"
const val PARAM_SEASON_NAME = "seasonName"
const val PARAM_UPLOADER_NAME = "uploaderName"
const val PARAM_AMBIENT_COLOR = "ambientColor"

class SeasonActivity : ComponentActivity() {
    private val viewModel by viewModels<SeasonViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mid = intent.getLongExtra(PARAM_MID, 0L)
        val seasonId = intent.getLongExtra(PARAM_SEASON_ID, 0L)
        val seasonName = intent.getStringExtra(PARAM_SEASON_NAME) ?: ""
        val uploaderName = intent.getStringExtra(PARAM_UPLOADER_NAME) ?: ""
        val ambientColor = intent.getIntExtra(PARAM_AMBIENT_COLOR, 0)

        val pagingData = viewModel.getPager(mid, seasonId)

        setContent {
            /*SeasonActivityScreen(
                pagingData = pagingData,
                seasonName = seasonName,
                uploaderName = uploaderName,
                ambientColor = ambientColor,
            )*/
        }
    }
}