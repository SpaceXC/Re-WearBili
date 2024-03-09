package cn.spacexc.wearbili.remake.app.video.info.info.remote

data class Page(
    val cid: Long,
    val dimension: Dimension,
    val duration: Int,
    val firstFrame: String,
    val from: String,
    val page: Int,
    val part: String,
    val vid: String,
    val weblink: String
)