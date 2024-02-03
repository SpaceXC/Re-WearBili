package cn.spacexc.wearbili.remake.app.season.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import cn.spacexc.wearbili.common.domain.network.KtorNetworkUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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
const val PARAM_AMBIENT_IMAGE = "ambientImage"

@AndroidEntryPoint
class SeasonActivity : ComponentActivity() {
    private val viewModel by viewModels<SeasonViewModel>()
    @Inject
    lateinit var networkUtils: KtorNetworkUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mid = intent.getLongExtra(PARAM_MID, 0L)
        val seasonId = intent.getLongExtra(PARAM_SEASON_ID, 0L)
        val seasonName = intent.getStringExtra(PARAM_SEASON_NAME) ?: ""
        val uploaderName = intent.getStringExtra(PARAM_UPLOADER_NAME) ?: ""
        val ambientImage = intent.getStringExtra(PARAM_AMBIENT_IMAGE) ?: ""

        val pagingData = viewModel.getPager(mid, seasonId)

        setContent {
            SeasonActivityScreen(
                pagingData = pagingData,
                seasonName = seasonName,
                uploaderName = uploaderName,
                ambientImage = ambientImage,
                networkUtils = networkUtils
            )
        }
    }
}