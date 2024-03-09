package cn.spacexc.wearbili.remake.app.main.recommend.domain.remote.rcmd.app

data class AdInfo(
    val ad_cb: String,
    val card_index: Int,
    val card_type: Int,
    val client_ip: String,
    val cm_mark: Int,
    val creative_content: CreativeContent,
    val creative_id: Long,
    val creative_style: Int,
    val creative_type: Int,
    val extra: Extra,
    val index: Int,
    val is_ad: Boolean,
    val is_ad_loc: Boolean,
    val request_id: String,
    val resource: Int,
    val source: Int
)