package cn.spacexc.wearbili.remake.ui.main.recommend.domain.remote.rcmd.app

data class AdInfo(
    val ad_cb: String,
    val card_index: Int,
    val card_type: Int,
    val client_ip: String,
    val cm_mark: Int,
    val creative_content: cn.spacexc.wearbili.remake.ui.main.recommend.domain.remote.rcmd.app.CreativeContent,
    val creative_id: Long,
    val creative_style: Int,
    val creative_type: Int,
    val extra: cn.spacexc.wearbili.remake.ui.main.recommend.domain.remote.rcmd.app.Extra,
    val index: Int,
    val is_ad: Boolean,
    val is_ad_loc: Boolean,
    val request_id: String,
    val resource: Int,
    val source: Int
)