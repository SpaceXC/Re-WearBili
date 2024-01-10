package cn.spacexc.wearbili.remake.ui.video.info.info.remote

data class Subtitle(
    val ai_status: Int,
    val ai_type: Int,
    val author: Author,
    val author_mid: Long,
    val id: Long,
    val id_str: String,
    val is_lock: Boolean,
    val lan: String,
    val lan_doc: String,
    val subtitle_url: String,
    val type: Int
)