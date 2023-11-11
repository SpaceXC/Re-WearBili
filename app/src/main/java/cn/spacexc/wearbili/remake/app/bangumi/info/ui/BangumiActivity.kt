package cn.spacexc.wearbili.remake.app.bangumi.info.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import cn.spacexc.wearbili.remake.app.video.info.comment.ui.CommentViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by XC-Qan on 2023/7/20.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

//TODO 番剧还没写完呢！记得回来填坑！！！（20230726

const val PARAM_BANGUMI_ID_TYPE = "bangumiIdType"
const val PARAM_BANGUMI_ID = "bangumiId"

/*@UnstableApi*/
@AndroidEntryPoint
class BangumiActivity : ComponentActivity() {
    private val bangumiInfoViewModel by viewModels<BangumiViewModel>()
    private val bangumiCommentViewModel by viewModels<CommentViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bangumiIdType = intent.getStringExtra(PARAM_BANGUMI_ID_TYPE) ?: BANGUMI_ID_TYPE_EPID
        val bangumiId = intent.getLongExtra(PARAM_BANGUMI_ID, 0L)
        if (bangumiId != 0L) {
            if(bangumiIdType == BANGUMI_ID_TYPE_EPID) {
                bangumiInfoViewModel.currentSelectedEpid = bangumiId
            }
            bangumiInfoViewModel.getBangumiInfo(bangumiIdType, bangumiId)
        }
        setContent {
            BangumiScreen(
                bangumiInfoViewModel = bangumiInfoViewModel,
                bangumiCommentViewModel = bangumiCommentViewModel
            )
        }
    }
}