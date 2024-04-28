package cn.spacexc.wearbili.remake.app.bangumi.info.episodes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import cn.spacexc.wearbili.remake.app.bangumi.info.episodes.ui.BangumiEpisodeListScreen
import cn.spacexc.wearbili.remake.app.bangumi.info.ui.BANGUMI_ID_TYPE_EPID
import cn.spacexc.wearbili.remake.app.bangumi.info.ui.PARAM_BANGUMI_ID
import cn.spacexc.wearbili.remake.app.bangumi.info.ui.PARAM_BANGUMI_ID_TYPE

const val PARAM_BANGUMI_EPISODE_SECTION_INDEX = "bangumiEpisodeSectionIndex"

class BangumiEpisodeListActivity : ComponentActivity() {
    private val viewModel by viewModels<BangumiEpisodesViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val index = intent.getIntExtra(PARAM_BANGUMI_EPISODE_SECTION_INDEX, 0)
        val bangumiIdType = intent.getStringExtra(PARAM_BANGUMI_ID_TYPE) ?: BANGUMI_ID_TYPE_EPID
        val bangumiId = intent.getLongExtra(PARAM_BANGUMI_ID, 0L)
        viewModel.getBangumiInfo(bangumiIdType, bangumiId)
        setContent {
            BangumiEpisodeListScreen(
                bangumiIdType = bangumiIdType,
                bangumiId = bangumiId,
                viewModel = viewModel,
                screenIndex = index
            )
        }
    }
}