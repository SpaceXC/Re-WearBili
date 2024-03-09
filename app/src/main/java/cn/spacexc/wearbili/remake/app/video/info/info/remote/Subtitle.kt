package cn.spacexc.wearbili.remake.app.video.info.info.remote

data class Subtitle(
    val aiStatus: Int,
    val aiType: Int,
    val author: Author,
    val authorMid: Long,
    val id: Long,
    val idStr: String,
    val isLock: Boolean,
    val lan: String,
    val lanDoc: String,
    val subtitleUrl: String,
    val type: Int
)