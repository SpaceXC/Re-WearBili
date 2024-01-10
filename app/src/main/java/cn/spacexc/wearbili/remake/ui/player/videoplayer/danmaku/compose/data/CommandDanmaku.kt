package cn.spacexc.wearbili.remake.ui.player.videoplayer.danmaku.compose.data

/**
 * Created by XC-Qan on 2023/12/31.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

const val COMMAND_SUBSCRIBE = "#ATTENTION#"
const val COMMAND_VOTE = "#VOTE#"
const val COMMAND_VIDEO = "#LINK#"
const val COMMAND_RATE = "#GRADE#"
const val COMMAND_ADVERTISE = "#CTIME#"

data class CommandDanmaku(
    val type: String,
    val appearTime: Int,
    val extra: Any,
    val displayedMillisecond: Int,  //应该（肯定）不会超过int上限,
    val isDisplaying: Boolean
)